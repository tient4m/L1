package com.globits.da.domain;
import com.globits.da.dto.CertificateDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tbl_Certificates")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Certificate {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "cer_id")
    private String cer_id;

    @Column(name = "emp_code")
    private String emp_code;


    @Column(name = "expiry_date")
    private LocalDate expiry_date;

    @Column(name = "prv_id")
    private String prv_id;

    public Certificate (CertificateDto certificateDto){
        this.id = certificateDto.getId();
        this.cer_id = certificateDto.getCer_id();
        this.emp_code = certificateDto.getEmp_code();
        this.expiry_date = certificateDto.getExpiry_date();
        this.prv_id = certificateDto.getPrv_id();
    }

}
