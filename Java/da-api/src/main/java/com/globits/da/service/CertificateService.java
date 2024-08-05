package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.Certificate;
import com.globits.da.dto.CertificateDto;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.List;

@Service
public interface CertificateService extends GenericService<Certificate,Integer> {
     List<CertificateDto> getAllCertificater();
     Boolean deleteById(int id);
     Certificate findById(Integer id);
     boolean addListCertificate(List<Certificate> certificates);
     boolean checkListCertificateValid(List<Certificate> certificates) throws ValidationException;
     CertificateDto saveOrUpdate(Integer id, CertificateDto dto);
}
