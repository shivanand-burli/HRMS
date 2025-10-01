package com.hrms.app.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hrms.app.util.Role;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee", indexes = @Index(name = "idx_employee_company", columnList = "company_id"))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Employee {

    @JsonIgnore
    private final String role = Role.EMPLOYEE.name();

    @Id
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String mobile;
    @NotNull
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private LocalDate doj;
    @NotNull
    private LocalDate dob;

    @NotNull
    @Embedded
    private EmployeeDetails details;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @JsonIgnore
    @Embedded
    private Auth auth;
}
