package com.ourdressingtable.auth.email.repository;

import java.time.Duration;
import java.util.Optional;

public interface ResetTokenRepository {
    void save(String token, String email, Duration ttl);
    Optional<String> getEmailByToken(String token);
    void delete(String token);
}
