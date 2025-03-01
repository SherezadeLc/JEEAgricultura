<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Añadir Agricultor</title>
    <style>
        /* Estilo general del cuerpo */
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f7f6;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        /* Contenedor principal */
        .contenedor {
            background-color: #ffffff;
            padding: 30px;
            width: 80%;
            max-width: 500px;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            box-sizing: border-box;
        }

        /* Títulos */
        h2 {
            color: #388e3c;
            text-align: center;
        }

        /* Estilo para las etiquetas de los campos */
        label {
            font-size: 16px;
            color: #388e3c;
            margin-bottom: 5px;
            display: inline-block;
        }

        /* Estilo para los inputs */
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 5px;
            border: 1px solid #ccc;
            font-size: 14px;
        }

        /* Botón de añadir */
        input[type="submit"] {
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

        input[type="submit"]:hover {
            background-color: #2c6e29;
        }

        /* Mensajes de éxito o error */
        p {
            text-align: center;
            font-weight: bold;
        }

        p.success {
            color: #388e3c;
        }

        p.error {
            color: #d32f2f;
        }
    </style>
</head>
<body>
    <div class="contenedor">
        <h2>Añadir Agricultores</h2>
        <!-- Formulario para añadir un agricultor -->
        <form action="Controlador?action=agregarAgricultor" method="POST">
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre" required><br><br>
            
            <label for="dni">DNI:</label>
            <input type="text" id="dni" name="dni" required><br><br>

            <label for="contrasena">Contraseña:</label>
            <input type="password" id="contrasena" name="contrasena" required><br><br>

            <input type="submit" name="añadir" value="Añadir">
        </form>

        <!-- Mensajes de respuesta -->
        <%
            String mensaje = request.getParameter("mensaje");
            String error = request.getParameter("error");
            if (mensaje != null) {
        %>
            <p class="success"><%= mensaje %></p>
        <%
            } else if (error != null) {
        %>
            <p class="error"><%= error %></p>
        <%
            }
        %>

        <a href="editar_agricultores.jsp">
            <input type="submit" name="volver" value="Volver">
        </a>
    </div>
</body>
</html>

