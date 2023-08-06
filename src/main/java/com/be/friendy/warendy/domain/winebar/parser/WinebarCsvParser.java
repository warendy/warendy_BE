package com.be.friendy.warendy.domain.winebar.parser;

import com.be.friendy.warendy.domain.winebar.entity.Winebar;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;

public class WinebarCsvParser {
    public List<Winebar> read(String fileName)
            throws FileNotFoundException, UnsupportedEncodingException {
        String[] row;
        List<Winebar> winebarList = new ArrayList<>();
        CSVReader csvReader = new CSVReader(
                new InputStreamReader(
                        new FileInputStream(fileName), StandardCharsets.UTF_8)
        );
        long idx = 1L;
        try {
            // 제목이나 컬럼명 제거
            csvReader.readNext();
            do {
                // 컴마 기준 자동으로 분리해 배열에 저장.
                row = csvReader.readNext();
                Long id = 0L;
                String name = null;
                String address = null;
                double lnt = 0.0;
                double lat = 0.0;
                try {
                    if(row[1] != null || !row[1].isEmpty()) {
                        name = row[1];
                    }
                    if(row[2] != null || !row[2].isEmpty()) {
                        address = row[2];
                    }
                    if(row[6] != null || !row[6].isEmpty()) {
                        lnt = Double.parseDouble(row[6]);
                    }
                    if(row[5] != null || !row[5].isEmpty()) {
                        lat = Double.parseDouble(row[5]);
                    }

                } catch (NumberFormatException |
                         DateTimeException |
                         NullPointerException e
                ) {

                }
                Winebar winebar = Winebar.builder()
                        .id(idx++)
                        .name(name)
                        .address(address)
                        .lat(lat)
                        .lnt(lnt)
                        .build();
                new WinebarDAO().insertBoard(winebar);
                winebarList.add(winebar);
            } while (row != null);
        } catch (IOException | CsvValidationException e) {
            System.out.println("Failed to parse it");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return winebarList;
    }

    public static void main(String[] args)
            throws FileNotFoundException, UnsupportedEncodingException
    {
        System.out.println("hello");
        String fileName = "C:\\teamproject\\warendy_BE\\src\\main\\java\\com\\be\\friendy\\warendy\\domain\\winebar\\parser\\winebar_data.csv";
        System.out.println(fileName);
        WinebarCsvParser csvParser = new WinebarCsvParser();
        List<Winebar> winebarList = csvParser.read(fileName);
        System.out.println(winebarList.size());

    }

}
