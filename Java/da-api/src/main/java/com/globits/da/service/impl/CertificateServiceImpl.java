package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.domain.Certificate;
import com.globits.da.dto.CertificateDto;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CertificateServiceImpl extends GenericServiceImpl<Certificate, Integer> implements CertificateService {

    @Autowired
    CertificateRepository certificateRepository;

    @Override
    public List<CertificateDto> getAllCertificater(){
        List<CertificateDto> certificateDtos = certificateRepository.getAllCertificater();
        return certificateDtos;
    }

    @Override
    public Boolean deleteById(int id){
        if (certificateRepository.existsById(id)) {
            certificateRepository.deleteById(id);
            return true;
        }
        return false;
    }
    @Override
    public Certificate findById(Integer id) {
        return certificateRepository.findById(id).get();
    }

    @Override
    public boolean addListCertificate(List<Certificate> certificates) {
        if(checkListCertificateValid(certificates)){
            certificateRepository.saveAll(certificates);
            return true;
        }
        return false;
    }



    @Override
    public boolean checkListCertificateValid(List<Certificate> certificates) {
        for (Certificate certificate : certificates) {
            List<CertificateDto> certificateDtos = certificateRepository.getCertificateOfSameType(certificate.getCer_id(), certificate.getEmp_code());
            long validCount = certificateDtos.stream()
                    .filter(c -> c.getExpiry_date().isAfter(LocalDate.now()))
                    .count();
            if (validCount >= 3) {
                throw new RuntimeException("Employee has 3 valid certificates of the same type");
            }
            boolean hasValidSameTypeAndProvince = certificateDtos.stream()
                    .anyMatch(c -> c.getExpiry_date().isAfter(LocalDate.now()) &&
                            c.getPrv_id().equals(certificate.getPrv_id()));
            if (hasValidSameTypeAndProvince) {
                throw new RuntimeException("Employee has a valid certificate of the same type and province");
            }
        }
        return true;
    }

    @Override
    public CertificateDto saveOrUpdate(Integer id,CertificateDto dto) {
        if (dto == null) {
            return null;
        }

        if (id != null && dto.getId() != id ) {
            return null;
        }

        Certificate entity = certificateRepository.findById(dto.getId())
                .orElse(new Certificate());

        entity.setCer_id(dto.getCer_id());
        entity.setEmp_code(dto.getEmp_code());
        entity.setExpiry_date(dto.getExpiry_date());
        entity.setPrv_id(dto.getPrv_id());

        Certificate savedEntity = certificateRepository.save(entity);
        return new CertificateDto(savedEntity);
    }

}
