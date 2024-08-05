package com.globits.da.dto;
import com.globits.da.domain.District;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DistrictDto {

    private String dt_id;
    private String name;
    private String prv_id;
    public DistrictDto(District district) {
        if (district != null){
        this.dt_id = district.getDt_id();
        this.name = district.getName();
        this.prv_id = district.getPrv_id();
        }
    }
}
