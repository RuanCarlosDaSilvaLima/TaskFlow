package com.ruandev.taskflow.db;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDB {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("✅ Conexão bem-sucedida!");
        } catch (SQLException e) {
            System.out.println("❌ Erro na conexão:");
            e.printStackTrace();
        }
    }
}