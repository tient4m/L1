package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.Commune;
import com.globits.da.dto.CommuneDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommuneService extends GenericService<Commune,String> {
    List<CommuneDto> getAllCommune();
    Boolean deleteById(String id);
//    boolean checkListCommune(List<CommuneDto> communeDtos);
    List<CommuneDto> findByDistrictId(String id);
    CommuneDto saveOrUpdate(String id, CommuneDto dto);
    Boolean checkCode( String code);
//    boolean isCommuneInCorrectDistrictAndProvince(String CommuneId);

}
