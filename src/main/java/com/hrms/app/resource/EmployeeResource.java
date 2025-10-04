package com.hrms.app.resource;

import com.hrms.app.config.ValidationError;
import com.hrms.app.dao.LoginReq;
import com.hrms.app.dao.ResetPasswordReq;
import com.hrms.app.entities.Attendance;
import com.hrms.app.entities.Employee;
import com.hrms.app.repo.EmployeeRepo;
import com.hrms.app.service.AttendenceService;
import com.hrms.app.service.AuthService;
import com.hrms.app.util.Constants;
import com.hrms.app.util.Role;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RolesAllowed({ "ADMIN", "SUPPORT", "EMPLOYEE" })
@Path("/v1/emp")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class EmployeeResource {

    @Inject
    com.hrms.app.util.Context ctx;

    @Inject
    EmployeeRepo employeeRepo;

    @Inject
    AuthService authService;

    @Inject
    AttendenceService attendenceService;

    @POST
    @Path("/login")
    @PermitAll
    public Response login(@Valid LoginReq loginReq) {
        final Employee emp = employeeRepo.findById(loginReq.getEmail())
                .orElseThrow(() -> new ValidationError(Constants.EMP_NT_FOUND));

        authService.verifyPass(loginReq.getPassword(), emp.getAuth().getPassword());
        return Response.ok(authService.generateToken(emp.getCompany().getEmail(), emp.getEmail(), Role.EMPLOYEE))
                .build();
    }

    @POST
    @Path("/password/reset")
    @PermitAll
    public Response resetPassword(@Valid ResetPasswordReq req) {
        final Employee emp = employeeRepo.findById(req.getEmail())
                .orElseThrow(() -> new ValidationError(Constants.CMP_NT_FOUND));

        authService.verifyPass(req.getOldPassword(), emp.getAuth().getPassword());
        employeeRepo.updatePassword(req.getEmail(), authService.hashPass(req.getNewPassword()));
        return Response.ok().build();
    }

    @POST
    @Path("/refresh")
    @PermitAll
    public Response refreshToken(@HeaderParam("Authorization") String refreshToken) {
        return Response.ok(authService.regenerateToken(refreshToken)).build();
    }

    @GET
    public Employee get() {
        ctx.checkEmployeeId();
        final Employee emp = employeeRepo.findById(ctx.getEmployeeId()).orElse(null);
        if (emp == null) {
            throw new ValidationError(Constants.EMP_NT_FOUND);
        }
        return emp;
    }

    @POST
    @Path("/checkin")
    public Attendance checkIn() {
        return attendenceService.checkIn();
    }

    @POST
    @Path("/checkout")
    public Attendance checkOut() {
        return attendenceService.checkOut();
    }

    @GET
    @Path("/attendence")
    public Attendance getAttendance() {
        ctx.checkEmployeeId();
        return attendenceService.getAttendance(ctx.getEmployeeId());
    }
}