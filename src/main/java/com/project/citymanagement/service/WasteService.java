package com.project.citymanagement.service;

import com.project.citymanagement.entity.Waste;
import com.project.citymanagement.repository.WasteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * This class represents the service for waste records.
 */
@Service
public class WasteService {

  /**
   * The waste repository.
   */
  @Autowired
  private WasteRepository wasteRepository;

  /**
   * Get all waste records.
   *
   * @return List of all waste records
   */
  public List<Waste> getAllWasteRecords() {
    return wasteRepository.findAll();
  }

  /**
   * Get waste record by ID.
   *
   * @param id ID of the waste record to be retrieved
   * @return waste record with the specified ID
   */
  public Optional<Waste> getWasteRecordById(Long id) {
    return wasteRepository.findById(id);
  }

  /**
   * @param cityId ID of the city to be retrieved
   * @return List of all waste records for the specified city
   */
  public List<Waste> getAllWasteDataForCity(Long cityId) {
    return wasteRepository.findByCityId(cityId);
  }

  /**
   * @param cityId ID of the city to be retrieved
   * @param startDate start date
   * @param endDate end date
   * @return List of all waste records for the specified city and period
   */
  public List<Waste> getWasteDataForPeriod(Long cityId, LocalDate startDate, LocalDate endDate) {
    return wasteRepository.findByCityIdAndDateBetween(cityId, startDate, endDate);
  }

  /**
   * Save a waste record.
   *
   * @param electricity waste record to be saved
   * @return Saved waste record
   */
  public Waste saveWasteData(Waste electricity) {
    return wasteRepository.save(electricity);
  }

  /**
   * Remove a waste record.
   *
   * @param id ID of the waste record to be removed
   */
  public void deleteWasteData(Long id) {
    wasteRepository.deleteById(id);
  }
}
