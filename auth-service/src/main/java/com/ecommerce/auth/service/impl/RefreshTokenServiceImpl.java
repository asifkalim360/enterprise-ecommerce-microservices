
package com.ecommerce.auth.service.impl;

import com.ecommerce.auth.entity.RefreshToken;
import com.ecommerce.auth.repository.RefreshTokenRepository;
import com.ecommerce.auth.service.RefreshTokenService;
import com.ecommerce.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

@Service // Spring ko batata hai ki ye service-layer class hai; Spring iska object (bean) manage karega.
@RequiredArgsConstructor // Lombok final fields ke liye constructor generate karta hai, jisse dependency injection hota hai.
@Transactional // Is class ke public methods default roop se transaction ke andar execute honge.
public class RefreshTokenServiceImpl implements RefreshTokenService {

    // Repository ki help se RefreshToken table mein data save aur search karenge.
    // final hone ke kaaran Lombok constructor ke through dependency inject karega.
    private final RefreshTokenRepository refreshTokenRepository;

    // application.yml se refresh token ki validity milliseconds mein read hogi.
    // Example: 604800000 milliseconds = 7 days.
    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpirationMs;

    // Cryptographically secure random bytes generate karne ke liye object.
    // SecureRandom predictable random numbers ke muqable mein secure tokens banata hai.
    private final SecureRandom secureRandom = new SecureRandom();

    @Override // Interface ke createRefreshToken() method ko implement kar rahe hain.
    public String createRefreshToken(Long userId)
    {
        // Invalid user ID ke liye token create nahi karna.
        if (userId == null || userId <= 0) {
            throw new BusinessException("Invalid user ID");
        }

        // Expiry configuration zero ya negative nahi honi chahiye.
        if (refreshTokenExpirationMs <= 0) {
            throw new IllegalStateException(
                    "Refresh token expiration must be positive"
            );
        }

        // 32 random bytes ke liye array banaya.
        // 32 bytes = 256 bits ki random entropy, jo token ko guess karna bahut mushkil banati hai.
        byte[] randomBytes = new byte[32];

        // Array mein cryptographically secure random bytes fill honge.
        secureRandom.nextBytes(randomBytes);

        // Random bytes ko URL-safe Base64 string mein convert kiya.
        // withoutPadding() akhir ke '=' padding characters hata deta hai.
        // Ye token ko HTTP requests mein bhejna convenient banata hai.
        String rawToken = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        // Security ke liye raw token database mein store nahi karenge.
        // Uska SHA-256 hash banayenge, taaki DB leak hone par raw token seedha expose na ho.
        String tokenHash = hashToken(rawToken);

        // RefreshToken entity ka naya object builder se create kar rahe hain.
        RefreshToken refreshToken = RefreshToken.builder()

                // Is refresh token ko us user se associate kar rahe hain.
                .userId(userId)

                // Database ke token column mein raw token nahi, hash save hoga.
                .token(tokenHash)

                // Current time mein configured validity add karke expiry set kar rahe hain.
                // Milliseconds ko nanoseconds mein convert karke LocalDateTime mein add kiya.
                // .expiryDate(LocalDateTime.now().plusNanos(refreshTokenExpirationMs * 1_000_000))
                .expiryDate(LocalDateTime.now().plus(Duration.ofMillis(refreshTokenExpirationMs)))

                // Naya token abhi revoked nahi hai, isliye false set kiya.
                .revoked(false)

                // Builder se entity object complete kiya.
                .build();

        // Entity ko database mein insert/save kar rahe hain.
        // @PrePersist laga hai to createdAt jaise fields save hone se pehle set ho sakte hain.
        refreshTokenRepository.save(refreshToken);

        // Client ko original raw token return kar rahe hain.
        // Database mein iska hash hai, isliye verify karte waqt bhi hash banana hoga.
        return rawToken;
    }

