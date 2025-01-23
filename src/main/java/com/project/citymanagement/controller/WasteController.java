package com.project.citymanagement.controller;

import com.project.citymanagement.entity.Waste;
import com.project.citymanagement.exception.ResourceNotFoundException;
import com.project.citymanagement.service.WasteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/waste")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Waste Management APIs", description = "API Operations related to managing waste records")
public class WasteController {

  @Autowired
  private WasteService wasteService;

  @Operation(summary = "Get all waste records", description = "Retrieve a list of all waste records")
  @GetMapping
  public List<Waste> getAllWasteRecords() {
    return wasteService.getAllWasteRecords();
  }

  @Operation(summary = "Get all waste records for a city", description = "Retrieve a list of all waste records for a specific city")
  @GetMapping("/city/{cityId}")
  public List<Waste> getAllWasteRecordsForCity(@PathVariable Long cityId) {
    return wasteService.getAllWasteDataForCity(cityId);
  }

  /**
   * Get waste records for a city within a specific date range API.
   *
   * @param cityId    ID of the city
   * @param startDate Start date (inclusive)
   * @param endDate   End date (inclusive)
   * @return List of waste records for the specified period
   */
  @Operation(
      summary = "Get waste data for a specific period",
      description = "Retrieve waste data for a city within a specified date range"
  )
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "Waste data retrieved successfully"),
          @ApiResponse(responseCode = "400", description = "Invalid date range provided"),
      }
  )
  @GetMapping("/city/{cityId}/period")
  public List<Waste> getWasteDataForPeriod(
      @PathVariable Long cityId,
      @RequestParam("startDate") @Parameter(description = "Start date in yyyy-MM-dd format") String startDate,
      @RequestParam("endDate") @Parameter(description = "End date in yyyy-MM-dd format") String endDate) {

    // Parse dates
    LocalDate start = LocalDate.parse(startDate);
    LocalDate end = LocalDate.parse(endDate);

    // Validate date range
    if (start.isAfter(end)) {
      throw new IllegalArgumentException("Start date must be before or equal to end date.");
    }

    // Fetch data from the service
    return wasteService.getWasteDataForPeriod(cityId, start, end);
  }

  @Operation(summary = "Get waste record by ID", description = "Retrieve a specific waste record by its ID")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "Waste record found"),
          @ApiResponse(responseCode = "404", description = "Waste record not found")
      })
  @GetMapping("/{id}")
  public ResponseEntity<Waste> getWasteRecordById(@PathVariable Long id) {
    Waste waste = wasteService.getWasteRecordById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Waste record not found with id: " + id));
    return ResponseEntity.ok(waste);
  }

  @Operation(summary = "Create a new waste record", description = "Create a new waste record")
  @ApiResponse(responseCode = "201", description = "Waste record created successfully")
  @PostMapping
  public Waste createWasteRecord(@RequestBody Waste waste) {
    return wasteService.saveWasteData(waste);
  }

  @Operation(summary = "Update an existing waste record", description = "Update an existing waste record's details")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "Waste record updated"),
          @ApiResponse(responseCode = "404", description = "Waste record not found")
      })
  @PutMapping("/{id}")
  public ResponseEntity<Waste> updateWasteRecord(@PathVariable Long id, @RequestBody Waste wasteDetails) {
    Waste waste = wasteService.getWasteRecordById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Waste record not found with id: " + id));

    waste.setArea(wasteDetails.getArea());
    waste.setWasteType(wasteDetails.getWasteType());
    waste.setQuantityKg(wasteDetails.getQuantityKg());
    waste.setCollectionSchedule(wasteDetails.getCollectionSchedule());

    Waste updatedWaste = wasteService.saveWasteData(waste);
    return ResponseEntity.ok(updatedWaste);
  }

  @Operation(summary = "Delete a waste record", description = "Delete a waste record by ID")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "204", description = "Waste record deleted"),
          @ApiResponse(responseCode = "404", description = "Waste record not found")
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteWasteRecord(@PathVariable Long id) {
    wasteService.getWasteRecordById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Waste record not found with id: " + id));

    wasteService.deleteWasteData(id);
    return ResponseEntity.noContent().build();
  }
}
