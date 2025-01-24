package com.project.citymanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.citymanagement.entity.City;
import com.project.citymanagement.entity.WaterSupply;
import com.project.citymanagement.exception.ResourceNotFoundException;
import com.project.citymanagement.model.watersupply.WaterSupplyDataRequest;
import com.project.citymanagement.service.WaterSupplyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {WaterSupplyController.class})
@ExtendWith(SpringExtension.class)
class WaterSupplyControllerIntegrationTest {
    @Autowired
    private WaterSupplyController waterSupplyController;

    @MockBean
    private WaterSupplyService waterSupplyService;

    /**
     * Method under test: {@link WaterSupplyController#getAllWaterSupplyRecords()}
     */
    @Test
    void testGetAllWaterSupplyRecords() throws Exception {
        when(this.waterSupplyService.getAllWaterSupplyRecords()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/water-supply");
        MockMvcBuilders.standaloneSetup(this.waterSupplyController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link WaterSupplyController#getAllWaterSupplyRecords()}
     */
    @Test
    void testGetAllWaterSupplyRecords2() throws Exception {
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(123L);
        city.setName("?");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());

        WaterSupply waterSupply = new WaterSupply();
        waterSupply.setArea("?");
        waterSupply.setCity(city);
        waterSupply.setConsumptionLiters(10.0d);
        waterSupply.setDate(LocalDate.ofEpochDay(1L));
        waterSupply.setId(123L);
        waterSupply.setProductionLiters(10.0d);
        waterSupply.setRainfallMm(10.0d);
        waterSupply.setReservoirLevelPercentage(10.0d);

        ArrayList<WaterSupply> waterSupplyList = new ArrayList<>();
        waterSupplyList.add(waterSupply);
        when(this.waterSupplyService.getAllWaterSupplyRecords()).thenReturn(waterSupplyList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/water-supply");
        MockMvcBuilders.standaloneSetup(this.waterSupplyController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"id\":123,\"date\":[1970,1,2],\"area\":\"?\",\"consumptionLiters\":10.0,\"productionLiters\":10.0,\"reservoirL"
                                        + "evelPercentage\":10.0,\"rainfallMm\":10.0,\"city\":{\"id\":123,\"name\":\"?\",\"country\":\"GB\"}}]"));
    }

    /**
     * Method under test: {@link WaterSupplyController#getAllWaterSupplyRecords()}
     */
    @Test
    void testGetAllWaterSupplyRecords3() throws Exception {
        when(this.waterSupplyService.getAllWaterSupplyRecords()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/water-supply");
        getResult.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(this.waterSupplyController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link WaterSupplyController#getAllWaterSupplyRecords()}
     */
    @Test
    void testGetAllWaterSupplyRecords4() throws Exception {
        when(this.waterSupplyService.getAllWaterSupplyRecords())
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/water-supply");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.waterSupplyController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link WaterSupplyController#getWaterSupplyRecordById(Long)}
     */
    @Test
    void testGetWaterSupplyRecordById() throws Exception {
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(123L);
        city.setName("Name");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());

        WaterSupply waterSupply = new WaterSupply();
        waterSupply.setArea("Area");
        waterSupply.setCity(city);
        waterSupply.setConsumptionLiters(10.0d);
        waterSupply.setDate(LocalDate.ofEpochDay(1L));
        waterSupply.setId(123L);
        waterSupply.setProductionLiters(10.0d);
        waterSupply.setRainfallMm(10.0d);
        waterSupply.setReservoirLevelPercentage(10.0d);
        Optional<WaterSupply> ofResult = Optional.of(waterSupply);
        when(this.waterSupplyService.getWaterSupplyRecordById(any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/water-supply/{id}", 123L);
        MockMvcBuilders.standaloneSetup(this.waterSupplyController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":123,\"date\":[1970,1,2],\"area\":\"Area\",\"consumptionLiters\":10.0,\"productionLiters\":10.0,"
                                        + "\"reservoirLevelPercentage\":10.0,\"rainfallMm\":10.0,\"city\":{\"id\":123,\"name\":\"Name\",\"country\":\"GB\"}}"));
    }

    /**
     * Method under test: {@link WaterSupplyController#getWaterSupplyRecordById(Long)}
     */
    @Test
    void testGetWaterSupplyRecordById2() throws Exception {
        when(this.waterSupplyService.getWaterSupplyRecordById(any())).thenReturn(Optional.empty());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/water-supply/{id}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.waterSupplyController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link WaterSupplyController#getWaterSupplyRecordById(Long)}
     */
    @Test
    void testGetWaterSupplyRecordById3() throws Exception {
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(123L);
        city.setName("Name");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());

        WaterSupply waterSupply = new WaterSupply();
        waterSupply.setArea("Area");
        waterSupply.setCity(city);
        waterSupply.setConsumptionLiters(10.0d);
        waterSupply.setDate(LocalDate.ofEpochDay(1L));
        waterSupply.setId(123L);
        waterSupply.setProductionLiters(10.0d);
        waterSupply.setRainfallMm(10.0d);
        waterSupply.setReservoirLevelPercentage(10.0d);
        Optional<WaterSupply> ofResult = Optional.of(waterSupply);
        when(this.waterSupplyService.getWaterSupplyRecordById(any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/water-supply/{id}", 123L);
        getResult.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(this.waterSupplyController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":123,\"date\":[1970,1,2],\"area\":\"Area\",\"consumptionLiters\":10.0,\"productionLiters\":10.0,"
                                        + "\"reservoirLevelPercentage\":10.0,\"rainfallMm\":10.0,\"city\":{\"id\":123,\"name\":\"Name\",\"country\":\"GB\"}}"));
    }

    /**
     * Method under test: {@link WaterSupplyController#getWaterSupplyRecordById(Long)}
     */
    @Test
    void testGetWaterSupplyRecordById4() throws Exception {
        when(this.waterSupplyService.getWaterSupplyRecordById(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/water-supply/{id}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.waterSupplyController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link WaterSupplyController#deleteWaterSupplyRecord(Long)}
     */
    @Test
    void testDeleteWaterSupplyRecord() throws Exception {
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(123L);
        city.setName("Name");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());

        WaterSupply waterSupply = new WaterSupply();
        waterSupply.setArea("Area");
        waterSupply.setCity(city);
        waterSupply.setConsumptionLiters(10.0d);
        waterSupply.setDate(LocalDate.ofEpochDay(1L));
        waterSupply.setId(123L);
        waterSupply.setProductionLiters(10.0d);
        waterSupply.setRainfallMm(10.0d);
        waterSupply.setReservoirLevelPercentage(10.0d);
        Optional<WaterSupply> ofResult = Optional.of(waterSupply);
        doNothing().when(this.waterSupplyService).deleteWaterSupplyData(any());
        when(this.waterSupplyService.getWaterSupplyRecordById(any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/water-supply/{id}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.waterSupplyController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link WaterSupplyController#deleteWaterSupplyRecord(Long)}
     */
    @Test
    void testDeleteWaterSupplyRecord2() throws Exception {
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(123L);
        city.setName("Name");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());

        WaterSupply waterSupply = new WaterSupply();
        waterSupply.setArea("Area");
        waterSupply.setCity(city);
        waterSupply.setConsumptionLiters(10.0d);
        waterSupply.setDate(LocalDate.ofEpochDay(1L));
        waterSupply.setId(123L);
        waterSupply.setProductionLiters(10.0d);
        waterSupply.setRainfallMm(10.0d);
        waterSupply.setReservoirLevelPercentage(10.0d);
        Optional<WaterSupply> ofResult = Optional.of(waterSupply);
        doThrow(new ResourceNotFoundException("An error occurred")).when(this.waterSupplyService)
                .deleteWaterSupplyData(any());
        when(this.waterSupplyService.getWaterSupplyRecordById(any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/water-supply/{id}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.waterSupplyController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link WaterSupplyController#deleteWaterSupplyRecord(Long)}
     */
    @Test
    void testDeleteWaterSupplyRecord3() throws Exception {
        doNothing().when(this.waterSupplyService).deleteWaterSupplyData(any());
        when(this.waterSupplyService.getWaterSupplyRecordById(any())).thenReturn(Optional.empty());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/water-supply/{id}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.waterSupplyController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link WaterSupplyController#getAllWaterSupplyRecordsForCity(Long)}
     */
    @Test
    void testGetAllWaterSupplyRecordsForCity() throws Exception {
        when(this.waterSupplyService.getAllWaterSupplyDataForCity(any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/water-supply/city/{cityId}", 123L);
        MockMvcBuilders.standaloneSetup(this.waterSupplyController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link WaterSupplyController#getAllWaterSupplyRecordsForCity(Long)}
     */
    @Test
    void testGetAllWaterSupplyRecordsForCity2() throws Exception {
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(123L);
        city.setName("?");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());

        WaterSupply waterSupply = new WaterSupply();
        waterSupply.setArea("?");
        waterSupply.setCity(city);
        waterSupply.setConsumptionLiters(10.0d);
        waterSupply.setDate(LocalDate.ofEpochDay(1L));
        waterSupply.setId(123L);
        waterSupply.setProductionLiters(10.0d);
        waterSupply.setRainfallMm(10.0d);
        waterSupply.setReservoirLevelPercentage(10.0d);

        ArrayList<WaterSupply> waterSupplyList = new ArrayList<>();
        waterSupplyList.add(waterSupply);
        when(this.waterSupplyService.getAllWaterSupplyDataForCity(any())).thenReturn(waterSupplyList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/water-supply/city/{cityId}", 123L);
        MockMvcBuilders.standaloneSetup(this.waterSupplyController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"id\":123,\"date\":[1970,1,2],\"area\":\"?\",\"consumptionLiters\":10.0,\"productionLiters\":10.0,\"reservoirL"
                                        + "evelPercentage\":10.0,\"rainfallMm\":10.0,\"city\":{\"id\":123,\"name\":\"?\",\"country\":\"GB\"}}]"));
    }

    /**
     * Method under test: {@link WaterSupplyController#getAllWaterSupplyRecordsForCity(Long)}
     */
    @Test
    void testGetAllWaterSupplyRecordsForCity3() throws Exception {
        when(this.waterSupplyService.getAllWaterSupplyDataForCity(any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/water-supply/city/{cityId}", 123L);
        getResult.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(this.waterSupplyController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link WaterSupplyController#getAllWaterSupplyRecordsForCity(Long)}
     */
    @Test
    void testGetAllWaterSupplyRecordsForCity4() throws Exception {
        when(this.waterSupplyService.getAllWaterSupplyDataForCity(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/water-supply/city/{cityId}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.waterSupplyController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link WaterSupplyController#getWaterSupplyDataForPeriod(Long, String, String)}
     */
    @Test
    void testGetWaterSupplyDataForPeriod() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/water-supply/city/{cityId}/period", "Uri Variables", "Uri Variables")
                .param("endDate", "foo")
                .param("startDate", "foo");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.waterSupplyController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link WaterSupplyController#importWaterSupplyDataForCity(Long, org.springframework.web.multipart.MultipartFile)}
     */
    @Test
    void testImportWaterSupplyDataForCity() throws Exception {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/api/water-supply/city/{cityId}/import",
                "Uri Variables", "Uri Variables");
        MockHttpServletRequestBuilder requestBuilder = postResult.param("file", String.valueOf((Object) null));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.waterSupplyController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link WaterSupplyController#updateWaterSupplyRecord(Long, WaterSupplyDataRequest)}
     */
    @Test
    void testUpdateWaterSupplyRecord() throws Exception {
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(123L);
        city.setName("Name");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());

        WaterSupply waterSupply = new WaterSupply();
        waterSupply.setArea("Area");
        waterSupply.setCity(city);
        waterSupply.setConsumptionLiters(10.0d);
        waterSupply.setDate(LocalDate.ofEpochDay(1L));
        waterSupply.setId(123L);
        waterSupply.setProductionLiters(10.0d);
        waterSupply.setRainfallMm(10.0d);
        waterSupply.setReservoirLevelPercentage(10.0d);
        when(this.waterSupplyService.updateWaterSupplyData(any(), any()))
                .thenReturn(waterSupply);

        WaterSupplyDataRequest waterSupplyDataRequest = new WaterSupplyDataRequest();
        waterSupplyDataRequest.setArea("Area");
        waterSupplyDataRequest.setCityId(123L);
        waterSupplyDataRequest.setConsumptionLiters(10.0d);
        waterSupplyDataRequest.setDate(null);
        waterSupplyDataRequest.setProductionLiters(10.0d);
        waterSupplyDataRequest.setRainfallMm(10.0d);
        waterSupplyDataRequest.setReservoirLevelPercentage(10.0d);
        String content = (new ObjectMapper()).writeValueAsString(waterSupplyDataRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/water-supply/{id}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.waterSupplyController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":123,\"date\":[1970,1,2],\"area\":\"Area\",\"consumptionLiters\":10.0,\"productionLiters\":10.0,"
                                        + "\"reservoirLevelPercentage\":10.0,\"rainfallMm\":10.0,\"city\":{\"id\":123,\"name\":\"Name\",\"country\":\"GB\"}}"));
    }

    /**
     * Method under test: {@link WaterSupplyController#updateWaterSupplyRecord(Long, WaterSupplyDataRequest)}
     */
    @Test
    void testUpdateWaterSupplyRecord2() throws Exception {
        when(this.waterSupplyService.updateWaterSupplyData(any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

        WaterSupplyDataRequest waterSupplyDataRequest = new WaterSupplyDataRequest();
        waterSupplyDataRequest.setArea("Area");
        waterSupplyDataRequest.setCityId(123L);
        waterSupplyDataRequest.setConsumptionLiters(10.0d);
        waterSupplyDataRequest.setDate(null);
        waterSupplyDataRequest.setProductionLiters(10.0d);
        waterSupplyDataRequest.setRainfallMm(10.0d);
        waterSupplyDataRequest.setReservoirLevelPercentage(10.0d);
        String content = (new ObjectMapper()).writeValueAsString(waterSupplyDataRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/water-supply/{id}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.waterSupplyController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

