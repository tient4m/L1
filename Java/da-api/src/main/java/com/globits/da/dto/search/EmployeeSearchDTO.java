package com.globits.da.dto.search;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmployeeSearchDTO {
    private String code;
    private String name;
    private String phone;
    private String email;
    private int pageIndex;
    private int pageSize;
}
