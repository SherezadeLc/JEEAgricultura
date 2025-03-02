
<%@page import="es.agriculturaJ2EE.modelo.Cliente"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Editar Cliente</title>
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

            h1 {
                color: #2e7d32;
            }

            input[type="submit"] {
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

            input[type="submit"]:hover {
                background-color: #1b5e20;
            }

            table {
                width: 100%;
                margin-top: 10px;
                border-collapse: collapse;
            }

            th, td {
                padding: 5px;
                text-align: center;
                border: 1px solid #ddd;
            }

            th {
                background-color: #2e7d32;
                color: white;
            }

            td form {
                margin: 0;
            }

            td input[type="submit"] {
                width: auto;
                margin: 5px;
                padding: 5px 10px;
            }

            .boton-volver{
                margin-top: 20px;
                text-align: center;
            }
        </style>
    </head>
    <body>
        <div class="contenedor">
            <h2>Editar Cliente</h2>

            <%
                Cliente cliente = (Cliente) request.getAttribute("cliente");
                if (cliente == null) {
                    out.println("<p>Error: No se encontró el cliente.</p>");
                } else {
            %>

            <form action="ClienteControlador" method="POST">
                <input type="hidden" name="action" value="ActualizarCliente">
                <input type="hidden" name="id_cliente" value="<%= cliente.getId()%>">

                <label for="nombre">Nombre:</label>
                <input type="text" id="nombre" name="nombre" value="<%= cliente.getNombre()%>" required><br><br>

                <label for="dni">DNI:</label>
                <input type="text" id="dni" name="dni" value="<%= cliente.getDni()%>" required><br><br>

                <label for="id_catastro">ID Catastro:</label>
                <input type="text" id="id_catastro" name="id_catastro" value="<%= cliente.getIdCatastro()%>" required><br><br>

                <input type="submit" value="Actualizar Cliente">
                <button><a href="Controlador?action=EliminarCliente&id_cliente=${cliente.id_cliente}"
                   onclick="return confirm('¿Estás seguro de eliminar este cliente?');">
                    Eliminar
                </a></button>
                
            </form>

            <%
                }
            %>
        </div>
    </body>

</html>
