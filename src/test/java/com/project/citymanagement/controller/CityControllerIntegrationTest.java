package com.project.citymanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.citymanagement.entity.City;
import com.project.citymanagement.exception.ResourceNotFoundException;
import com.project.citymanagement.model.city.CityDto;
import com.project.citymanagement.service.CityService;
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

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {CityController.class})
@ExtendWith(SpringExtension.class)
class CityControllerIntegrationTest {
    @Autowired
    private CityController cityController;

    @MockBean
    private CityService cityService;

    /**
     * Test {@link CityController#getAllCities()}.
     * <ul>
     *   <li>Given {@link City#City()} Country is {@code GB}.</li>
     *   <li>Then content string {@code [{"id":1,"name":"?","country":"GB"}]}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CityController#getAllCities()}
     */
    @Test
    @DisplayName("Test getAllCities(); given City() Country is 'GB'; then content string '[{\"id\":1,\"name\":\"?\",\"country\":\"GB\"}]'")
    void testGetAllCities_givenCityCountryIsGb_thenContentStringId1NameCountryGb() throws Exception {
        // Arrange
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(1L);
        city.setName("?");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());

        ArrayList<City> cityList = new ArrayList<>();
        cityList.add(city);
        when(cityService.getAllCities()).thenReturn(cityList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/city");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(cityController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[{\"id\":1,\"name\":\"?\",\"country\":\"GB\"}]"));
    }

    /**
     * Test {@link CityController#getAllCities()}.
     * <ul>
     *   <li>Then content string
     * {@code [{"id":2,"name":"Name","country":"GBR"},{"id":1,"name":"?","country":"GB"}]}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CityController#getAllCities()}
     */
    @Test
    @DisplayName("Test getAllCities(); then content string '[{\"id\":2,\"name\":\"Name\",\"country\":\"GBR\"},{\"id\":1,\"name\":\"?\",\"country\":\"GB\"}]'")
    void testGetAllCities_thenContentStringId2NameNameCountryGbrId1NameCountryGb() throws Exception {
        // Arrange
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(1L);
        city.setName("?");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());

        City city2 = new City();
        city2.setCountry("GBR");
        city2.setElectricityData(new ArrayList<>());
        city2.setId(2L);
        city2.setName("Name");
        city2.setWasteData(new ArrayList<>());
        city2.setWaterSupplyData(new ArrayList<>());

        ArrayList<City> cityList = new ArrayList<>();
        cityList.add(city2);
        cityList.add(city);
        when(cityService.getAllCities()).thenReturn(cityList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/city");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(cityController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("[{\"id\":2,\"name\":\"Name\",\"country\":\"GBR\"},{\"id\":1,\"name\":\"?\",\"country\":\"GB\"}]"));
    }

    /**
     * Test {@link CityController#getAllCities()}.
     * <ul>
     *   <li>Then content string {@code []}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CityController#getAllCities()}
     */
    @Test
    @DisplayName("Test getAllCities(); then content string '[]'")
    void testGetAllCities_thenContentStringLeftSquareBracketRightSquareBracket() throws Exception {
        // Arrange
        when(cityService.getAllCities()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/city");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(cityController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Test {@link CityController#getAllCities()}.
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CityController#getAllCities()}
     */
    @Test
    @DisplayName("Test getAllCities(); then status isNotFound()")
    void testGetAllCities_thenStatusIsNotFound() throws Exception {
        // Arrange
        when(cityService.getAllCities()).thenThrow(new ResourceNotFoundException("An error occurred"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/city");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cityController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test {@link CityController#createCity(CityDto)}.
     * <ul>
     *   <li>Given {@link City#City()} Country is {@code GB}.</li>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CityController#createCity(CityDto)}
     */
    @Test
    @DisplayName("Test createCity(CityDto); given City() Country is 'GB'; then status isOk()")
    void testCreateCity_givenCityCountryIsGb_thenStatusIsOk() throws Exception {
        // Arrange
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(1L);
        city.setName("Name");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());
        when(cityService.saveCity(Mockito.<City>any())).thenReturn(city);

        CityDto cityDto = new CityDto();
        cityDto.setCountry("GB");
        cityDto.setId(1L);
        cityDto.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(cityDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/city")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(cityController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"name\":\"Name\",\"country\":\"GB\"}"));
    }

    /**
     * Test {@link CityController#deleteCity(Long)}.
     * <p>
     * Method under test: {@link CityController#deleteCity(Long)}
     */
    @Test
    @DisplayName("Test deleteCity(Long)")
    void testDeleteCity() throws Exception {
        // Arrange
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(1L);
        city.setName("Name");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());
        Optional<City> ofResult = Optional.of(city);
        doThrow(new ResourceNotFoundException("An error occurred")).when(cityService).deleteCity(Mockito.<Long>any());
        when(cityService.getCityById(Mockito.<Long>any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/city/{id}", 1L);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cityController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test {@link CityController#deleteCity(Long)}.
     * <ul>
     *   <li>Given {@link City#City()} Country is {@code GB}.</li>
     *   <li>Then status {@link StatusResultMatchers#isNoContent()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CityController#deleteCity(Long)}
     */
    @Test
    @DisplayName("Test deleteCity(Long); given City() Country is 'GB'; then status isNoContent()")
    void testDeleteCity_givenCityCountryIsGb_thenStatusIsNoContent() throws Exception {
        // Arrange
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(1L);
        city.setName("Name");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());
        Optional<City> ofResult = Optional.of(city);
        doNothing().when(cityService).deleteCity(Mockito.<Long>any());
        when(cityService.getCityById(Mockito.<Long>any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/city/{id}", 1L);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cityController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Test {@link CityController#deleteCity(Long)}.
     * <ul>
     *   <li>Given {@link CityService} {@link CityService#getCityById(Long)} return
     * empty.</li>
     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CityController#deleteCity(Long)}
     */
    @Test
    @DisplayName("Test deleteCity(Long); given CityService getCityById(Long) return empty; then status isNotFound()")
    void testDeleteCity_givenCityServiceGetCityByIdReturnEmpty_thenStatusIsNotFound() throws Exception {
        // Arrange
        doNothing().when(cityService).deleteCity(Mockito.<Long>any());
        Optional<City> emptyResult = Optional.empty();
        when(cityService.getCityById(Mockito.<Long>any())).thenReturn(emptyResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/city/{id}", 1L);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cityController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test {@link CityController#getCityById(Long)}.
     * <p>
     * Method under test: {@link CityController#getCityById(Long)}
     */
    @Test
    @DisplayName("Test getCityById(Long)")
    void testGetCityById() throws Exception {
        // Arrange
        when(cityService.getCityById(Mockito.<Long>any())).thenThrow(new ResourceNotFoundException("An error occurred"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/city/{id}", 1L);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cityController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test {@link CityController#getCityById(Long)}.
     * <ul>
     *   <li>Given {@link City#City()} Country is {@code GB}.</li>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CityController#getCityById(Long)}
     */
    @Test
    @DisplayName("Test getCityById(Long); given City() Country is 'GB'; then status isOk()")
    void testGetCityById_givenCityCountryIsGb_thenStatusIsOk() throws Exception {
        // Arrange
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(1L);
        city.setName("Name");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());
        Optional<City> ofResult = Optional.of(city);
        when(cityService.getCityById(Mockito.<Long>any())).thenReturn(ofResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/city/{id}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(cityController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"name\":\"Name\",\"country\":\"GB\"}"));
    }

    /**
     * Test {@link CityController#getCityById(Long)}.
     * <ul>
     *   <li>Given {@link CityService} {@link CityService#getCityById(Long)} return
     * empty.</li>
     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CityController#getCityById(Long)}
     */
    @Test
    @DisplayName("Test getCityById(Long); given CityService getCityById(Long) return empty; then status isNotFound()")
    void testGetCityById_givenCityServiceGetCityByIdReturnEmpty_thenStatusIsNotFound() throws Exception {
        // Arrange
        Optional<City> emptyResult = Optional.empty();
        when(cityService.getCityById(Mockito.<Long>any())).thenReturn(emptyResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/city/{id}", 1L);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cityController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test {@link CityController#updateCity(Long, CityDto)}.
     * <ul>
     *   <li>Given {@link CityService} {@link CityService#getCityById(Long)} return
     * empty.</li>
     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CityController#updateCity(Long, CityDto)}
     */
    @Test
    @DisplayName("Test updateCity(Long, CityDto); given CityService getCityById(Long) return empty; then status isNotFound()")
    void testUpdateCity_givenCityServiceGetCityByIdReturnEmpty_thenStatusIsNotFound() throws Exception {
        // Arrange
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(1L);
        city.setName("Name");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());
        when(cityService.saveCity(Mockito.<City>any())).thenReturn(city);
        Optional<City> emptyResult = Optional.empty();
        when(cityService.getCityById(Mockito.<Long>any())).thenReturn(emptyResult);

        CityDto cityDto = new CityDto();
        cityDto.setCountry("GB");
        cityDto.setId(1L);
        cityDto.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(cityDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/city/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cityController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test {@link CityController#updateCity(Long, CityDto)}.
     * <p>
     * Method under test: {@link CityController#updateCity(Long, CityDto)}
     */
    @Test
    @DisplayName("Test updateCity(Long, CityDto)")
    void testUpdateCity() throws Exception {
        // Arrange
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(1L);
        city.setName("Name");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());
        Optional<City> ofResult = Optional.of(city);
        when(cityService.saveCity(Mockito.<City>any())).thenThrow(new ResourceNotFoundException("An error occurred"));
        when(cityService.getCityById(Mockito.<Long>any())).thenReturn(ofResult);

        CityDto cityDto = new CityDto();
        cityDto.setCountry("GB");
        cityDto.setId(1L);
        cityDto.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(cityDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/city/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cityController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test {@link CityController#updateCity(Long, CityDto)}.
     * <ul>
     *   <li>Given {@link CityService} {@link CityService#saveCity(City)} return
     * {@link City#City()}.</li>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CityController#updateCity(Long, CityDto)}
     */
    @Test
    @DisplayName("Test updateCity(Long, CityDto); given CityService saveCity(City) return City(); then status isOk()")
    void testUpdateCity_givenCityServiceSaveCityReturnCity_thenStatusIsOk() throws Exception {
        // Arrange
        City city = new City();
        city.setCountry("GB");
        city.setElectricityData(new ArrayList<>());
        city.setId(1L);
        city.setName("Name");
        city.setWasteData(new ArrayList<>());
        city.setWaterSupplyData(new ArrayList<>());
        Optional<City> ofResult = Optional.of(city);

        City city2 = new City();
        city2.setCountry("GB");
        city2.setElectricityData(new ArrayList<>());
        city2.setId(1L);
        city2.setName("Name");
        city2.setWasteData(new ArrayList<>());
        city2.setWaterSupplyData(new ArrayList<>());
        when(cityService.saveCity(Mockito.<City>any())).thenReturn(city2);
        when(cityService.getCityById(Mockito.<Long>any())).thenReturn(ofResult);

        CityDto cityDto = new CityDto();
        cityDto.setCountry("GB");
        cityDto.setId(1L);
        cityDto.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(cityDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/city/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(cityController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"name\":\"Name\",\"country\":\"GB\"}"));
    }

    /**
     * Test {@link CityController#createCity(CityDto)}.
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link CityController#createCity(CityDto)}
     */
    @Test
    @DisplayName("Test createCity(CityDto); then status isNotFound()")
    void testCreateCity_thenStatusIsNotFound() throws Exception {
        // Arrange
        when(cityService.saveCity(Mockito.<City>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        CityDto cityDto = new CityDto();
        cityDto.setCountry("GB");
        cityDto.setId(1L);
        cityDto.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(cityDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/city")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(cityController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}