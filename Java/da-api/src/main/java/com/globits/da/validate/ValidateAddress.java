package com.globits.da.validate;

import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ValidateAddress {

    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private CommuneRepository communeRepository;


    public boolean checkListDistrict(List<DistrictDto> districtDtos) {
        Set<String> existingIds = districtDtos.stream()
                .map(DistrictDto::getDt_id)
                .collect(Collectors.toSet());
        List<String> existingIdsList = districtRepository.findAllById(existingIds)
                .stream()
                .map(District::getDt_id)
                .collect(Collectors.toList());
        return existingIds.size() != existingIdsList.size();
    }

    public boolean checkListProvince(List<ProvinceDto> input){
        Set<String> dtoIds = input.stream()
                .map(ProvinceDto::getId)
                .collect(Collectors.toSet());
        List<String> existingIds = provinceRepository.findAllById(dtoIds).stream()
                .map(Province::getId)
                .collect(Collectors.toList());
        return dtoIds.size() != existingIds.size();
    }
    public boolean checkListCommune(List<CommuneDto> communeDtos) {
        Set<String> communeIds = communeDtos.stream()
                .map(CommuneDto::getId)
                .collect(Collectors.toSet());

        List<String> existingIdsList = communeRepository.findAllById(communeIds)
                .stream()
                .map(Commune::getId)
                .collect(Collectors.toList());
        return communeIds.size() != existingIdsList.size();
    }
    public boolean isProvinceValid(String provinceId) {
        Optional<Province> optionalProvince = provinceRepository.findById(provinceId);
        if (!optionalProvince.isPresent()) {
            throw new EntityNotFoundException("Province not found with ID: " + provinceId);
        }
        return true;
    }
    public boolean isDistrictValid(String districtId) {
        Optional<District> optionalDistrict = districtRepository.findById(districtId);
        if (!optionalDistrict.isPresent()) {
            throw new EntityNotFoundException("District not found with ID: " + districtId);
        }
        return true;
    }

    public boolean isDistrictInProvinceForUpdate(String districtId) {
        Optional<District> optionalDistrict = districtRepository.findById(districtId);
        if (!optionalDistrict.isPresent()) {
            throw new EntityNotFoundException("District not found with ID: " + districtId);
        }
        District district = optionalDistrict.get();
        return isProvinceValid(district.getPrv_id());
    }
    public boolean isDistrictInProvince(String districtId, String provinceId) {
        Optional<District> optionalDistrict = districtRepository.findById(districtId);
        if (!optionalDistrict.isPresent()) {
            throw new EntityNotFoundException("District not found with ID: " + districtId);
        }

        District district = optionalDistrict.get();
        if (!district.getPrv_id().equals(provinceId)) {
            throw new IllegalArgumentException("District with ID: " + districtId + " does not belong to Province with ID: " + provinceId);
        }
        return true;
    }
    public boolean isCommuneInDistrict(String communeId, String districtId) {
        Optional<Commune> optionalCommune = communeRepository.findById(communeId);
        if (!optionalCommune.isPresent()) {
            throw new EntityNotFoundException("Commune not found with ID: " + communeId);
        }

        Commune commune = optionalCommune.get();
        if (!commune.getDt_id().equals(districtId)) {
            throw new IllegalArgumentException("Commune with ID: " + communeId + " does not belong to District with ID: " + districtId);
        }
        return true;
    }

    public boolean validateCommune(String provinceId, String districtId, String communeId) {
        isProvinceValid(provinceId);
        isDistrictInProvince(districtId, provinceId);
        isCommuneInDistrict(communeId, districtId);
        return true;
    }

}
