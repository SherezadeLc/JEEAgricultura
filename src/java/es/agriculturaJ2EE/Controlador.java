package es.agriculturaJ2EE;

import es.agriculturaJ2EE.conexion.Conexion;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Controlador extends HttpServlet {

    private static String URL = "jdbc:mysql://localhost:3306/agricultura";
    private static String USER = "root";
    private static String PASSWORD = "";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        /*Creamos una instancia de la clase conexion*/
        Conexion conexion = new Conexion();
        HttpSession session = request.getSession(true);

        try {

            RequestDispatcher rd = null;
            String ruta = "";
            // Recojo el value de los botones de login y registro y en función de cuál se le de entra en uno y te manda a una cosa o a otra
            String botonSeleccionado = request.getParameter("enviar");
            //aqui comprueba si es entrar lo que se ha guardado en la variable y si es lo que se quiere que en este caso es entrar entra en el if
            if ("Entrar".equals(botonSeleccionado)) {
                login(request, response);

                // Aseguramos que la ruta esté correcta
                ruta = "/menu.jsp";

            } else if ("Registrar".equals(botonSeleccionado)) {
                // Aseguramos que la ruta esté correcta
                ruta = "/registro.jsp";

            } else if ("Salir".equals(botonSeleccionado)) {
                // Aseguramos que la ruta esté correcta
                ruta = "/login.jsp";

            } else if ("Confirmar".equals(botonSeleccionado)) {
                String nombre = request.getParameter("nombre");
                String dni = request.getParameter("dni");
                String contrasena = request.getParameter("password");
                String idCatastro = request.getParameter("id_catastro");
                String numero_parcela = request.getParameter("numero_parcela");
                String latitud = request.getParameter("latitud");
                String longitud = request.getParameter("longitud");

                System.out.println(nombre);
                System.out.println(dni);
                System.out.println(contrasena);
                System.out.println(idCatastro);
                System.out.println(numero_parcela);
                System.out.println(latitud);
                System.out.println(longitud);

                boolean comprobar = conexion.insertarCliente(nombre, dni, contrasena, idCatastro, numero_parcela, latitud, longitud);
                if (comprobar) {
                    session.setAttribute("registroMensaje", "El usuario " + nombre + " fue guardado correctamente");
                    ruta = "/registro.jsp";
                } else {
                    session.setAttribute("registroMensaje", "No se pudo crear el registro, introduce otro usuario");
                    ruta = "/registro.jsp";
                }

            }
            /*Redirigo la peticion */
            rd = getServletContext().getRequestDispatcher(ruta);
            rd.forward(request, response);

        } finally {
            out.close();
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usuario = request.getParameter("usuario");
        String contraseña = request.getParameter("contraseña");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM usuarios WHERE usuario = ? AND contraseña = ?");
            stmt.setString(1, usuario);
            stmt.setString(2, contraseña);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                response.sendRedirect("dashboard.jsp");
            } else {
                response.sendRedirect("login.jsp?error=1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cambiarContraseña(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usuario = request.getParameter("usuario");
        String nuevaContraseña = request.getParameter("nuevaContraseña");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE usuarios SET contraseña = ? WHERE usuario = ?");
            stmt.setString(1, nuevaContraseña);
            stmt.setString(2, usuario);
            stmt.executeUpdate();
            response.sendRedirect("login.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listarParcelas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM parcelas");
            ResultSet rs = stmt.executeQuery();
            List<String> parcelas = new ArrayList<>();
            while (rs.next()) {
                parcelas.add(rs.getString("nombre"));
            }
            request.setAttribute("parcelas", parcelas);
            request.getRequestDispatcher("listarParcelas.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para agregar una nueva parcela
    private void agregarParcela(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Obtener los parámetros enviados por el formulario
        String idCatastro = request.getParameter("id_catastro");
        String numeroParcela = request.getParameter("numero_parcela");
        String latitud = request.getParameter("latitud");
        String longitud = request.getParameter("longitud");

        // Validar que todos los campos están completos
        if (idCatastro == null || numeroParcela == null || latitud == null || longitud == null
                || idCatastro.isEmpty() || numeroParcela.isEmpty() || latitud.isEmpty() || longitud.isEmpty()) {
            response.sendRedirect("añadir_parcela.jsp?error=Todos los campos son obligatorios");
            return;
        }

        // Conexión a la base de datos y manejo de inserciones
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Insertar en la tabla parcela
            String sqlParcela = "INSERT INTO parcela (id_catastro, numero_parcela) VALUES (?, ?)";
            PreparedStatement stmtParcela = conn.prepareStatement(sqlParcela);
            stmtParcela.setString(1, idCatastro);
            stmtParcela.setString(2, numeroParcela);
            int filasParcela = stmtParcela.executeUpdate(); // Ejecutar la inserción

            // Insertar en la tabla puntos (coordenadas)
            String sqlPuntos = "INSERT INTO puntos (numero_parcela, latitud, longitud) VALUES (?, ?, ?)";
            PreparedStatement stmtPuntos = conn.prepareStatement(sqlPuntos);
            stmtPuntos.setString(1, numeroParcela);
            stmtPuntos.setString(2, latitud);
            stmtPuntos.setString(3, longitud);
            int filasPuntos = stmtPuntos.executeUpdate(); // Ejecutar la inserción

            // Obtener ID de la parcela recién insertada
            String sqlObtenerParcela = "SELECT id_parcela FROM parcela WHERE numero_parcela=?";
            PreparedStatement stmtGetParcela = conn.prepareStatement(sqlObtenerParcela);
            stmtGetParcela.setString(1, numeroParcela);
            ResultSet rsParcela = stmtGetParcela.executeQuery();
            int idParcela = rsParcela.next() ? rsParcela.getInt("id_parcela") : -1;

            // Obtener ID del punto recién insertado
            String sqlObtenerPunto = "SELECT id_punto FROM puntos WHERE numero_parcela=?";
            PreparedStatement stmtGetPunto = conn.prepareStatement(sqlObtenerPunto);
            stmtGetPunto.setString(1, numeroParcela);
            ResultSet rsPunto = stmtGetPunto.executeQuery();
            int idPunto = rsPunto.next() ? rsPunto.getInt("id_punto") : -1;

            // Obtener el DNI del cliente desde la sesión
            HttpSession session = request.getSession();
            String dniCliente = (String) session.getAttribute("dni");

            // Insertar la relación entre puntos y parcela en la tabla puntos_parcela
            if (idParcela > 0 && idPunto > 0) {
                String sqlRelacion = "INSERT INTO puntos_parcela (id_punto, id_parcela, dni_cliente) VALUES (?, ?, ?)";
                PreparedStatement stmtRelacion = conn.prepareStatement(sqlRelacion);
                stmtRelacion.setInt(1, idPunto);
                stmtRelacion.setInt(2, idParcela);
                stmtRelacion.setString(3, dniCliente);
                stmtRelacion.executeUpdate(); // Ejecutar la inserción
            }

            // Redirigir al usuario con un mensaje de éxito
            response.sendRedirect("añadir_parcela.jsp?mensaje=Parcela añadida correctamente");
        } catch (Exception e) {
            // Si hay un error, mostrar mensaje de error
            e.printStackTrace();
            response.sendRedirect("añadir_parcela.jsp?error=Error en la base de datos");
        }
    }

    private void listarMaquinas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM maquinas");
            ResultSet rs = stmt.executeQuery();
            List<String> maquinas = new ArrayList<>();
            while (rs.next()) {
                maquinas.add(rs.getString("nombre"));
            }
            request.setAttribute("maquinas", maquinas);
            request.getRequestDispatcher("listarMaquinas.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void agregarMaquina(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Obtener el tipo de máquina seleccionado en el formulario
        String tipoMaquina = request.getParameter("tipo_maquina");

        // 2. Verificar que el usuario haya seleccionado una opción
        if (tipoMaquina == null || tipoMaquina.isEmpty()) {
            response.sendRedirect("agregar_maquina.jsp?error=Debe seleccionar un tipo de máquina");
            return; // Detiene la ejecución del método
        }

        // 3. Conectar a la base de datos
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            // 4. Crear la consulta SQL para insertar la máquina
            String sqlInsertar = "INSERT INTO maquina (tipo_maquina) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sqlInsertar);
            stmt.setString(1, tipoMaquina);

            // 5. Ejecutar la consulta
            int filasAfectadas = stmt.executeUpdate();

            // 6. Verificar si se insertó correctamente
            if (filasAfectadas > 0) {
                response.sendRedirect("agregar_maquina.jsp?mensaje=Máquina añadida correctamente");
            } else {
                response.sendRedirect("agregar_maquina.jsp?error=No se pudo agregar la máquina");
            }

        } catch (Exception e) {
            // 7. Manejar errores y mostrar mensaje de error
            e.printStackTrace();
            response.sendRedirect("agregar_maquina.jsp?error=Error en la base de datos");
        }
    }

    private void crearTrabajo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String descripcion = request.getParameter("descripcion");
        int parcelaId = Integer.parseInt(request.getParameter("parcelaId"));
        int maquinaId = Integer.parseInt(request.getParameter("maquinaId"));
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO trabajos (descripcion, parcela_id, maquina_id) VALUES (?, ?, ?)");
            stmt.setString(1, descripcion);
            stmt.setInt(2, parcelaId);
            stmt.setInt(3, maquinaId);
            stmt.executeUpdate();
            response.sendRedirect("index.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listarAgricultores(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM agricultores");
            ResultSet rs = stmt.executeQuery();
            List<String> agricultores = new ArrayList<>();
            while (rs.next()) {
                agricultores.add(rs.getString("nombre"));
            }
            request.setAttribute("agricultores", agricultores);
            request.getRequestDispatcher("listarAgricultores.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void agregarAgricultor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Obtener los datos enviados desde el formulario
        String nombre = request.getParameter("nombre");
        String dni = request.getParameter("dni");
        String contrasena = request.getParameter("contrasena");

        // 2. Verificar que los campos no estén vacíos
        if (nombre == null || nombre.isEmpty()
                || dni == null || dni.isEmpty()
                || contrasena == null || contrasena.isEmpty()) {

            response.sendRedirect("añadir_agricultores.jsp?error=Todos los campos son obligatorios");
            return; // Detener la ejecución del método
        }

        // 3. Conectar a la base de datos
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            // 4. Verificar si el DNI ya existe en la base de datos
            String sqlVerificar = "SELECT COUNT(*) FROM agricultores WHERE dni = ?";
            PreparedStatement checkStmt = conn.prepareStatement(sqlVerificar);
            checkStmt.setString(1, dni);
            ResultSet rs = checkStmt.executeQuery();
            rs.next(); // Mover el cursor al primer resultado

            if (rs.getInt(1) > 0) {
                response.sendRedirect("añadir_agricultores.jsp?error=El DNI ya está registrado");
                return;
            }

            // 5. Insertar el agricultor en la base de datos
            String sqlInsertar = "INSERT INTO agricultores (nombre, dni, contrasena) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sqlInsertar);
            stmt.setString(1, nombre);
            stmt.setString(2, dni);
            stmt.setString(3, contrasena);

            // 6. Ejecutar la consulta
            int filasAfectadas = stmt.executeUpdate();

            // 7. Comprobar si la inserción fue exitosa
            if (filasAfectadas > 0) {
                response.sendRedirect("añadir_agricultores.jsp?mensaje=Agricultor añadido correctamente");
            } else {
                response.sendRedirect("añadir_agricultores.jsp?error=No se pudo añadir el agricultor");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("añadir_agricultores.jsp?error=Error en la base de datos");
        }
    }

    private void registro() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
