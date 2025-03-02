
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
            <h2>Detalles del Cliente</h2>
            <%
                Cliente cliente = (Cliente) request.getAttribute("cliente");
                if (cliente != null) {
            %>
            <table>
                <tr>
                    <td><strong>Nombre:</strong></td>
                    <td><%= cliente.getNombre()%></td>
                </tr>
                <tr>
                    <td><strong>DNI:</strong></td>
                    <td><%= cliente.getDni()%></td>
                </tr>
                <tr>
                    <td><strong>Nº Catastro:</strong></td>
                    <td><%= cliente.getIdCatastro()%></td>
                </tr>
            </table>
            <% } else { %>
            <p>Error al cargar el cliente.</p>
            <% }%>
            <br>
            <form action="Controlador" method="GET">
                <input type="submit" value="Cancelar">
            </form>
        </div>
    </body>

</html>
