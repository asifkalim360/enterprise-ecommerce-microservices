package com.enterprise.product.service.impl;


import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.enterprise.product.dto.request.CreateCategoryRequest;
import com.enterprise.product.dto.request.UpdateCategoryRequest;
import com.enterprise.product.dto.response.CategoryResponse;
import com.enterprise.product.entity.Category;
import com.enterprise.product.repository.CategoryRepository;
import com.enterprise.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Spring ko batata hai ki ye service-layer component hai.
// Spring iska object (bean) banata hai, taaki ise doosri classes mein inject kiya ja sake.
@Service
// Ye Lombok annotation hai. Ye final fields ke liye constructor generate karta hai, taaki dependency injection aasaan ho jaaye.
@RequiredArgsConstructor
// Is class ke public methods par, jab tak method-level configuration ise override na kare, transaction apply hota hai.
// Agar transactional method ke andar unchecked exception aaye, to transaction aam taur par rollback hota hai.
@Transactional
public class CategoryServiceImpl implements CategoryService
{
    //  CategoryRepository -> database ke saath communicate karne ke liye use hoti hai.
    //  private -> field ko isi class tak restrict karta hai.
    //  final -> ka matlab hai constructor ke baad is reference ko reassign nahi kar sakte.
    //  @RequiredArgsConstructor -> constructor generate karta hai aur Spring uske through repository inject karta hai.
    //  Conceptually, Lombok is tarah ka constructor generate karta hai:
    //  public CategoryServiceImpl(CategoryRepository categoryRepository) { this.categoryRepository = categoryRepository; }
    private final CategoryRepository categoryRepository;

    // @Override -> batata hai ki interface ka method implement kiya ja raha hai.
    @Override
    // CreateCategoryRequest request -> me client se aayi category ki details hoti hain, jaise name aur description.
    // CategoryResponse -> return type hai, yani client ko response DTO milega, poori entity directly return nahi hogi.
    public CategoryResponse createCategory(CreateCategoryRequest request)   // category kar rahe hain.
    {
        // existsByNameIgnoreCase() -> Spring Data JPA ka derived query method hai. Iske liye repository mein method declaration karna padta hai.
        // mainly Yahan check kar rahe hain ki us name ka category pehle se hai ya nahi, case ko ignore karke.
        // Example: Electronics → pehli baar create ho sakti hai, electronics → agar Electronics pehle se hai, to reject hogi, ELECTRONICS → ye bhi reject hogi.
        if(categoryRepository.existsByNameIgnoreCase(request.getName()))
        {
            // throw new BusinessException(...) ka matlab hai ki business rule violate hua, isliye operation rok do.
            // Tumhare project ka common exception handler is exception ko appropriate HTTP response mein convert kar sakta hai.
            // Ye check kyun? Taaki category ke duplicate names create na hon.
            // Saath hi database ka unique constraint concurrent requests ke case mein bhi final protection deta hai.
            throw new BusinessException("Category already exists with name: " + request.getName());
        }

        Category category = new Category(); // new Category() —> naya Java entity object banata hai.
        category.setName(request.getName().trim()); // request.getName().trim() —> naam ke aage aur peeche ke spaces hata deta hai. Example: " Electronics " → "Electronics".
        category.setDescription(request.getDescription());  // setDescription(...) — request se description entity mein set karta hai.
        //save() entity ko persist ya update karne ke liye use hota hai. Nayi entity ke case mein, generated ID aur configured auditing fields jaise timestamps save hone ke baad available ho sakte hain.
        //Hum returned entity ko savedCategory mein rakhte hain, taaki saved data se response banaya ja sake.
        Category savedCategory = categoryRepository.save(category);
        // Database entity ko directly controller/client tak bhejne ke bajaye CategoryResponse DTO mein convert kar rahe hain.
        // Isse API response par control rehta hai aur hum sirf required fields expose karte hain.
        return mapToResponse(savedCategory);
    }
    //----------------------------------------------END CREATE METHOD--------------------------------------------------------
    // es method me category ka existence aur duplicate name, dono check karte hain.
    //

