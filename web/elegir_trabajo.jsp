<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Trabajos</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f1f8e9;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: flex-start;
            height: 100vh;
        }
        .contenedor {
            margin-top: 50px;
            width: 80%;
            max-width: 1000px;
            background-color: #ffffff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            box-sizing: border-box;
        }
        h2 {
            color: #388e3c;
            text-align: center;
        }
        label {
            font-size: 16px;
            color: #388e3c;
            display: block;
            margin: 10px 0 5px;
        }
        select {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 5px;
            border: 1px solid #ccc;
            font-size: 14px;
        }
        button, input[type="submit"] {
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
        button:hover, input[type="submit"]:hover {
            background-color: #2c6e29;
        }
        table {
            width: 100%;
            margin-top: 20px;
            border-collapse: collapse;
        }
        th, td {
            padding: 10px;
            text-align: center;
            border: 1px solid #ddd;
        }
        th {
            background-color: #388e3c;
            color: white;
        }
    </style>
</head>
<body>
    <div class="contenedor">
        <h2>Elegir Trabajo</h2>
        <form method="POST" action="ElegirTrabajoServlet">
            <label for="id_trabajo">Seleccione un trabajo:</label>
            <select name="id_trabajo" id="id_trabajo" required>
                <option value="">Seleccione un trabajo</option>
                <%@ page import="java.util.List, es.agriculturaJ2EE.modelo.Trabajo" %>
                <% List<Trabajo> trabajos = (List<Trabajo>) request.getAttribute("trabajosDisponibles");
                   if (trabajos != null) {
                       for (Trabajo trabajo : trabajos) { %>
                           <option value="<%= trabajo.getId() %>">
                               Trabajo #<%= trabajo.getId() %> - Parcela #<%= trabajo.getParcelaId() %> - <%= trabajo.getMaquinaId() %> - <%= trabajo.getDescripcion() %>
                           </option>
                       <% }
                   } %>
            </select>
            <br><br>
            <button type="submit">Asignarme este trabajo</button>
        </form>
        <h2>Trabajos Asignados</h2>
        <table>
            <tr>
                <th>ID Trabajo</th>
                <th>Parcela</th>
                <th>Máquina</th>
                <th>Tipo de Trabajo</th>
                <th>Estado</th>
                <th>Fecha Inicio</th>
                <th>Fecha Fin</th>
                <th>Acción</th>
            </tr>
            <% List<Trabajo> trabajosAsignados = (List<Trabajo>) request.getAttribute("trabajosAsignados");
               if (trabajosAsignados != null) {
                   for (Trabajo trabajo : trabajosAsignados) { %>
                       <tr>
                           <td><%= trabajo.getId() %></td>
                           <td><%= trabajo.getParcelaId() %></td>
                           <td><%= trabajo.getMaquinaId() %></td>
                           <td><%= trabajo.getDescripcion() %></td>
                           <td><%= trabajo.getEstado() %></td>
                           <td><%= trabajo.getFechaInicio() %></td>
                           <td><%= trabajo.getFechaFin() %></td>
                           <td>
                               <form method="POST" action="CompletarTrabajoServlet">
                                   <input type="hidden" name="id_trabajo" value="<%= trabajo.getId() %>">
                                   <button type="submit">Marcar como completado</button>
                               </form>
                           </td>
                       </tr>
                   <% }
               } %>
        </table>
        <br>
        <form action="menu.jsp" method="POST">
            <input type="submit" value="Volver">
        </form>
    </div>
</body>
</html>