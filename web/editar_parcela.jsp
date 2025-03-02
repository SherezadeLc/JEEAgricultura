<%@page import="java.util.ArrayList"%>
<%@ page import="java.util.List" %>
<%@ page import="es.agriculturaJ2EE.modelo.Parcela" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Editar Parcelas</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                padding: 0;
                background-color: #f4f4f4;
            }

            .contenedor {
                width: 80%;
                margin: 20px auto;
                background-color: #fff;
                padding: 20px;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            }

            h2 {
                text-align: center;
                color: #333;
            }

            table {
                width: 100%;
                margin: 20px 0;
                border-collapse: collapse;
            }

            table, th, td {
                border: 1px solid #ddd;
            }

            th, td {
                padding: 10px;
                text-align: left;
            }

            th {
                background-color: #f2f2f2;
            }

            .boton-volver {
                display: flex;
                justify-content: space-between;
                margin-top: 20px;
            }

            .boton-volver input {
                padding: 10px 20px;
                background-color: #007bff;
                color: white;
                border: none;
                cursor: pointer;
                border-radius: 5px;
            }

            .boton-volver input:hover {
                background-color: #0056b3;
            }

            form {
                display: inline-block;
            }

            form input[type="submit"] {
                padding: 8px 16px;
                margin: 5px;
                background-color: #ff4d4d;
                color: white;
                border: none;
                cursor: pointer;
                border-radius: 5px;
            }

            form input[type="submit"]:hover {
                background-color: #e60000;
            }

            .c-message {
                font-size: 14px;
                color: green;
                text-align: center;
                margin-bottom: 20px;
            }
        </style>
    </head>
    <body>
        <div class="contenedor">
            <h2>Editar Parcelas</h2>
            <form method="POST" action="Controlador">
                <label>DNI:</label>
                <input type="text" name="dni">
                <button type="submit" name="enviar" value="editar_parcela">Editar parcelas</button><br><br>
            </form>

            <table border="1">
                <tr>
                    <th>ID Parcela</th>
                    <th>ID Catastro</th>
                    <th>Número Parcela</th>
                    <th>Acciones</th>
                </tr>
                <%
                    // Obtener la lista de parcelas desde la sesión
                    ArrayList<Parcela> parcelas = (ArrayList<Parcela>) session.getAttribute("parcelas");

                    // Verificar si la lista no es nula y tiene elementos
                    if (parcelas != null && !parcelas.isEmpty()) {
                        for (int i = 0; i < parcelas.size(); i++) {
                %>
                <tr>
                    <td><%= parcelas.get(i).getIdParcela()%></td>
                    <td><%= parcelas.get(i).getIdCatastro()%></td>
                    <td><%= parcelas.get(i).getNumeroParcela()%></td>
                    <td>
                        <!-- Botón Editar -->
                        <form action="Controlador" method="GET" style="display:inline-block;">
                            <input type="hidden" name="id_parcela" value="<%= parcelas.get(i).getIdParcela()%>">
                            <input type="submit" value="Editar">
                        </form>

                        <!-- Botón Eliminar -->
                        <form action="Eliminar_Parcela" method="POST" onsubmit="return confirm('¿Estás seguro de eliminar esta parcela?')" style="display:inline-block;">
                            <input type="hidden" name="id_parcela" value="<%= parcelas.get(i).getIdParcela()%>">
                            <input type="submit" name="eliminar" value="Eliminar">
                        </form>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="4" style="text-align:center;">No hay parcelas registradas.</td>
                </tr>
                <%
                    }
                %>
            </table>

            <div class="boton-volver">
                <form action="Controlador" method="POST">
                    <input type="submit" value="Añadir Parcela">
                </form>
                <form action="Controlador" method="POST">
                    <input type="submit" value="Volver">
                </form>
            </div>
        </div>
    </body>
</html>

