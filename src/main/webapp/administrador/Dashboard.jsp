<%@ page import="com.example.webapp_petlink.beans.Usuario" %><%--
  Created by IntelliJ IDEA.
  User: aleJo
  Date: 16/12/2024
  Time: 09:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE HTML>
<%
  Usuario administrador = (Usuario) session.getAttribute("datosUsuario");

%>
<html>
<head>
  <title>PetLink - Dashboard Mejorado</title>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
  <link rel="stylesheet" href="<%=request.getContextPath()%>/administrador/assets/css/main.css" />
  <link rel="stylesheet" href="<%=request.getContextPath()%>/administrador/assets/css/aditional.css">
  <link rel="icon" href="<%=request.getContextPath()%>/administrador/images/favicon.png" type="image/x-icon">
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script> <!-- Librería para gráficos -->
  <style>
    /* Contenedor para los gráficos */
    .contenedor-graficos {
      display: grid;
      grid-template-columns: repeat(2, 1fr); /* 2 columnas para organizar los gráficos */
      gap: 20px;
      margin-top: 30px;
    }

    /* Ajustar el tamaño de los gráficos */
    .grafico-principal {
      background-color: #f9f9f9;
      padding: 20px;
      border-radius: 10px;
      box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);
      text-align: center;
    }

    .grafico-principal canvas {
      width: 100% !important;
      height: 250px !important; /* Ajusta la altura de los gráficos */
    }

    /* Estilos para las tarjetas */
    .circulares-container {
      display: flex;
      justify-content: space-evenly;
      margin-top: 30px;
    }

    .tarjeta-usuarios {
      background-color: #f0f0f0;
      border-radius: 10px;
      box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);
      padding: 20px;
      text-align: center;
      width: 30%;
    }

    .usuarios-count {
      font-size: 2em;
      font-weight: bold;
      margin: 10px 0;
    }

    .stat-update {
      color: #888;
      font-size: 0.9em;
    }

    /* Ajustes responsivos para la vista móvil */
    @media (max-width: 768px) {
      .circulares-container {
        flex-direction: column;
        align-items: center;
      }

      .tarjeta-usuarios {
        width: 80%;
        margin-bottom: 20px;
      }

      .contenedor-graficos {
        grid-template-columns: 1fr; /* En pantallas pequeñas, los gráficos se apilan en una columna */
      }
    }
  </style>
</head>
<body class="is-preload">

<!-- Wrapper -->
<div id="wrapper">

  <!-- Main -->
  <div id="main">
    <div class="inner">

      <!-- Header -->
      <header id="header">
        <img src="<%=request.getContextPath()%>/administrador/images/dashboard_final.png" class="icons">
        <h1 class="logo"><strong>Dashboard </strong></h1>

      </header>

      <!-- Contenedor de estadísticas (Usuarios y Albergues) -->
      <div class="circulares-container">
        <!-- Tarjeta de Usuarios Activos -->
        <div class="tarjeta-usuarios">
          <h3>Usuarios Activos <span class="emoji">🟢</span></h3>
          <p class="usuarios-count">3</p>
          <p class="stat-update"><span class="emoji">📈</span> +2.4% más que el mes anterior</p>
        </div>

        <!-- Tarjeta de Usuarios Baneados -->
        <div class="tarjeta-usuarios">
          <h3>Usuarios Baneados <span class="emoji">🔴</span></h3>
          <p class="usuarios-count">1</p>
          <p class="stat-update"><span class="emoji">📉</span> -1.2% respecto al mes anterior</p>
        </div>

        <!-- Tarjeta de Albergues Registrados -->
        <div class="tarjeta-usuarios">
          <h3>Albergues Registrados <span class="emoji">🏠</span></h3>
          <p class="usuarios-count">3</p>
          <p class="stat-update"><span class="emoji">📊</span> Datos actualizados</p>
        </div>
      </div>

      <!-- Contenedor de gráficos -->
      <div class="contenedor-graficos">
        <!-- Gráfico: Mascotas Perdidas -->
        <div class="grafico-principal">
          <h3>Mascotas Perdidas (Últimos 3 Meses y Totales por Año)</h3>
          <canvas id="graficoMascotasPerdidas"></canvas>
        </div>

        <!-- Gráfico: Mascotas Encontradas -->
        <div class="grafico-principal">
          <h3>Mascotas Encontradas (Últimos 3 Meses y Totales por Año)</h3>
          <canvas id="graficoMascotasEncontradas"></canvas>
        </div>

        <!-- Gráfico: Top 10 Albergues con Más Donaciones -->
        <div class="grafico-principal">
          <h3>Top 10 Albergues con Más Donaciones</h3>
          <canvas id="graficoTopAlbergues"></canvas>
        </div>

        <!-- Gráfico: Top 10 Donaciones por Usuario -->
        <div class="grafico-principal">
          <h3>Top 10 Donaciones por Usuario</h3>
          <canvas id="graficoTopUsuarios"></canvas>
        </div>
      </div>

    </div>
  </div>

  <!-- Sidebar -->
  <jsp:include page="navbar.jsp">
    <jsp:param name="idUsuario" value="<%= administrador.getId_usuario() %>" />
  </jsp:include>
