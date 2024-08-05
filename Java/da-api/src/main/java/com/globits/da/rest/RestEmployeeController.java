package com.globits.da.rest;

import com.globits.da.AFFakeConstants;
import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.EmployeeSearchDTO;
import com.globits.da.response.ApiResponse;
import com.globits.da.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class RestEmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
    public ResponseEntity<Page<EmployeeDto>> getPage(@PathVariable int pageIndex, @PathVariable int pageSize) {
        Page<EmployeeDto> results = employeeService.getListPage(pageSize, pageIndex);
        return new ResponseEntity<Page<EmployeeDto>>(results, HttpStatus.OK);
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @GetMapping()
    public ResponseEntity<List<EmployeeDto>> getAllEmployee(){
        List<EmployeeDto> result = employeeService.getAllEmployee();
        return new ResponseEntity<List<EmployeeDto>>(result, HttpStatus.OK);
    }
    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @GetMapping("/getAllEmployeeSearchDTO")
    public ResponseEntity<List<EmployeeSearchDTO>> getAllEmployeeSearchDTO(){
        List<EmployeeSearchDTO> result = employeeService.getAllEmployeeSearchDTO();
        return new ResponseEntity<List<EmployeeSearchDTO>>(result, HttpStatus.OK);
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @GetMapping("/{name}")
    public ResponseEntity<List<EmployeeDto>> getByName(@PathVariable String name){
        List<EmployeeDto> result = employeeService.getByName(name);
        return new ResponseEntity<List<EmployeeDto>>(result, HttpStatus.OK);
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id){
        Employee employee = employeeService.findById(id);
        EmployeeDto result = convertToDto(employee);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    private EmployeeDto convertToDto(Employee employee) {
        return new EmployeeDto(employee);
    }


    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        Boolean result = employeeService.deleteByID(id);
        if(result){
            return ResponseEntity.ok().body("Employee deleted successfull by ID: "+ id);
        }
        return ResponseEntity.notFound().build();
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @GetMapping("/exportExcelFile")
    public void exportExcelFile(HttpServletResponse response) throws IOException{
        List<EmployeeDto> employees = employeeService.getAllEmployee();
        employeeService.exportExcelFile(employees, response);
    }
    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @PostMapping()
    public ResponseEntity<?> addEmployee(@RequestBody Employee employee){
        boolean savedEmployee = employeeService.addEmployee(employee);
        if (!savedEmployee) {
            return new ResponseEntity<>(new ApiResponse<>(null, "VALIDATION_ERROR", "Employee not saved"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(employee,null,null), HttpStatus.CREATED);
    }
    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @PutMapping()
    public ResponseEntity<?> updateEmployee(@RequestBody Employee employee){
        employeeService.updateEmployee(employee);
        return new ResponseEntity<>(new ApiResponse(employee,null,null), HttpStatus.OK);
    }
    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @PostMapping("/importEmployee")
    public ResponseEntity<?> importEmployee(@RequestParam("file") MultipartFile file) throws IOException {
        List<String> errors = employeeService.importEmployee(file);
        if (errors.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>(null, null, null), HttpStatus.CREATED);
        } else {
            String errorMessage = String.join("; ", errors);
            return new ResponseEntity<>(new ApiResponse<>(null, "VALIDATION_ERROR", errorMessage), HttpStatus.BAD_REQUEST);
        }
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @RequestMapping(value = "/searchByPage", method = RequestMethod.POST)
    public ResponseEntity<Page<EmployeeDto>> searchByPage(@RequestBody EmployeeSearchDTO searchDto) {
        Page<EmployeeDto> page = this.employeeService.searchByPage(searchDto);
        return new ResponseEntity<Page<EmployeeDto>>(page, HttpStatus.OK);
    }
}
