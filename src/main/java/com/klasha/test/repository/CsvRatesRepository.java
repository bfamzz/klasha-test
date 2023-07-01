package com.klasha.test.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class CsvRatesRepository {

    @Value("classpath:exchange_rate.csv")
    Resource resource;

    @Bean
    public List<List<String>> availableExchanges() {
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new
                FileReader(resource.getFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return records;
    }
}
