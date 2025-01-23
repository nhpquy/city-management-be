package com.project.citymanagement.entity;

import com.project.citymanagement.model.watersupply.WaterSupplyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "water_supply")
public class WaterSupply {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "city_id", nullable = false)
  private City city;

  private LocalDate date;

  private String area;

  private Double consumptionLiters;

  private Double productionLiters;

  private Double reservoirLevelPercentage;

  private Double rainfallMm;

  public WaterSupplyDto dto() {
    return WaterSupplyDto
            .builder()
            .id(id)
            .area(area)
            .consumptionLiters(consumptionLiters)
            .productionLiters(productionLiters)
            .reservoirLevelPercentage(reservoirLevelPercentage)
            .rainfallMm(rainfallMm)
            .date(date)
            .city(city.dto())
            .build();
  }
}
