package com.ourdressingtable.security.auth.email.service;

public interface EmailVerificationService {
    void sendVerificationEmail(String email);
    void confirmVerification(String email, String code);
}
