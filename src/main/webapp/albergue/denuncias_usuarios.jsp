<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.webapp_petlink.beans.DenunciaMaltratoAnimal" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
  <title>PetLink - Lista de Denuncias</title>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no"/>
  <link rel="stylesheet" href="assets/css/main.css"/>
  <link rel="stylesheet" href="assets/css/aditional.css">
  <link rel="stylesheet" href="assets/css/popup-window.css">
</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">
  <!-- Main Content -->
  <div id="main">
    <div class="inner">
      <!-- Header -->
      <header id="header">
        <h1><strong>Denuncias por Maltrato Animal</strong></h1>
      </header>

      <!-- Botón para registrar nueva denuncia -->
      <section>
        <a href="DenunciaServlet?action=create" class="button primary big">Registrar denuncia</a>
      </section>

      <!-- Tabla de Denuncias -->
      <section>
        <table>
          <thead>
          <tr>
            <th>ID Denuncia</th>
            <th>Descripción</th>
            <th>Estado</th>
            <th>Acciones</th>
          </tr>
          </thead>
          <tbody>
          <%
            List<DenunciaMaltratoAnimal> denuncias = (List<DenunciaMaltratoAnimal>) request.getAttribute("denuncias");
            if (denuncias != null) {
              for (DenunciaMaltratoAnimal denuncia : denuncias) {
          %>
          <tr>
            <td><%= denuncia.getIdDenunciaMaltratoAnimal() %></td>
            <td><%= denuncia.getDescripcionMaltrato() %></td>
            <td><%= denuncia.getEstado().getNombre_estado() %></td>
            <td>
              <a href="DenunciaServlet?action=update&id=<%= denuncia.getIdDenunciaMaltratoAnimal() %>" class="button small">Modificar</a>
              <a href="DenunciaServlet?action=delete&id=<%= denuncia.getIdDenunciaMaltratoAnimal() %>" class="button small">Eliminar</a>
            </td>
          </tr>
          <%
            }
          } else {
          %>
          <tr>
            <td colspan="4">No hay denuncias disponibles.</td>
          </tr>
          <%
            }
          %>
          </tbody>
        </table>
      </section>
    </div>
  </div>
</div>

<!-- Scripts -->
<script src="assets/js/jquery.min.js"></script>
<script src="assets/js/main.js"></script>

</body>
</html>
