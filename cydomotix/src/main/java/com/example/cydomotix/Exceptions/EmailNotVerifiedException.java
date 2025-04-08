package com.example.cydomotix.Exceptions;

import org.springframework.security.authentication.InternalAuthenticationServiceException;

public class EmailNotVerifiedException extends InternalAuthenticationServiceException {
    public EmailNotVerifiedException() {
        super("Account not verified by email.");
    }
}
