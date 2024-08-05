package com.globits.da.dto;
import com.globits.da.domain.Commune;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommuneDto {
    private String id;
    private String name;
    private String dt_id;

    public CommuneDto(Commune entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.dt_id = entity.getDt_id();
    }
}
