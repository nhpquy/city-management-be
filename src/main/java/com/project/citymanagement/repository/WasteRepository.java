package com.project.citymanagement.repository;

import com.project.citymanagement.entity.Waste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WasteRepository extends JpaRepository<Waste, Long> {
  List<Waste> findByCityId(Long cityId);

  List<Waste> findByCityIdAndDateBetween(Long cityId, LocalDate startDate, LocalDate endDate);
}
