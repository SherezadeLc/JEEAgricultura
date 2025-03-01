<%@page import="java.sql.ResultSet"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Crear Trabajo</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #e8f5e9;
            text-align: center;
        }
        .contenedor {
            background: #fff;
            padding: 20px;
            width: 50%;
            margin: auto;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }
        select, button {
            width: 100%;
            padding: 10px;
            margin-top: 10px;
        }
        table {
            width: 100%;
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <div class="contenedor">
        <h2>Crear Trabajo</h2>
        <form method="POST" action="Controlador">
            <label>Parcela:</label>
            <select name="id_parcela" required>
                <option value="">Seleccione</option>
                <% 
                    ResultSet resultadoParcelas = (ResultSet) request.getAttribute("resultadoParcelas");
                    while (resultadoParcelas != null && resultadoParcelas.next()) { 
                %>
                    <option value="<%= resultadoParcelas.getString("id_parcela") %>">
                        Parcela #<%= resultadoParcelas.getString("numero_parcela") %>
                    </option>
                <% } %>
            </select>

            <label>Máquina:</label>
            <select name="id_maquina" required>
                <option value="">Seleccione</option>
                <% 
                    ResultSet resultadoMaquinas = (ResultSet) request.getAttribute("resultadoMaquinas");
                    while (resultadoMaquinas != null && resultadoMaquinas.next()) { 
                %>
                    <option value="<%= resultadoMaquinas.getString("id_maquina") %>">
                        <%= resultadoMaquinas.getString("tipo_maquina") %>
                    </option>
                <% } %>
            </select>

            <label>Tipo de Trabajo:</label>
            <select name="tipo_trabajo" required>
                <option value="Labrar">Labrar</option>
                <option value="Sembrar">Sembrar</option>
                <option value="Regar">Regar</option>
                <option value="Fertilizar">Fertilizar</option>
            </select>

            <button type="submit">Crear Trabajo</button>
        </form>
    </div>
</body>
</html>
