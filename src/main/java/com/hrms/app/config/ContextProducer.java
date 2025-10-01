package com.hrms.app.config;

import org.eclipse.microprofile.jwt.Claim;

import com.hrms.app.util.Constants;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@RequestScoped
public class ContextProducer {

    @Inject
    @Claim(Constants.X_CMP_ID)
    String cmpId;

    @Inject
    @Claim(Constants.X_EMP_ID)
    String empId;

    @Produces
    @RequestScoped
    public com.hrms.app.util.Context currentContext() {
        if (cmpId == null && empId == null) {
            return com.hrms.app.util.Context.EMPTY_CONTEXT;
        }
        return new com.hrms.app.util.Context(cmpId, empId);
    }
}
