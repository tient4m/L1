package com.globits.da.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrvDctComDTO {
    private List<ProvinceDto> provinceDtos;
    private List<DistrictDto> districtDtos;
    private List<CommuneDto> communeDtos;

}
