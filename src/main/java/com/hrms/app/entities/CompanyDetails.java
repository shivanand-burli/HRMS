package com.hrms.app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyDetails {

    @Column(name = "reg_number")
    private String regNumber;

    private String pan;

    @Column(name = "company_type")
    private String companyType;
}
