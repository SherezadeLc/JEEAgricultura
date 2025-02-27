/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.agriculturaJ2EE.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Trabajo {
    private int id;
    private String descripcion;
    private int parcelaId;
    private int maquinaId;
    
    public Trabajo() {}
    
    public Trabajo(int id, String descripcion, int parcelaId, int maquinaId) {
        this.id = id;
        this.descripcion = descripcion;
        this.parcelaId = parcelaId;
        this.maquinaId = maquinaId;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getParcelaId() { return parcelaId; }
    public void setParcelaId(int parcelaId) { this.parcelaId = parcelaId; }
    public int getMaquinaId() { return maquinaId; }
    public void setMaquinaId(int maquinaId) { this.maquinaId = maquinaId; }
    
    public static List<Trabajo> listarTrabajos() {
        List<Trabajo> lista = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/agricultura", "root", "")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM trabajos");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Trabajo(rs.getInt("id"), rs.getString("descripcion"), rs.getInt("parcela_id"), rs.getInt("maquina_id")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public static boolean crearTrabajo(String descripcion, int parcelaId, int maquinaId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/agricultura", "root", "")) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO trabajos (descripcion, parcela_id, maquina_id) VALUES (?, ?, ?)");
            stmt.setString(1, descripcion);
            stmt.setInt(2, parcelaId);
            stmt.setInt(3, maquinaId);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}