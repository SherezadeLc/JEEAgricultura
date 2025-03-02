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
    private String dni;
    private String telefono;

    public Agricultor() {

    }

    public Agricultor(String dni, String nombre) {
        this.dni = dni;
        this.nombre = nombre;
    }

    public Agricultor(int id, String nombre, String dni) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
    }

    public Agricultor(int id, String nombre, String telefono, String dni) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.dni = dni;
    }

    public Agricultor(String pid, String pnombre, String pdni) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    /*public static List<Agricultor> listarAgricultores() {
        List<Agricultor> lista = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/agricultura", "root", "")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, nombre, telefono, dni FROM agricultor"); // Cambié 'agricultores' a 'agricultor'
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Agricultor(rs.getInt("id"), rs.getString("nombre"), rs.getString("telefono"), rs.getString("dni"))); // Ahora usa el constructor correcto
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }*/

}
