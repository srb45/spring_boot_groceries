package com.example.demo.controllers;

import com.example.demo.response.GreetingResponse;
import com.example.demo.response.UploadCsvResponse;
import com.example.demo.services.GroceryCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GroceryController {

    private static final String template = "Hello, %s %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private GroceryCsvService groceryCsvService;

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

        // Reading, Parsing and Saving data to MongoDB
        String error = groceryCsvService.readCsvAndSave(file);

        if (error != null) {
            response = new UploadCsvResponse((error));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            response = new UploadCsvResponse("CSV file uploaded successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
