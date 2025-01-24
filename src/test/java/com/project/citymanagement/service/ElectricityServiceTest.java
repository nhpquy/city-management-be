package com.project.citymanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.project.citymanagement.entity.City;
import com.project.citymanagement.entity.Electricity;
import com.project.citymanagement.exception.ResourceNotFoundException;
import com.project.citymanagement.model.electricity.ElectricityDataRequest;
import com.project.citymanagement.repository.CityRepository;
import com.project.citymanagement.repository.ElectricityRepository;
import com.project.citymanagement.service.ElectricityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ElectricityServiceTest {

    @Mock
    private ElectricityRepository electricityRepository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private ElectricityService electricityService;


    @Test
    void testGetAllElectricityRecords() {
        List<Electricity> electricityList = List.of(new Electricity());
        when(electricityRepository.findAll()).thenReturn(electricityList);

        List<Electricity> result = electricityService.getAllElectricityRecords();
        assertEquals(electricityList, result);
    }

    @Test
    void testGetElectricityById() {
        Electricity electricity = new Electricity();
        when(electricityRepository.findById(1L)).thenReturn(Optional.of(electricity));

        Optional<Electricity> result = electricityService.getElectricityById(1L);
        assertTrue(result.isPresent());
        assertEquals(electricity, result.get());
    }

    @Test
    void testGetAllElectricityDataForCity() {
        List<Electricity> electricityList = List.of(new Electricity());
        when(electricityRepository.findByCityId(1L)).thenReturn(electricityList);

        List<Electricity> result = electricityService.getAllElectricityDataForCity(1L);
        assertEquals(electricityList, result);
    }

    @Test
    void testGetElectricityDataForPeriod() {
        List<Electricity> electricityList = List.of(new Electricity());
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now();
        when(electricityRepository.findByCityIdAndDateBetween(1L, startDate, endDate)).thenReturn(electricityList);

        List<Electricity> result = electricityService.getElectricityDataForPeriod(1L, startDate, endDate);
        assertEquals(electricityList, result);
    }

    @Test
    void testSaveElectricityData() {
        City city = new City();
        ElectricityDataRequest request = new ElectricityDataRequest();
        request.setCityId(1L);
        request.setArea("Area");
        request.setConsumptionKwh(100.0);
        request.setOutageDurationMinutes(60);
        request.setOutageReason("Reason");
        request.setDate(LocalDate.now());

        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        Electricity electricity = new Electricity();
        when(electricityRepository.save(any(Electricity.class))).thenReturn(electricity);

        Electricity result = electricityService.saveElectricityData(request);
        assertEquals(electricity, result);
    }

    @Test
    void testUpdateElectricityData() {
        City city = new City();
        Electricity electricity = new Electricity();
        ElectricityDataRequest request = new ElectricityDataRequest();
        request.setCityId(1L);
        request.setArea("Area");
        request.setConsumptionKwh(100.0);
        request.setOutageDurationMinutes(60);
        request.setOutageReason("Reason");
        request.setDate(LocalDate.now());

        when(electricityRepository.findById(1L)).thenReturn(Optional.of(electricity));
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(electricityRepository.save(any(Electricity.class))).thenReturn(electricity);

        Electricity result = electricityService.updateElectricityData(1L, request);
        assertEquals(electricity, result);
    }

    @Test
    void testDeleteElectricityData() {
        doNothing().when(electricityRepository).deleteById(1L);

        electricityService.deleteElectricityData(1L);
        verify(electricityRepository, times(1)).deleteById(1L);
    }

    @Test
    void testSaveElectricityData_CityNotFound() {
        ElectricityDataRequest request = new ElectricityDataRequest();
        request.setCityId(1L);
        request.setArea("Area");
        request.setConsumptionKwh(100.0);
        request.setOutageDurationMinutes(60);
        request.setOutageReason("Reason");
        request.setDate(LocalDate.now());

        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            electricityService.saveElectricityData(request);
        });
    }

    @Test
    void testUpdateElectricityData_ElectricityNotFound() {
        ElectricityDataRequest request = new ElectricityDataRequest();
        request.setCityId(1L);
        request.setArea("Area");
        request.setConsumptionKwh(100.0);
        request.setOutageDurationMinutes(60);
        request.setOutageReason("Reason");
        request.setDate(LocalDate.now());

        when(electricityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            electricityService.updateElectricityData(1L, request);
        });
    }

    @Test
    void testUpdateElectricityData_CityNotFound() {
        Electricity electricity = new Electricity();
        ElectricityDataRequest request = new ElectricityDataRequest();
        request.setCityId(1L);
        request.setArea("Area");
        request.setConsumptionKwh(100.0);
        request.setOutageDurationMinutes(60);
        request.setOutageReason("Reason");
        request.setDate(LocalDate.now());

        when(electricityRepository.findById(1L)).thenReturn(Optional.of(electricity));
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            electricityService.updateElectricityData(1L, request);
        });
    }

    @Test
    void testDeleteElectricityData_NotFound() {
        doThrow(new ResourceNotFoundException("Electricity record not found with id: 1")).when(electricityRepository).deleteById(1L);

        assertThrows(ResourceNotFoundException.class, () -> {
            electricityService.deleteElectricityData(1L);
        });
    }
}
