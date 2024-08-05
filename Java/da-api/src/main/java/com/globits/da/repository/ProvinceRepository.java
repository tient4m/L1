package com.globits.da.repository;

import com.globits.da.domain.Province;
import com.globits.da.dto.ProvinceDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProvinceRepository extends JpaRepository<Province, String> {
    @Query("SELECT new com.globits.da.dto.ProvinceDto(p.id, p.name) FROM Province p")
    List<ProvinceDto> getAllProvince();

    @Query("SELECT Count(p.id) from Province p where p.id = ?1")
    long checkCode(String id);
}