    @Override
    public CategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest request)  // existing category update kar rahe hain.
    {
        // findById(categoryId) repository se Optional<Category> return karta hai.
        //- Category mil gae toh → category variable me entity aa jayegi aur agar Category nahi mili toh → orElseThrow() ResourceNotFoundException throw karega.
        // Example: ID -> 10 database mein nahi hai, to category update nahi hogi.
        //ResourceNotFoundException kyun? Kyunki jis resource ko update karna chahte hain, woh exist hi nahi karta.
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        // - category.getName() — database mein maujood current name.
        //- request.getName().trim() — naya requested name, outer spaces remove karke.
        //- equalsIgnoreCase() — dono names ko case-insensitively compare karta hai.
        //- ! — result ko reverse karta hai.
        //Example:
        //Existing-name	Requested-name	nameChanged
        //Electronics -	Electronics	 -   false
        //Electronics -	electronics	 -   false
        //Electronics -	Clothing	 -   true
        boolean nameChanged = !category.getName().equalsIgnoreCase(request.getName().trim());

        // Yahan && ka matlab dono conditions true honi chahiye.
        //1. Name actually change hua ho, 2. Requested name pehle se kisi category mein exist karta ho.
        //Example:
        //- Category ID 1 ka name Electronics hai aur tum usi ko Electronics hi rakhte ho → duplicate check ki zaroorat nahi.
        //- Agar Electronics ko Clothing karna chahte hain aur kisi doosri category ka naam already Clothing hai → toh exception aayega.
        //- Aur agar usko Books karna chahte hain aur Books exist nahi karta → toh update allowed ho jayega.
        //Ye condition bhut useful hai? Taaki existing category ko uske naam ke saath update karne par galti se duplicate error na aaye.
        if(nameChanged && categoryRepository.existsByNameIgnoreCase(request.getName()))
        {
            throw new BusinessException("Category already exists with name: " + request.getName());
        }

        // Yahan existing entity ki fields update hoti hain, phir repository se save kiya jata hai aur response DTO return hota hai.
        //Ek practical detail: findById() se aayi entity transactional context mein managed ho to Hibernate dirty checking ke through changes flush kar sakta hai;
        // isliye save() har aise case mein technically necessary nahi hota. Lekin code mein explicit save readable aur common pattern hai.
        category.setName(request.getName().trim());
        category.setDescription(request.getDescription());
        Category updatedCategory = categoryRepository.save(category);
        return mapToResponse(updatedCategory);
    }
    //----------------------------------------------------END UPDATE METHOD---------------------------------------------------------

    // Is method mein hum category create ya update nahi kar rahe, sirf read kar rahe hain.
    //@Transactional(readOnly = true) kyun?
    //Ye Spring ko batata hai ki method read-only operation ke liye hai. Isse kuch persistence providers ke saath unnecessary dirty checking kam ho sakti hai.
    //Dhyan rahe, readOnly = true ek optimization hint hai; ye har database ya provider mein writes ko guaranteed block nahi karta.
    //Aur kyunki method par method-level @Transactional hai, ye class-level @Transactional ki settings ko override karta hai.
    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long categoryId)    //- Long categoryId — jis category ko fetch karna hai uski ID.
    {
        //- findById() — database se category fetch karne ke liye use karte hain.
        //- orElseThrow() — category missing hone pe exception throw karta hai.
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        //- mapToResponse() — entity ko response DTO mein convert karta hai.
        return mapToResponse(category);
    }
    //---------------------------------------------END GETBYID METHOD-------------------------------------------------------------

    // fetch All categories:
    //Agar database mein 20 categories hain, to har entity ko CategoryResponse mein convert karke list return ki jayegi.
    //Production mein categories bahut zyada ho sakti hain, isliye large datasets ke liye pagination use karna better hota hai.
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategory()
    {
        return categoryRepository.findAll() //- findAll() — yeh saari category entities return karta hai.
                .stream()   //- stream() — yeh collection ko process karne ke liye stream banata hai.
                .map(this::mapToResponse)   //- map(...) — yeh har entity ko doosre object mein transform karta hai.
                //- this::mapToResponse — current class ke mapToResponse() method ka method reference hai.
                .toList();  //- toList() — yeh transformed responses ki list banata hai.
    }
    //---------------------------------------------END GETALLID METHOD-------------------------------------------------------------

    // ye method entity ko DTO mein convert karne ka kaam kar rahi hai.
    private CategoryResponse mapToResponse(Category category)
    {
        // yeh method ek CategoryResponse object banayega.
        // jisme ye fields(id = 1, name = Electronics, description = Electronic products, status = ACTIVE) response ke liye available hongi.
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getStatus(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

}

//-----------------------------Poore class ka flow------------------------------------------------------|
//  Method	              |               Kaam	                |      Important logic                  |
//  createCategory()      |	    Nayi category banana	        |    Duplicate check, save              |
//  updateCategory()      |	    Existing category update karna	|    ID check, duplicate name check     |
//  getCategoryById()     |	    Ek category fetch karna	        |    Missing ID par exception           |
//  getAllCategories()    |	    Saari categories fetch karna	|    Stream + DTO mapping               |
//  mapToResponse()       |	    Entity → DTO	                |    Reusable conversion                |
//------------------------------------------------------------------------------------------------------|