package com.globits.da.rest;

import com.globits.da.AFFakeConstants;
import com.globits.da.domain.Certificate;
import com.globits.da.dto.CertificateDto;
import com.globits.da.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Certificate")
public class RestCertificateController {
    @Autowired
    private CertificateService certificateService;

    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @GetMapping()
    public ResponseEntity<List<CertificateDto>> getAllCertificates() {
        List<CertificateDto> result = certificateService.getAllCertificater();
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(result);
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @GetMapping("/{id}")
    public ResponseEntity<CertificateDto> findCertificateById(@PathVariable int id) {
        Certificate certificate = certificateService.findById(id);
        if (certificate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        CertificateDto certificateDto = new CertificateDto(certificate);
        return ResponseEntity.ok(certificateDto);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN})
    @PostMapping()
    public ResponseEntity<?> addCertificate(@RequestBody CertificateDto certificate) {
        CertificateDto savedCertificate = certificateService.saveOrUpdate(null, certificate);
        if (savedCertificate != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCertificate);
        } else {
            return ResponseEntity.badRequest().body("Unable to create the certificate due to validation errors.");
        }
    }

    @Secured({AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id) {
        boolean deleted = certificateService.deleteById(id);
        if (deleted) {
            return ResponseEntity.ok().body("Certificate deleted successfully by ID: " + id);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Secured({AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN})
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCertificate(@PathVariable Integer id, @RequestBody CertificateDto certificateDto) {
        CertificateDto updatedCertificate = certificateService.saveOrUpdate(id, certificateDto);
        if (updatedCertificate != null) {
            return ResponseEntity.ok(updatedCertificate);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Secured({AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN})
    @PostMapping("/bulk")
    public ResponseEntity<?> addListCertificate(@RequestBody List<Certificate> certificates) {
        boolean success = certificateService.addListCertificate(certificates);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Certificates added successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to add certificates due to validation errors or policy restrictions.");
        }
    }

}
