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
    private String estado;
    private String fechaInicio;
    private String fechaFin;

    public Trabajo() {
    }

    public Trabajo(int id, String descripcion, int parcelaId, int maquinaId, String estado, String fechaInicio, String fechaFin) {
        this.id = id;
        this.descripcion = descripcion;
        this.parcelaId = parcelaId;
        this.maquinaId = maquinaId;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Trabajo(int aInt, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getParcelaId() {
        return parcelaId;
    }

    public void setParcelaId(int parcelaId) {
        this.parcelaId = parcelaId;
    }

    public int getMaquinaId() {
        return maquinaId;
    }

    public void setMaquinaId(int maquinaId) {
        this.maquinaId = maquinaId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public static List<Trabajo> listarTrabajos() {
        List<Trabajo> lista = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/agricultura", "root", "")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM trabajos");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Trabajo(
                        rs.getInt("id"),
                        rs.getString("descripcion"),
                        rs.getInt("parcela_id"),
                        rs.getInt("maquina_id"),
                        rs.getString("estado"),
                        rs.getString("fecha_inicio"),
                        rs.getString("fecha_fin")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static boolean crearTrabajo(String descripcion, int parcelaId, int maquinaId, String estado, String fechaInicio, String fechaFin) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/agricultura", "root", "")) {
            String query = "INSERT INTO trabajos (descripcion, parcela_id, maquina_id, estado, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, descripcion);
            stmt.setInt(2, parcelaId);
            stmt.setInt(3, maquinaId);
            stmt.setString(4, estado); // Establece el estado
            stmt.setString(5, fechaInicio); // Establece la fecha de inicio
            stmt.setString(6, fechaFin); // Establece la fecha de fin
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
