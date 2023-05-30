package com.example.demo.services;

import com.example.demo.models.GroceryItem;
import com.example.demo.repo.GroceryRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroceryCsvService {

    @Autowired
    private GroceryRepository groceryRepository;

    public String readCsvAndSave(MultipartFile file) {
        // Process the CSV file
        List<GroceryItem> groceryItems = processCsvFile(file);

        if (groceryItems == null) {
            return "Error reading CSV file";
        }

        // Putting into Mongodb
        for (GroceryItem groceryItem : groceryItems) {
            groceryRepository.save(groceryItem);
        }

        return null;
    }

    private List<GroceryItem> processCsvFile(MultipartFile file) {
        List<GroceryItem> groceryItems = new ArrayList<>();
        // Handle the CSV file and metadata
        try {
            InputStream inputStream = file.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            CSVReader csvReader = new CSVReaderBuilder(reader).build();
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                for (String data : line) {
                    System.out.print(data + " ");
                }
                System.out.println();

                // Skipping Header Row
                if (csvReader.getLinesRead() > 1) {
                    groceryItems.add(new GroceryItem(line[0], line[1], line[2], line[3], line[4], line[5]));
                }
            }

            csvReader.close();
            reader.close();
            inputStream.close();
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
            return null;
        }
        return groceryItems;
    }
}
