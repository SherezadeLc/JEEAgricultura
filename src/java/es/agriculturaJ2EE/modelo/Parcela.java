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

public class Parcela {
    private int id;
    private String nombre;
    private int agricultorId;
    
    public Parcela() {}
    
    public Parcela(int id, String nombre, int agricultorId) {
        this.id = id;
        this.nombre = nombre;
        this.agricultorId = agricultorId;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getAgricultorId() { return agricultorId; }
    public void setAgricultorId(int agricultorId) { this.agricultorId = agricultorId; }
    
    public static List<Parcela> listarParcelas() {
        List<Parcela> lista = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/agricultura", "root", "")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM parcelas");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Parcela(rs.getInt("id"), rs.getString("nombre"), rs.getInt("agricultor_id")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public static boolean agregarParcela(String nombre, int agricultorId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/agricultura", "root", "")) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO parcelas (nombre, agricultor_id) VALUES (?, ?)");
            stmt.setString(1, nombre);
            stmt.setInt(2, agricultorId);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

