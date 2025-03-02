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
        HttpSession sesion = request.getSession(true);
        if (sesion != null && sesion.getAttribute("rol") != null) {
            String nombre = (String) sesion.getAttribute("nombre");
            String tipo = (String) sesion.getAttribute("rol");
        %>
        <h1>Bienvenido, <%= nombre %></h1>
        <p>Rol: <%= tipo %></p>
        
        <% if ("admin".equals(tipo)) { %>
            <form action="Controlador" method="POST">
                <input type="submit" value="Anadir_Agricultores"name="enviar">
            </form>
            <form action="Controlador" method="POST">
                <input type="submit" value="Listar_Clientes"name="enviar">
            </form>
            <form action="Controlador" method="POST">
                <input type="submit" value="Anadir_Maquinas"name="enviar">
            </form>
        <% } else if ("agricultor".equals(tipo)) { %>
            <form action="Controlador" method="POST">
                <input type="submit" value="Elegir_trabajo"name="enviar">
            </form>
            <form action="Controlador" method="POST">
                <input type="submit" value="Cambiar_contrasena"name="enviar">
            </form>
        <% } else if ("cliente".equals(tipo)) { %>
            <form action="Controlador" method="POST">
                <input type="submit" value="Anadir_parcelas"name="enviar">
            </form>
            <form action="Controlador" method="POST">
                <input type="submit" value="Crear_trabajo"name="enviar">
            </form>
        <% } %>
        <form action="Controlador" method="POST">
            <input type="submit" value="Cerrar_sesion" name="enviar">
        </form>
        <% } else { %>
            <p class="alert">Usted no tiene acceso a esta página.</p>
        <% } %>
    </div>
</body>
</html>

