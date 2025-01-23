package com.project.citymanagement.controller;

import com.project.citymanagement.entity.City;
import com.project.citymanagement.exception.ResourceNotFoundException;
import com.project.citymanagement.model.city.CityDto;
import com.project.citymanagement.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class represents the REST API controller for cities.
 */
@RestController
@RequestMapping("/api/city")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Cities APIs", description = "API Operations related to managing cities")
public class CityController {

    /**
     * The city service.
     */
    @Autowired
    private CityService cityService;

    /**
     * Get all cities API.
     *
     * @return List of all cities
     */
    @Operation(summary = "Get all cities", description = "Retrieve a list of all cities")
    @GetMapping
    public ResponseEntity<List<CityDto>> getAllCities() {
        List<CityDto> cityList = CityService.recordsToDto(cityService.getAllCities());
        return ResponseEntity.ok(cityList);
    }

    /**
     * Get city by ID API.
     *
     * @param id ID of the city to be retrieved
     * @return City with the specified ID
     */
    @Operation(
            summary = "Get city by ID",
            description = "Retrieve a specific city by their ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "City found"),
                    @ApiResponse(responseCode = "404", description = "City not found")
            })
    @GetMapping("/{id}")
    public ResponseEntity<CityDto> getCityById(@PathVariable Long id) {
        City city =
                cityService
                        .getCityById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
        return ResponseEntity.ok(city.dto());
    }

    /**
     * Create a new city API.
     *
     * @param city New city details
     * @return New city record
     */
    @Operation(summary = "Create a new city", description = "Create a new city record")
    @PostMapping
    public ResponseEntity<CityDto> createCity(@RequestBody CityDto city) {
        City newCity = City.builder()
                .name(city.getName())
                .country(city.getCountry())
                .build();

        newCity = cityService.saveCity(newCity);

        return ResponseEntity.ok(newCity.dto());
    }

    /**
     * Update an existing city API.
     *
     * @param id          ID of the city to be updated
     * @param cityDetails Updated city details
     * @return Updated city record
     */
    @Operation(
            summary = "Update an existing city",
            description = "Update an existing city's details")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "City updated"),
                    @ApiResponse(responseCode = "404", description = "City not found")
            })
    @PutMapping("/{id}")
    public ResponseEntity<CityDto> updateCity(
            @PathVariable Long id, @RequestBody CityDto cityDetails) {
        City city =
                cityService
                        .getCityById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
        city.setName(cityDetails.getName());
        city.setCountry(cityDetails.getCountry());

        City updatedCity = cityService.saveCity(city);
        return ResponseEntity.ok(updatedCity.dto());
    }

    /**
     * Delete an city API.
     *
     * @param id ID of the city to be deleted
     * @return No content
     */
    @Operation(summary = "Delete an city", description = "Delete an city record by ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "City deleted"),
                    @ApiResponse(responseCode = "404", description = "City not found")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        cityService
                .getCityById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));

        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }
}
