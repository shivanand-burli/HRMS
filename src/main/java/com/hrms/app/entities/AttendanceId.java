package com.hrms.app.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceId implements Serializable {

    @NotNull
    @Column(name = "employee_email")
    private String employeeEmail;
    @NotNull
    @Column(name = "attendance_date")
    private LocalDate attendanceDate;

    @NotNull
    @JsonIgnore
    @Column(name = "company_id", nullable = false)
    private String companyId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AttendanceId))
            return false;
        AttendanceId that = (AttendanceId) o;
        return Objects.equals(employeeEmail, that.employeeEmail) &&
                Objects.equals(attendanceDate, that.attendanceDate) &&
                Objects.equals(companyId, that.companyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeEmail, attendanceDate, companyId);
    }
}
