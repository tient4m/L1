package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.EmployeeSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public interface EmployeeService extends GenericService<Employee, Integer>  {
    List<EmployeeDto> getAllEmployee();
    List<EmployeeSearchDTO> getAllEmployeeSearchDTO();
    Page<EmployeeDto> getListPage(int pageSize, int pageIndex);
    Page<EmployeeDto> searchByPage(EmployeeSearchDTO searchDto);
    List<EmployeeDto> getByName(String name);
    Boolean deleteByID(Integer id);
    Employee updateEmployee(Employee employee);
    Employee findById(Integer id);
    boolean addEmployee(Employee employee);
    void exportExcelFile(List<EmployeeDto> employees, HttpServletResponse response)throws IOException;
    List<String> importEmployee(MultipartFile file) throws IOException;
    Boolean checkEmployeeCode(String code);
}
