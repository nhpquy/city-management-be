package com.project.citymanagement.service;

import com.project.citymanagement.entity.City;
import com.project.citymanagement.model.city.CityDto;
import com.project.citymanagement.repository.CityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CityServiceTest {
    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    @Test
    void testGetAllCities() {
        List<City> cityList = List.of(new City());
        when(cityRepository.findAll()).thenReturn(cityList);

        List<City> result = cityService.getAllCities();
        assertEquals(cityList, result);
    }

    @Test
    void testGetCityById() {
        City city = new City();
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        Optional<City> result = cityService.getCityById(1L);
        assertTrue(result.isPresent());
        assertEquals(city, result.get());
    }

    @Test
    void testSaveCity() {
        City city = new City();
        when(cityRepository.save(city)).thenReturn(city);

        City result = cityService.saveCity(city);
        assertEquals(city, result);
    }

    @Test
    void testDeleteCity() {
        doNothing().when(cityRepository).deleteById(1L);

        cityService.deleteCity(1L);
        verify(cityRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRecordsToDto() {
        List<City> cityList = List.of(new City());
        List<CityDto> cityDtoList = CityService.recordsToDto(cityList);

        assertNotNull(cityDtoList);
        assertEquals(cityList.size(), cityDtoList.size());
    }
}
