package com.hrms.app.resource;

import java.util.List;
import com.hrms.app.config.ValidationError;
import com.hrms.app.entities.Auth;
import com.hrms.app.entities.Company;
import com.hrms.app.repo.CompanyRepo;
import com.hrms.app.service.AuthService;
import com.hrms.app.util.Constants;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

// @RolesAllowed({ "ADMIN", "SUPPORT" })
@PermitAll // TODO remove
@Path("/v1/support")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class SupportResource {

    @Inject
    CompanyRepo companyRepo;

    @Inject
    AuthService authService;

    @POST
    @Path("/create/cmp")
    public Company createCompany(@Valid Company cmp) {
        if (companyRepo.findById(cmp.getEmail()).isPresent()) {
            throw new ValidationError(Constants.CMP_EXISTS);
        }

        cmp.setAuth(Auth.builder().password(authService.hashPass(cmp.getDoc().toString())).build());
        return companyRepo.insert(cmp);
    }

    @GET
    @Path("/cmp/{cmpId}")
    public Company getCompany(@PathParam("cmpId") String cmpId) {
        final Company cmp = companyRepo.findById(cmpId).orElse(null);
        if (cmp == null) {
            throw new ValidationError(Constants.CMP_NT_FOUND);
        }
        return cmp;
    }

    @GET
    @Path("/cmp")
    public List<Company> getAllCompany() {
        return companyRepo.findAll().toList();
    }
}