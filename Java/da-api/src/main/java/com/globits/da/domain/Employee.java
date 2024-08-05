package com.globits.da.domain;


import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_employees")
public class Employee {
    @Getter
    @Setter
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Min(value = 0, message = "Age must be non-negative")
    @Column(name = "age",nullable = false)
    private Integer age;

    @Column(name = "code", nullable = false)
    @Length(min = 6,max = 10,message = "Code must be between 6 and 10 characters")
    @Pattern(regexp = "^[^\\s]*$", message = "Code must not contain spaces")
    private String code;

    @Email(message = "Email must be a valid email format")
    @Column(name = "email", nullable = false)
    @NotBlank(message = "Email must not be blank")
    @Pattern(regexp = "^[^\\s]*$", message = "Code must not contain spaces")
    private String email;

    @Column(name = "name")
    @NotBlank(message = "Name is required")
    private String name;

    @Pattern(regexp = "^[0-9]{1,11}$", message = "Phone must be numeric and not more than 11 characters")
    @Column(name = "phone")
    private String phone;

    @NotBlank(message = "Province is required")
    @Column(name = "province")
    private String province;

    @NotBlank(message = "District is required")
    @Column(name = "district")
    private String district;

    @NotBlank(message = "Commune is required")
    @Column(name = "commune")
    private String commune;


}