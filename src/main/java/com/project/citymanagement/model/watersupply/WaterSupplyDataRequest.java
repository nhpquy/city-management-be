package com.project.citymanagement.model.watersupply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaterSupplyDataRequest {
    @NotNull
    private Long cityId;

    @NotNull
    private Double consumptionLiters;

    @NotNull
    private String area;

    private Double productionLiters;

    private Double reservoirLevelPercentage;

    private Double rainfallMm;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate date;
}
