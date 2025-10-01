package com.hrms.app.util;

public final class Constants {

    private Constants() {
    }

    public static final String X_CMP_ID = "X-Company-Id";
    public static final String X_EMP_ID = "X-Employee-Id";

    // Messages
    public static final String CMP_NT_FOUND = "Company not found";
    public static final String CMP_EXISTS = "Company already exists";
    public static final String EMP_NT_FOUND = "Employee not found";
    public static final String EMP_EXISTS = "Employee already exists";

    // Token
    public static final String JWT_ISSUER = "myIssuer";
    public static final String JWT_UPN = "email"; // unique name of this principal

    // Refresh Token
    public static final String INVALID_RT_TOKEN = "Invalid token!";
    public static final String RT_GRP_ID = "GRP";
    public static final String RT_COMPANY = "YUIOPSND";
    public static final String RT_EMPLOYEE = "NSAKFSLE";
}
