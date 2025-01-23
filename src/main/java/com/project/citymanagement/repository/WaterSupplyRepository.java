package com.project.citymanagement.repository;

import com.project.citymanagement.entity.WaterSupply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WaterSupplyRepository extends JpaRepository<WaterSupply, Long> {
  List<WaterSupply> findByCityId(Long cityId);

  List<WaterSupply> findByCityIdAndDateBetween(Long cityId, LocalDate startDate, LocalDate endDate);
}
