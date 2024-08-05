package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.domain.Commune;
import com.globits.da.dto.CommuneDto;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.service.CommuneService;
import com.globits.da.validate.ValidateAddress;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class CommuneServiceImpl extends GenericServiceImpl<Commune, String> implements CommuneService {
    private final CommuneRepository communeRepository;
    private final ValidateAddress validateAddress;

    public CommuneServiceImpl(CommuneRepository communeRepository, ValidateAddress validateAddress) {
        this.communeRepository = communeRepository;
        this.validateAddress = validateAddress;
    }

    @Override
    public List<CommuneDto> getAllCommune(){
        List<CommuneDto> communeDtos = communeRepository.getAllCommune();
        return communeDtos;
    }

    @Override
    public Boolean deleteById(String id ){
        if (communeRepository.findById(id).isPresent()){
            communeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Commune findById(String id){return communeRepository.findById(id).get();}

    @Override
    public List<CommuneDto> findByDistrictId(String id) {
        List<CommuneDto> communeDtos = communeRepository.findByDistrictId(id);
        if ( communeDtos == null){
            return new ArrayList<>();
        }
        return communeDtos;
    }

    @Override
    public CommuneDto saveOrUpdate(String id, CommuneDto dto) {
        if (dto == null) {
            return null;
        }
        validateAddress.isDistrictValid(dto.getDt_id());
        Commune entity;
        Optional<Commune> existingCommune = communeRepository.findById(dto.getId());
        if (existingCommune.isPresent() && !Objects.equals(id, dto.getId())) {
            throw new IllegalArgumentException("ID mismatch");
        }
        entity = existingCommune.orElse(new Commune());
        entity.setName(dto.getName());
        entity.setId(dto.getId());
        entity.setDt_id(dto.getDt_id());

        entity = communeRepository.save(entity);
        return new CommuneDto(entity);
    }
    @Override
    public Boolean checkCode( String code) {
        if(code != null && StringUtils.hasText(code)) {
            Long count = communeRepository.checkCode(code);
            return count != 0l;
        }
        return null;
    }

}
