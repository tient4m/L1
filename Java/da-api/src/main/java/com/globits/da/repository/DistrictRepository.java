package com.globits.da.repository;

import com.globits.da.domain.District;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DistrictRepository extends JpaRepository<District, String> {
    @Query("SELECT new com.globits.da.dto.DistrictDto(di.dt_id, di.name, di. prv_id) FROM District di")
    List<DistrictDto> getAllDistrict();
    @Query("SELECT new com.globits.da.dto.DistrictDto(di.dt_id, di.name, di. prv_id) FROM District di WHERE di.prv_id = ?1")
    List<DistrictDto> findByProvinceId(String id);
    @Query("SELECT new com.globits.da.dto.ProvinceDto(p.id , p.name) FROM District di INNER JOIN di.province p WHERE di.name = ?1")
    List<ProvinceDto> findProvinceNamesByProvinceId(String nameDistrict);
    @Query("SELECT Count(di.dt_id) from District di where di.dt_id = ?1")
    long checkCode(String id);


}
