package com.clinicmanagement.Model;

public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    DOCTOR("ROLE_DOCTOR"),
    NURSE("ROLE_NURSE"),
    STAFF("ROLE_STAFF");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
} 