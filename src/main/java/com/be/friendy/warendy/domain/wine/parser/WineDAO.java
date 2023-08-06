package com.be.friendy.warendy.domain.wine.parser;

import com.be.friendy.warendy.domain.wine.entity.Wine;
import com.nimbusds.jose.shaded.gson.Gson;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WineDAO {

    private Connection conn;
    String url = "jdbc:mysql://localhost:3306/warendy";
    String dbUserId = "chanee";
    String dbPassword = "wbbk77822!";

    public WineDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, dbUserId, dbPassword);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (SQLException e) {
            System.out.println("드라이버 로딩 실패 ");
            try {
                conn.close();
            } catch (SQLException e1) {
            }
        }
    }

    public void insertBoard(Wine wine) throws
            FileNotFoundException, UnsupportedEncodingException, SQLException {
        PreparedStatement pstm = null;

        String sql = "insert ignore into wine " +
                "(wine_id, name, vintage, price, picture,body,dry,tannin,acidity" +
                ",alcohol,grapes,paring,region,type,winery,rating) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try {
            pstm = conn.prepareStatement(sql);
            int affected = 0;
            pstm.setLong(1, wine.getId());
            pstm.setString(2, wine.getName());
            pstm.setInt(3, wine.getVintage());
            pstm.setString(4, wine.getPrice());
            pstm.setString(5, wine.getPicture());
            pstm.setInt(6, wine.getBody());
            pstm.setInt(7, wine.getDry());
            pstm.setInt(8, wine.getTannin());
            pstm.setInt(9, wine.getAcidity());
            pstm.setDouble(10, wine.getAlcohol());
            pstm.setString(11, wine.getGrapes());
            // Convert List to JSON
            Gson gson = new Gson();
            String paringJson = gson.toJson(wine.getPairing());
            pstm.setString(12, paringJson);
//            pstm.setObject(12, wine.getParing());
            pstm.setString(13, wine.getRegion());
            pstm.setString(14, wine.getType());
            pstm.setString(15, wine.getWinery());
            pstm.setFloat(16, wine.getRating());
            affected = pstm.executeUpdate();
            System.out.println(affected);
            if (affected > 0) {
                System.out.println("INSERT Success");
            } else {
                System.out.println("INSERT FAILED");
            }
        } catch (SQLException e) {
            System.out.println("INSERT FAILED");
            throw new RuntimeException(e);
        } finally {
            try {
                if (pstm != null && !pstm.isClosed()) {
                    pstm.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
