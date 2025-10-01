package com.hrms.app.entities;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeDetails {
    @NotNull
    @Column(scale = 2)
    private BigDecimal salary;
    private String aadhar;
    private String pan;
    @Column(name = "account_no")
    private Long accountNo;
    private String ifsc;
}
