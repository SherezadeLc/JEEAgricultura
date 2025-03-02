<%@page import="es.agriculturaJ2EE.modelo.Agricultor"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modificar Agricultores</title>
    <style>
        body { 
            font-family: Arial, sans-serif;
            background-color: #e8f5e9; 
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
        h1 {
            color: #2e7d32;
        }
        input[type="submit"] {
            background-color: #2e7d32;
            color: white; 
            padding: 10px 20px;
            border:  none; 
            border-radius:5px; 
            cursor: pointer; 
            font-size: 16px; 
            width: 100%;
            transition: background-color 0.3s; 
            margin-top: 10px; 
        }
        input[type="submit"]:hover {
            background-color: #1b5e20; 
        }
        select, input {
            width: 100%; 
            padding: 8px;
            margin: 5px 0; 
            border: 1px solid #ddd;
            border-radius: 5px; 
        }
        .error {
            color: red; 
            font-weight: bold;
        }
        .mensaje {
            color: green; 
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="contenedor">
        <h2>Modificar Agricultores</h2>

        <% if (request.getAttribute("error") != null) { %>
            <p class="error"><%= request.getAttribute("error") %></p>
        <% } %>

        <% if (request.getAttribute("mensaje") != null) { %>
            <p class="mensaje"><%= request.getAttribute("mensaje") %></p>
        <% } %>

        <form action="Controlador" method="POST">
            <label for="dni">Seleccionar Agricultor:</label>
            <select name="dni" id="dni" required>
                <% 
                    List<Agricultor> agricultores = (List<Agricultor>) request.getAttribute("agricultores");
                    if (agricultores != null) {
                        for (Agricultor agricultor : agricultores) {
                %>
                    <option value="<%= agricultor.getDni() %>"><%= agricultor.getNombre() %> (DNI: <%= agricultor.getDni() %>)</option>
                <% 
                        }
                    } else {
                        out.println("<option value=''>No hay agricultores disponibles</option>");
                    }
                %>
            </select><br><br>

            <label for="nombre">Nuevo Nombre:</label>
            <input type="text" name="nombre" id="nombre" required><br><br>
            <label for="contrasena">Nueva Contraseña:</label>
            <input type="password" name="contrasena" id="contrasena" required><br><br>

            <input type="submit" name="modificar" value="Modificar">
        </form>

        <form action="Controlador">
            <input type="submit" value="Volver">
        </form>
    </div>
</body>
</html>
