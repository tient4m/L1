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
import com.globits.da.service.ProvinceService;
import com.globits.da.validate.ValidateAddress;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProvinceServiceImpl extends GenericServiceImpl<Province, String> implements ProvinceService {

    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final CommuneRepository communeRepository;
    private final ValidateAddress validateAddress;
    private final DistrictService districtService;
    public ProvinceServiceImpl(ProvinceRepository provinceRepository, DistrictRepository districtRepository, CommuneRepository communeRepository, ValidateAddress validateAddress,DistrictService districtService) {
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.communeRepository = communeRepository;
        this.validateAddress = validateAddress;
        this.districtService = districtService;
    }

    @Override
    public List<ProvinceDto> getAllProvince(){
        List<ProvinceDto> provinceDtos = provinceRepository.getAllProvince();
        return provinceDtos;
    }
    @Override
    public ProvinceDto saveOrUpdate(String id, ProvinceDto dto) {
        if (dto == null) {
            return null;
        }
        if (dto.getId() != null && (id != null && !dto.getId().equals(id))) {
            return null;
        }
        Province entity = provinceRepository.findById(dto.getId())
                .orElse(new Province());
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity = provinceRepository.save(entity);
        return new ProvinceDto(entity);
    }
    @Override
    public boolean checkCode(String code) {
        if (code != null && StringUtils.hasText(code)){
            long count = provinceRepository.checkCode(code);
            return count != 0l;
        }
        return false;
    }
    @Override
    public Boolean deleteById(String id){
        if(provinceRepository.findById(id).isPresent()){
            provinceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean addProvinceWithDistricts(ProvinceDto provinceDto, List<DistrictDto> districtDtos) {
        if (provinceDto == null || districtDtos == null) {
            return false;
        }
        boolean districtsValid = validateAddress.checkListDistrict(districtDtos);
        List<District> districts = districtDtos.stream()
                .map(District::new)
                .collect(Collectors.toList());
        if (districtsValid) {
            saveOrUpdate(null,provinceDto);
            districtRepository.saveAll(districts);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    @Override
    public boolean updateProvinceWithDistricts(ProvinceDto provinceDto, List<DistrictDto> districtDtoss){
        boolean districtsValid = validateAddress.checkListDistrict(districtDtoss);
        if ((provinceRepository.checkCode(provinceDto.getId()) != 0l) && !districtsValid) {
            saveOrUpdate(null,provinceDto);
            for (DistrictDto districtDto : districtDtoss){
                District saveDistrict;
                Optional<District> save = districtRepository.findById(districtDto.getDt_id());
                    saveDistrict = save.get();
                    saveDistrict.setName(districtDto.getName());
                    saveDistrict.setPrv_id(districtDto.getPrv_id());
                    districtRepository.save(saveDistrict);
            }
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean addProvincDistrictCommune(List<ProvinceDto> provinceDtos, List<DistrictDto> districtDtos, List<CommuneDto> communeDtos) {
        boolean provinceValid = validateAddress.checkListProvince(provinceDtos);
        boolean districtValid = validateAddress.checkListDistrict(districtDtos);
        boolean communeValid = validateAddress.checkListCommune(communeDtos);

        if (provinceValid && districtValid && communeValid) {
            List<Province> provinces = provinceDtos.stream().map(Province::new).collect(Collectors.toList());
            provinceRepository.saveAll(provinces);
            List<District> district = districtDtos.stream().map(District::new).collect(Collectors.toList());
            districtRepository.saveAll(district);
            List<Commune> communes = communeDtos.stream().map(Commune::new).collect(Collectors.toList());
            communeRepository.saveAll(communes);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Province findById(String id) {
        return provinceRepository.findById(id).get();
    }
}
