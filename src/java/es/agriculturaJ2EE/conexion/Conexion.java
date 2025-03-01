/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.agriculturaJ2EE.conexion;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class Conexion extends HttpServlet {

    public static Connection getConnection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

  Connection conexion;
  
  private String url;
  private String usuario;
  private String contrasena;
  
  public Conexion(){
      this.url="jdbc:mysql://localhost/agricultura";
      this.usuario="root";
      this.contrasena="";
       try {
            /*Cargamos el driver JDBC para permitir una comunicacion con la base de datos*/
            Class.forName("com.mysql.jdbc.Driver").newInstance();

        } catch (ClassNotFoundException ex) {
            // Imprimir un mensaje de error en la consola
            System.err.println("Error: No se pudo encontrar el driver de MySQL.");

        } catch (InstantiationException ex) {
            // Imprimir un mensaje de error en la consola
            System.err.println("Error: No se pudo instanciar el driver de MySQL.");

        } catch (IllegalAccessException ex) {
            // Imprimir un mensaje de error en la consola
            System.err.println("Error: No se pudo acceder.");

        }
  }
  public void conectarBaseDatos(){
      try{
          conexion = DriverManager.getConnection(this.url, this.usuario, this.contrasena);
      }catch (SQLException ex) {
            System.err.println("Error al conectar a la base de datos: ");
        }
  }
  public void desconectarBaseDatos(){
      try {
            /*Se cierra la conexion*/
            conexion.close();

        } catch (SQLException ex) {
            System.err.println("Error al desconectar a la base de datos");
        }
  }
  
  public boolean insertarCliente(String nombre,String dni,String contrasena,String idCatastro,String numero_parcela,String latitud,String longitud){
       /*Usamos el metodo encargado de iniciar la conexion con la BD*/
        conectarBaseDatos();
      try{
            String sqlStr = "INSERT INTO cliente (dni,nombre, contrasena,id_catastro) VALUES (?, ?,?,?)";
            String sqlStr1 = "INSERT INTO parcela (id_catastro, numero_parcela) VALUES  (?, ?)";
            String sqlStr2 = "INSERT INTO puntos (numero_parcela, latitud, longitud) VALUES (?, ?,?)";
            PreparedStatement preparedStatement = conexion.prepareStatement(sqlStr);
            PreparedStatement preparedStatement1 = conexion.prepareStatement(sqlStr1);
            PreparedStatement preparedStatement2 = conexion.prepareStatement(sqlStr2);
            
            
            preparedStatement.setString(1, dni);
            preparedStatement.setString(2, nombre);
            preparedStatement.setString(3, contrasena);
            preparedStatement.setString(4, idCatastro);
            
            preparedStatement1.setString(1, idCatastro);
            preparedStatement1.setString(2, numero_parcela);
            
            preparedStatement2.setString(1, numero_parcela);
            preparedStatement2.setString(2, latitud);
            preparedStatement2.setString(3, longitud);

            preparedStatement.executeUpdate();
            preparedStatement1.executeUpdate();
            preparedStatement2.executeUpdate();
            //Cerramos los recursos
            preparedStatement.close();
            //Cerramos los recursos
            preparedStatement1.close();
            //Cerramos los recursos
            preparedStatement2.close();
           return true;
        } catch (SQLException e) {
            
            return false;
        }
      
  }
  
  
  public ResultSet loginCliente(String dni,String contrasena){
      conectarBaseDatos();
      
       /*Creamos un objeto statement*/
        Statement stmt;
        try{
             stmt = conexion.createStatement();
             String sqlStr ="SELECT * FROM cliente WHERE dni='"+dni+"' AND contrasena='"+contrasena+"'";
             ResultSet rset = stmt.executeQuery(sqlStr);
              return rset;
        }
      catch (SQLException ex) {
            return null;
        }
      
  }
  public ResultSet loginAgricultor(String dni,String contrasena){
      conectarBaseDatos();
      
       /*Creamos un objeto statement*/
        Statement stmt;
        try{
             stmt = conexion.createStatement();
             String sqlStr ="SELECT * FROM agricultor WHERE dni='"+dni+"' AND contrasena='"+contrasena+"'";
             ResultSet rset = stmt.executeQuery(sqlStr);
              return rset;
        }
      catch (SQLException ex) {
            return null;
        }
      
  }
  
  public ResultSet loginAadministrador(String dni,String contrasena){
      conectarBaseDatos();
      
       /*Creamos un objeto statement*/
        Statement stmt;
        try{
             stmt = conexion.createStatement();
             String sqlStr ="SELECT * FROM cliente WHERE dni='"+dni+"' AND contrasena='"+contrasena+"'";
             ResultSet rset = stmt.executeQuery(sqlStr);
              return rset;
        }
      catch (SQLException ex) {
            return null;
        }
      
  }

   

}
