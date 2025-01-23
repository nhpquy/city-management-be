package com.project.citymanagement.model.watersupply;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.citymanagement.model.city.CityDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WaterSupplyDto {
    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate date;

    private String area;

    private Double consumptionLiters;

    private Double productionLiters;

    private Double reservoirLevelPercentage;

    private Double rainfallMm;

    private CityDto city;
}
