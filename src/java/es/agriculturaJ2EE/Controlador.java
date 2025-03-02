package es.agriculturaJ2EE;

import es.agriculturaJ2EE.conexion.Conexion;
import es.agriculturaJ2EE.modelo.Cliente;
import es.agriculturaJ2EE.modelo.Parcela;
import es.agriculturaJ2EE.modelo.Trabajo;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.validator.internal.util.Contracts;

public class Controlador extends HttpServlet {

    private static String MENU = "/menu.jsp";
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
        HttpSession session = request.getSession();

        try {

            RequestDispatcher rd = null;
            String ruta = "";
            // Recojo el value de los botones de login y registro y en función de cuál se le de entra en uno y te manda a una cosa o a otra
            String botonSeleccionado = request.getParameter("enviar");
            //aqui comprueba si es entrar lo que se ha guardado en la variable y si es lo que se quiere que en este caso es entrar entra en el if
            if ("Entrar".equals(botonSeleccionado)) {
                // Obtener credenciales del request
                String dni = request.getParameter("dni");
                String contrasena = request.getParameter("password");

// Conectar a la base de datos
                conexion.conectarBaseDatos();

// Obtener la sesión
                ResultSet resultados = null;
                ResultSet resultado = null;
                ResultSet resultadoAdmin = null;

                try {
                    // Realizar consultas
                    resultados = conexion.loginCliente(dni, contrasena);
                    resultado = conexion.loginAgricultor(dni, contrasena);
                    resultadoAdmin = conexion.loginAdministrador(dni, contrasena);

                    String rol = null, nombrePers = null;

                    if (resultados != null && resultados.next()) {
                        rol = "cliente";
                        nombrePers = resultados.getString("nombre");

                    } else if (resultado != null && resultado.next()) {
                        rol = "agricultor";
                        nombrePers = resultado.getString("nombre");

                    } else if (resultadoAdmin != null && resultadoAdmin.next()) {
                        rol = "admin";
                        nombrePers = resultadoAdmin.getString("nombre");

                    }

                    if (rol != null) {
                        session.setAttribute("rol", rol);
                        session.setAttribute("nombre", nombrePers);

                        System.out.println("¡Credenciales válidas!");
                        System.out.println(rol);
                        System.out.println(nombrePers);

                        ruta = MENU;
                    } else {
                        System.out.println("¡Credenciales inválidas!");
                        session.invalidate(); // Destruir sesión si es inválido
                        ruta = "/login.jsp";
                    }

                } catch (SQLException e) {
                    e.printStackTrace();

                }

            } else if ("Registrar".equals(botonSeleccionado)) {
                // Aseguramos que la ruta esté correcta
                ruta = "/registro.jsp";

            } else if ("Anadir_Agricultores".equals(botonSeleccionado)) {
                // Aseguramos que la ruta esté correcta
                ruta = "/añadir_agricultores.jsp";

            } else if ("Listar_Clientes".equals(botonSeleccionado)) {
                // Aseguramos que la ruta esté correcta
                ruta = "/registro.jsp";

            } else if ("Anadir_Maquinas".equals(botonSeleccionado)) {
                // Aseguramos que la ruta esté correcta
                ruta = "/añadir_maquina.jsp";

            } else if ("anadir_maquina".equals(botonSeleccionado)) {
                // 1. Obtener el tipo de máquina seleccionado en el formulario
                String tipoMaquina = request.getParameter("tipo_maquina");

                conexion.conectarBaseDatos();

                // 2. Verificar que el usuario haya seleccionado una opción
                if (tipoMaquina == null || tipoMaquina.isEmpty()) {
                    response.sendRedirect("agregar_maquina.jsp?error=Debe seleccionar un tipo de máquina");
                    return; // Detiene la ejecución del método
                }
                boolean comprobar = conexion.insertarMaquina(tipoMaquina);
                if (comprobar) {
                    session.setAttribute("registroMensaje", "La maquina nueva " + tipoMaquina + " fue guardado correctamente");
                    // Aseguramos que la ruta esté correcta
                    ruta = "/añadir_maquina.jsp";
                } else {
                    session.setAttribute("registroMensaje", "No se pudo añadir la nueva maquina");
                    // Aseguramos que la ruta esté correcta
                    ruta = "/añadir_maquina.jsp";
                }

            } else if ("Elegir_trabajo".equals(botonSeleccionado)) {
                // Aseguramos que la ruta esté correcta
                ruta = "/registro.jsp";

            } else if ("Cambiar_contrasena".equals(botonSeleccionado)) {
                // Aseguramos que la ruta esté correcta
                ruta = "/cambiar_contraseña.jsp";

            } else if ("actualizarContrasena".equals(botonSeleccionado)) {
                String dni = request.getParameter("dni");
                String contrasena = request.getParameter("nueva");

// Verificar que los parámetros no sean nulos ni vacíos
                if (dni == null || dni.isEmpty() || contrasena == null || contrasena.isEmpty()) {
                    session.setAttribute("cambioContrasenaMensaje", "Error: Debe llenar todos los campos.");
                    ruta = "/cambiar_contraseña.jsp";
                } else {
                    System.out.println("DNI ingresado: " + dni);
                    System.out.println("Nueva contraseña ingresada: " + contrasena);

                    boolean cambiar = conexion.cambiarContraseña(dni, contrasena);

                    if (cambiar) {
                        session.setAttribute("cambioContrasenaMensaje", "El usuario con DNI " + dni + " cambió correctamente la contraseña.");
                    } else {
                        session.setAttribute("cambioContrasenaMensaje", "Error: No se pudo cambiar la contraseña. Verifique los datos ingresados.");
                    }

                    ruta = "/cambiar_contraseña.jsp";
                }

            } else if ("Anadir_parcelas".equals(botonSeleccionado)) {
                // Aseguramos que la ruta esté correcta
                ruta = "/añadir_parcelas.jsp";

            } else if ("anadir_parcela".equals(botonSeleccionado)) {
                // Obtener los parámetros enviados por el formulario
                String dni = request.getParameter("dni");
                String idCatastro = request.getParameter("id_catastro");
                String numeroParcela = request.getParameter("numero_parcela");
                String latitud = request.getParameter("latitud");
                String longitud = request.getParameter("longitud");

                // Validar que todos los campos están completos
                if (dni == null || idCatastro == null || numeroParcela == null || latitud == null || longitud == null
                        || dni.isEmpty() || idCatastro.isEmpty() || numeroParcela.isEmpty() || latitud.isEmpty() || longitud.isEmpty()) {
                    response.sendRedirect("añadir_parcela.jsp?error=Todos los campos son obligatorios");
                    return;
                }
                boolean comprobar = conexion.agregarParcela(dni, idCatastro, numeroParcela, latitud, longitud);
                if (comprobar) {
                    session.setAttribute("registroMensaje", "Se añadio la parcela");
                    ruta = "/añadir_parcelas.jsp";
                } else {
                    session.setAttribute("registroMensaje", "No se pudo añadir la parcela");
                    ruta = "/añadir_parcelas.jsp";
                }

            } else if ("editar_parcela".equals(botonSeleccionado)) {
                String dni = request.getParameter("dni");

                if (dni != null && !dni.isEmpty()) { // Validación de DNI
                    ResultSet recogerParcelas = conexion.listarParcelas(dni);

                    if (recogerParcelas != null) {
                        ArrayList<Parcela> todasParcelas = new ArrayList<>();

                        try {
                            while (recogerParcelas.next()) {
                                // Obtiene los datos reales de la base de datos
                                String pidParcela = recogerParcelas.getString("id_parcela");
                                String pidCatastro = recogerParcelas.getString("id_catastro");
                                String pnumeroParcela = recogerParcelas.getString("numero_parcela");

                                Parcela datosParcela = new Parcela(pidParcela, pidCatastro, pnumeroParcela);
                                todasParcelas.add(datosParcela);
                            }

                            if (!todasParcelas.isEmpty()) {
                                session.setAttribute("parcelas", todasParcelas);
                            } else {
                                session.setAttribute("parcelas", null); // Si no hay parcelas
                            }

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("El ResultSet es NULL, posiblemente la consulta SQL falló.");
                    }
                } else {
                    System.out.println("DNI no válido o vacío.");
                }

                ruta = "/editar_parcela.jsp"; // Ruta para la página JSP
            } else if ("Crear_trabajo".equals(botonSeleccionado)) {
                // Aseguramos que la ruta esté correcta
                ruta = "/registro.jsp";

            }else if ("eliminar".equals(botonSeleccionado)) {
                String id_parcela = request.getParameter("id_parcela");
                ResultSet recogerParcelas = conexion.listarParcelas(id_parcela);
                
                // Aseguramos que la ruta esté correcta
                ruta = "/registro.jsp";

            }else if ("editar".equals(botonSeleccionado)) {
                // Aseguramos que la ruta esté correcta
                ruta = "/registro.jsp";

            } else if ("Cerrar_sesion".equals(botonSeleccionado)) {
                session.invalidate();

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

    private void crearTrabajo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String descripcion = request.getParameter("descripcion");
        int parcelaId = Integer.parseInt(request.getParameter("parcelaId"));
        int maquinaId = Integer.parseInt(request.getParameter("maquinaId"));

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO trabajos (descripcion, parcela_id, maquina_id) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, descripcion);
            stmt.setInt(2, parcelaId);
            stmt.setInt(3, maquinaId);
            stmt.executeUpdate();

            // Redirige a una página de éxito o la vista de trabajos
            response.sendRedirect("menu.jsp?mensaje=Trabajo creado correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("crearTrabajo.jsp?error=Error al crear el trabajo");
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

    private void editarAgricultor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idAgricultor = request.getParameter("id_agricultor");
        String nombre = request.getParameter("nombre");
        String dni = request.getParameter("dni");
        String contrasena = request.getParameter("contrasena");

        // Validar que los campos no estén vacíos
        if (nombre == null || nombre.isEmpty() || dni == null || dni.isEmpty() || contrasena == null || contrasena.isEmpty()) {
            response.sendRedirect("editar_agricultor.jsp?error=Todos los campos son obligatorios");
            return;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Crear la consulta SQL para actualizar los datos del agricultor
            String sql = "UPDATE agricultor SET nombre = ?, dni = ?, contrasena = ? WHERE id_agricultor = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, dni);
            stmt.setString(3, contrasena);
            stmt.setString(4, idAgricultor);

            // Ejecutar la actualización
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                response.sendRedirect("listarAgricultores.jsp?mensaje=Agricultor actualizado correctamente");
            } else {
                response.sendRedirect("editar_agricultor.jsp?error=No se pudo actualizar el agricultor");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("editar_agricultor.jsp?error=Error en la base de datos");
        }
    }
    private static String EDITAR_CLIENTE = "/editar_cliente.jsp";

    private void editarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Conexion conexion = new Conexion();
        HttpSession session = request.getSession();

        try {
            RequestDispatcher rd = null;
            String ruta = "";

            if ("EditarCliente".equals(action)) {
                // Obtener ID del cliente a editar
                String idCliente = request.getParameter("id_cliente");

                Cliente cliente = null;
                try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cliente WHERE id_cliente = ?")) {
                    stmt.setInt(1, Integer.parseInt(idCliente));
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        cliente = new Cliente(
                                rs.getInt("id_cliente"),
                                rs.getString("nombre"),
                                rs.getString("dni"),
                                rs.getString("id_catastro")
                        );
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                request.setAttribute("cliente", cliente);
                ruta = EDITAR_CLIENTE;

            } else if ("ActualizarCliente".equals(action)) {
                // Obtener datos del formulario
                String idCliente = request.getParameter("id_cliente");
                String nombre = request.getParameter("nombre");
                String dni = request.getParameter("dni");
                String idCatastro = request.getParameter("id_catastro");

                try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                        PreparedStatement stmt = conn.prepareStatement(
                                "UPDATE cliente SET nombre = ?, dni = ?, id_catastro = ? WHERE id_cliente = ?")) {
                    stmt.setString(1, nombre);
                    stmt.setString(2, dni);
                    stmt.setString(3, idCatastro);
                    stmt.setInt(4, Integer.parseInt(idCliente));

                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                response.sendRedirect("ListarClientesServlet");
                return;
            }

            rd = getServletContext().getRequestDispatcher(ruta);
            rd.forward(request, response);

        } finally {
            out.close();
        }
    }

    private void editarMaquina(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idMaquina = request.getParameter("idMaquina");
        String nuevoNombre = request.getParameter("nombre");
        String nuevoTipo = request.getParameter("tipo");

        // Validar que los campos no estén vacíos
        if (idMaquina == null || nuevoNombre == null || nuevoTipo == null
                || idMaquina.isEmpty() || nuevoNombre.isEmpty() || nuevoTipo.isEmpty()) {

            response.sendRedirect("editar_maquina.jsp?error=Todos los campos son obligatorios");
            return;
        }

        try {
            // Cargar el driver de MySQL (necesario en GlassFish)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecer la conexión
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                    PreparedStatement stmt = conn.prepareStatement("UPDATE maquinas SET nombre = ?, tipo = ? WHERE id_maquina = ?")) {

                stmt.setString(1, nuevoNombre);
                stmt.setString(2, nuevoTipo);
                stmt.setString(3, idMaquina);

                int filasActualizadas = stmt.executeUpdate();

                if (filasActualizadas > 0) {
                    response.sendRedirect("editar_maquina.jsp?mensaje=Máquina actualizada correctamente");
                } else {
                    response.sendRedirect("editar_maquina.jsp?error=No se encontró la máquina a actualizar");
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de MySQL.");
            e.printStackTrace();
            response.sendRedirect("editar_maquina.jsp?error=Error interno del servidor");
        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("editar_maquina.jsp?error=Error al actualizar en la base de datos");
        }
    }

    private void obtenerDatosMaquina(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idMaquina = request.getParameter("id_maquina");
        String nombre = "";
        String tipo = "";
        String estado = "";

        if (idMaquina != null && !idMaquina.isEmpty()) {
            try {
                // Cargar el driver de MySQL (para evitar problemas en GlassFish)
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Conectar a la base de datos
                try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM maquinas WHERE id_maquina = ?")) {

                    stmt.setString(1, idMaquina);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            nombre = rs.getString("nombre");
                            tipo = rs.getString("tipo");
                            estado = rs.getString("estado");
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                System.err.println("Error: No se encontró el driver de MySQL.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("Error de conexión a la base de datos.");
                e.printStackTrace();
            }
        }

        // Enviar datos como atributos para la vista JSP
        request.setAttribute("idMaquina", idMaquina);
        request.setAttribute("nombre", nombre);
        request.setAttribute("tipo", tipo);
        request.setAttribute("estado", estado);
        request.getRequestDispatcher("editar_maquina.jsp").forward(request, response);
    }

    private void editarParcela(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        /*Creamos una instancia de la clase conexion*/
        Conexion conexion = new Conexion();
        HttpSession session = request.getSession();

        try {

            RequestDispatcher rd = null;
            String ruta = "";
            // Aquí recojo el valor del botón de acción (como 'Eliminar Parcela')
            String botonSeleccionado = request.getParameter("enviar");

            // Eliminar parcela
            if ("Eliminar_parcela".equals(botonSeleccionado)) {
                String idParcela = request.getParameter("id_parcela");
                String dniCliente = (String) session.getAttribute("dni"); // Obtener el DNI del cliente desde la sesión

                // Realizar la verificación en la base de datos
                conexion.conectarBaseDatos();
                ResultSet resultadoReferencias = conexion.verificarPuntosParcela(idParcela);

                if (resultadoReferencias.next()) {
                    // Eliminar puntos de asociación
                    boolean eliminarPuntoAsociado = conexion.eliminarPuntoAsociado(idParcela);
                    if (eliminarPuntoAsociado) {
                        // Eliminar parcela
                        boolean eliminarParcela = conexion.eliminarParcela(idParcela);
                        if (eliminarParcela) {
                            session.setAttribute("mensaje", "La parcela ha sido eliminada correctamente.");
                        } else {
                            session.setAttribute("mensaje", "Error al eliminar la parcela.");
                        }
                    } else {
                        session.setAttribute("mensaje", "No se pudo eliminar el punto de asociación.");
                    }
                } else {
                    session.setAttribute("mensaje", "No puedes eliminar esta parcela porque no se encuentran puntos asociados o no pertenecen a tu cuenta.");
                }

                // Redirigir después de la operación
                ruta = "/editar_parcelas.jsp"; // o la ruta que corresponda
            }

            /* Redirijo la petición */
            rd = getServletContext().getRequestDispatcher(ruta);
            rd.forward(request, response);

        } catch (SQLException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
    }
    public class ElegirTrabajo extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String dniAgricultor = (String) session.getAttribute("dni");

        if (dniAgricultor == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Conexion conexion = new Conexion();
        int idAgricultor = conexion.obtenerIdAgricultorPorDni(dniAgricultor);
        List<Trabajo> trabajosDisponibles = conexion.obtenerTrabajosDisponibles(idAgricultor);
        List<Trabajo> trabajosAsignados = conexion.obtenerTrabajosAsignados(idAgricultor);

        request.setAttribute("trabajosDisponibles", trabajosDisponibles);
        request.setAttribute("trabajosAsignados", trabajosAsignados);
        request.getRequestDispatcher("elegirTrabajo.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String dniAgricultor = (String) session.getAttribute("dni");

        if (dniAgricultor == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int idAgricultor = new Conexion().obtenerIdAgricultorPorDni(dniAgricultor);
        int idTrabajo = Integer.parseInt(request.getParameter("id_trabajo"));

        Conexion conexion = new Conexion();
        boolean asignado = conexion.asignarTrabajo(idTrabajo, idAgricultor);

        if (asignado) {
            response.sendRedirect("ElegirTrabajoServlet?mensaje=Trabajo asignado correctamente");
        } else {
            response.sendRedirect("ElegirTrabajoServlet?mensaje=Error al asignar el trabajo");
        }
    }
}

    private void registro() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
