package com.globits.da.repository;

import com.globits.da.domain.Commune;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommuneRepository extends JpaRepository<Commune, String> {
    @Query("SELECT new com.globits.da.dto.CommuneDto(co.id, co.name,co.dt_id) FROM Commune co")
    List<CommuneDto> getAllCommune();

    @Query("SELECT new com.globits.da.dto.CommuneDto(co.id, co.name, co.dt_id)  FROM Commune co Where co.dt_id = ?1")
    List<CommuneDto> findByDistrictId(String id);
    @Query("SELECT new com.globits.da.dto.DistrictDto(d.dt_id, d.name, d.prv_id) FROM Commune co INNER JOIN co.district d WHERE co.name = ?1")
    List<DistrictDto> findDistrictNamesByDistrictId(String nameDistrict);
    @Query("SELECT count (co.id) FROM Commune co WHERE co.dt_id = ?1")
    long checkCode(String id);
}
