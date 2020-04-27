package main.java.framework;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class CSV_IO {

    @SuppressWarnings("null")
    public static List<LinkedHashMap<String, String>> readCSVFile(String strFilePath) throws IOException {

        try (Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream((strFilePath)), "utf-8"));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);) {
            int intRow = 0;
            List<LinkedHashMap<String, String>> inputs = new ArrayList<LinkedHashMap<String, String>>();
            String[] aHeaders = {};
            for (CSVRecord csvRecord : csvParser) {
                LinkedHashMap<String, String> input = new LinkedHashMap<>();
                int iColCount = csvRecord.size();
                if (intRow == 0) {
                    aHeaders = new String[iColCount];
                    for (int intI = 0; intI < iColCount; intI++) {
                        aHeaders[intI] = csvRecord.get(intI).toString();
                    }
                } else {
                    for (int intI = 0; intI < iColCount; intI++) {
                        String coValue = csvRecord.get(intI).toString();

                        if (coValue.length() != 0 && coValue != null) {
                            input.put(aHeaders[intI], coValue);
                        }
                    }
                    @SuppressWarnings("unchecked")
                    LinkedHashMap<String, String> excelRecord1 = (LinkedHashMap<String, String>) input.clone();
                    inputs.add(intRow - 1, excelRecord1);
                }
                intRow++;
            }
            return inputs;
        } catch (Exception e) {
            System.out.println("Error while fetching data from " + strFilePath);
            e.printStackTrace();
            return null;
        }
    }

    public static void writeinCSV(CSVPrinter csvPrinter, String[] aValues) throws IOException {

        try {
            csvPrinter.printRecord(aValues);
            csvPrinter.flush();
        } catch (Exception e) {
            System.out.println("Error while writing data in CSV Report:" + aValues.toString());
        }
    }

    public static CSVPrinter createNewCSVFile(String sReportPath, String[] aHeaders) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(sReportPath));

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(aHeaders));
            return csvPrinter;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}