package es.agriculturaJ2EE;

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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Controlador extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String URL = "jdbc:mysql://localhost:3306/agricultura";
    private static final String USER = "root";
    private static final String PASSWORD = "";

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

            }
            if ("Registrar".equals(botonSeleccionado)) {
                // Aseguramos que la ruta esté correcta
                ruta = "/registro.jsp";

            }
            if ("Salir".equals(botonSeleccionado)) {
                // Aseguramos que la ruta esté correcta
                ruta = "/login.jsp";

            }
            if ("Confirmar".equals(botonSeleccionado)) {
                String nombre = request.getParameter("nombre");
                String dni = request.getParameter("dni");
                String contraseña = request.getParameter("password");
                String idCatastro = request.getParameter("id_catastro");
                String numero_parcela = request.getParameter("numero_parcela");
                String latitud = request.getParameter("latitud");
                String longitud = request.getParameter("longitud");

            }
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

    private void registro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String dni = request.getParameter("dni");
        String contrasena = request.getParameter("password");
        String idCatastro = request.getParameter("id_catastro");
        String numero_parcela = request.getParameter("numero_parcela");
        String latitud = request.getParameter("latitud");
        String longitud = request.getParameter("longitud");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO cliente (dni,nombre, contraseña,id_catastro) VALUES (?, ?,?,?)");
            PreparedStatement stmt1 = conn.prepareStatement(" INSERT INTO parcela (id_catastro, numero_parcela) VALUES  (?, ?)");
            PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO puntos (numero_parcela, latitud, longitud) VALUES (?, ?,?)");
            stmt.setString(1, dni);
            stmt.setString(2, nombre);
            stmt.setString(3, contrasena);
            stmt.setString(4, idCatastro);
            stmt1.setString(1, idCatastro);
            stmt1.setString(2, numero_parcela);
            stmt2.setString(1, numero_parcela);
            stmt2.setString(2, latitud);
            stmt2.setString(2, longitud);

            stmt.executeUpdate();
            stmt1.executeUpdate();
            stmt2.executeUpdate();
            response.sendRedirect("login.jsp");
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

    private void agregarParcela(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO parcelas (nombre) VALUES (?)");
            stmt.setString(1, nombre);
            stmt.executeUpdate();
            response.sendRedirect("Controlador?action=listarParcelas");
        } catch (Exception e) {
            e.printStackTrace();
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
        String nombre = request.getParameter("nombre");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO maquinas (nombre) VALUES (?)");
            stmt.setString(1, nombre);
            stmt.executeUpdate();
            response.sendRedirect("Controlador?action=listarMaquinas");
        } catch (Exception e) {
            e.printStackTrace();
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
        String nombre = request.getParameter("nombre");
        String dni = request.getParameter("dni");
        String contrasena = request.getParameter("contrasena");

        if (nombre == null || dni == null || contrasena == null || nombre.isEmpty() || dni.isEmpty() || contrasena.isEmpty()) {
            response.sendRedirect("añadir_agricultores.jsp?error=Todos los campos son obligatorios");
            return;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Verificar si el DNI ya está registrado
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM agricultores WHERE dni = ?");
            checkStmt.setString(1, dni);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                response.sendRedirect("añadir_agricultores.jsp?error=El DNI ya está registrado");
                return;
            }

            // Insertar el nuevo agricultor
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO agricultores (nombre, dni, contrasena) VALUES (?, ?, ?)");
            stmt.setString(1, nombre);
            stmt.setString(2, dni);
            stmt.setString(3, contrasena);

            int filasAfectadas = stmt.executeUpdate();
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
}
