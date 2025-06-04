package com.evega.website_backend.repositories;

import com.evega.website_backend.models.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    Optional<UserToken> findByToken(String token);

    Optional<UserToken> findByUserIdAndDeviceId(Long userId, String deviceId);
    
    Optional<UserToken> findByUserIdAndDeviceIdAndInvalidatedAtIsNull(Long id, String deviceId);

}