package com.globits.da.dto;

import com.globits.da.domain.Employee;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDto {
    private Integer id;

//    @NotBlank(message = "Name is required")
    private String name;

//    @NotBlank(message = "Code is required")
//    @Pattern(regexp = "^[\\S]{6,10}$", message = "Code must be between 6 and 10 characters and must not contain spaces")
    private String code;

//    @PositiveOrZero(message = "Age must be zero or a positive number")
    private int age;

//    @NotBlank(message = "Phone number us required")
//    @Size(max = 11, message = "Phone number must not exceed 11 characters")
//    @Pattern(regexp = "^[0,9]+$",message = "Phone number must only contain digits")
    private String phone;

//    @Email(message = "Email must be valid")
//    @NotBlank(message = "Email is required")
    private String email;
    private String province;
    private String district;
    private String commune;

    public EmployeeDto(Employee employee) {
        if (employee != null) {
            this.id = employee.getId();
            this.name = employee.getName();
            this.code = employee.getCode();
            this.age = employee.getAge();
            this.phone = employee.getPhone();
            this.email = employee.getEmail();
            this.district = employee.getDistrict();
            this.commune = employee.getCommune();
            this.province = employee.getProvince();
        }
    }
}
