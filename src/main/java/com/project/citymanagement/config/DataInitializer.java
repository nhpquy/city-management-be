package com.project.citymanagement.config;

import com.github.javafaker.Faker;
import com.project.citymanagement.entity.City;
import com.project.citymanagement.repository.CityRepository;
import com.project.citymanagement.repository.ElectricityRepository;
import com.project.citymanagement.repository.WaterSupplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * This class initializes fake data for the application when it starts.
 */
@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private ElectricityRepository electricityRepository;
    @Autowired
    private WaterSupplyRepository waterSupplyRepository;


    private final Faker faker = new Faker();

    /**
     * This method is called when the application starts.
     *
     * @param args Command line arguments
     */
    @Override
    public void run(String... args) {
        // Always clear existing data before inserting new data
        cityRepository.deleteAll();
        electricityRepository.deleteAll();
        waterSupplyRepository.deleteAll();

        List<City> cities = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            City city = new City();
            var country = faker.country();
            city.setName(country.capital());
            city.setCountry(country.name());
            cities.add(city);
        }
        cityRepository.saveAll(cities);

        System.out.println("Fake data initialized successfully, replacing any existing data!");
    }
}
