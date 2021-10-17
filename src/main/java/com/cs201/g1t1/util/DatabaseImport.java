package com.cs201.g1t1.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Set;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.cs201.g1t1.model.*;
import com.cs201.g1t1.repository.BusinessRepository;
import com.cs201.g1t1.repository.CategoryRepository;

import org.slf4j.*;

@Component
public class DatabaseImport {

    Logger logger = LoggerFactory.getLogger(DatabaseImport.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BusinessRepository businessRepository;

    /**
     * Utility method to import records into database -- DO NOT RUN THIS UNLESS YOU
     * NEED TO REFRESH
     * 
     * @throws IOException
     */
    // @EventListener(ApplicationReadyEvent.class)
    @Transactional(rollbackOn = { IOException.class })
    public void importBusiness() throws IOException {
        BufferedReader br = null;
        logger.info("Run");
        try {
            br = new BufferedReader(
                    new FileReader(new ClassPathResource("/static/yelp_academic_dataset_business.json").getFile()));
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map businessMap = objectMapper.readValue(currentLine, Map.class);

                String stringCategoryNames = (String) (businessMap.get("categories"));

                List<String> categoryNames = null;
                if (stringCategoryNames != null) {
                    categoryNames = Arrays.asList(stringCategoryNames.split(","));
                }

                businessMap.remove("categories");
                Set<Category> categories = null;
                if (categoryNames != null) {
                    categories = categoryNames.stream().map(e -> new Category(e.trim())).collect(Collectors.toSet());

                    categoryRepository.saveAll(categories);
                }

                Business toSave = objectMapper.readValue(currentLine, Business.class);

                toSave.setCategories(categories);

                businessRepository.save(toSave);

                logger.info("Saved business");
            }
        } catch (Exception e) {
            logger.error("Error", e);
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }
}
