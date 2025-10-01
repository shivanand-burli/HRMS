package com.hrms.app.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hrms.app.util.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "company")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Company {

    @JsonIgnore
    private final String role = Role.COMPANY.name();

    @Id
    @NotNull
    private String email;
    @NotNull
    private String name;
    @NotNull
    private LocalDate doc;
    private String about;

    @NotNull
    @Column(name = "last_allowed_check_in_time")
    private LocalTime lastAllowedCheckInTime;

    @NotNull
    @Column(name = "begin_check_out_time")
    private LocalTime beginCheckOutTime;

    @NotNull
    @Embedded
    private CompanyDetails details;

    @JsonIgnore
    @Embedded
    private Auth auth;
}
