<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Agricultor</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 50%;
            margin: 50px auto;
            padding: 20px;
            background-color: white;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }
        h1 {
            text-align: center;
            color: #333;
        }
        label {
            font-size: 16px;
            color: #555;
            margin-bottom: 5px;
            display: block;
        }
        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0 20px 0;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
        }
        input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 10px 20px;
            cursor: pointer;
            border-radius: 4px;
            font-size: 16px;
            width: 100%;
        }
        input[type="submit"]:hover {
            background-color: #45a049;
        }
        .error-message {
            color: red;
            font-size: 14px;
            margin-top: 10px;
            text-align: center;
        }
        .success-message {
            color: green;
            font-size: 14px;
            margin-top: 10px;
            text-align: center;
        }
    </style>
</head>
<body>

    <div class="container">
        <h1>Editar Agricultor</h1>
        
        <c:if test="${not empty param.error}">
            <div class="error-message">${param.error}</div>
        </c:if>
        <c:if test="${not empty param.mensaje}">
            <div class="success-message">${param.mensaje}</div>
        </c:if>

        <form action="Controlador" method="post">
            <input type="hidden" name="action" value="editarAgricultor">
            <input type="hidden" name="id_agricultor" value="${agricultor.id_agricultor}"> 
            
            <label for="nombre">Nombre:</label>
            <input type="text" name="nombre" id="nombre" value="${agricultor.nombre}" required>

            <label for="dni">DNI:</label>
            <input type="text" name="dni" id="dni" value="${agricultor.dni}" required>

            <label for="contrasena">Contraseña:</label>
            <input type="password" name="contrasena" id="contrasena" value="${agricultor.contrasena}" required>

            <input type="submit" value="Actualizar">
        </form>
    </div>

</body>
</html>
