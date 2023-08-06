package com.be.friendy.warendy.domain.winebar.parser;

import com.be.friendy.warendy.domain.winebar.entity.Winebar;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.List;

public class WinebarDAO {

    private Connection conn;
    String url = "jdbc:mysql://localhost:3306/warendy";
    String dbUserId = "chanee";
    String dbPassword = "wbbk77822!";

    public WinebarDAO() {
        try{
//            System.out.println("생성자");
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, dbUserId, dbPassword);
//            System.out.println("드라이버 로딩 성공");
        } catch(ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (SQLException e) {
            System.out.println("드라이버 로딩 실패 ");
            try {
                conn.close();
            } catch (SQLException e1) {    }
        }
    }

    public void insertBoard(Winebar winebar) throws
            FileNotFoundException, UnsupportedEncodingException, SQLException
    {
        PreparedStatement pstm = null;

        // csv 파일 파싱
//        String fileName = "C:\\teamproject\\warendy_BE\\src\\main\\java\\com\\be\\friendy\\warendy\\domain\\winebar\\parser\\winebar_data2.csv";
//        List<Winebar> winebarList = new WinebarCsvParser().read(fileName);

        //쿼리문 준비
        String sql = "insert ignore into winebar (winebar_id, name, address, lnt, lat) " +
                "values(?,?,?,?,?);";
        try {
            pstm = conn.prepareStatement(sql);
            int affected = 0;
//            for (Winebar winebar : winebarList){
                pstm.setLong(1, winebar.getId());
                pstm.setString(2, winebar.getName());
                pstm.setString(3, winebar.getAddress());
                pstm.setDouble(4, winebar.getLnt());
                pstm.setDouble(5, winebar.getLat());
                affected = pstm.executeUpdate();
//            }
//            System.out.println(affected);
            if(affected > 0){
                System.out.println("INSERT SUCCESS!");
            } else {
                System.out.println("INSERT FAILED");
            }
        } catch (SQLException e) {
            System.out.println("INSERT FAILED");
            throw new RuntimeException(e);
        } finally {
            try {
                if (pstm != null && !pstm.isClosed()){
                    pstm.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if (conn != null && !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
