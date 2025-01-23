package com.project.citymanagement.entity;

import com.project.citymanagement.model.city.CityDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "city")
public class City {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String country;

  @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
  private List<WaterSupply> waterSupplyData;

  @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
  private List<Electricity> electricityData;

  @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
  private List<Waste> wasteData;

  /**
   * Create data transfer object.
   * @return record dto
   */
  public CityDto dto() {
    return CityDto.builder()
            .id(id)
            .name(name)
            .country(country)
            .build();
  }
}
