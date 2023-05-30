package com.example.demo.controllers;

import com.example.demo.models.GroceryItem;
import com.example.demo.repo.GroceryRepository;
import com.example.demo.response.GreetingResponse;
import com.example.demo.response.UploadCsvResponse;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GroceryController {

    private static final String template = "Hello, %s %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private GroceryRepository groceryRepository;

    @GetMapping("/greeting")
    public GreetingResponse greeting(
            @RequestParam(value = "fname", defaultValue = "World") String fname,
            @RequestParam(value = "lname", defaultValue = "") String lname
    ) {
        String content = String.format(template, fname, lname).trim();
        return new GreetingResponse(counter.incrementAndGet(), content);
    }

    @PostMapping("/upload_csv")
    public ResponseEntity<UploadCsvResponse> uploadCsv(
            @RequestParam("file") MultipartFile file,
            @RequestParam("metadata") String metadata
    ) {
        System.out.println(file.getName() + " - " + metadata);

        UploadCsvResponse response;
        if (file.getSize() == 0) {
            response = new UploadCsvResponse("CSV File is required");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Process the CSV file
        List<GroceryItem> groceryItems = processCsvFile(file);

        if (groceryItems == null) {
            response = new UploadCsvResponse("Error reading CSV file");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Putting into Mongodb
        for (GroceryItem groceryItem : groceryItems) {
            groceryRepository.save(groceryItem);
        }

        response = new UploadCsvResponse(String.format("CSV file uploaded successfully - %d records", groceryItems.size()));
        return new ResponseEntity<>(response, HttpStatus.OK);
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
