package com.wildbeeslabs.jentle.algorithms.misc;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvFileProcessor {
    private Logger logger = LoggerFactory.getLogger(CsvFileProcessor.class);
    private HashedValueRegistry valueRegistry;
    private Path inputPath;
    private Path outputPath;

    public CsvFileProcessor (HashedValueRegistry valueRegistry, Path inputPath, Path outputPath) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.valueRegistry = valueRegistry;
    }

    public void process(List<Integer> obfuscatedFields) {
        logger.info("Processing '{}' saving in '{}'", inputPath, fileOutputPath());

        try {
            CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new FileReader(inputPath.toFile()));
            String[] headerFields = getHeaderFields(parser);

            CSVPrinter printer = new CSVPrinter(new FileWriter(fileOutputPath().toFile(),false),
                    CSVFormat.DEFAULT.withHeader(headerFields));

            for (CSVRecord record: parser) {
                List<String> outputFields = new ArrayList<>();

                for(int index = 0; index < record.size(); index++) {
                    String fieldValue = record.get(index);

                    if(obfuscatedFields.contains(index)) {
                        outputFields.add(valueRegistry.get(fieldValue));
                    } else {
                        outputFields.add(fieldValue);
                    }
                }

                printer.printRecord(outputFields);
                printer.flush();
                printer.close();
            }
        } catch (IOException e) {
            logger.error("Failed to process log file", e);
        }
    }

    private String[] getHeaderFields(CSVParser parser) {
        return (String[]) parser.getHeaderMap().keySet().toArray();
    }

    private Path fileOutputPath() {
        return Paths.get(outputPath.toString(), inputPath.getFileName().toString());
    }
}
