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
       <h1>Login </h1>
        
            <form method="POST" action="Controlador">
                
                <label>DNI:</label>
                <input type="text" name="dni" value="">
                <label>Contraseña:</label>
                <input type="password" name="password" value=""/><br><br>
                 <input type="submit" value="Entrar" name="enviar"/>
                <p>¿No estás registrado?</p>
                <button type="submit" name="enviar" value="Registrar" name="Registrar">Registrar</button>
               
            </form>
    </body>
</html>
