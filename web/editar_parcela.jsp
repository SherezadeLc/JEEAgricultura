<%@ page import="java.util.List" %>
<%@ page import="es.agriculturaJ2EE.modelo.Parcela" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Parcelas</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }

        .contenedor {
            width: 80%;
            margin: 20px auto;
            background-color: #fff;
            padding: 20px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        h2 {
            text-align: center;
            color: #333;
        }

        table {
            width: 100%;
            margin: 20px 0;
            border-collapse: collapse;
        }

        table, th, td {
            border: 1px solid #ddd;
        }

        th, td {
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: #f2f2f2;
        }

        .boton-volver {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
        }

        .boton-volver input {
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            cursor: pointer;
            border-radius: 5px;
        }

        .boton-volver input:hover {
            background-color: #0056b3;
        }

        form {
            display: inline-block;
        }

        form input[type="submit"] {
            padding: 8px 16px;
            margin: 5px;
            background-color: #ff4d4d;
            color: white;
            border: none;
            cursor: pointer;
            border-radius: 5px;
        }

        form input[type="submit"]:hover {
            background-color: #e60000;
        }

        .c-message {
            font-size: 14px;
            color: green;
            text-align: center;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="contenedor">
        <h2>Editar Parcelas</h2>
        <c:if test="${not empty message}">
            <p class="c-message">${message}</p>
        </c:if>

        <table>
            <tr>
                <th>ID Parcela</th>
                <th>ID Catastro</th>
                <th>Número Parcela</th>
                <th>Acciones</th>
            </tr>

            <c:forEach var="parcela" items="${parcelas}">
                <tr>
                    <td>${parcela.idParcela}</td>
                    <td>${parcela.idCatastro}</td>
                    <td>${parcela.numeroParcela}</td>
                    <td>
                        <form action="Controlador" method="GET" style="display:inline-block;">
                            <input type="hidden" name="id_parcela" value="${parcela.idParcela}">
                            <input type="submit" value="Editar">
                        </form>
                        <form action="editarParcela" method="POST" onsubmit="return confirm('¿Estás seguro de eliminar esta parcela?')" style="display:inline-block;">
                            <input type="hidden" name="id_parcela" value="${parcela.idParcela}">
                            <input type="submit" name="eliminar" value="Eliminar">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <div class="boton-volver">
            <form action="añadir_parcelas.jsp" method="POST">
                <input type="submit" value="Añadir Parcela">
            </form>
            <form action="menu.jsp" method="POST">
                <input type="submit" value="Volver">
            </form>
        </div>
    </div>
</body>
</html>

