package com.ecommerce.common.security;


// ye class Authenticated user ki identity aur database ID represent karta hai.

import java.security.Principal;

public record JwtUserPrincipal(String username, Long userId) implements Principal
{
    @Override
    public String getName()
    {
        return username;
    }

}

// Is class se email/username bhi available rahega aur user ki ID bhi.
// getName() override hone ke kaaran existing authentication.getName() usage bhi username return karega.