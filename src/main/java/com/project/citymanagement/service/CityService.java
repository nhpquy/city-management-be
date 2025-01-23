package com.project.citymanagement.service;

import com.project.citymanagement.entity.City;
import com.project.citymanagement.model.city.CityDto;
import com.project.citymanagement.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/** This class represents the service for cities. */
@Service
public class CityService {

  /** The city repository. */
  @Autowired private CityRepository cityRepository;

  /**
   * Get all cities.
   *
   * @return List of all cities
   */
  public List<City> getAllCities() {
    return cityRepository.findAll();
  }

  /**
   * Get city by ID.
   *
   * @param id ID of the city to be retrieved
   * @return city with the specified ID
   */
  public Optional<City> getCityById(Long id) {
    return cityRepository.findById(id);
  }

  /**
   * Save an city.
   *
   * @param city city to be saved
   * @return Saved city
   */
  public City saveCity(City city) {
    return cityRepository.save(city);
  }

  /**
   * Update an city.
   *
   * @param id ID of the city to be updated
   */
  public void deleteCity(Long id) {
    cityRepository.deleteById(id);
  }

  /**
   * Map list of records to list of data transfer objects
   * @param records list of records
   * @return list of record dtos
   */
  public static List<CityDto> recordsToDto(List<City> records) {
    return records.stream().map(City::dto).toList();
  }
}
