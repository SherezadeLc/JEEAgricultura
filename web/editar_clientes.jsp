<%@ page import="modelo.Cliente" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar Cliente</title>
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
        input[type="text"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        input[type="submit"] {
            background-color: #2e7d32;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        input[type="submit"]:hover {
            background-color: #1b5e20;
        }
    </style>
</head>
<body>
    <div class="contenedor">
        <h2>Editar Cliente</h2>
        <%
            Cliente cliente = (Cliente) request.getAttribute("cliente");
            if (cliente != null) {
        %>
        <form action="Controlador?action=ActualizarCliente" method="POST">
            <input type="hidden" name="id_cliente" value="<%= cliente.getId() %>">
            <label>Nombre:</label>
            <input type="text" name="nombre" value="<%= cliente.getNombre() %>" required>
            <label>DNI:</label>
            <input type="text" name="dni" value="<%= cliente.getDni() %>" required>
            <label>Nº Catastro:</label>
            <input type="text" name="id_catastro" value="<%= cliente.getIdCatastro() %>" required>
            <input type="submit" value="Actualizar">
        </form>
        <% } else { %>
            <p>Error al cargar el cliente.</p>
        <% } %>
        <br>
        <form action="ListarClientesServlet" method="GET">
            <input type="submit" value="Cancelar">
        </form>
    </div>
</body>
</html>
