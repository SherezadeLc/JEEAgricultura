<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<%
    HttpSession sesion = request.getSession();
    String mensaje = request.getParameter("mensaje");
    String error = request.getParameter("error");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Agregar Máquina</title>
    <style>
        /* General */
        body {
            font-family: Arial, sans-serif;
            background-color: #e8f5e9;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .contenedor {
            width: 50%;
            padding: 20px;
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        h1, h2 {
            color: #2e7d32;
        }

        input[type="submit"], button {
            background-color: #2e7d32;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            width: 100%;
            transition: background-color 0.3s;
            margin-top: 10px;
        }

        input[type="submit"]:hover, button:hover {
            background-color: #1b5e20;
        }

        select {
            padding: 10px;
            font-size: 16px;
            width: 100%;
            border-radius: 5px;
            border: 1px solid #ccc;
            margin-bottom: 15px;
        }

        .boton-volver {
            margin-top: 20px;
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="contenedor">
        <form method="POST" action="Controlador">
            <h2>Agregar Máquina</h2>
            <label for="tipo_maquina">Tipo de máquina:</label><br>
            <select name="tipo_maquina" id="tipo_maquina" required> 
                <option value="">Seleccione un tipo de máquina</option>
                <option value="Graduadas y cultivadores">Graduadas y cultivadores</option>
                <option value="Sembradoras de precisión">Sembradoras de precisión</option>
                <option value="Aspersores">Aspersores</option>
                <option value="Abonadoras">Abonadoras</option>
                <option value="Cosechadora">Cosechadora</option>
                <option value="Desbrozadora">Desbrozadora</option>
                <option value="Arado">Arado</option>
                <option value="Subsoladores">Subsoladores</option>
            </select>
            <br>
            <button type="submit"name="enviar" value="anadir_maquina">Agregar Máquina</button><br><br>
        </form>

        <% if (mensaje != null) { %>
            <p style="color: green;"><%= mensaje %></p>
        <% } %>

        <% if (error != null) { %>
            <p style="color: red;"><%= error %></p>
        <% } %>

        <form action="editar_maquinas.jsp" method="POST">
            <input type="submit" name="volver" value="Volver">
        </form>
    </div>
</body>
</html>
