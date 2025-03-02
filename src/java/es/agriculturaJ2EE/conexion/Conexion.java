/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.agriculturaJ2EE.conexion;

import es.agriculturaJ2EE.modelo.Trabajo;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
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
        // Usamos el método encargado de iniciar la conexión con la BD
        conectarBaseDatos();

        try (
                // Declaramos los PreparedStatement necesarios
                PreparedStatement preparedStatementCliente = conexion.prepareStatement("INSERT INTO cliente (dni, nombre, contrasena, id_catastro) VALUES (?, ?, ?, ?)");
                PreparedStatement preparedStatementParcela = conexion.prepareStatement("INSERT INTO parcela (id_catastro, numero_parcela) VALUES (?, ?)");
                PreparedStatement preparedStatementPuntos = conexion.prepareStatement("INSERT INTO puntos (numero_parcela, latitud, longitud) VALUES (?, ?, ?)");
                PreparedStatement preparedStatementPuntosParcela = conexion.prepareStatement("INSERT INTO puntos_parcela (id_punto, id_parcela, dni_cliente) VALUES (?, ?, ?)");
                PreparedStatement stmtPunto = conexion.prepareStatement("SELECT id_punto FROM puntos WHERE numero_parcela = ?");
                PreparedStatement stmtParcela = conexion.prepareStatement("SELECT id_parcela FROM parcela WHERE numero_parcela = ?")) {
            // Insertar datos en la tabla cliente
            preparedStatementCliente.setString(1, dni);
            preparedStatementCliente.setString(2, nombre);
            preparedStatementCliente.setString(3, contrasena);
            preparedStatementCliente.setString(4, idCatastro);

            // Insertar datos en la tabla parcela
            preparedStatementParcela.setString(1, idCatastro);
            preparedStatementParcela.setString(2, numero_parcela);

            // Insertar datos en la tabla puntos
            preparedStatementPuntos.setString(1, numero_parcela);
            preparedStatementPuntos.setString(2, latitud);
            preparedStatementPuntos.setString(3, longitud);

            // Ejecutar inserciones en las tablas cliente, parcela y puntos
            preparedStatementCliente.executeUpdate();
            preparedStatementParcela.executeUpdate();
            preparedStatementPuntos.executeUpdate();

            // Verificar si el punto existe en la tabla puntos
            stmtPunto.setString(1, numero_parcela);
            ResultSet rsetPunto = stmtPunto.executeQuery();

            // Verificar si la parcela existe en la tabla parcela
            stmtParcela.setString(1, numero_parcela);
            ResultSet rsetParcela = stmtParcela.executeQuery();

            if (rsetPunto.next() && rsetParcela.next()) {
                // Si el punto y la parcela existen, obtenemos los ids correspondientes
                String idPunto = rsetPunto.getString("id_punto");
                String idParcela = rsetParcela.getString("id_parcela");

                // Insertar en la tabla puntos_parcela
                preparedStatementPuntosParcela.setString(1, idPunto);
                preparedStatementPuntosParcela.setString(2, idParcela);
                preparedStatementPuntosParcela.setString(3, dni);
                preparedStatementPuntosParcela.executeUpdate();
            }

            // Cerrar recursos
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
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

    public ResultSet listarParcelas(String dni) {
        conectarBaseDatos();

        try {
            // Consulta SQL corregida con PreparedStatement para evitar SQL Injection
            String sqlStr = "SELECT * FROM parcela WHERE id_parcela IN (SELECT id_parcela FROM puntos_parcela WHERE dni_cliente = ?)";

            PreparedStatement stmt = conexion.prepareStatement(sqlStr);
            stmt.setString(1, dni);

            // Ejecutar la consulta y devolver el resultado
            return stmt.executeQuery();

        } catch (SQLException ex) {
            // Imprimir error en consola para depuración
            ex.printStackTrace();
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

    public boolean agregarParcela(String dni, String idCatastro, String numero_parcela, String latitud, String longitud) {
        // Usamos el método encargado de iniciar la conexión con la BD
        conectarBaseDatos();

        try (
                // Declaramos los PreparedStatement necesarios
                PreparedStatement preparedStatementParcela = conexion.prepareStatement("INSERT INTO parcela (id_catastro, numero_parcela) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                PreparedStatement preparedStatementPuntos = conexion.prepareStatement("INSERT INTO puntos (numero_parcela, latitud, longitud) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                PreparedStatement preparedStatementPuntosParcela = conexion.prepareStatement("INSERT INTO puntos_parcela (id_punto, id_parcela, dni_cliente) VALUES (?, ?, ?)");
                PreparedStatement stmtPunto = conexion.prepareStatement("SELECT id_punto FROM puntos WHERE numero_parcela = ?");
                PreparedStatement stmtParcela = conexion.prepareStatement("SELECT id_parcela FROM parcela WHERE numero_parcela = ?")) {
            // Insertar datos en la tabla parcela
            preparedStatementParcela.setString(1, idCatastro);
            preparedStatementParcela.setString(2, numero_parcela);
            int rowsInsertedParcela = preparedStatementParcela.executeUpdate();
            if (rowsInsertedParcela == 0) {
                System.out.println("Error al insertar en la tabla parcela");
                return false;
            }

            // Insertar datos en la tabla puntos
            preparedStatementPuntos.setString(1, numero_parcela);
            preparedStatementPuntos.setString(2, latitud);
            preparedStatementPuntos.setString(3, longitud);
            int rowsInsertedPuntos = preparedStatementPuntos.executeUpdate();
            if (rowsInsertedPuntos == 0) {
                System.out.println("Error al insertar en la tabla puntos");
                return false;
            }

            // Obtener los IDs generados
            ResultSet rsParcela = preparedStatementParcela.getGeneratedKeys();
            String idParcela = null;
            if (rsParcela.next()) {
                idParcela = rsParcela.getString(1);
                System.out.println("idParcela generado: " + idParcela);
            } else {
                System.out.println("No se generó idParcela");
                return false;
            }

            ResultSet rsPunto = preparedStatementPuntos.getGeneratedKeys();
            String idPunto = null;
            if (rsPunto.next()) {
                idPunto = rsPunto.getString(1);
                System.out.println("idPunto generado: " + idPunto);
            } else {
                System.out.println("No se generó idPunto");
                return false;
            }

            // Insertar en la tabla puntos_parcela
            preparedStatementPuntosParcela.setString(1, idPunto);
            preparedStatementPuntosParcela.setString(2, idParcela);
            preparedStatementPuntosParcela.setString(3, dni);
            int rowsInsertedPuntosParcela = preparedStatementPuntosParcela.executeUpdate();
            if (rowsInsertedPuntosParcela == 0) {
                System.out.println("Error al insertar en la tabla puntos_parcela");
                return false;
            }

            System.out.println("Insertado correctamente en puntos_parcela");

            // Confirmar que todo se insertó correctamente
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

// Método para verificar si existen puntos asociados a una parcela
    public ResultSet verificarPuntosParcela(String idParcela) {
        ResultSet resultSet = null;
        String query = "SELECT * FROM puntos_parcela WHERE id_parcela = ? ";

        try {
            PreparedStatement preparedStatement = conexion.prepareStatement(query);
            preparedStatement.setString(1, idParcela);  // Establece el valor para id_parcela
           

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    // Método para eliminar el punto de asociación de la parcela
    public boolean eliminarPuntoAsociado(String idParcela) {
        String query = "DELETE FROM puntos_parcela WHERE id_parcela = ? ";
        boolean exito = false;

        try {
            PreparedStatement preparedStatement = conexion.prepareStatement(query);
            preparedStatement.setString(1, idParcela);  // Establece el valor para id_parcela
          

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
    public boolean eliminarParcela(String idParcela) {
        String query = "DELETE FROM parcela WHERE id_parcela = ? ";
        boolean exito = false;

        try {
            PreparedStatement preparedStatement = conexion.prepareStatement(query);
            preparedStatement.setString(1, idParcela);  // Establece el valor para id_parcela
            

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

    public int obtenerIdAgricultorPorDni(String dniAgricultor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Trabajo> obtenerTrabajosDisponibles(int idAgricultor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Trabajo> obtenerTrabajosAsignados(int idAgricultor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean asignarTrabajo(int idTrabajo, int idAgricultor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
