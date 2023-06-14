package com.dnc.auth.model.enums;

public enum AuthenticationType {
    BY_USERNAME("USERNAME"),
    BY_EMAIL("EMAIL");

    private String type;

    AuthenticationType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
