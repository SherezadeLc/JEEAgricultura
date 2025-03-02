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

    public Conexion() {
        this.url = "jdbc:mysql://localhost/agriculturaj2ee";
        this.usuario = "root";
        this.contrasena = "";
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

    public void conectarBaseDatos() {
        try {
            conexion = DriverManager.getConnection(this.url, this.usuario, this.contrasena);
        } catch (SQLException ex) {
            System.err.println("Error al conectar a la base de datos: ");
        }
    }

    public void desconectarBaseDatos() {
        try {
            /*Se cierra la conexion*/
            conexion.close();

        } catch (SQLException ex) {
            System.err.println("Error al desconectar a la base de datos");
        }
    }

    public boolean insertarCliente(String nombre, String dni, String contrasena, String idCatastro, String numero_parcela, String latitud, String longitud) {
        /*Usamos el metodo encargado de iniciar la conexion con la BD*/
        conectarBaseDatos();
        try {
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

    public ResultSet loginCliente(String dni, String contrasena) {
        conectarBaseDatos();

        /*Creamos un objeto statement*/
        Statement stmt;
        try {
            stmt = conexion.createStatement();
            String sqlStr = "SELECT * FROM cliente WHERE dni= '" + dni + "' AND contrasena= '" + contrasena + "'";
            ResultSet rset = stmt.executeQuery(sqlStr);
            return rset;
        } catch (SQLException ex) {
            return null;
        }

    }

    public ResultSet loginAgricultor(String dni, String contrasena) {
        conectarBaseDatos();

        /*Creamos un objeto statement*/
        Statement stmt;
        try {
            stmt = conexion.createStatement();
            String sqlStr = "SELECT * FROM agricultor WHERE dni= '" + dni + "' AND contrasena= '" + contrasena + "'";
            ResultSet rset = stmt.executeQuery(sqlStr);
            return rset;
        } catch (SQLException ex) {
            return null;
        }

    }

    public ResultSet loginAdministrador(String dni, String contrasena) {
        conectarBaseDatos();

        /*Creamos un objeto statement*/
        Statement stmt;
        try {
            stmt = conexion.createStatement();
            String sqlStr = "SELECT * FROM administrador WHERE dni= '" + dni + "' AND contrasena= '" + contrasena + "'";
            ResultSet rset = stmt.executeQuery(sqlStr);
            return rset;
        } catch (SQLException ex) {
            return null;
        }

    }

    public boolean cambiarContraseña(String dni, String nuevaContraseña) {
        conectarBaseDatos(); // Asegurar conexión

        if (conexion == null) {
            System.out.println("Error: conexión no establecida.");
            return false;
        }

        String sqlSelect = "SELECT nombre FROM agricultor WHERE dni = ?";
        String sqlUpdate = "UPDATE agricultor SET contrasena = ? WHERE dni = ?";

        try (
                PreparedStatement selectStmt = conexion.prepareStatement(sqlSelect);
                PreparedStatement updateStmt = conexion.prepareStatement(sqlUpdate)) {
            // Verifica si el usuario existe
            selectStmt.setString(1, dni);
            ResultSet rset = selectStmt.executeQuery();

            if (rset.next()) { // Si el usuario existe, actualiza la contraseña
                updateStmt.setString(1, nuevaContraseña);
                updateStmt.setString(2, dni);

                int filasActualizadas = updateStmt.executeUpdate();
                return filasActualizadas > 0; // Devuelve true si al menos 1 fila fue actualizada
            } else {
                System.out.println("El usuario con DNI " + dni + " no existe.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Devuelve false si hubo un error o el usuario no existe
    }

    public ResultSet listarParcelas() {
        conectarBaseDatos();

        /*Creamos un objeto statement*/
        Statement stmt;
        try {
            stmt = conexion.createStatement();
            String sqlStr = "SELECT * FROM parcelas";
            ResultSet rset = stmt.executeQuery(sqlStr);
            return rset;
        } catch (SQLException ex) {
            return null;
        }

    }
    
    public boolean insertarMaquina(String tipoMaquina) {
        conectarBaseDatos(); // Asegurar conexión
         try {
            String sqlInsertar = "INSERT INTO maquina (tipo_maquina) VALUES (?)";
           
            PreparedStatement preparedStatement = conexion.prepareStatement(sqlInsertar);
         
            preparedStatement.setString(1, tipoMaquina);
          
            preparedStatement.executeUpdate();
           
            //Cerramos los recursos
            preparedStatement.close();
            
            return true;
        } catch (SQLException e) {

            return false;
        }

        
    }
    public boolean agregarParcela(String idCatastro, String numeroParcela, String latitud, String longitud) {
    conectarBaseDatos(); // Asegurar conexión

    if (conexion == null) {
        System.out.println("Error: conexión no establecida.");
        return false;
    }

    try {
        // Desactivar autocommit para manejar transacciones
        conexion.setAutoCommit(false);

        // Insertar en `parcela`
        String sqlParcela = "INSERT INTO parcela (id_catastro, numero_parcela) VALUES (?, ?)";
        PreparedStatement stmtParcela = conexion.prepareStatement(sqlParcela);
        stmtParcela.setString(1, idCatastro);
        stmtParcela.setString(2, numeroParcela);
        stmtParcela.executeUpdate();

        // Insertar en `puntos`
        String sqlPuntos = "INSERT INTO puntos (numero_parcela, latitud, longitud) VALUES (?, ?, ?)";
        PreparedStatement stmtPuntos = conexion.prepareStatement(sqlPuntos);
        stmtPuntos.setString(1, numeroParcela);
        stmtPuntos.setString(2, latitud);
        stmtPuntos.setString(3, longitud);
        stmtPuntos.executeUpdate();

        // Confirmar la transacción
        conexion.commit();

        // Cerrar recursos
        stmtParcela.close();
        stmtPuntos.close();

        return true;
    } catch (SQLException e) {
        try {
            // Si hay error, deshacer la transacción
            conexion.rollback();
        } catch (SQLException rollbackEx) {
            rollbackEx.printStackTrace();
        }

        e.printStackTrace();
        return false;
    } finally {
        try {
            // Reactivar autocommit
            conexion.setAutoCommit(true);
        } catch (SQLException autoCommitEx) {
            autoCommitEx.printStackTrace();
        }
    }
}
    
     // Método para verificar si existen puntos asociados a una parcela
    public ResultSet verificarPuntosParcela(String idParcela, String dniCliente) {
        ResultSet resultSet = null;
        String query = "SELECT * FROM puntos_parcela WHERE id_parcela = ? AND dni_cliente = ?";
        
        try {
            PreparedStatement preparedStatement = conexion.prepareStatement(query);
            preparedStatement.setString(1, idParcela);  // Establece el valor para id_parcela
            preparedStatement.setString(2, dniCliente); // Establece el valor para dni_cliente
            
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return resultSet;
    }

    // Método para eliminar el punto de asociación de la parcela
    public boolean eliminarPuntoAsociado(String idParcela, String dniCliente) {
        String query = "DELETE FROM puntos_parcela WHERE id_parcela = ? AND dni_cliente = ?";
        boolean exito = false;

        try {
            PreparedStatement preparedStatement = conexion.prepareStatement(query);
            preparedStatement.setString(1, idParcela);  // Establece el valor para id_parcela
            preparedStatement.setString(2, dniCliente); // Establece el valor para dni_cliente
            
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
                exito = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exito;
    }

    // Método para eliminar la parcela de la base de datos
    public boolean eliminarParcela(String idParcela, String dniCliente) {
        String query = "DELETE FROM parcela WHERE id_parcela = ? AND dni_cliente = ?";
        boolean exito = false;

        try {
            PreparedStatement preparedStatement = conexion.prepareStatement(query);
            preparedStatement.setString(1, idParcela);  // Establece el valor para id_parcela
            preparedStatement.setString(2, dniCliente); // Establece el valor para dni_cliente
            
            int filasAfectadas = preparedStatement.executeUpdate();
            if (filasAfectadas > 0) {
                exito = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exito;
    }

    // Cerrar la conexión a la base de datos
    public void cerrarConexion() {
        try {
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    
    
    
    

}
