package com.sajid.college_app.services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CsvProcessingService {

    /**
     * A generic method to process any CSV file and map it to a list of objects.
     * @param file The CSV file uploaded by the user.
     * @param clazz The target class (e.g., Course.class) to which the CSV rows will be mapped.
     * @param <T> The generic type of the objects in the list.
     * @return A list of objects of type T.
     * @throws CsvValidationException if the file is invalid.
     */
    public <T> List<T> process(MultipartFile file, Class<T> clazz) throws CsvValidationException {
        if (file.isEmpty()) {
            throw new CsvValidationException("The uploaded file is empty.");
        }

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            
            // The only change from before is using the 'clazz' parameter here
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(clazz) // This makes the method generic
                    .withIgnoreLeadingWhiteSpace(true)
                    .withThrowExceptions(true)
                    .build();

            List<T> records = csvToBean.parse();

            if (records.isEmpty()) {
                throw new CsvValidationException("The CSV file contains no data rows.");
            }

            return records;

        } catch (Exception ex) {
            throw new CsvValidationException("Failed to process CSV file: " + ex.getMessage());
        }
    }
}