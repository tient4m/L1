package com.globits.da.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.globits.da.dto.ProvinceDto;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tbl_Provinces")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Province {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<District> districts;

    public Province(ProvinceDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
    }
}
