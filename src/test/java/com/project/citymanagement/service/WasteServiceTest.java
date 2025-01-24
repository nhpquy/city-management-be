package com.project.citymanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.project.citymanagement.entity.Waste;
import com.project.citymanagement.repository.WasteRepository;
import com.project.citymanagement.service.WasteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class WasteServiceTest {

    @Mock
    private WasteRepository wasteRepository;

    @InjectMocks
    private WasteService wasteService;

    @Test
    void testGetAllWasteRecords() {
        List<Waste> wasteList = List.of(new Waste());
        when(wasteRepository.findAll()).thenReturn(wasteList);

        List<Waste> result = wasteService.getAllWasteRecords();
        assertEquals(wasteList, result);
    }

    @Test
    void testGetWasteRecordById() {
        Waste waste = new Waste();
        when(wasteRepository.findById(1L)).thenReturn(Optional.of(waste));

        Optional<Waste> result = wasteService.getWasteRecordById(1L);
        assertTrue(result.isPresent());
        assertEquals(waste, result.get());
    }

    @Test
    void testGetAllWasteDataForCity() {
        List<Waste> wasteList = List.of(new Waste());
        when(wasteRepository.findByCityId(1L)).thenReturn(wasteList);

        List<Waste> result = wasteService.getAllWasteDataForCity(1L);
        assertEquals(wasteList, result);
    }

    @Test
    void testGetWasteDataForPeriod() {
        List<Waste> wasteList = List.of(new Waste());
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now();
        when(wasteRepository.findByCityIdAndDateBetween(1L, startDate, endDate)).thenReturn(wasteList);

        List<Waste> result = wasteService.getWasteDataForPeriod(1L, startDate, endDate);
        assertEquals(wasteList, result);
    }

    @Test
    void testSaveWasteData() {
        Waste waste = new Waste();
        when(wasteRepository.save(waste)).thenReturn(waste);

        Waste result = wasteService.saveWasteData(waste);
        assertEquals(waste, result);
    }

    @Test
    void testDeleteWasteData() {
        doNothing().when(wasteRepository).deleteById(1L);

        wasteService.deleteWasteData(1L);
        verify(wasteRepository, times(1)).deleteById(1L);
    }
}
