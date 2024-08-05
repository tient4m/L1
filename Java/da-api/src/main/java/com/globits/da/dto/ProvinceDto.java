package com.globits.da.dto;
import com.globits.da.domain.Province;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProvinceDto {
    private String id;
    private String name;

    public ProvinceDto(Province entity) {
        if(entity != null) {
            this.id = entity.getId();
            this.name = entity.getName();
        }
    }
}
