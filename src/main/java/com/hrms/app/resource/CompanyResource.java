package com.hrms.app.resource;

import java.time.LocalDate;
import java.util.List;
import com.hrms.app.config.ValidationError;
import com.hrms.app.dao.LoginReq;
import com.hrms.app.dao.ResetPasswordReq;
import com.hrms.app.entities.Attendance;
import com.hrms.app.entities.Auth;
import com.hrms.app.entities.Company;
import com.hrms.app.entities.Employee;
import com.hrms.app.repo.CompanyRepo;
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
import jakarta.validation.constraints.Email;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RolesAllowed({ "ADMIN", "SUPPORT", "COMPANY" })
@Path("/v1/cmp")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class CompanyResource {

    @Inject
    com.hrms.app.util.Context ctx;

    @Inject
    CompanyRepo companyRepo;

    @Inject
    EmployeeRepo employeeRepo;

    @Inject
    AuthService authService;

    @Inject
    AttendenceService attendenceService;

    @POST
    @Path("/login")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@Valid LoginReq loginReq) {
        final Company cmp = companyRepo.findById(loginReq.getEmail())
                .orElseThrow(() -> new ValidationError(Constants.CMP_NT_FOUND));

        authService.verifyPass(loginReq.getPassword(), cmp.getAuth().getPassword());
        return Response.ok(authService.generateToken(loginReq.getEmail(), null, Role.COMPANY)).build();
    }

    @POST
    @Path("/password/reset")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    public Response resetPassword(@Valid ResetPasswordReq req) {
        final Company cmp = companyRepo.findById(req.getEmail())
                .orElseThrow(() -> new ValidationError(Constants.CMP_NT_FOUND));

        authService.verifyPass(req.getOldPassword(), cmp.getAuth().getPassword());
        companyRepo.updatePassword(req.getEmail(), authService.hashPass(req.getNewPassword()));
        return Response.ok().build();
    }

    @POST
    @Path("/refresh")
    @PermitAll
    public Response refreshToken(@HeaderParam("Authorization") String refreshToken) {
        return Response.ok(authService.regenerateToken(refreshToken)).build();
    }

    @GET
    public Company get() {
        ctx.checkCompanyId();
        final Company cmp = companyRepo.findById(ctx.getCompanyId()).orElse(null);
        if (cmp == null) {
            throw new ValidationError(Constants.CMP_NT_FOUND);
        }
        return cmp;
    }

    @POST
    @Path("/emp")
    @Consumes(MediaType.APPLICATION_JSON)
    public Employee addEmp(@Valid Employee emp) {
        if (employeeRepo.findById(emp.getEmail()).isPresent()) {
            throw new ValidationError(Constants.EMP_EXISTS);
        }
        final Company cmp = get();
        emp.setCompany(cmp);
        emp.setAuth(Auth.builder().password(authService.hashPass(emp.getDob().toString())).build());
        return employeeRepo.insert(emp);
    }

    @GET
    @Path("/emp")
    public List<Employee> getAllEmp() {
        return companyRepo.findEmployeesByCompanyEmail(ctx.getCompanyId());
    }

    @GET
    @Path("/emp/{empId}")
    public Employee getEmp(@Valid @Email @PathParam("empId") String empId) {
        final Employee emp = companyRepo.findEmployeeByEmailAndCompanyEmail(empId, ctx.getCompanyId()).orElse(null);
        if (emp == null) {
            throw new ValidationError(Constants.EMP_NT_FOUND);
        }
        return emp;
    }

    @GET
    @Path("/attendence")
    public List<Attendance> getAllAttendance(@QueryParam("date") LocalDate date) {
        return attendenceService.getAllAttendenceByCompany(date);
    }

    @POST
    @Path("/attendence/approve/{empId}")
    public Attendance approveAttendence(@PathParam("empId") String empId) {
        return attendenceService.approveRequest(empId);
    }

    @GET
    @Path("/attendence/{empId}")
    public Attendance getEmpAttendance(@PathParam("empId") String empId) {
        return attendenceService.getAttendance(empId);
    }
}