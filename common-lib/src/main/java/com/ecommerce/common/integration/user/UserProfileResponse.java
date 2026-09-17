package com.ecommerce.common.integration.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse
{
    private Long id;
    private Long authUserId;
    private String name;
    private String phone;
    private String status;

}

// Ye User Service → Auth Service response contract ke liye hai.
//Lekin abhi Auth registration me hume User Service ka poora response use nahi karna hai.
// Isliye ye class baad me simplify/expand bhi ho sakti hai.


