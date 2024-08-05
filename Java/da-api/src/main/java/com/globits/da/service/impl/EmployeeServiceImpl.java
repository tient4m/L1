package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.EmployeeSearchDTO;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.service.EmployeeService;
import com.globits.da.validate.ValidateAddress;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Query;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl extends GenericServiceImpl<Employee, Integer> implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ValidateAddress validateAddress;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ValidateAddress validateAddress) {
        this.employeeRepository = employeeRepository;
        this.validateAddress = validateAddress;
    }


    @Override
    public List<EmployeeDto> getAllEmployee(){
        List<EmployeeDto> listEmployee = employeeRepository.getAllEmployee();
        return listEmployee;
    }

    @Override
    public List<EmployeeSearchDTO> getAllEmployeeSearchDTO() {
        return null;
    }

    @Override
    public Page<EmployeeDto> getListPage(int pageSize, int pageIndex) {
        Pageable pageable = PageRequest.of(pageIndex-1, pageSize);
        return employeeRepository.getListPage(pageable);
    }

    @Override
    public Page<EmployeeDto> searchByPage(EmployeeSearchDTO dto) {
        if(dto == null){
            return null;
        }

        int pageIndex = dto.getPageIndex();
        int pageSize = dto.getPageSize();

        if(pageIndex > 0){
            pageIndex --;
        }else {
            pageIndex = 0;
        }

        String whereClause = "";

        String orderBy = " ORDER BY entity.id DESC ";

        String sqlCount = "select count(entity.code) from Employee as entity where (1=1) ";
        String sql = "select new com.globits.da.dto.EmployeeDto(entity) from Employee as entity where (1=1) ";

        if(dto.getCode() != null && StringUtils.hasText(dto.getCode())){
            whereClause += " AND ( entity.code LIKE :text )";
        }

        sql += whereClause + orderBy;
        sqlCount += whereClause;

        Query q = manager.createQuery(sql, EmployeeDto.class);
        Query qCount = manager.createQuery(sqlCount);

        if (dto.getCode() != null && StringUtils.hasText(dto.getCode())) {
            q.setParameter("text", '%' + dto.getCode() + '%');
            qCount.setParameter("text", '%' + dto.getCode() + '%');
        }

        int startPosition = pageIndex * pageSize;

        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<EmployeeDto> entities = q.getResultList();
        long count = (long) qCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<EmployeeDto> result = new PageImpl<EmployeeDto>(entities, pageable, count);
        return result;
    }

    @Override
    public List<EmployeeDto> getByName(String name) {
        List<EmployeeDto> listEmployee = employeeRepository.getByName(name);
        if (listEmployee != null && !listEmployee.isEmpty()) {
            return listEmployee;
        }
        return new ArrayList<>();
    }
    @Override
    public Employee findById(Integer id) {
        return employeeRepository.findById(id).get();
    }

    @Override
    public boolean addEmployee(Employee employee) {
        boolean check = checkEmployeeCode(employee.getCode());
        Employee emp = null;
        if (check){
            emp = employeeRepository.save(employee);
        }
        return emp != null;
    }

    @Override
    public Boolean deleteByID(Integer id){
        if (id != null){
            employeeRepository.deleteByID(id);
            return true;
        }
        return false;
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        if(employee.getCommune() !=null
                && employee.getProvince() !=null
                && employee.getDistrict() !=null
                && validateAddress.isDistrictInProvinceForUpdate(employee.getDistrict())
                && validateAddress.validateCommune(employee.getProvince(), employee.getDistrict(), employee.getCommune())
                && checkEmployeeCode(employee.getCode())){
            return employeeRepository.save(employee);
        }
        return null;
    }

    @Override
    public void exportExcelFile(List<EmployeeDto> employees, HttpServletResponse response) throws IOException {
        Workbook worbook = new XSSFWorkbook();
        Sheet sheet = worbook.createSheet("Employees");
        String[] columnHeaders = {"STT", "ID", "Mã", "Tên", "Email", "Điện thoại", "Tuổi"};
            Row headerRow = sheet.createRow(0);
            CellStyle headerCellStyle = worbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            for (int i = 0; i < columnHeaders.length; i++){
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnHeaders[i]);
                cell.setCellStyle(headerCellStyle);
        }
            int rowNum = 1;
            for (EmployeeDto employee : employees){
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum - 1);
                row.createCell(1).setCellValue(employee.getId());
                row.createCell(2).setCellValue(employee.getCode());
                row.createCell(3).setCellValue(employee.getName());
                row.createCell(4).setCellValue(employee.getEmail());
                row.createCell(5).setCellValue(employee.getPhone());
                row.createCell(6).setCellValue(employee.getAge());

            }

            for (int i =0; i < columnHeaders.length; i++){
                sheet.autoSizeColumn(i);
            }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=employees.xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        worbook.write(outputStream);
        worbook.close();
        outputStream.close();
        }

    @Override
    public List<String> importEmployee(MultipartFile file) throws IOException{
        List<String> errors = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++){
            Row row = sheet.getRow(rowIndex);
            if (row != null){
                try {
                    Employee employee = new Employee();
                    String code = row.getCell(0).getStringCellValue();
                    if (checkEmployeeCode(code)){
                        employee.setCode("NoData");
                        errors.add("Row " + rowIndex + " has error: Code is duplicated");
                        continue;
                    }else {
                        employee.setCode(code);
                    }
                    employee.setName(row.getCell(1).getStringCellValue());
                    employee.setEmail(row.getCell(2).getStringCellValue());
                    employee.setPhone(row.getCell(3).getStringCellValue());
                    employee.setAge((int) row.getCell(4).getNumericCellValue());
                    String Province = row.getCell(5).getStringCellValue();
                    String District = row.getCell(6).getStringCellValue();
                    String Commune = row.getCell(7).getStringCellValue();
                    if (!validateAddress.validateCommune(Province, District, Commune)){
                        errors.add("Row " + rowIndex + " has error: Address is not valid");
                        continue;
                    }
                    employee.setProvince(Province);
                    employee.setDistrict(District);
                    employee.setCommune(Commune);
                    Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
                    if (!violations.isEmpty()){
                        String errorMessage = violations.stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.joining(", "));
                        errors.add("Row " + rowIndex + " has error: " + errorMessage);
                    } else {
                            employeeRepository.save(employee);
                    }
                } catch (Exception e){
                    errors.add("Row " + rowIndex + " has error: " + e.getMessage());
                }
            }
        }
        workbook.close();
        return errors;
    }

    @Override
    public Boolean checkEmployeeCode(String code) {
        if (code != null && StringUtils.hasText(code)){
            Long count = employeeRepository.checkCode(code);
            return count != 0l;
        }
        return null;
    }
}
