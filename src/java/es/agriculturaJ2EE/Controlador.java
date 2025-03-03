package es.agriculturaJ2EE;

import es.agriculturaJ2EE.conexion.Conexion;
import es.agriculturaJ2EE.modelo.Agricultor;
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
import java.sql.Statement;
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
                String dni = request.getParameter("dni");

                if (dni != null && !dni.isEmpty()) { // Validación de DNI

                    ResultSet recogerAgricultores = conexion.listarAgricultores(dni);

                    if (recogerAgricultores != null) {
                        ArrayList<Agricultor> todosAgricultor = new ArrayList<>();

                        try {
                            while (recogerAgricultores.next()) {
                                // Obtiene los datos reales de la base de datos
                                String pid = recogerAgricultores.getString("id_agricultor");
                                String pnombre = recogerAgricultores.getString("nombre");

                                String pdni = recogerAgricultores.getString("dni");

                                Agricultor datosAgricultores = new Agricultor(pid, pnombre, pdni);
                                todosAgricultor.add(datosAgricultores);
                            }

                            if (!todosAgricultor.isEmpty()) {
                                session.setAttribute("agricultor", todosAgricultor);
                            } else {
                                session.setAttribute("agricultor", null); // Si no hay parcelas
                            }

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("El ResultSet es NULL, posiblemente la consulta SQL falló.");
                    }
                }

                // Aseguramos que la ruta esté correcta
                ruta = "/añadir_agricultores.jsp";
            } else if ("Listar_Clientes".equals(botonSeleccionado)) {
                String dniCliente = request.getParameter("id_cliente");

                if (dniCliente != null && !dniCliente.isEmpty()) { // Validación de DNI
                    ResultSet recogerClientes = conexion.editarCliente(dniCliente);

                    if (recogerClientes != null) {
                        ArrayList<Cliente> todasClientes = new ArrayList<>();

                        try {
                            while (recogerClientes.next()) {
                                // Obtiene los datos reales de la base de datos

                                String pidNombre = recogerClientes.getString("nombre");
                                String piDni = recogerClientes.getString("ddni");
                                String pidCatastro = recogerClientes.getString("id_catastro");

                                Cliente datosClientes = new Cliente(pidNombre, piDni, pidCatastro);
                                todasClientes.add(datosClientes);
                            }

                            if (!todasClientes.isEmpty()) {
                                session.setAttribute("cliente", todasClientes);
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

                // Aseguramos que la ruta esté correcta
                ruta = "/editar_clientes.jsp";

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
                ruta = "/elegir_trabajo.jsp";

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
                ruta = "/crear_trabajo.jsp";

            } else if ("Eliminar".equals(botonSeleccionado)) {
                // Obtener el ID de la parcela a eliminar
                String idParcela = request.getParameter("id_parcelas");

                // Verificar que se ha proporcionado un ID de parcela
                if (idParcela != null && !idParcela.isEmpty()) {
                    // Eliminar el punto de asociación (de la tabla puntos_parcela)
                    boolean eliminarPuntoAsociado = conexion.eliminarPuntoAsociado(idParcela);

                    if (eliminarPuntoAsociado) {
                        // Si la eliminación del punto de asociación fue exitosa, proceder a eliminar la parcela
                        boolean eliminarParcela = conexion.eliminarParcela(idParcela);

                        if (eliminarParcela) {
                            // Si la eliminación de la parcela fue exitosa, establecer mensaje en la sesión
                            session.setAttribute("mensaje", "La parcela y su punto de asociación han sido eliminados correctamente.");
                        } else {
                            // Si hubo un error al eliminar la parcela, establecer mensaje de error
                            session.setAttribute("mensaje", "Error al eliminar la parcela.");
                        }
                    } else {
                        // Si no se pudo eliminar el punto de asociación, establecer mensaje de error
                        session.setAttribute("mensaje", "No se pudo eliminar el punto de asociación.");
                    }
                } else {
                    // Si no se proporcionó un ID de parcela válido
                    session.setAttribute("mensaje", "No se proporcionó un ID de parcela válido.");
                }

                // Redirigir a la página de edición de parcelas
                ruta = "/editar_parcelas.jsp"; // o la ruta que corresponda

            } else if ("editar".equals(botonSeleccionado)) {
                // Aseguramos que la ruta esté correcta
                ruta = "/modificar_parcela.jsp";

            } else if ("Volver".equals(botonSeleccionado)) {
                // Aseguramos que la ruta esté correcta
                ruta = "/menu.jsp";

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
        List<Agricultor> agricultores = new ArrayList<>();

        try {
            // Cargar el driver de MySQL (necesario en algunos entornos)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Conexión y consulta
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                    PreparedStatement stmt = conn.prepareStatement("SELECT dni, Nombre FROM agricultor");
                    ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    agricultores.add(new Agricultor(rs.getString("dni"), rs.getString("Nombre")));
                }
            }
        } catch (ClassNotFoundException e) {
            log("Error: No se encontró el driver de MySQL", e);
        } catch (SQLException e) {
            log("Error de conexión a la base de datos", e);
        }

        // Enviar la lista de agricultores a la vista
        request.setAttribute("agricultores", agricultores);
        request.getRequestDispatcher("modificar_agricultores.jsp").forward(request, response);
    }

    private void modificarAgricultor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String dni = request.getParameter("dni");
        String nuevoNombre = request.getParameter("nombre");
        String nuevaContrasena = request.getParameter("contrasena");

        if (dni == null || nuevoNombre == null || nuevaContrasena == null || dni.isEmpty() || nuevoNombre.isEmpty() || nuevaContrasena.isEmpty()) {
            response.sendRedirect("editar_agricultores.jsp?error=Todos los campos son obligatorios");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Cargar el driver para GlassFish 4.1.1
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "UPDATE agricultor SET nombre = ?, contrasena = ? WHERE dni = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nuevoNombre);
                    stmt.setString(2, nuevaContrasena);
                    stmt.setString(3, dni);
                    int filasActualizadas = stmt.executeUpdate();

                    if (filasActualizadas > 0) {
                        response.sendRedirect("editar_agricultores.jsp?mensaje=Agricultor actualizado correctamente");
                    } else {
                        response.sendRedirect("editar_agricultores.jsp?error=No se pudo actualizar el agricultor");
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect("editar_agricultores.jsp?error=Error: No se encontró el driver de MySQL");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("editar_agricultores.jsp?error=Error en la base de datos");
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

    private void editarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        response.setContentType("text/html;charset=UTF-8");

        // Usa try-with-resources para PrintWriter
        try (PrintWriter out = response.getWriter()) {

            RequestDispatcher rd;
            String ruta = "";

            // Verificamos la acción que llega por parámetro
            if ("EditarCliente".equals(action)) {

                // 1. Obtenemos el parámetro id_cliente
                String idCliente = request.getParameter("id_cliente");
                System.out.println("ID del cliente a editar: " + idCliente);

                // 2. Si está vacío o es nulo, manejamos el error
                if (idCliente == null || idCliente.isEmpty()) {
                    out.println("Error: No se ha recibido un ID de cliente válido.");
                    return;
                }

                Cliente cliente = null;

                // 3. Conectamos a la base de datos y hacemos la consulta
                try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                        PreparedStatement stmt = conn.prepareStatement(
                                "SELECT * FROM cliente WHERE id_cliente = ?")) {

                    stmt.setInt(1, Integer.parseInt(idCliente));

                    // Ejecutamos la consulta
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            cliente = new Cliente(
                                    rs.getInt("id_cliente"),
                                    rs.getString("nombre"),
                                    rs.getString("dni"),
                                    rs.getString("id_catastro")
                            );
                            System.out.println("Cliente recuperado: " + cliente.getNombre());
                        } else {
                            System.out.println("No se encontró ningún cliente con el ID: " + idCliente);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // 4. Asignamos el cliente al request
                request.setAttribute("cliente", cliente);

                // 5. Definimos la ruta de la JSP que mostrará los datos
                ruta = "/editar_clientes.jsp";

            } else if ("ActualizarCliente".equals(action)) {

                // 1. Obtenemos los datos del formulario
                String idCliente = request.getParameter("id_cliente");
                String nombre = request.getParameter("nombre");
                String dni = request.getParameter("dni");
                String idCatastro = request.getParameter("id_catastro");

                System.out.println("Actualizando cliente con ID: " + idCliente);

                // 2. Ejecutamos el UPDATE en la base de datos
                try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                        PreparedStatement stmt = conn.prepareStatement(
                                "UPDATE cliente SET nombre = ?, dni = ?, id_catastro = ? WHERE id_cliente = ?")) {

                    stmt.setString(1, nombre);
                    stmt.setString(2, dni);
                    stmt.setString(3, idCatastro);
                    stmt.setInt(4, Integer.parseInt(idCliente));

                    stmt.executeUpdate();
                    System.out.println("Cliente actualizado con éxito.");

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // 3. Redirigimos a donde corresponda tras actualizar.
                //    Ajusta esta ruta a tu necesidad (p.e. volver al listado).
                //    Si tienes un servlet "ListarClientesServlet", redirígelo allí.
                response.sendRedirect("Controlador?action=editar_clientes");
                return;
            }

            // Finalmente, hacemos el forward a la JSP correspondiente
            rd = getServletContext().getRequestDispatcher(ruta);
            rd.forward(request, response);

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
            List<Trabajo> trabajosDisponibles = conexion.obtenerTrabajosDisponibles(idAgricultor); // Ahora se asigna

            System.out.println("Trabajos disponibles obtenidos: " + (trabajosDisponibles != null ? trabajosDisponibles.size() : "Lista nula"));

            if (trabajosDisponibles != null) {
                for (Trabajo t : trabajosDisponibles) {
                    System.out.println("ID: " + t.getId() + ", Descripción: " + t.getDescripcion());
                }
            } else {
                System.out.println("Error: trabajosDisponibles es null.");
            }

            /*Conexion conexion = new Conexion();
            int idAgricultor = conexion.obtenerIdAgricultorPorDni(dniAgricultor);
            List<Trabajo> trabajosDisponibles = conexion.obtenerTrabajosDisponibles(idAgricultor);*/
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

    private void modificarParcela(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        /*Creamos una instancia de la clase conexion*/
        Conexion conexion = new Conexion();
        HttpSession session = request.getSession();

        try {

            RequestDispatcher rd = null;
            String ruta = "";
            String botonSeleccionado = request.getParameter("enviar");

            // Lógica de login y otras acciones...
            // Nueva acción para modificar parcelas
            if ("ModificarParcelas".equals(botonSeleccionado)) {
                // Llamar al servlet ModificarParcelasServlet
                request.getRequestDispatcher("/ModificarParcelasServlet").forward(request, response);
            }
            // Otras acciones, como "Anadir_Agricultores", "Anadir_Maquinas", etc.

            /*Redirigo la peticion */
            rd = getServletContext().getRequestDispatcher(ruta);
            rd.forward(request, response);

        } finally {
            out.close();
        }
    }

    private void eliminarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Definimos el tipo de contenido de la respuesta (opcional, pero recomendable)
        response.setContentType("text/html;charset=UTF-8");

        // 2. Obtenemos el parámetro 'id_cliente' que nos llega en la URL o formulario
        String idCliente = request.getParameter("id_cliente");
        System.out.println("ID del cliente a eliminar: " + idCliente);

        // 3. Validamos que el parámetro no sea nulo ni vacío
        if (idCliente == null || idCliente.isEmpty()) {
            // Si no hay un id válido, mostramos un mensaje o redirigimos a un error
            try (PrintWriter out = response.getWriter()) {
                out.println("Error: No se recibió un ID de cliente válido.");
            }
            return;
        }

        // 4. Convertimos el id a entero
        int idClienteInt;
        try {
            idClienteInt = Integer.parseInt(idCliente);
        } catch (NumberFormatException e) {
            // Si no se puede convertir a entero, es un error
            try (PrintWriter out = response.getWriter()) {
                out.println("Error: El ID de cliente no es un número válido.");
            }
            return;
        }

        // 5. Conectamos a la base de datos y ejecutamos el DELETE
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM cliente WHERE id_cliente = ?")) {

            stmt.setInt(1, idClienteInt); // Reemplaza la interrogación por el valor del id

            // Ejecutamos la sentencia DELETE
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Cliente eliminado con éxito.");
            } else {
                System.out.println("No se encontró ningún cliente con ese ID para eliminar.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 6. Redirigimos a donde corresponda tras eliminar.
        //    Ajusta esta ruta a lo que necesites (por ejemplo, volver a la lista de clientes)
        response.sendRedirect("Controlador?action=ListarClientes");
    }

    private void registro() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