    @Override // Interface ke verifyRefreshToken() method ko implement kar rahe hain.
    @Transactional(readOnly = true) // Method ko transaction mein execute karne ka explicit declaration.
    public RefreshToken verifyRefreshToken(String rawToken)
    {
        // Null ya blank token ko hash karne ki zaroorat nahi.
        if (rawToken == null || rawToken.isBlank()) {
            throw new BusinessException("Refresh token is required");
        }

        // Client ke bheje hue raw token ka wahi SHA-256 hash banaya.
        // Same token ka deterministic hash banega, jisse DB record search ho sake.
        String tokenHash = hashToken(rawToken);

        // Hash ke basis par database mein token find kar rahe hain.
        // Record nahi mila to Invalid refresh token exception throw hogi.
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(tokenHash)
                .orElseThrow(() ->
                        new BusinessException("Invalid refresh token"));

        // Check kar rahe hain ki token pehle revoke/logout/rotation ke kaaran invalid to nahi kiya gaya.
        // Logout ya rotation mein revoke hua token dobara use nahi ho sakta.
        if (Boolean.TRUE.equals(refreshToken.getRevoked()))
        {
            // Revoked token se naya Access Token issue nahi hona chahiye.
            throw new BusinessException("Refresh token has been revoked");
        }

        // Expiry time current time ke barabar ya usse pehle ho to token expired maana jayega.
        if(!refreshToken.getExpiryDate().isAfter(LocalDateTime.now()))
        {
            // Expired token reject karenge. Exception transaction rollback kar sakti hai,
            // isliye is method mein revoked status save karne ka claim nahi karenge.
            throw new BusinessException("Refresh token has expired");
        }

        // Token database mein mila, revoked nahi hai aur expire bhi nahi hua.
        // Validated entity caller ko return karte hain.
        // Refresh endpoint is entity se associated user ki identity le sakta hai.
        return refreshToken;
    }

    // Raw token ko SHA-256 hash mein convert karne wala private helper method.
    // Isko class ke andar use karenge; bahar se call karne ki zaroorat nahi.
    private String hashToken(String rawToken) {

        // SHA-256 hashing algorithm ka instance le rahe hain.
        try {
            // SHA-256 ek hi input ke liye consistently same hash deta hai.
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Raw token ko UTF-8 bytes mein convert karke SHA-256 hash banaya.
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));

//            // Hash bytes ko hexadecimal string mein convert karne ke liye builder.
//            StringBuilder hex = new StringBuilder();

//            // Hash ke har byte ko process kar rahe hain.
//            for (byte b : hash)
//            {
//                // Har byte ko unsigned value ke roop mein interpret karke
//                // 2 hexadecimal characters mein convert kar rahe hain.
//                // %02x ensure karta hai ki single-digit byte ke liye leading zero aaye.
//                hex.append(String.format("%02x", b & 0xff));
//            }
//
//            // SHA-256 ke 32 bytes ka 64-character hexadecimal hash return hoga.
//            return hex.toString();

            // 32 hash bytes ko 64-character hexadecimal string banayenge.
            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException exception) {

            // SHA-256 standard Java runtime mein available hota hai.
            // Agar unavailable ho, to unexpected runtime configuration error hai.
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }


    @Override
    @Transactional
    public String rotateRefreshToken(String rawToken)
    {
        // Client ka raw token DB me store nahi hota. Isliye pahle uska SHA-256 hash banayenge.
        if(rawToken == null || rawToken.isBlank())
        {
            throw new BusinessException("Refresh token is required");
        }

        String tokenHash = hashToken(rawToken);

        // Database row ko lock karke token fetch karenge. Esse concurrent rotation requests ko control karne me help milegi.
        RefreshToken oldToken = refreshTokenRepository.findByTokenForUpdate(tokenHash).orElseThrow(() -> new BusinessException("Invalid refresh token"));

        // Pahle use ho chuka token ya fhr logout me revoke hua token reject hoga.
        if(Boolean.TRUE.equals(oldToken.getRevoked()))
        {
            throw new BusinessException("Refresh token has been revoked or already used");
        }

        // Expired token rotate nahi ho sakta hai.
        if(!oldToken.getExpiryDate().isAfter(LocalDateTime.now()))
        {
            throw new BusinessException("Refresh token has expired");
        }

        // Purane token ka user ID new token ke liye use hoga.
        Long userId = oldToken.getUserId();

        // Purane token invalidate kar rahe hain.
        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        // New random raw token generate hoga aur uska hash Db me save hoga.
        // Dono operations esi transaction me honge.
        // Agar new token save fail hua to purana token ka revoke bhi rollback hoga.
        return createRefreshToken(userId);

    }

    @Override
    @Transactional
    public void revokeRefreshToken(String rawToken)
    {
        //Empty token par logout operation continue nahi karna hai.
        if(rawToken == null || rawToken.isBlank())
        {
            throw new BusinessException("Refresh token is required");
        }

        // Database me raw-token nahi uska SHA-256 hash stored hota hai.
        String tokenHash = hashToken(rawToken);

        // Token nahi mila to invalid token reject hoga.
        RefreshToken refreshToken = refreshTokenRepository.findByToken(tokenHash).orElseThrow(() -> new BusinessException("Invalid refresh token"));

        // Already revoked token ko dobara revoke karne ki zaroorat nahi.
        if (Boolean.TRUE.equals(refreshToken.getRevoked()))
        {
            throw new BusinessException("Refresh token has already been revoked");
        }

        // Token ko parmanently unusable mark kar rahe hain.
        refreshToken.setRevoked(true);

        // Update status database me save hoga.
        refreshTokenRepository.save(refreshToken);
    }
}
