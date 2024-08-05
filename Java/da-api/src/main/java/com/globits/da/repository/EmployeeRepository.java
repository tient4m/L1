package com.globits.da.repository;

import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Query("select new com.globits.da.dto.EmployeeDto(ed) from com.globits.da.domain.Employee ed")
    List<EmployeeDto> getAllEmployee();
    @Query("select emp from Employee emp where emp.name LIKE ?1")
    List<EmployeeDto> getByName(String name);
    @Query("select new com.globits.da.dto.EmployeeDto(ed) from Employee ed")
    Page<EmployeeDto> getListPage( Pageable pageable);

    @Query("SELECT Count(emp.code) from Employee emp where emp.code = ?1")
    Long checkCode(String code);
    @Modifying
    @Query("DELETE FROM Employee  Where id = ?1")
    void deleteByID(Integer id);
}