</div>

<!-- Scripts -->
<script src="<%=request.getContextPath()%>/administrador/assets/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/browser.min.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/breakpoints.min.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/util.js"></script>
<script src="<%=request.getContextPath()%>/administrador/assets/js/main.js"></script>

<!-- Gráficos -->
<script>
  // Gráfico de Mascotas Perdidas por Mes y Totales por Año
  var ctx1 = document.getElementById('graficoMascotasPerdidas').getContext('2d');
  var myChart1 = new Chart(ctx1, {
    type: 'line',
    data: {
      labels: ['Enero', 'Febrero', 'Marzo'],
      datasets: [{
        label: 'Mascotas Perdidas (Últimos 3 Meses)',
        data: [1, 2, 3],
        borderColor: 'rgba(255, 99, 132, 1)',
        tension: 0.1
      }, {
        label: 'Mascotas Perdidas (Totales por Año)',
        data: [4, 4, 4],
        borderColor: 'rgba(54, 162, 235, 1)',
        tension: 0.1
      }]
    },
    options: {
      maintainAspectRatio: false // Ajusta el tamaño según el contenedor
    }
  });

  // Gráfico de Mascotas Encontradas por Mes y Totales por Año
  var ctx2 = document.getElementById('graficoMascotasEncontradas').getContext('2d');
  var myChart2 = new Chart(ctx2, {
    type: 'line',
    data: {
      labels: ['Enero', 'Febrero', 'Marzo'],
      datasets: [{
        label: 'Mascotas Encontradas (Últimos 3 Meses)',
        data: [1, 2, 3],
        borderColor: 'rgba(75, 192, 192, 1)',
        tension: 0.1
      }, {
        label: 'Mascotas Encontradas (Totales por Año)',
        data: [4, 4, 4],
        borderColor: 'rgba(153, 102, 255, 1)',
        tension: 0.1
      }]
    },
    options: {
      maintainAspectRatio: false // Ajusta el tamaño según el contenedor
    }
  });

  // Gráfico de Top 10 Albergues con Más Donaciones
  var ctx3 = document.getElementById('graficoTopAlbergues').getContext('2d');
  var myChart3 = new Chart(ctx3, {
    type: 'bar',
    data: {
      labels: [' Amigos Fieles ', ' Esperanza', ' Patitas felices', 'null ', 'null', 'null', 'null', 'null', 'null', 'null'],
      datasets: [{
        label: 'Donaciones (S/.)',
        data: [0, 18, 15, 12, 10, 8, 6, 5, 3, 2],
        backgroundColor: 'rgba(75, 192, 192, 0.2)',
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 1
      }]
    },
    options: {
      maintainAspectRatio: false, // Ajusta el tamaño según el contenedor
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });

  // Gráfico de Top 10 Donaciones por Usuario
  var ctx4 = document.getElementById('graficoTopUsuarios').getContext('2d');
  var myChart4 = new Chart(ctx4, {
    type: 'bar',
    data: {
      labels: ['Usuario 1', 'Usuario 2', 'Usuario 3', 'Usuario 4', 'Usuario 5', 'Usuario 6', 'Usuario 7', 'Usuario 8', 'Usuario 9', 'Usuario 10'],
      datasets: [{
        label: 'Donaciones Enviadas (S/.)',
        data: [10, 9, 8, 7, 6, 5, 4, 3, 2, 1],
        backgroundColor: [
          'rgba(255, 99, 132, 0.2)',
          'rgba(54, 162, 235, 0.2)',
          'rgba(255, 206, 86, 0.2)',
          'rgba(75, 192, 192, 0.2)',
          'rgba(153, 102, 255, 0.2)',
          'rgba(255, 159, 64, 0.2)',
          'rgba(199, 199, 199, 0.2)',
          'rgba(83, 102, 255, 0.2)',
          'rgba(60, 186, 159, 0.2)',
          'rgba(255, 94, 132, 0.2)'
        ],
        borderColor: [
          'rgba(255, 99, 132, 1)',
          'rgba(54, 162, 235, 1)',
          'rgba(255, 206, 86, 1)',
          'rgba(75, 192, 192, 1)',
          'rgba(153, 102, 255, 1)',
          'rgba(255, 159, 64, 1)',
          'rgba(199, 199, 199, 1)',
          'rgba(83, 102, 255, 1)',
          'rgba(60, 186, 159, 1)',
          'rgba(255, 94, 132, 1)'
        ],
        borderWidth: 1
      }]
    },
    options: {
      maintainAspectRatio: false, // Ajusta el tamaño según el contenedor
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });
</script>

</body>
</html>

