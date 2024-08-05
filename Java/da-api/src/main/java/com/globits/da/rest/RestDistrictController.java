package com.globits.da.rest;

import com.globits.da.AFFakeConstants;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.DistrictWithCommunesDTO;
import com.globits.da.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/District")
public class RestDistrictController {
    @Autowired
    private DistrictService districtService;

    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @GetMapping()
    public ResponseEntity<?> getAllDistrict(){
        List<DistrictDto> result = districtService.getAllDistrict();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id){
        return ResponseEntity.ok(districtService.findById(id));
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @GetMapping("/findByProvinceId/{id}")
    public ResponseEntity<List<DistrictDto>> findByProvinceId(@PathVariable String id) {
        List<DistrictDto> result = districtService.findByProvinceId(id);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN})
    @PostMapping()
    public ResponseEntity<?> addDistrict(@RequestBody DistrictDto districtDto) {
        return ResponseEntity.ok(districtService.saveOrUpdate(null,districtDto));
    }

    @Secured({AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id){
        if(districtService.deleteById(id)){
            return ResponseEntity.ok().body("District deleted successfull by ID:"+ id);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @Secured({AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN})
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDistrict(@PathVariable String id,@RequestBody DistrictDto districtDto){
        try {
                districtService.saveOrUpdate(id,districtDto);
                return ResponseEntity.ok().body(districtDto);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Some thing was wrong: " + e.getMessage());
            }
    }


    @Secured({AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN})
    @PutMapping("/updateDistrictWithCommunes")
    public ResponseEntity<?> updateDistrictWithCommunes(@RequestBody DistrictWithCommunesDTO request){
        DistrictDto districtDto = request.getDistrictDto();
        List<CommuneDto> communeDtos = request.getCommuneDtos();
        try{
            boolean updatedSuccess = districtService.updateDistrictWithCommunes(districtDto, communeDtos);
            if(!updatedSuccess){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("District update failed");
            }
            return ResponseEntity.ok().body("District updated successfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("District update failed");
        }
    }
}
