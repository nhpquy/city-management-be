package com.project.citymanagement.controller;

import com.project.citymanagement.entity.Electricity;
import com.project.citymanagement.exception.ResourceNotFoundException;
import com.project.citymanagement.model.electricity.ElectricityDataRequest;
import com.project.citymanagement.model.electricity.ElectricityDto;
import com.project.citymanagement.service.ElectricityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * This class represents the REST API controller for electricity records.
 */
@RestController
@RequestMapping("/api/electricity")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Electricity record APIs", description = "API Operations related to managing electricity records")
public class ElectricityController {

    /**
     * The electricity record service.
     */
    @Autowired
    private ElectricityService electricityService;

    /**
     * Get all electricity records API.
     *
     * @return List of all electricity records
     */
    @Operation(summary = "Get all electricity records", description = "Retrieve a list of all electricity records")
    @GetMapping
    public ResponseEntity<List<ElectricityDto>> getAllElectricityRecords() {
        List<ElectricityDto> electricityDtoList = ElectricityService.recordsToDto(electricityService.getAllElectricityRecords());
        return ResponseEntity.ok(electricityDtoList);
    }

    /**
     * Get all electricity records API.
     *
     * @return List of all electricity records
     */
    @Operation(summary = "Get all electricity records", description = "Retrieve a list of all electricity records")
    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<ElectricityDto>> getAllElectricityRecordsForCity(@Parameter(description = "ID of the city record to be retrieved") @PathVariable Long cityId) {
        List<ElectricityDto> electricityDtoList = ElectricityService.recordsToDto(electricityService.getAllElectricityDataForCity(cityId));
        return ResponseEntity.ok(electricityDtoList);
    }

    /**
     * Get electricity records for a city within a specific date range API.
     *
     * @param cityId    ID of the city
     * @param startDate Start date (inclusive)
     * @param endDate   End date (inclusive)
     * @return List of electricity records for the specified period
     */
    @Operation(
            summary = "Get electricity data for a specific period",
            description = "Retrieve electricity data for a city within a specified date range"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Electricity data retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid date range provided"),
            }
    )
    @GetMapping("/city/{cityId}/period")
    public ResponseEntity<List<ElectricityDto>> getElectricityDataForPeriod(
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
        List<ElectricityDto> electricityDtoList = ElectricityService.recordsToDto(electricityService.getElectricityDataForPeriod(cityId, start, end));
        return ResponseEntity.ok(electricityDtoList);
    }

    /**
     * Get electricity record by ID API.
     *
     * @param id ID of the electricity record to be retrieved
     * @return Electricity record with the specified ID
     */
    @Operation(
            summary = "Get electricity record by ID",
            description = "Retrieve a specific electricity record by its ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Electricity record found"),
                    @ApiResponse(responseCode = "404", description = "Electricity record not found")
            })
    @GetMapping("/{id}")
    public ResponseEntity<ElectricityDto> getElectricityRecordById(
            @Parameter(description = "ID of the electricity record to be retrieved") @PathVariable Long id) {
        Electricity electricity =
                electricityService
                        .getElectricityById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Electricity record not found with id: " + id));
        return ResponseEntity.ok(electricity.dto());
    }

    /**
     * Create a new electricity record API.
     *
     * @param request Electricity record object to be created
     * @return Created electricity record object
     */
    @Operation(summary = "Create a new electricity record", description = "Create a new electricity record record")
    @ApiResponse(responseCode = "201", description = "Electricity record created successfully")
    @PostMapping
    public ResponseEntity<ElectricityDto> createElectricityRecord(@Valid @RequestBody ElectricityDataRequest request) {
        Electricity electricity = electricityService.saveElectricityData(request);
        return ResponseEntity.ok(electricity.dto());
    }

    /**
     * Update an existing electricity record API.
     *
     * @param id      ID of the electricity record to be updated
     * @param request Updated electricity record object
     * @return Updated electricity record object
     */
    @Operation(
            summary = "Update an existing electricity record",
            description = "Update an existing electricity record's details")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Electricity record updated"),
                    @ApiResponse(responseCode = "404", description = "Electricity record not found")
            })
    @PutMapping("/{id}")
    public ResponseEntity<ElectricityDto> updateElectricityRecord(
            @Parameter(description = "ID of the electricity record to be updated") @PathVariable Long id,
            @Valid @RequestBody ElectricityDataRequest request) {
        Electricity updatedElectricity = electricityService.updateElectricityData(id, request);
        return ResponseEntity.ok(updatedElectricity.dto());
    }

    /**
     * Delete a electricity record API.
     *
     * @param id ID of the electricity record to be deleted
     * @return Response entity with no content
     */
    @Operation(summary = "Delete a electricity record", description = "Delete a electricity record record by ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Electricity record deleted"),
                    @ApiResponse(responseCode = "404", description = "Electricity record not found")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteElectricityRecord(
            @Parameter(description = "ID of the electricity record to be deleted") @PathVariable Long id) {
        electricityService
                .getElectricityById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Electricity record not found with id: " + id));

        electricityService.deleteElectricityData(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Import electricity data from a CSV file for a specific city API.
     *
     * @param cityId ID of the city to associate the electricity records
     * @param file   CSV file containing electricity records
     * @return Success message with the count of imported records
     */
    @Operation(
            summary = "Import electricity data for a city from CSV",
            description = "Upload a CSV file to import electricity records for a specific city"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Electricity data imported successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid CSV file format"),
                    @ApiResponse(responseCode = "404", description = "City not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error during CSV processing")
            }
    )
    @PostMapping("/city/{cityId}/import")
    public ResponseEntity<String> importElectricityDataForCity(
            @PathVariable Long cityId,
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty.");
        }

        try {
            int recordsImported = electricityService.importDataFromCsvForCity(cityId, file);
            return ResponseEntity.ok(recordsImported + " records imported successfully for city ID: " + cityId);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Invalid input: " + ex.getMessage());
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during import: " + ex.getMessage());
        }
    }

    /**
     * Get all outage data API.
     *
     * @return List of electricity records with outages
     */
    @Operation(summary = "Get all outage data", description = "Retrieve a list of electricity records with outages")
    @GetMapping("/outages")
    public ResponseEntity<List<ElectricityDto>> getOutageData() {
        List<ElectricityDto> electricityDtoList = ElectricityService.recordsToDto(electricityService.getOutageData());
        return ResponseEntity.ok(electricityDtoList);
    }

    /**
     * Analyze area trends API.
     *
     * @return List of area trends with total consumption
     */
    @Operation(summary = "Analyze area trends", description = "Retrieve area-wise electricity consumption trends")
    @GetMapping("/area-trends")
    public ResponseEntity<List<Map<String, Object>>> getAreaTrends() {
        return ResponseEntity.ok(electricityService.getAreaTrends());
    }
}
