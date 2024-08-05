package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.Province;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProvinceService extends GenericService<Province, String> {
    List<ProvinceDto> getAllProvince();
    Boolean deleteById(String id);
    boolean addProvinceWithDistricts(ProvinceDto province, List<DistrictDto> districtDtoss);
    boolean updateProvinceWithDistricts(ProvinceDto province, List<DistrictDto> districtDtoss) ;
    boolean addProvincDistrictCommune(List<ProvinceDto> provinceDtos, List<DistrictDto> districtDtos, List<CommuneDto> communeDtos);
    ProvinceDto saveOrUpdate(String id, ProvinceDto dto);
    boolean checkCode( String code);
}
