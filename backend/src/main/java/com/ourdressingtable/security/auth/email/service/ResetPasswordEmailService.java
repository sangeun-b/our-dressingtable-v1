package com.ourdressingtable.security.auth.email.service;

public interface ResetPasswordEmailService {
    void sendResetLink(String email);
    void resetPassword(String token, String newPassword);
}
