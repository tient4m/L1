package com.globits.da.rest;

import com.globits.da.AFFakeConstants;
import com.globits.da.dto.CommuneDto;
import com.globits.da.service.CommuneService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Commune")
public class RestCommuneController {

    private CommuneService communeService;

    public RestCommuneController(CommuneService communeService) {
        this.communeService = communeService;
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @GetMapping()
    public ResponseEntity<?> getAllCommune(){
        List<CommuneDto> result = communeService.getAllCommune();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id){
        return ResponseEntity.ok(communeService.findById(id));
    }

    @Secured({AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN})
    @PostMapping()
    public ResponseEntity<?> addCommune(@RequestBody CommuneDto communeDto) {
        return ResponseEntity.ok(communeService.saveOrUpdate(null,communeDto));
    }

    @Secured({AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id){
        if(communeService.deleteById(id)){
            return ResponseEntity.ok().body("Commune deleted successfull by ID:"+ id);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @Secured({AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN})
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProvnice(@PathVariable String id,@RequestBody CommuneDto communeDto){
        try {
            communeService.saveOrUpdate(id,communeDto);
            return ResponseEntity.ok().body("Commune updated successfull by ID:"+ id);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Some thing was wrong: " + e.getMessage());
        }
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @GetMapping("/findByDistrictID/{id}")
    public ResponseEntity<?> findByDistrictId(@PathVariable String id){
        List<CommuneDto> result = communeService.findByDistrictId(id);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
