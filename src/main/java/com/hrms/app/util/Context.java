package com.hrms.app.util;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Context {

    public static final Context EMPTY_CONTEXT = new Context();
    private final LocalDate date = LocalDate.now(Clock.systemUTC());
    private final LocalTime time = LocalTime.now(Clock.systemUTC()).withNano(0);

    private String companyId;
    private String employeeId;

    public void checkCompanyId() {
        Validator.requiresNonNull(this.companyId, "companyId");
    }

    public void checkEmployeeId() {
        Validator.requiresNonNull(this.employeeId, "employeeId");
    }
}
