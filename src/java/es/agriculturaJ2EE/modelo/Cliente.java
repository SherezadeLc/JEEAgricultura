/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.agriculturaJ2EE.modelo;

public class Cliente {
    private int id;
    private String nombre;
    private String dni;
    private String idCatastro;

    public Cliente(int id, String nombre, String dni, String idCatastro) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.idCatastro = idCatastro;
    }

    public Cliente(String nombre, String dni, String idCatastro) {
        this.nombre = nombre;
        this.dni = dni;
        this.idCatastro = idCatastro;
    }

    public int getId() { 
        return id; 
    }
    public String getNombre() {
        return nombre; 
    }
    public String getDni() {
        return dni; 
    }
    public String getIdCatastro() { 
        return idCatastro; 
    }
}

