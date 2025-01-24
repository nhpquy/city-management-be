package com.project.citymanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.project.citymanagement.entity.City;
import com.project.citymanagement.entity.WaterSupply;
import com.project.citymanagement.exception.ResourceNotFoundException;
import com.project.citymanagement.model.watersupply.WaterSupplyDataRequest;
import com.project.citymanagement.repository.CityRepository;
import com.project.citymanagement.repository.WaterSupplyRepository;
import com.project.citymanagement.service.WaterSupplyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class WaterSupplyServiceTest {

    @Mock
    private WaterSupplyRepository waterSupplyRepository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private WaterSupplyService waterSupplyService;

    @Test
    void testGetAllWaterSupplyRecords() {
        List<WaterSupply> waterSupplyList = List.of(new WaterSupply());
        when(waterSupplyRepository.findAll()).thenReturn(waterSupplyList);

        List<WaterSupply> result = waterSupplyService.getAllWaterSupplyRecords();
        assertEquals(waterSupplyList, result);
    }

    @Test
    void testGetWaterSupplyRecordById() {
        WaterSupply waterSupply = new WaterSupply();
        when(waterSupplyRepository.findById(1L)).thenReturn(Optional.of(waterSupply));

        Optional<WaterSupply> result = waterSupplyService.getWaterSupplyRecordById(1L);
        assertTrue(result.isPresent());
        assertEquals(waterSupply, result.get());
    }

    @Test
    void testGetAllWaterSupplyDataForCity() {
        List<WaterSupply> waterSupplyList = List.of(new WaterSupply());
        when(waterSupplyRepository.findByCityId(1L)).thenReturn(waterSupplyList);

        List<WaterSupply> result = waterSupplyService.getAllWaterSupplyDataForCity(1L);
        assertEquals(waterSupplyList, result);
    }

    @Test
    void testGetWaterSupplyDataForPeriod() {
        List<WaterSupply> waterSupplyList = List.of(new WaterSupply());
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now();
        when(waterSupplyRepository.findByCityIdAndDateBetween(1L, startDate, endDate)).thenReturn(waterSupplyList);

        List<WaterSupply> result = waterSupplyService.getWaterSupplyDataForPeriod(1L, startDate, endDate);
        assertEquals(waterSupplyList, result);
    }

    @Test
    void testSaveWaterSupplyData() {
        City city = new City();
        WaterSupplyDataRequest request = new WaterSupplyDataRequest();
        request.setCityId(1L);
        request.setArea("Area");
        request.setConsumptionLiters(100.0);
        request.setProductionLiters(200.0);
        request.setReservoirLevelPercentage(50.0);
        request.setRainfallMm(10.0);
        request.setDate(LocalDate.now());

        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        WaterSupply waterSupply = new WaterSupply();
        when(waterSupplyRepository.save(any(WaterSupply.class))).thenReturn(waterSupply);

        WaterSupply result = waterSupplyService.saveWaterSupplyData(request);
        assertEquals(waterSupply, result);
    }

    @Test
    void testUpdateWaterSupplyData() {
        City city = new City();
        WaterSupply waterSupply = new WaterSupply();
        WaterSupplyDataRequest request = new WaterSupplyDataRequest();
        request.setCityId(1L);
        request.setArea("Area");
        request.setConsumptionLiters(100.0);
        request.setProductionLiters(200.0);
        request.setReservoirLevelPercentage(50.0);
        request.setRainfallMm(10.0);
        request.setDate(LocalDate.now());

        when(waterSupplyRepository.findById(1L)).thenReturn(Optional.of(waterSupply));
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(waterSupplyRepository.save(any(WaterSupply.class))).thenReturn(waterSupply);

        WaterSupply result = waterSupplyService.updateWaterSupplyData(1L, request);
        assertEquals(waterSupply, result);
    }

    @Test
    void testDeleteWaterSupplyData() {
        doNothing().when(waterSupplyRepository).deleteById(1L);

        waterSupplyService.deleteWaterSupplyData(1L);
        verify(waterSupplyRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetWaterSupplyRecordById_NotFound() {
        when(waterSupplyRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<WaterSupply> result = waterSupplyService.getWaterSupplyRecordById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void testSaveWaterSupplyData_CityNotFound() {
        WaterSupplyDataRequest request = new WaterSupplyDataRequest();
        request.setCityId(1L);
        request.setArea("Area");
        request.setConsumptionLiters(100.0);
        request.setProductionLiters(200.0);
        request.setReservoirLevelPercentage(50.0);
        request.setRainfallMm(10.0);
        request.setDate(LocalDate.now());

        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            waterSupplyService.saveWaterSupplyData(request);
        });
    }

    @Test
    void testUpdateWaterSupplyData_WaterSupplyNotFound() {
        WaterSupplyDataRequest request = new WaterSupplyDataRequest();
        request.setCityId(1L);
        request.setArea("Area");
        request.setConsumptionLiters(100.0);
        request.setProductionLiters(200.0);
        request.setReservoirLevelPercentage(50.0);
        request.setRainfallMm(10.0);
        request.setDate(LocalDate.now());

        when(waterSupplyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            waterSupplyService.updateWaterSupplyData(1L, request);
        });
    }

    @Test
    void testUpdateWaterSupplyData_CityNotFound() {
        WaterSupply waterSupply = new WaterSupply();
        WaterSupplyDataRequest request = new WaterSupplyDataRequest();
        request.setCityId(1L);
        request.setArea("Area");
        request.setConsumptionLiters(100.0);
        request.setProductionLiters(200.0);
        request.setReservoirLevelPercentage(50.0);
        request.setRainfallMm(10.0);
        request.setDate(LocalDate.now());

        when(waterSupplyRepository.findById(1L)).thenReturn(Optional.of(waterSupply));
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            waterSupplyService.updateWaterSupplyData(1L, request);
        });
    }

    @Test
    void testDeleteWaterSupplyData_NotFound() {
        doThrow(new ResourceNotFoundException("Water Supply record not found with id: 1")).when(waterSupplyRepository).deleteById(1L);

        assertThrows(ResourceNotFoundException.class, () -> {
            waterSupplyService.deleteWaterSupplyData(1L);
        });
    }
}
