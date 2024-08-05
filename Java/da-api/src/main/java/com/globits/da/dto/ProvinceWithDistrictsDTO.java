package com.globits.da.dto;


import javax.validation.Valid;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProvinceWithDistrictsDTO {
    @Valid
    private ProvinceDto provinceDto;
    @Valid
    private List<DistrictDto> districtDtos;

}
