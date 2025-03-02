<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modificar Parcelas</title>
    <style>
        /* Estilos para la página */
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f0f0;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .contenedor {
            background-color: white;
            padding: 20px;
            width: 80%;
            max-width: 800px;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        th, td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }
        th {
            background-color: #388e3c;
            color: white;
        }
        input[type="text"], input[type="submit"] {
            padding: 10px;
            width: 100%;
            margin: 5px 0 20px 0;
            border-radius: 5px;
            border: 1px solid #ccc;
        }
        input[type="submit"] {
            background-color: #388e3c;
            color: white;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #2c6e29;
        }
    </style>
</head>
<body>
    <div class="contenedor">
        <h2>PARCELAS EXISTENTES</h2>
        
        <!-- Mostrar las parcelas si existen -->
        <c:if test="${not empty parcelas}">
            <div style="overflow-x:auto;">
                <table>
                    <tr><th>ID Parcela</th><th>Numero Catastro</th><th>Numero Parcela</th></tr>
                    <!-- Iterar sobre las parcelas y mostrarlas -->
                    <c:forEach var="parcela" items="${parcelas}">
                        <tr>
                            <td>${parcela.idParcela}</td>
                            <td>${parcela.idCatastro}</td>
                            <td>${parcela.numeroParcela}</td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </c:if>

        <!-- Formulario para modificar parcela -->
        <form action="Controlador" method="POST">
            <label for="id_parcelas">ID Parcela:</label>
            <input type="text" name="id_parcelas" id="id_parcelas" required><br><br>

            <label for="id_catastros">Número Catastro:</label>
            <input type="text" name="id_catastros" id="id_catastros" required><br><br>

            <label for="numero_parcelas">Número Parcela:</label>
            <input type="text" name="numero_parcelas" id="numero_parcelas" required><br><br>

            <label for="latitudes">Latitud:</label>
            <input type="text" name="latitudes" id="latitudes" required><br><br>

            <label for="longitudes">Longitud:</label>
            <input type="text" name="longitudes" id="longitudes" required><br><br>

            <input type="submit" name="modificar" value="Modificar"><br><br>
        </form>

        <!-- Formulario para volver -->
        <form action="Controlador" method="POST">
            <input type="submit" name="volver" value="Volver">
        </form>
    </div>
</body>
</html>
