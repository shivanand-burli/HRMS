package com.hrms.app.repo;

import java.time.LocalDate;
import java.util.List;

import com.hrms.app.entities.Attendance;
import com.hrms.app.entities.AttendanceId;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Param;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

@Repository
public interface AttendenceRepo extends CrudRepository<Attendance, AttendanceId> {

    @Query("SELECT a FROM Attendance a WHERE a.id.companyId = :companyId AND a.id.attendanceDate = :date")
    List<Attendance> findByCompanyAndDate(@Param("companyId") String companyId,
            @Param("date") LocalDate date);
}
