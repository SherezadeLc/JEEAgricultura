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
                <input type="text" name="nombre" >
                <label>Apellidos:</label>
                <input type="text" name="apellidos" >
                <label>DNI:</label>
                <input type="text" name="dni" >
                <label>Id Catastro:</label>
                <input type="text" name="id_catastro" >
                <label>Número Parcela:</label>
                <input type="text" name="numero_parcela" >
                <label>Latitud:</label>
                <input type="text" name="latitud" >
                <label>Longitud:</label>
                <input type="text" name="longitud" >
                <label>Contraseña:</label>
                <input type="password" name="password" >
                <input type="submit" value="Confirmar" name="enviar" /><br><br>
            </form>
    </body>
</html>
