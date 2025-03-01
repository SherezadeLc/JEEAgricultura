<%-- 
    Document   : registro
    Created on : 28-feb-2025, 17:32:50
    Author     : lorea
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        
         <form method="POST" action="Controlador">
                <label>Nombre:</label>
                <input type="text" name="nombre" required>
                <label>Apellidos:</label>
                <input type="text" name="apellidos" required>
                <label>DNI:</label>
                <input type="text" name="dni" required>
                <label>Id Catastro:</label>
                <input type="text" name="id_catastro" required>
                <label>Número Parcela:</label>
                <input type="text" name="numero_parcela" required>
                <label>Latitud:</label>
                <input type="text" name="latitud" required>
                <label>Longitud:</label>
                <input type="text" name="longitud" required>
                <label>Contraseña:</label>
                <input type="password" name="password" required>
                <input type="submit" name="enviar" value="Confirmar">
            </form>
    </body>
</html>
