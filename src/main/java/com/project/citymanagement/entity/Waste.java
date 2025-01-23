package com.project.citymanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "waste")
public class Waste {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "city_id", nullable = false)
  private City city;

  private LocalDate date;

  private String area;

  private String wasteType; // e.g., "Recyclable", "Organic", "Non-Recyclable"

  private Double quantityKg;

  private String collectionSchedule; // Optional: e.g., "Weekly", "Bi-weekly"
}
