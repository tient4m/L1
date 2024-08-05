package com.globits.da.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DistrictWithCommunesDTO {
    private DistrictDto districtDto;
    private List<CommuneDto> communeDtos;
}
