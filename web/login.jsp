<%-- 
    Document   : Agricultor
    Created on : 27-feb-2025, 17:08:08
    Author     : Sherezade
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form name="form" action="Controlador" method="POST" enctype="multipart/form-data">
                <h1>Login</h1>
                <label>DNI:</label>
                <input type="text" name="dni" required>
                <label>Contraseña:</label>
                <input type="password" name="password" required>
                <input type="submit" name="enviar" value="Enviar">
                <p>¿No estás registrado?</p>
                <a href="registro.php"><input type="button" class="boton-registro" name="Registrar" value="Registrar"/></a>
            </form>
    </body>
</html>
