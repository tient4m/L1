package com.globits.da.dto;
import com.globits.da.domain.Certificate;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateDto {
    private int id;
    private String cer_id;
    private String emp_code;
    private LocalDate expiry_date;
    private String prv_id;

    public CertificateDto(Certificate certificate) {
        this.id = certificate.getId();
        this.cer_id = certificate.getCer_id();
        this.emp_code = certificate.getEmp_code();
        this.expiry_date = certificate.getExpiry_date();
        this.prv_id = certificate.getPrv_id();
    }
}

