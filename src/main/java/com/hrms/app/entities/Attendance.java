package com.hrms.app.entities;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "attendance", indexes = {
        @Index(name = "idx_attendance_company_date", columnList = "company_id, attendance_date"),
        @Index(name = "idx_attendance_company_date_status", columnList = "company_id, attendance_date, attendance_status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @NotNull
    @EmbeddedId
    @JsonProperty(value = "details", access = Access.READ_ONLY)
    private AttendanceId id;

    @Column(name = "check_in")
    private LocalTime checkIn;

    @Column(name = "check_out")
    private LocalTime checkOut;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status", nullable = false)
    private AttendenceStatus attendanceStatus;

    @Column(name = "ot_hours")
    private double otHours;
}
