package com.project.citymanagement.service;

import com.project.citymanagement.entity.City;
import com.project.citymanagement.entity.Electricity;
import com.project.citymanagement.exception.ResourceNotFoundException;
import com.project.citymanagement.model.electricity.ElectricityDataRequest;
import com.project.citymanagement.model.electricity.ElectricityDto;
import com.project.citymanagement.repository.CityRepository;
import com.project.citymanagement.repository.ElectricityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class represents the service for electricity records.
 */
@Service
public class ElectricityService {

  /**
   * The electricity repository.
   */
  @Autowired
  private ElectricityRepository electricityRepository;

  @Autowired
  private CityRepository cityRepository;

  /**
   * Get all electricity records.
   *
   * @return List of all electricity records
   */
  public List<Electricity> getAllElectricityRecords() {
    return electricityRepository.findAll();
  }

  /**
   * Get electricity record by ID.
   *
   * @param id ID of the electricity to be retrieved
   * @return electricity record with the specified ID
   */
  public Optional<Electricity> getElectricityById(Long id) {
    return electricityRepository.findById(id);
  }

  /**
   * @param cityId ID of the city to be retrieved
   * @return List of all electricity records for the specified city
   */
  public List<Electricity> getAllElectricityDataForCity(Long cityId) {
    return electricityRepository.findByCityId(cityId);
  }

  /**
   * @param cityId ID of the city to be retrieved
   * @param startDate start date
   * @param endDate end date
   * @return List of all electricity records for the specified city and period
   */
  public List<Electricity> getElectricityDataForPeriod(Long cityId, LocalDate startDate, LocalDate endDate) {
    return electricityRepository.findByCityIdAndDateBetween(cityId, startDate, endDate);
  }

  /**
   * Save an electricity record.
   *
   * @param request electricity to be saved
   * @return Saved electricity record
   */
  public Electricity saveElectricityData(ElectricityDataRequest request) {
    City city = cityRepository.findById(request.getCityId())
            .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + request.getCityId()));

    Electricity newElectricity = Electricity
            .builder()
            .area(request.getArea())
            .consumptionKwh(request.getConsumptionKwh())
            .outageDurationMinutes(request.getOutageDurationMinutes())
            .outageReason(request.getOutageReason())
            .date(request.getDate())
            .city(city)
            .build();

    return electricityRepository.save(newElectricity);
  }

  /**
   * Save an electricity record.
   *
   * @param request electricity to be saved
   * @return Saved electricity record
   */
  public Electricity updateElectricityData(Long id, ElectricityDataRequest request) {
    Electricity electricity =
            electricityRepository
                    .findById(id)
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Electricity record not found with id: " + id));

    City city = cityRepository.findById(request.getCityId())
            .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + request.getCityId()));

    electricity.setArea(request.getArea());
    electricity.setConsumptionKwh(request.getConsumptionKwh());
    electricity.setOutageDurationMinutes(request.getOutageDurationMinutes());
    electricity.setOutageReason(request.getOutageReason());
    electricity.setDate(request.getDate());
    electricity.setCity(city);

    return electricityRepository.save(electricity);
  }

  /**
   * Remove an electricity record.
   *
   * @param id ID of the electricity record to be removed
   */
  public void deleteElectricityData(Long id) {
    electricityRepository.deleteById(id);
  }

  /**
   * Fetch all power outage data.
   *
   * @return List of electricity records with outages
   */
  public List<Electricity> getOutageData() {
    return electricityRepository.findOutageData();
  }

  /**
   * Fetch area trends by querying the repository.
   *
   * @return List of area trends with total consumption
   */
  public List<Map<String, Object>> getAreaTrends() {
    return electricityRepository.findAreaTrends().stream()
        .map(row -> Map.of(
            "area", row[0],
            "consumptionKwh", row[1]
        ))
        .collect(Collectors.toList());
  }

  /**
   * Import electricity data from a CSV file for a specific city.
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

        // Skip header line area,consumptionKwh,outageDurationMinutes,outageReason,date
        if (data[0].equalsIgnoreCase("area")) continue;

        Electricity electricity = new Electricity();
        electricity.setCity(city);
        electricity.setArea(data[0].trim());
        electricity.setConsumptionKwh(Double.parseDouble(data[1].trim()));
        electricity.setOutageDurationMinutes(Integer.parseInt(data[2].trim()));
        electricity.setOutageReason(data[3].trim());
        electricity.setDate(LocalDate.parse(data[4].trim()));

        electricityRepository.save(electricity);
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
  public static List<ElectricityDto> recordsToDto(List<Electricity> records) {
    return records.stream().map(Electricity::dto).toList();
  }
}
