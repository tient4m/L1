package com.globits.da.repository;

import com.globits.da.domain.Certificate;
import com.globits.da.dto.CertificateDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Integer> {
    @Query("SELECT new com.globits.da.dto.CertificateDto(cer.id,cer.emp_code,cer.cer_id,cer.expiry_date,cer.prv_id) FROM  Certificate cer")
    List<CertificateDto> getAllCertificater();
    @Query("SELECT new com.globits.da.dto.CertificateDto(cer.id,cer.emp_code,cer.cer_id,cer.expiry_date,cer.prv_id) FROM  Certificate cer WHERE cer.emp_code = :employee_code and cer.cer_id = :certificate_id")
    List<CertificateDto> getCertificateOfSameType(@Param("certificate_id") String certificate_id,@Param("employee_code") String employee_code);
}
