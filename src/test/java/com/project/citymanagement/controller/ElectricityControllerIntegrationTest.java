package com.project.citymanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.citymanagement.entity.City;
import com.project.citymanagement.entity.Electricity;
import com.project.citymanagement.exception.ResourceNotFoundException;
import com.project.citymanagement.model.electricity.ElectricityDataRequest;
import com.project.citymanagement.service.ElectricityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ElectricityController.class})
@ExtendWith(SpringExtension.class)
class ElectricityControllerIntegrationTest {
    @Autowired
    private ElectricityController electricityController;

    @MockBean
    private ElectricityService electricityService;

    /**
     * Test
     * {@link ElectricityController#createElectricityRecord(ElectricityDataRequest)}.
     * <ul>
     *   <li>Given {@link City#City()} Country is {@code GB}.</li>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link ElectricityController#createElectricityRecord(ElectricityDataRequest)}
     */
    @Test
    @DisplayName("Test createElectricityRecord(ElectricityDataRequest); given City() Country is 'GB'; then status isOk()")
    void testCreateElectricityRecord_givenCityCountryIsGb_thenStatusIsOk() throws Exception {
        // Arrange
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(1L);
        city.setName("Name");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());

        Electricity electricity = new Electricity();
        electricity.setArea("Area");
        electricity.setCity(city);
        electricity.setConsumptionKwh(10.0d);
        electricity.setDate(LocalDate.of(1970, 1, 1));
        electricity.setId(1L);
        electricity.setOutageDurationMinutes(1);
        electricity.setOutageReason("Just cause");
        when(electricityService.saveElectricityData(Mockito.<ElectricityDataRequest>any())).thenReturn(electricity);

