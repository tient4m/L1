package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.District;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface DistrictService extends GenericService<District, String> {

    List<DistrictDto> getAllDistrict();
    List<DistrictDto> findByProvinceId(String id);
    Boolean deleteById(String id);
    boolean updateDistrictWithCommunes(DistrictDto districtDto, List<CommuneDto> communeDtos);

    @Transactional
    boolean updateProvinceWithDistricts(ProvinceDto provinceDto, List<DistrictDto> districtDtoss);

    DistrictDto saveOrUpdate(String id, DistrictDto dto);
    Boolean checkCode( String code);
}
