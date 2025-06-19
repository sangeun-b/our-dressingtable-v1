package com.ourdressingtable.security.auth.email.service;

public interface EmailService {
    void send(String to, String subject, String body);
}
