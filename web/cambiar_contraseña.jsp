<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Cambiar Contraseña</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                padding: 0;
                background-color: #f4f4f4;
            }
            .container {
                width: 50%;
                margin: 50px auto;
                background-color: white;
                padding: 20px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            }
            h2 {
                text-align: center;
            }
            .input-group {
                margin: 10px 0;
            }
            label {
                font-size: 14px;
            }
            input[type="password"] {
                width: 100%;
                padding: 10px;
                margin-top: 5px;
                border: 1px solid #ccc;
                border-radius: 5px;
            }
            .btn {
                width: 100%;
                padding: 10px;
                background-color: #4CAF50;
                color: white;
                border: none;
                border-radius: 5px;
                cursor: pointer;
            }
            .btn:hover {
                background-color: #45a049;
            }
            .error, .success {
                color: red;
                text-align: center;
                margin-top: 10px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Cambiar Contraseña</h2>
            <form action="Controlador" method="post">
                <label>DNI:</label>
                <input type="text" name="dni" value="">
               
                
                <label >Contraseña Actual:</label>
                <input type="password" id="actual" name="actual" >

                <label >Nueva Contraseña:</label>
                <input type="password" id="nueva" name="nueva" value="" >

                <label >Confirmar Nueva Contraseña:</label>
                <input type="password" id="confirmar" name="confirmar" value="" >

                <button type="submit"name="enviar" value="actualizarContrasena">Actualizar Contraseña</button>
            </form>
        </div>
    </body>
</html>
