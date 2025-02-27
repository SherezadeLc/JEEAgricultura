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

public class Agricultor {
    private int id;
    private String nombre;
    private String telefono;
    
    public Agricultor() {}
    
    public Agricultor(int id, String nombre, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
    }
    
    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
    public String getTelefono() { 
        return telefono;
    }
    public void setTelefono(String telefono) { 
        this.telefono = telefono; 
    }
    
    public static List<Agricultor> listarAgricultores() {
        List<Agricultor> lista = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/agricultura", "root", "")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM agricultores");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Agricultor(rs.getInt("id"), rs.getString("nombre"), rs.getString("telefono")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}


