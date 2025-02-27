package es.agriculturaJ2EE;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Controlador")
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

        if (action == null) {
            response.sendRedirect("index.html");
            return;
        }

        switch (action) {
            case "login":
                login(request, response);
                break;
            case "registro":
                registro(request, response);
                break;
            case "cambiarContraseña":
                cambiarContraseña(request, response);
                break;
            case "listarParcelas":
                listarParcelas(request, response);
                break;
            case "agregarParcela":
                agregarParcela(request, response);
                break;
            case "listarMaquinas":
                listarMaquinas(request, response);
                break;
            case "agregarMaquina":
                agregarMaquina(request, response);
                break;
            case "crearTrabajo":
                crearTrabajo(request, response);
                break;
            case "listarAgricultores":
                listarAgricultores(request, response);
                break;
            case "agregarAgricultor":
                agregarAgricultor(request, response);
                break;
            default:
                response.sendRedirect("index.html");
                break;
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
        String usuario = request.getParameter("usuario");
        String contraseña = request.getParameter("contraseña");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO usuarios (usuario, contraseña) VALUES (?, ?)");
            stmt.setString(1, usuario);
            stmt.setString(2, contraseña);
            stmt.executeUpdate();
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
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO agricultores (nombre) VALUES (?)");
            stmt.setString(1, nombre);
            stmt.executeUpdate();
            response.sendRedirect("Controlador?action=listarAgricultores");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
