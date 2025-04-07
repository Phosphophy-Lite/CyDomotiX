package com.example.cydomotix.Exceptions;

import org.springframework.security.authentication.InternalAuthenticationServiceException;

public class NotApprovedByAdminException extends InternalAuthenticationServiceException {
    public NotApprovedByAdminException() {
        super("Account not approved by admin.");
    }
}
