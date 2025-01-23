package com.project.citymanagement.service;

import com.project.citymanagement.entity.City;
import com.project.citymanagement.entity.WaterSupply;
import com.project.citymanagement.exception.ResourceNotFoundException;
import com.project.citymanagement.model.watersupply.WaterSupplyDataRequest;
import com.project.citymanagement.model.watersupply.WaterSupplyDto;
import com.project.citymanagement.repository.CityRepository;
import com.project.citymanagement.repository.WaterSupplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * This class represents the service for water supply records.
 */
@Service
public class WaterSupplyService {

  /**
   * The water supply repository.
   */
  @Autowired
  private WaterSupplyRepository waterSupplyRepository;

  @Autowired
  private CityRepository cityRepository;

  /**
   * Get all water supply records.
   *
   * @return List of all water supply records
   */
  public List<WaterSupply> getAllWaterSupplyRecords() {
    return waterSupplyRepository.findAll();
  }

  /**
   * Get water supply record by ID.
   *
   * @param id ID of the water supply record to be retrieved
   * @return water supply record with the specified ID
   */
  public Optional<WaterSupply> getWaterSupplyRecordById(Long id) {
    return waterSupplyRepository.findById(id);
  }

  /**
   * @param cityId ID of the city to be retrieved
   * @return List of all water supply records for the specified city
   */
  public List<WaterSupply> getAllWaterSupplyDataForCity(Long cityId) {
    return waterSupplyRepository.findByCityId(cityId);
  }

  /**
   * @param cityId ID of the city to be retrieved
   * @param startDate start date
   * @param endDate end date
   * @return List of all water supply records for the specified city and period
   */
  public List<WaterSupply> getWaterSupplyDataForPeriod(Long cityId, LocalDate startDate, LocalDate endDate) {
    return waterSupplyRepository.findByCityIdAndDateBetween(cityId, startDate, endDate);
  }

  /**
   * Save a water supply record.
   *
   * @param request water supply record to be saved
   * @return Saved water supply record
   */
  public WaterSupply saveWaterSupplyData(WaterSupplyDataRequest request) {
    City city = cityRepository.findById(request.getCityId())
            .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + request.getCityId()));

    WaterSupply newWaterSupply = WaterSupply
            .builder()
            .area(request.getArea())
            .consumptionLiters(request.getConsumptionLiters())
            .productionLiters(request.getProductionLiters())
            .reservoirLevelPercentage(request.getReservoirLevelPercentage())
            .rainfallMm(request.getRainfallMm())
            .date(request.getDate())
            .city(city)
            .build();

    return waterSupplyRepository.save(newWaterSupply);
  }

  /**
   * Save an Water Supply record.
   *
   * @param request Water Supply to be saved
   * @return Saved Water Supply record
   */
  public WaterSupply updateWaterSupplyData(Long id, WaterSupplyDataRequest request) {
    WaterSupply waterSupply =
            waterSupplyRepository
                    .findById(id)
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Water Supply record not found with id: " + id));

    City city = cityRepository.findById(request.getCityId())
            .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + request.getCityId()));

    waterSupply.setArea(request.getArea());
    waterSupply.setConsumptionLiters(request.getConsumptionLiters());
    waterSupply.setReservoirLevelPercentage(request.getReservoirLevelPercentage());
    waterSupply.setProductionLiters(request.getProductionLiters());
    waterSupply.setRainfallMm(request.getRainfallMm());
    waterSupply.setDate(request.getDate());
    waterSupply.setCity(city);

    return waterSupplyRepository.save(waterSupply);
  }

  /**
   * Remove a water supply record.
   *
   * @param id ID of the water supply record to be removed
   */
  public void deleteWaterSupplyData(Long id) {
    waterSupplyRepository.deleteById(id);
  }

  /**
   * Import water supply data from a CSV file for a specific city.
   *
   * @param cityId ID of the city to import data for
   * @param file CSV file containing the data
   * @return Number of records imported
   */
  public int importDataFromCsvForCity(Long cityId, MultipartFile file) {
    // Check if the city exists
    City city = cityRepository.findById(cityId)
            .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + cityId));

    int recordsCount = 0;
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");

        // Skip header line area,consumptionLiters,productionLiters,reservoirLevelPercentage,rainfallMm,date
        if (data[0].equalsIgnoreCase("area")) continue;

        WaterSupply waterSupply = new WaterSupply();
        waterSupply.setCity(city);
        waterSupply.setArea(data[0].trim());
        waterSupply.setConsumptionLiters(Double.parseDouble(data[1].trim()));
        waterSupply.setProductionLiters(Double.parseDouble(data[2].trim()));
        waterSupply.setReservoirLevelPercentage(Double.parseDouble(data[3].trim()));
        waterSupply.setRainfallMm(Double.parseDouble(data[4].trim()));
        waterSupply.setDate(LocalDate.parse(data[5].trim()));

        waterSupplyRepository.save(waterSupply);
        recordsCount++;
      }
    } catch (IOException ex) {
      throw new RuntimeException("Error reading CSV file: " + ex.getMessage());
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("Invalid data format in CSV: " + ex.getMessage());
    }

    return recordsCount;
  }

  /**
   * Map list of records to list of data transfer objects
   * @param records list of records
   * @return list of record dtos
   */
  public static List<WaterSupplyDto> recordsToDto(List<WaterSupply> records) {
    return records.stream().map(WaterSupply::dto).toList();
  }
}
