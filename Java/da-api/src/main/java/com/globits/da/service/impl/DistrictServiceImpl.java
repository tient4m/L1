package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.DistrictService;
import com.globits.da.validate.ValidateAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;


@Service
public class DistrictServiceImpl extends GenericServiceImpl<District, String> implements DistrictService {

    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private CommuneRepository communeRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private ValidateAddress validateAddress;

    @Override
    public List<DistrictDto> getAllDistrict(){
        List<DistrictDto> districtDtos = districtRepository.getAllDistrict();
        return districtDtos;
    }
    @Override
    public Boolean deleteById(String id){
        if(districtRepository.findById(id).isPresent()){
            districtRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateDistrictWithCommunes(DistrictDto districtDto, List<CommuneDto> communeDtos) {
        long districtExists = districtRepository.checkCode(districtDto.getDt_id());
        boolean communesValid = validateAddress.checkListCommune(communeDtos);
        if (districtExists != 0L && !communesValid) {
            saveOrUpdate(null,districtDto);
            for (CommuneDto communeDto : communeDtos) {
                Commune save = new Commune(communeDto);
                communeRepository.save(save);
            }
            return true;
        }else {
            return false;
        }
    }
    @Transactional
    @Override
    public boolean updateProvinceWithDistricts(ProvinceDto provinceDto, List<DistrictDto> districtDtoss){
        boolean districtsValid = validateAddress.checkListDistrict(districtDtoss);
        if ((provinceRepository.checkCode(provinceDto.getId()) != 0l) && !districtsValid) {
            Province save = new Province(provinceDto);
            provinceRepository.save(save);
            for (DistrictDto districtDto : districtDtoss){
                saveOrUpdate(districtDto.getDt_id(),districtDto);
            }
            return true;
        }else {
            return false;
        }
    }

    @Override
    public DistrictDto saveOrUpdate(String id, DistrictDto dto) {
        if (dto == null) {
            return null;
        }
        validateAddress.isProvinceValid(dto.getPrv_id());
        District entity;
        Optional<District> existingDistrict = districtRepository.findById(dto.getDt_id());

        if (existingDistrict.isPresent() && !Objects.equals(dto.getDt_id(), id)) {
            throw new IllegalArgumentException("ID mismatch");
        }
        entity = existingDistrict.orElse(new District());
        entity.setDt_id(dto.getDt_id());
        entity.setName(dto.getName());
        entity.setPrv_id(dto.getPrv_id());

        entity = districtRepository.save(entity);

        return new DistrictDto(entity);
    }

    @Override
    public District findById(String id){return districtRepository.findById(id).get();}

    @Override
    public List<DistrictDto> findByProvinceId(String id){
        List<DistrictDto> districts = districtRepository.findByProvinceId(id);
        return districts != null ? districts : new ArrayList<>();
    }

    @Override
    public Boolean checkCode(String code) {
        if (code != null && StringUtils.hasText(code)){
            Long count = districtRepository.checkCode(code);
            return count != 0l;
        }
        return null;
    }


}


