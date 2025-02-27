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

public class Maquina {
    private int id;
    private String nombre;
    
    public Maquina() {}
    
    public Maquina(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public static List<Maquina> listarMaquinas() {
        List<Maquina> lista = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/agricultura", "root", "")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM maquinas");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Maquina(rs.getInt("id"), rs.getString("nombre")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public static boolean agregarMaquina(String nombre) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/agricultura", "root", "")) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO maquinas (nombre) VALUES (?)");
            stmt.setString(1, nombre);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}