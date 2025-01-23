package com.project.citymanagement.repository;

import com.project.citymanagement.entity.Electricity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ElectricityRepository extends JpaRepository<Electricity, Long> {
  List<Electricity> findByCityId(Long cityId);

  List<Electricity> findByCityIdAndDateBetween(Long cityId, LocalDate startDate, LocalDate endDate);

  @Query("SELECT e FROM Electricity e WHERE e.outageDurationMinutes > 0")
  List<Electricity> findOutageData();

  @Query("SELECT e.area AS area, SUM(e.consumptionKwh) AS totalConsumption " +
      "FROM Electricity e " +
      "GROUP BY e.area")
  List<Object[]> findAreaTrends();
}
