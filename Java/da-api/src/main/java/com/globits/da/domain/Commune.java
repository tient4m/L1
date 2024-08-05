package com.globits.da.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.globits.da.dto.CommuneDto;
import lombok.*;


@Entity
@Table(name = "tbl_Communes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Commune {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "dt_id")
    private  String dt_id;

    @ManyToOne
    @JoinColumn(name = "dt_id", insertable=false, updatable=false)
    @JsonBackReference
    private District district;

    public Commune(CommuneDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.dt_id = dto.getDt_id();
    }
}