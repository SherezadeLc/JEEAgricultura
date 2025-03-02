<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Máquina</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            text-align: center;
            padding: 20px;
        }

        .container {
            width: 40%;
            margin: auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        }

        h2 {
            color: #333;
        }

        form {
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        label {
            margin-top: 10px;
            font-weight: bold;
        }

        input {
            width: 80%;
            padding: 8px;
            margin: 5px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-top: 15px;
        }

        button:hover {
            background-color: #45a049;
        }

        .back-link {
            display: block;
            margin-top: 20px;
            text-decoration: none;
            color: #007BFF;
        }

        .back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Editar Máquina</h2>
        <form action="Controlador" method="post">
            <input type="hidden" name="action" value="Editar_Maquina">
            
            <label>ID Máquina:</label>
            <input type="text" name="id_maquina" value="${idMaquina}" readonly>
            
            <label>Nombre:</label>
            <input type="text" name="nombre" value="${nombre}" required>
            
            <label>Tipo:</label>
            <input type="text" name="tipo" value="${tipo}" required>
            
            <label>Estado:</label>
            <input type="text" name="estado" value="${estado}" required>
            <button type="submit">Guardar Cambios</button>
        </form>
           <a class="back-link" href="Controlador">Volver al menú</a>
    </div>
</body>
</html>
