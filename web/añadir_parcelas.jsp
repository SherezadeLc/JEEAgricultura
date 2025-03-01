<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Añadir Parcelas</title>
        <style>
            /* Estilos igual que en tu código PHP */
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
                background-color: #ffffff;
                padding: 30px;
                width: 80%;
                max-width: 600px;
                border-radius: 10px;
                box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
                box-sizing: border-box;
            }
            h2 {
                color: #388e3c;
                margin-bottom: 20px;
            }
            label {
                font-size: 16px;
                font-weight: bold;
                color: #333;
            }
            input[type="text"] {
                width: 100%;
                padding: 10px;
                margin: 10px 0;
                border: 1px solid #ccc;
                border-radius: 5px;
                font-size: 16px;
            }
            button[type="submit"] {
                background-color: #388e3c;
                color: white;
                padding: 10px 20px;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                font-size: 16px;
                width: 100%;
                transition: background-color 0.3s;
            }
            button[type="submit"]:hover {
                background-color: #2c6e29;
            }
            .mensaje {
                font-size: 14px;
                color: #d32f2f;
                font-weight: bold;
                margin-top: 10px;
            }
            .mensaje-exito {
                color: #388e3c;
            }
        </style>
    </head>
    <body>
        <div class="contenedor">
            <form method="POST" action="Controlador">
                <label for="id_catastro">Número Catastro:</label><br>
                <input type="text" name="id_catastro" id="id_catastro" required><br><br>

                <label for="numero_parcela">Número Parcela:</label><br>
                <input type="text" name="numero_parcela" id="numero_parcela" required><br><br>

                <label for="latitud">Latitud:</label><br>
                <input type="text" name="latitud" id="latitud" required><br><br>

                <label for="longitud">Longitud:</label><br>
                <input type="text" name="longitud" id="longitud" required><br><br>

                <button type="submit">Agregar Parcela</button><br><br>
            </form>

            <%-- Mensajes de error o éxito --%>
            <%
                String mensaje = request.getParameter("mensaje");
                if (mensaje != null) {
            %>
            <p class="mensaje-exito"><%= mensaje%></p>
            <%
                }
                String error = request.getParameter("error");
                if (error != null) {
            %>
            <p class="mensaje"><%= error%></p>
            <% }%>

            <form action="editar_parcela.jsp" method="POST">
                <input type="submit" value="Volver">
            </form>
        </div>
    </body>
</html>
