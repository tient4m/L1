package com.globits.da.rest;

import com.globits.da.AFFakeConstants;
import com.globits.da.dto.*;
import com.globits.da.service.ProvinceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/province")
public class RestProvinceController {

    private final ProvinceService provinceService;


    public RestProvinceController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAllProvince(){
        List<ProvinceDto> result = provinceService.getAllProvince();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Secured({  AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN })
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id){
        return ResponseEntity.ok(provinceService.findById(id));
    }

    @Secured({AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addProvince(@RequestBody ProvinceDto provinceDto) {
        return ResponseEntity.ok(provinceService.saveOrUpdate(null,provinceDto));
    }

    @Secured({AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id){
        if(provinceService.deleteById(id)){
            return ResponseEntity.ok().body("Province deleted successfull by ID:"+ id);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @Secured({AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN})
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProvnice(@PathVariable String id,@RequestBody ProvinceDto dto){
        return ResponseEntity.ok(provinceService.saveOrUpdate(id,dto));
    }

    @Secured({AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN})
    @PostMapping("/addProvinceWithDistricts")
    public ResponseEntity<?> addProvinceWithDistricts(@RequestBody @Valid ProvinceWithDistrictsDTO input) {
        try {
            boolean addedSuccessfully = provinceService.addProvinceWithDistricts(input.getProvinceDto(), input.getDistrictDtos());
            if (!addedSuccessfully) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add province and districts: Existing data or validation errors.");
            }
            return ResponseEntity.ok("Province and districts added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add province and districts: " + e.getMessage());
        }
    }
    @Secured({AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN})
    @PutMapping("/updateProvinceWithDistricts")
    public ResponseEntity<?> updateProvinceWithDistricts(@RequestBody ProvinceWithDistrictsDTO request) {
        ProvinceDto provinceDto = request.getProvinceDto();
        List<DistrictDto> districtDtos = request.getDistrictDtos();
        try {
            boolean updatedSuccessfully = provinceService.updateProvinceWithDistricts(provinceDto, districtDtos);
            if (!updatedSuccessfully) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update province and districts: Existing data or validation errors.");
            }
            return ResponseEntity.ok("Province and districts updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add province and districts: " + e.getMessage());
        }
    }
    @Secured({AFFakeConstants.ROLE_ADMIN, AFFakeConstants.ROLE_SUPER_ADMIN})
    @PostMapping("/addProvideDistrictCommune")
    public ResponseEntity<?> addProviceDistrictCommune(@RequestBody PrvDctComDTO prvDctComDTO) {
        List<ProvinceDto> provinceDtos = prvDctComDTO.getProvinceDtos();
        List<DistrictDto> districtDtos = prvDctComDTO.getDistrictDtos();
        List<CommuneDto> communeDtos = prvDctComDTO.getCommuneDtos();
        boolean addStatus = provinceService.addProvincDistrictCommune(provinceDtos, districtDtos, communeDtos);
        if (!addStatus) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add");
        }
        return ResponseEntity.ok("Province and districts added successfully!");
    }

}
