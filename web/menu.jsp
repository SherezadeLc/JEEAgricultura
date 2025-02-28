<%-- 
    Document   : menu
    Created on : 28-feb-2025, 16:26:10
    Author     : Sherezade
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Menú Principal</title>
</head>
<body>
    <div class="contenedor">
        <%
        HttpSession sesion = request.getSession(false);
        if (sesion != null && sesion.getAttribute("tipo") != null) {
            String nombre = (String) sesion.getAttribute("nombre");
            String tipo = (String) sesion.getAttribute("tipo");
        %>
        <h1>Bienvenido, <%= nombre %></h1>
        <p>Rol: <%= tipo %></p>
        
        <% if ("administrador".equals(tipo)) { %>
            <form action="ControladorServlet?accion=editarAgricultores" method="POST">
                <input type="submit" value="Añadir Agricultores">
            </form>
            <form action="ControladorServlet?accion=editarClientes" method="POST">
                <input type="submit" value="Listar Clientes">
            </form>
            <form action="ControladorServlet?accion=editarMaquinas" method="POST">
                <input type="submit" value="Añadir Máquinas">
            </form>
        <% } else if ("agricultor".equals(tipo)) { %>
            <form action="ControladorServlet?accion=elegirTrabajo" method="POST">
                <input type="submit" value="Elegir trabajo">
            </form>
            <form action="ControladorServlet?accion=cambiarContraseña" method="POST">
                <input type="submit" value="Cambiar contraseña">
            </form>
        <% } else if ("cliente".equals(tipo)) { %>
            <form action="ControladorServlet?accion=editarParcela" method="POST">
                <input type="submit" value="Añadir parcelas">
            </form>
            <form action="ControladorServlet?accion=crearTrabajo" method="POST">
                <input type="submit" value="Crear trabajo">
            </form>
        <% } %>
        <form action="ControladorServlet?accion=logout" method="POST">
            <input type="submit" value="Cerrar sesión">
        </form>
        <% } else { %>
            <p class="alert">Usted no tiene acceso a esta página.</p>
        <% } %>
    </div>
</body>
</html>

