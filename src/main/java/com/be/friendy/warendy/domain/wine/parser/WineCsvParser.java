package com.be.friendy.warendy.domain.wine.parser;

import com.be.friendy.warendy.domain.wine.entity.Wine;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.DateTimeException;

public class WineCsvParser {
    public int read(String fileName)
            throws FileNotFoundException, UnsupportedEncodingException {
        String[] row;
//        List<Wine> wineList = new ArrayList<>();
        CSVReader csvReader = new CSVReader(
                new InputStreamReader(
                        new FileInputStream(fileName), StandardCharsets.UTF_8)
        );
        long idx = 1L;
        try {
            // 제목이나 컬럼명 제거
            csvReader.readNext();
            do {
                row = csvReader.readNext();
                if (row == null || row.length <= 1) continue;
                Long id = 0L;
                String name = null;     //0
                int vintage = 0;        //1
                String price = null;    //2
                String picture = null;  //3
                int body = 0;           //5
                int dry = 0;            //5
                int tannin = 0;         //5
                int acidity = 0;        //5
                double alcohol = 0.0;   //8
                String grapes = null;   //8
                String pairing = null; //6, 7
                String region = null;   //8
                String type = null;     //8
                String winery = null;   //8
                float rating = 0.0f;    //4
                try {
                    // 와인 이름 - 0
                    if (row[0] != null || !row[0].isEmpty()) {
                        name = row[0];
                        System.out.println(name);
                    }
                    // 생산 년도 - 1
                    if (row[1] != null || !row[1].isEmpty()) {
                        if (row[1].length() == 4) {
                            vintage = Integer.parseInt(row[1]);
                        }
                    }
                    // 이미지 url - 2
                    if (row[2] != null || !row[2].isEmpty()) {
                        picture = row[2];
                    }
                    // 가격(원화) - 3
                    if (row[3] != null || !row[3].isEmpty()) {
                        price = row[3];
                    }
                    // 전문가 평점 - 4
                    if (row[4] != null || !row[4].isEmpty()) {
                        rating = Float.parseFloat(row[4]);
                    }
                    // tastes(body, dry, tan, acid) - 5
                    if (row[5] != null || !row[5].isEmpty()) {
                        String[] tasteArr = row[5]
                                .replaceAll("['\\[\\]]", "")
                                .replaceAll(" ", "")
                                .split(",");
                        for (int i = 0; i < tasteArr.length; i++) {
                            //body
                            if (tasteArr[i].equals("Light")) {
                                i += 3;
                                double body_Double =
                                        Double.parseDouble(tasteArr[i]) / 20;
                                body = (int) Math.round(body_Double);
                            }
                            //dry
                            if (tasteArr[i].equals("Dry")) {
                                i += 3;
                                double body_Double =
                                        Double.parseDouble(tasteArr[i]) / 20;
                                dry = (int) Math.round(body_Double);
                            }
                            //tannin
                            if (tasteArr[i].equals("Smooth")) {
                                i += 3;
                                double body_Double =
                                        Double.parseDouble(tasteArr[i]) / 20;
                                tannin = (int) Math.round(body_Double);
                            }
                            //acid
                            if (tasteArr[i].equals("Soft")) {
                                i += 3;
                                double body_Double =
                                        Double.parseDouble(tasteArr[i]) / 20;
                                acidity = (int) Math.round(body_Double);
                            }
                        }
                    }
                    // pairing(foods) : 6(음식 이름)
                    if (row[6] != null || !row[6].isEmpty()) {
                        String[] foodArr = row[6]
                                .replaceAll("['\\[\\]]", "")
                                .replaceAll(" ", "")
                                .split(",(?![^(]*\\))", -1);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < foodArr.length; i++) {
                            if (i == foodArr.length - 1) {
                                sb.append(foodArr[i]);
                            } else {
                                sb.append(foodArr[i]);
                                sb.append("/");
                            }
                        }
                        pairing = sb.toString();
                    }
                    // 나머지
                    if (row[8] != null || !row[8].isEmpty()) {
                        String[] facts = row[8]
                                .replaceAll("['\\[]", "")
                                .split("]");
                        for (String factor : facts) {
                            String[] factorSplit = factor.split(",");
                            String factorSplitFirst =
                                    (factorSplit[0].equals("") ?
                                            factorSplit[1] : factorSplit[0]).trim();
                            if (factorSplitFirst.equals("Alcohol content")) {
                                if (factorSplit.length == 2) {
                                    alcohol = Double.parseDouble(factorSplit[1]
                                            .trim().replace("%", ""));
                                    continue;
                                }
                                alcohol = Double.parseDouble(factorSplit[2]
                                        .trim().replace("%", ""));
                            }
                            if (factorSplitFirst.equals("Grapes")) {
                                StringBuilder grapeSpecies = new StringBuilder();
                                for (int i = 2; i < factorSplit.length; i++) {
                                    if (i == factorSplit.length - 1) {
                                        grapeSpecies.append(factorSplit[i]);
                                        break;
                                    }
                                    grapeSpecies.append(factorSplit[i]);
                                    grapeSpecies.append(" /");
                                }
                                grapes = grapeSpecies.toString();
                            }
                            if (factorSplitFirst.equals("Region")) {
                                if (factorSplit.length == 2) {
                                    region = factorSplit[1].trim();
                                    continue;
                                }
                                region = factorSplit[2].trim();
                            }
                            if (factorSplitFirst.equals("Wine style")) {
                                if (factorSplit.length == 2) {
                                    type = factorSplit[1].trim();
                                    continue;
                                }
                                type = factorSplit[2].trim();
                            }
                            if (factorSplitFirst.equals("Winery")) {
                                winery = factorSplit[1].trim();
                            }
                        }
                    }
                } catch (NumberFormatException |
                         DateTimeException |
                         NullPointerException e
                ) {

                }
                Wine wine = Wine.builder()
                        .id(idx++)
                        .name(name)
                        .vintage(vintage)
                        .price(price)
                        .picture(picture)
                        .body(body)
                        .dry(dry)
                        .tannin(tannin)
                        .acidity(acidity)
                        .alcohol(alcohol)
                        .grapes(grapes)
                        .pairing(pairing)
                        .region(region)
                        .type(type)
                        .winery(winery)
                        .rating(rating)
                        .build();
                new WineDAO().insertBoard(wine);
            } while (row != null);
        } catch (IOException e) {
            System.out.println("Failed to parse it becaues of IOE");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            System.out.println("Failed to parse it becaues of CSV");
        }
        return (int) idx;
    }

    public static void main(String[] args)
            throws FileNotFoundException, UnsupportedEncodingException {
        System.out.println("hello");
        String fileName = "C:\\teamproject\\warendy_BE\\src\\main\\java\\com\\be\\friendy\\warendy\\domain\\wine\\parser\\wine_info_test.csv";
        System.out.println(fileName);
        WineCsvParser csvParser = new WineCsvParser();
        int wineList = csvParser.read(fileName);
        System.out.println(wineList);

    }
}
