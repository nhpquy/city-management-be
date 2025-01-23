package com.project.citymanagement.model.electricity;

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
public class ElectricityDto {
    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate date;

    private String area;

    private Double consumptionKwh;

    private Integer outageDurationMinutes;

    private String outageReason;

    private CityDto city;
}
