package com.ecommerce.auth.controller;

import com.ecommerce.auth.dto.request.LoginRequest;
import com.ecommerce.auth.dto.request.RegisterRequest;
import com.ecommerce.auth.dto.response.AuthResponse;
import com.ecommerce.auth.dto.response.LoginResponse;
import com.ecommerce.auth.service.AuthService;
import com.ecommerce.auth.service.AuthenticationService;
import com.ecommerce.common.dto.ApiResponse;
import com.ecommerce.common.enums.ResponseStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  Authentication APIs.
 **/
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    // Constructor Injection.
    private final AuthService authService;
    private final AuthenticationService authenticationService;

    // User Registration.
    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request)
    {
        AuthResponse respponse = authService.register(request);

        return ApiResponse.<AuthResponse>builder()
                .status(ResponseStatus.SUCCESSS)
                .message("Registration successful.")
                .data(respponse)
                .build();
    }
//======================================================================
    // @PostMapping("/login")
    // Ye batata hai ki ye method HTTP POST request ko handle karega.
    // Agar controller ke upar: @RequestMapping("/api/v1/auth") laga hai,
    // to complete API hogi: POST /api/v1/auth/login -> Login ke liye client/Postman isi endpoint ko hit karega.
    @PostMapping("/login")


    // ResponseEntity = complete HTTP response ko represent karta hai.
    // ApiResponse<LoginResponse> = hamara custom standard API response.
    // LoginResponse = actual login data, yani: accessToken, tokenType
    // Isliye poora return type:
    // ResponseEntity
    //      ↓
    // ApiResponse<LoginResponse>
    //      ↓
    // LoginResponse
    public ResponseEntity<ApiResponse<LoginResponse>> login(


            // @Valid: LoginRequest ke andar jo validation annotations hain,unko validate karega.
            // Example: @NotBlank, @Email
            // Agar validation fail hui,toh method ke andar login process nahi chalega.
            @Valid


            // @RequestBody: HTTP request ke JSON body ko LoginRequest Java object mein convert karega.
            // Postman se:
            // {
            //     "email": "asif@gmail.com",
            //     "password": "123456"
            // }
            // ye convert hoga: LoginRequest request
            // request.getEmail()
            // request.getPassword()
            @RequestBody LoginRequest request) {


        // authenticationService ke login() method ko call kar rahe hain.
        // request ke andar email + password already aa chuka hai.
        // Actual authentication yahan nahi ho rahi.Ye kaam AuthenticationServiceImpl karega.
        // Flow: Controller -> authenticationService.login(request) -> AuthenticationServiceImpl -> AuthenticationManager -> Password verification -> WT generation
        LoginResponse response = authenticationService.login(request);


        // ResponseEntity.ok(...)
        // HTTP status 200 OK ke saath response return karega.
        // Matlab: HTTP/1.1 200 OK
        // Aur body mein ApiResponse<LoginResponse> jayega.
        return ResponseEntity.ok(


                // Yahan ApiResponse ka Builder use kar rahe hain.
                // ApiResponse<T> generic class hai.
                // <LoginResponse>
                // explicitly bata raha hai ki:
                // T = LoginResponse
                // Matlab: ApiResponse<LoginResponse> Ye type exactly method ke return type se match karega.
                ApiResponse.<LoginResponse>builder()


                        // ApiResponse ka status set kar rahe hain. ResponseStatus enum se SUCCESSS value aa rahi hai.
                        // Final response mein roughly: "status": "SUCCESSS"
                        .status(ResponseStatus.SUCCESSS)


                        // Client ko success message bhej rahe hain.
                        // Final response mein: "message": "Login successful."
                        .message("Login successful.")


                        // Yahan actual login result set kar rahe hain. response variable ke andar LoginResponse hai:
                        // response
                        //   ├── accessToken
                        //   └── tokenType
                        // Isko ApiResponse ke "data" field mein daal rahe hain.
                        .data(response)

                        // Builder se final ApiResponse<LoginResponse> object
                        // create kar raha hai.
                        .build()
        );
    }
}
