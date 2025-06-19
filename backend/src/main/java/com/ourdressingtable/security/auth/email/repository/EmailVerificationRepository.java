package com.ourdressingtable.security.auth.email.repository;

import java.util.Optional;

public interface EmailVerificationRepository {
    void save(String email, String verificationCode);
    Optional<String> getVerificationCode(String email);
    void markVerified(String email);
    boolean isVerified(String email);
}
