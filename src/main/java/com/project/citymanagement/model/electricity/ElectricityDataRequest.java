package com.project.citymanagement.model.electricity;

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
public class ElectricityDataRequest {
    @NotNull
    private Long cityId;

    @NotNull
    private Double consumptionKwh;

    @NotNull
    private String area;

    private Integer outageDurationMinutes;

    private String outageReason;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate date;
}
