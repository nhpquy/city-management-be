package com.project.citymanagement.controller;

import com.project.citymanagement.entity.WaterSupply;
import com.project.citymanagement.exception.ResourceNotFoundException;
import com.project.citymanagement.model.watersupply.WaterSupplyDataRequest;
import com.project.citymanagement.model.watersupply.WaterSupplyDto;
import com.project.citymanagement.service.WaterSupplyService;
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

@RestController
@RequestMapping("/api/water-supply")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Water Supply APIs", description = "API Operations related to managing water supply records")
public class WaterSupplyController {

    @Autowired
    private WaterSupplyService waterSupplyService;

    @Operation(summary = "Get all water supply records", description = "Retrieve a list of all water supply records")
    @GetMapping
    public ResponseEntity<List<WaterSupplyDto>> getAllWaterSupplyRecords() {
        List<WaterSupplyDto> waterSupplyDtoList = WaterSupplyService.recordsToDto(waterSupplyService.getAllWaterSupplyRecords());
        return ResponseEntity.ok(waterSupplyDtoList);
    }

    @Operation(summary = "Get all water supply records for a city", description = "Retrieve a list of all water supply records for a specific city")
    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<WaterSupplyDto>> getAllWaterSupplyRecordsForCity(@PathVariable Long cityId) {
        List<WaterSupplyDto> waterSupplyDtoList = WaterSupplyService.recordsToDto(waterSupplyService.getAllWaterSupplyDataForCity(cityId));
        return ResponseEntity.ok(waterSupplyDtoList);
    }

    /**
     * Get water supply records for a city within a specific date range API.
     *
     * @param cityId    ID of the city
     * @param startDate Start date (inclusive)
     * @param endDate   End date (inclusive)
     * @return List of water supply records for the specified period
     */
    @Operation(
            summary = "Get water supply data for a specific period",
            description = "Retrieve water supply data for a city within a specified date range"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Water Supply data retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid date range provided"),
            }
    )
    @GetMapping("/city/{cityId}/period")
    public ResponseEntity<List<WaterSupplyDto>> getWaterSupplyDataForPeriod(
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
        List<WaterSupplyDto> waterSupplyDtoList = WaterSupplyService.recordsToDto(waterSupplyService.getWaterSupplyDataForPeriod(cityId, start, end));
        return ResponseEntity.ok(waterSupplyDtoList);
    }

    @Operation(summary = "Get water supply record by ID", description = "Retrieve a specific water supply record by its ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Water Supply record found"),
                    @ApiResponse(responseCode = "404", description = "Water Supply record not found")
            })
    @GetMapping("/{id}")
    public ResponseEntity<WaterSupplyDto> getWaterSupplyRecordById(@PathVariable Long id) {
        WaterSupply waterSupply = waterSupplyService.getWaterSupplyRecordById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Water supply record not found with id: " + id));
        return ResponseEntity.ok(waterSupply.dto());
    }

    @Operation(summary = "Create a new water supply record", description = "Create a new water supply record")
    @ApiResponse(responseCode = "201", description = "Water Supply record created successfully")
    @PostMapping
    public ResponseEntity<WaterSupplyDto> createWaterSupplyRecord(@Valid @RequestBody WaterSupplyDataRequest request) {
        WaterSupply waterSupply = waterSupplyService.saveWaterSupplyData(request);
        return ResponseEntity.ok(waterSupply.dto());
    }

    @Operation(summary = "Update an existing water supply record", description = "Update an existing water supply record's details")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Water Supply record updated"),
                    @ApiResponse(responseCode = "404", description = "Water Supply record not found")
            })
    @PutMapping("/{id}")
    public ResponseEntity<WaterSupplyDto> updateWaterSupplyRecord(@PathVariable Long id, @Valid @RequestBody WaterSupplyDataRequest request) {
        WaterSupply updatedWaterSupply = waterSupplyService.updateWaterSupplyData(id, request);
        return ResponseEntity.ok(updatedWaterSupply.dto());
    }

    @Operation(summary = "Delete a water supply record", description = "Delete a water supply record by ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Water Supply record deleted"),
                    @ApiResponse(responseCode = "404", description = "Water Supply record not found")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWaterSupplyRecord(@PathVariable Long id) {
        waterSupplyService.getWaterSupplyRecordById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Water supply record not found with id: " + id));

        waterSupplyService.deleteWaterSupplyData(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Import water supply data from a CSV file for a specific city API.
     *
     * @param cityId ID of the city to associate the water supply records
     * @param file   CSV file containing water supply records
     * @return Success message with the count of imported records
     */
    @Operation(
            summary = "Import water supply data for a city from CSV",
            description = "Upload a CSV file to import water supply records for a specific city"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Water supply data imported successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid CSV file format"),
                    @ApiResponse(responseCode = "404", description = "City not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error during CSV processing")
            }
    )
    @PostMapping("/city/{cityId}/import")
    public ResponseEntity<String> importWaterSupplyDataForCity(
            @PathVariable Long cityId,
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty.");
        }

        try {
            int recordsImported = waterSupplyService.importDataFromCsvForCity(cityId, file);
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
}
