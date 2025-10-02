package com.hrms.app.repo;

import java.util.List;
import java.util.Optional;

import com.hrms.app.entities.Company;
import com.hrms.app.entities.Employee;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

@Repository
public interface CompanyRepo extends CrudRepository<Company, String> {

    @Query("SELECT e FROM Employee e WHERE e.company.email = :companyEmail")
    List<Employee> findEmployeesByCompanyEmail(String companyEmail);

    @Query("SELECT e FROM Employee e WHERE e.email = :email AND e.company.email = :companyEmail")
    Optional<Employee> findEmployeeByEmailAndCompanyEmail(String email, String companyEmail);

    @Query("UPDATE Company c SET c.auth.password = :password WHERE c.email = :email")
    void updatePassword(String email, String password);
}
