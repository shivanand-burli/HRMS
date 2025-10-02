package com.hrms.app.repo;

import com.hrms.app.entities.Employee;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

@Repository
public interface EmployeeRepo extends CrudRepository<Employee, String> {

    @Query("UPDATE Employee e SET e.auth.password = :password WHERE e.email = :email")
    void updatePassword(String email, String password);
}
