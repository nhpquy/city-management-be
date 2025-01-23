package com.project.citymanagement.entity;

import com.project.citymanagement.model.electricity.ElectricityDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "electricity")
public class Electricity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    private LocalDate date;

    private String area;

    private Double consumptionKwh;

    private Integer outageDurationMinutes;

    private String outageReason; // Optional: e.g., "Maintenance", "Load-shedding"

    public ElectricityDto dto() {
        return ElectricityDto
                .builder()
                .id(id)
                .area(area)
                .consumptionKwh(consumptionKwh)
                .outageDurationMinutes(outageDurationMinutes)
                .outageReason(outageReason)
                .date(date)
                .city(city.dto())
                .build();
    }
}