        ElectricityDataRequest electricityDataRequest = new ElectricityDataRequest();
        electricityDataRequest.setArea("Area");
        electricityDataRequest.setCityId(1L);
        electricityDataRequest.setConsumptionKwh(10.0d);
        electricityDataRequest.setDate(null);
        electricityDataRequest.setOutageDurationMinutes(1);
        electricityDataRequest.setOutageReason("Just cause");
        String content = (new ObjectMapper()).writeValueAsString(electricityDataRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/electricity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(electricityController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"date\":[1970,1,1],\"area\":\"Area\",\"consumptionKwh\":10.0,\"outageDurationMinutes\":1,\"outageReason\":\"Just"
                                        + " cause\",\"city\":{\"id\":1,\"name\":\"Name\",\"country\":\"GB\"}}"));
    }

    /**
     * Test {@link ElectricityController#getAllElectricityRecords()}.
     * <p>
     * Method under test: {@link ElectricityController#getAllElectricityRecords()}
     */
    @Test
    @DisplayName("Test getAllElectricityRecords()")
    void testGetAllElectricityRecords() throws Exception {
        // Arrange
        when(electricityService.getAllElectricityRecords()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/electricity");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(electricityController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Test {@link ElectricityController#getAllElectricityRecords()}.
     * <ul>
     *   <li>Given {@link City#City()} Country is {@code GB}.</li>
     *   <li>Then content string a string.</li>
     * </ul>
     * <p>
     * Method under test: {@link ElectricityController#getAllElectricityRecords()}
     */
    @Test
    @DisplayName("Test getAllElectricityRecords(); given City() Country is 'GB'; then content string a string")
    void testGetAllElectricityRecords_givenCityCountryIsGb_thenContentStringAString() throws Exception {
        // Arrange
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(1L);
        city.setName("?");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());

        Electricity electricity = new Electricity();
        electricity.setArea("?");
        electricity.setCity(city);
        electricity.setConsumptionKwh(10.0d);
        electricity.setDate(LocalDate.of(1970, 1, 1));
        electricity.setId(1L);
        electricity.setOutageDurationMinutes(1);
        electricity.setOutageReason("Just cause");

        ArrayList<Electricity> electricityList = new ArrayList<>();
        electricityList.add(electricity);
        when(electricityService.getAllElectricityRecords()).thenReturn(electricityList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/electricity");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(electricityController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"id\":1,\"date\":[1970,1,1],\"area\":\"?\",\"consumptionKwh\":10.0,\"outageDurationMinutes\":1,\"outageReason\":\"Just"
                                        + " cause\",\"city\":{\"id\":1,\"name\":\"?\",\"country\":\"GB\"}}]"));
    }

    /**
     * Test {@link ElectricityController#getAllElectricityRecords()}.
     * <ul>
     *   <li>Given {@link City#City()} Country is {@code GBR}.</li>
     *   <li>Then content string a string.</li>
     * </ul>
     * <p>
     * Method under test: {@link ElectricityController#getAllElectricityRecords()}
     */
    @Test
    @DisplayName("Test getAllElectricityRecords(); given City() Country is 'GBR'; then content string a string")
    void testGetAllElectricityRecords_givenCityCountryIsGbr_thenContentStringAString() throws Exception {
        // Arrange
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(1L);
        city.setName("?");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());

        Electricity electricity = new Electricity();
        electricity.setArea("?");
        electricity.setCity(city);
        electricity.setConsumptionKwh(10.0d);
        electricity.setDate(LocalDate.of(1970, 1, 1));
        electricity.setId(1L);
        electricity.setOutageDurationMinutes(1);
        electricity.setOutageReason("Just cause");

        City city2 = new City();
        city2.setCountry("GBR");
        city2.setElectricityData(new ArrayList<>());
        city2.setId(2L);
        city2.setName("Name");
        city2.setWasteData(new ArrayList<>());
        city2.setWaterSupplyData(new ArrayList<>());

        Electricity electricity2 = new Electricity();
        electricity2.setArea("Area");
        electricity2.setCity(city2);
        electricity2.setConsumptionKwh(0.5d);
        electricity2.setDate(LocalDate.of(1970, 1, 1));
        electricity2.setId(2L);
        electricity2.setOutageDurationMinutes(0);
        electricity2.setOutageReason("?");

        ArrayList<Electricity> electricityList = new ArrayList<>();
        electricityList.add(electricity2);
        electricityList.add(electricity);
        when(electricityService.getAllElectricityRecords()).thenReturn(electricityList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/electricity");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(electricityController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"id\":2,\"date\":[1970,1,1],\"area\":\"Area\",\"consumptionKwh\":0.5,\"outageDurationMinutes\":0,\"outageReason"
                                        + "\":\"?\",\"city\":{\"id\":2,\"name\":\"Name\",\"country\":\"GBR\"}},{\"id\":1,\"date\":[1970,1,1],\"area\":\"?\",\"consumptionKwh"
                                        + "\":10.0,\"outageDurationMinutes\":1,\"outageReason\":\"Just cause\",\"city\":{\"id\":1,\"name\":\"?\",\"country\":\"GB"
                                        + "\"}}]"));
    }

    /**
     * Test {@link ElectricityController#getAllElectricityRecords()}.
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ElectricityController#getAllElectricityRecords()}
     */
    @Test
    @DisplayName("Test getAllElectricityRecords(); then status isNotFound()")
    void testGetAllElectricityRecords_thenStatusIsNotFound() throws Exception {
        // Arrange
        when(electricityService.getAllElectricityRecords()).thenThrow(new ResourceNotFoundException("An error occurred"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/electricity");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(electricityController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test {@link ElectricityController#getAllElectricityRecordsForCity(Long)}.
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link ElectricityController#getAllElectricityRecordsForCity(Long)}
     */
    @Test
    @DisplayName("Test getAllElectricityRecordsForCity(Long); then status isNotFound()")
    void testGetAllElectricityRecordsForCity_thenStatusIsNotFound() throws Exception {
        // Arrange
        when(electricityService.getAllElectricityDataForCity(Mockito.<Long>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/electricity/city/{cityId}", 1L);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(electricityController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test {@link ElectricityController#getAllElectricityRecordsForCity(Long)}.
     * <ul>
     *   <li>Given {@link City#City()} Country is {@code GBR}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link ElectricityController#getAllElectricityRecordsForCity(Long)}
     */
    @Test
    @DisplayName("Test getAllElectricityRecordsForCity(Long); given City() Country is 'GBR'")
    void testGetAllElectricityRecordsForCity_givenCityCountryIsGbr() throws Exception {
        // Arrange
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(1L);
        city.setName("?");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());

        Electricity electricity = new Electricity();
        electricity.setArea("?");
        electricity.setCity(city);
        electricity.setConsumptionKwh(10.0d);
        electricity.setDate(LocalDate.of(1970, 1, 1));
        electricity.setId(1L);
        electricity.setOutageDurationMinutes(1);
        electricity.setOutageReason("Just cause");

        City city2 = new City();
        city2.setCountry("GBR");
        city2.setElectricityData(new ArrayList<>());
        city2.setId(2L);
        city2.setName("U");
        city2.setWasteData(new ArrayList<>());
        city2.setWaterSupplyData(new ArrayList<>());

        Electricity electricity2 = new Electricity();
        electricity2.setArea("U");
        electricity2.setCity(city2);
        electricity2.setConsumptionKwh(0.5d);
        electricity2.setDate(LocalDate.of(1970, 1, 1));
        electricity2.setId(2L);
        electricity2.setOutageDurationMinutes(0);
        electricity2.setOutageReason("?");

        ArrayList<Electricity> electricityList = new ArrayList<>();
        electricityList.add(electricity2);
        electricityList.add(electricity);
        when(electricityService.getAllElectricityDataForCity(Mockito.<Long>any())).thenReturn(electricityList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/electricity/city/{cityId}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(electricityController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"id\":2,\"date\":[1970,1,1],\"area\":\"U\",\"consumptionKwh\":0.5,\"outageDurationMinutes\":0,\"outageReason"
                                        + "\":\"?\",\"city\":{\"id\":2,\"name\":\"U\",\"country\":\"GBR\"}},{\"id\":1,\"date\":[1970,1,1],\"area\":\"?\",\"consumptionKwh"
                                        + "\":10.0,\"outageDurationMinutes\":1,\"outageReason\":\"Just cause\",\"city\":{\"id\":1,\"name\":\"?\",\"country\":"
                                        + "\"GB\"}}]"));
    }

    /**
     * Test {@link ElectricityController#getAllElectricityRecordsForCity(Long)}.
     * <ul>
     *   <li>Then content string a string.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link ElectricityController#getAllElectricityRecordsForCity(Long)}
     */
    @Test
    @DisplayName("Test getAllElectricityRecordsForCity(Long); then content string a string")
    void testGetAllElectricityRecordsForCity_thenContentStringAString() throws Exception {
        // Arrange
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(1L);
        city.setName("?");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());

        Electricity electricity = new Electricity();
        electricity.setArea("?");
        electricity.setCity(city);
        electricity.setConsumptionKwh(10.0d);
        electricity.setDate(LocalDate.of(1970, 1, 1));
        electricity.setId(1L);
        electricity.setOutageDurationMinutes(1);
        electricity.setOutageReason("Just cause");

        ArrayList<Electricity> electricityList = new ArrayList<>();
        electricityList.add(electricity);
        when(electricityService.getAllElectricityDataForCity(Mockito.<Long>any())).thenReturn(electricityList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/electricity/city/{cityId}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(electricityController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"id\":1,\"date\":[1970,1,1],\"area\":\"?\",\"consumptionKwh\":10.0,\"outageDurationMinutes\":1,\"outageReason\":\"Just"
                                        + " cause\",\"city\":{\"id\":1,\"name\":\"?\",\"country\":\"GB\"}}]"));
    }

    /**
     * Test {@link ElectricityController#getAllElectricityRecordsForCity(Long)}.
     * <p>
     * Method under test:
     * {@link ElectricityController#getAllElectricityRecordsForCity(Long)}
     */
    @Test
    @DisplayName("Test getAllElectricityRecordsForCity(Long)")
    void testGetAllElectricityRecordsForCity() throws Exception {
        // Arrange
        when(electricityService.getAllElectricityDataForCity(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/electricity/city/{cityId}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(electricityController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Test {@link ElectricityController#deleteElectricityRecord(Long)}.
     * <ul>
     *   <li>Given {@link ElectricityService}
     * {@link ElectricityService#getElectricityById(Long)} return empty.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link ElectricityController#deleteElectricityRecord(Long)}
     */
    @Test
    @DisplayName("Test deleteElectricityRecord(Long); given ElectricityService getElectricityById(Long) return empty")
    void testDeleteElectricityRecord_givenElectricityServiceGetElectricityByIdReturnEmpty() throws Exception {
        // Arrange
        doNothing().when(electricityService).deleteElectricityData(Mockito.<Long>any());
        Optional<Electricity> emptyResult = Optional.empty();
        when(electricityService.getElectricityById(Mockito.<Long>any())).thenReturn(emptyResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/electricity/{id}", 1L);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(electricityController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test {@link ElectricityController#deleteElectricityRecord(Long)}.
     * <ul>
     *   <li>Given {@link City#City()} Country is {@code GB}.</li>
     *   <li>Then status {@link StatusResultMatchers#isNoContent()}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link ElectricityController#deleteElectricityRecord(Long)}
     */
    @Test
    @DisplayName("Test deleteElectricityRecord(Long); given City() Country is 'GB'; then status isNoContent()")
    void testDeleteElectricityRecord_givenCityCountryIsGb_thenStatusIsNoContent() throws Exception {
        // Arrange
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(1L);
        city.setName("Name");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());

        Electricity electricity = new Electricity();
        electricity.setArea("Area");
        electricity.setCity(city);
        electricity.setConsumptionKwh(10.0d);
        electricity.setDate(LocalDate.of(1970, 1, 1));
        electricity.setId(1L);
        electricity.setOutageDurationMinutes(1);
        electricity.setOutageReason("Just cause");
        Optional<Electricity> ofResult = Optional.of(electricity);
        doNothing().when(electricityService).deleteElectricityData(Mockito.<Long>any());
        when(electricityService.getElectricityById(Mockito.<Long>any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/electricity/{id}", 1L);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(electricityController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Test
     * {@link ElectricityController#createElectricityRecord(ElectricityDataRequest)}.
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link ElectricityController#createElectricityRecord(ElectricityDataRequest)}
     */
    @Test
    @DisplayName("Test createElectricityRecord(ElectricityDataRequest); then status isNotFound()")
    void testCreateElectricityRecord_thenStatusIsNotFound() throws Exception {
        // Arrange
        when(electricityService.saveElectricityData(Mockito.<ElectricityDataRequest>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

        ElectricityDataRequest electricityDataRequest = new ElectricityDataRequest();
        electricityDataRequest.setArea("Area");
        electricityDataRequest.setCityId(1L);
        electricityDataRequest.setConsumptionKwh(10.0d);
        electricityDataRequest.setDate(null);
        electricityDataRequest.setOutageDurationMinutes(1);
        electricityDataRequest.setOutageReason("Just cause");
        String content = (new ObjectMapper()).writeValueAsString(electricityDataRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/electricity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(electricityController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test {@link ElectricityController#deleteElectricityRecord(Long)}.
     * <p>
     * Method under test:
     * {@link ElectricityController#deleteElectricityRecord(Long)}
     */
    @Test
    @DisplayName("Test deleteElectricityRecord(Long)")
    void testDeleteElectricityRecord() throws Exception {
        // Arrange
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(1L);
        city.setName("Name");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());

        Electricity electricity = new Electricity();
        electricity.setArea("Area");
        electricity.setCity(city);
        electricity.setConsumptionKwh(10.0d);
        electricity.setDate(LocalDate.of(1970, 1, 1));
        electricity.setId(1L);
        electricity.setOutageDurationMinutes(1);
        electricity.setOutageReason("Just cause");
        Optional<Electricity> ofResult = Optional.of(electricity);
        doThrow(new ResourceNotFoundException("An error occurred")).when(electricityService)
                .deleteElectricityData(Mockito.<Long>any());
        when(electricityService.getElectricityById(Mockito.<Long>any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/electricity/{id}", 1L);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(electricityController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
