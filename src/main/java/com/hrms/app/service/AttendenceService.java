package com.hrms.app.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.hrms.app.config.ValidationError;
import com.hrms.app.entities.Attendance;
import com.hrms.app.entities.AttendanceId;
import com.hrms.app.entities.AttendenceStatus;
import com.hrms.app.repo.AttendenceRepo;
import com.hrms.app.repo.CompanyRepo;
import com.hrms.app.util.Context;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;

@Default
@ApplicationScoped
public class AttendenceService {

    @Inject
    AttendenceRepo attendenceRepo;

    @Inject
    CompanyRepo companyRepo;

    @Inject
    Context ctx;

    public Attendance checkIn() {

        ctx.checkCompanyId();
        ctx.checkEmployeeId();

        final LocalTime lastAllowedCheckInTime = companyRepo.findById(ctx.getCompanyId()).get()
                .getLastAllowedCheckInTime();

        if (ctx.getTime().isAfter(lastAllowedCheckInTime)) {
            throw new ValidationError("Check In time expired!");
        }

        Attendance attendance = getAttendance(ctx.getEmployeeId());
        if (attendance == null) {
            attendance = Attendance.builder()
                    .attendanceStatus(AttendenceStatus.CHECK_IN_APPROVAL_REQUESTED)
                    .checkIn(ctx.getTime())
                    .id(AttendanceId.builder()
                            .attendanceDate(ctx.getDate())
                            .employeeEmail(ctx.getEmployeeId())
                            .companyId(ctx.getCompanyId())
                            .build())
                    .build();
        } else {
            throw new ValidationError("Attendence already present!");
        }

        return attendenceRepo.save(attendance);
    }

    public Attendance checkOut() {
        ctx.checkCompanyId();
        ctx.checkEmployeeId();

        final Attendance attendance = getAttendance(ctx.getEmployeeId());
        if (attendance == null) {
            throw new ValidationError("Attendence not found");
        }

        if (attendance.getAttendanceStatus() == null
                || !attendance.getAttendanceStatus().equals(AttendenceStatus.CHECKED_IN)) {
            throw new ValidationError("Employee not checked in yet!");
        }

        final LocalTime beginCheckOutTime = companyRepo.findById(ctx.getCompanyId()).get()
                .getBeginCheckOutTime();
        if (ctx.getTime().isBefore(beginCheckOutTime)) {
            throw new ValidationError("Check Out time not started!");
        }

        attendance.setAttendanceStatus(AttendenceStatus.CHECK_OUT_APPROVAL_REQUESTED);

        return attendenceRepo.save(attendance);
    }

    public List<Attendance> getAllAttendenceByCompany(LocalDate localDate) {
        ctx.checkCompanyId();
        return attendenceRepo.findByCompanyAndDate(ctx.getCompanyId(), localDate == null ? ctx.getDate() : localDate);
    }

    public Attendance approveRequest(String employeeEmail) {
        final Attendance attendance = getAttendance(employeeEmail);
        if (attendance == null) {
            throw new ValidationError("Attendence not found!");
        }
        AttendenceStatus status = attendance.getAttendanceStatus();
        status = status == null
                ? null
                : AttendenceStatus.CHECK_IN_APPROVAL_REQUESTED.equals(status)
                        ? AttendenceStatus.CHECKED_IN
                        : AttendenceStatus.CHECK_OUT_APPROVAL_REQUESTED.equals(status)
                                ? AttendenceStatus.CHECKED_OUT
                                : null;

        if (status == null) {
            throw new ValidationError("Approve request failed!");
        }

        attendance.setAttendanceStatus(status);
        return attendenceRepo.save(attendance);
    }

    public Attendance getAttendance(String employeeEmail) {
        ctx.checkCompanyId();
        return attendenceRepo.findById(AttendanceId.builder()
                .employeeEmail(employeeEmail)
                .attendanceDate(ctx.getDate())
                .companyId(ctx.getCompanyId())
                .build())
                .orElse(null);
    }
}
