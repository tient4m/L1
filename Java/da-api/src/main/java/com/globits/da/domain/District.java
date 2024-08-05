package com.globits.da.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.globits.da.dto.DistrictDto;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tbl_Districts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class District {
    @Id
    @Column(name = "dt_id")
    private String dt_id;

    @Column(name = "name")
    private String name;

    @Column(name = "prv_id")
    private String prv_id;

    @ManyToOne
    @JoinColumn(name = "prv_id", insertable=false, updatable=false)
    @JsonBackReference
    private Province province;

    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Commune>communes;

    public District(DistrictDto dto) {
        this.dt_id = dto.getDt_id();
        this.name = dto.getName();
        this.prv_id = dto.getPrv_id();
    }
}