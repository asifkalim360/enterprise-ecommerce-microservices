package com.ecommerce.auth.service;

import com.ecommerce.auth.entity.RefreshToken;

public interface RefreshTokenService
{
    // Naya refresh token generate karke DB me save karega.
    // Return value raw token hogi. jisko baad me client ko diya ja sakta hai.
    String createRefreshToken(Long userId);

    // Client se aaye token ko validate karega.
    // valid token hone pe uska database record return hoga.
    RefreshToken verifyRefreshToken(String token);

    // ye valid token to rotate karega matlab purane token ko revoke aur naya token ko create karega.
    String rotateRefreshToken(String rawToken);

    // Logout ke waqt current refresh token invalidate karega.
    void revokeRefreshToken(String rawToken);

}
