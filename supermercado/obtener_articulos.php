<?php

$hostname = "localhost";
$database = "supermercado";
$username = "root";
$password = "";

$json = array();

// Conexión
$conexion = mysqli_connect($hostname, $username, $password, $database);

if ($conexion) {
    $consulta = "SELECT * FROM articulos";
    $resultado = mysqli_query($conexion, $consulta);

    while ($registro = mysqli_fetch_array($resultado)) {
        $result["descripcion"] = $registro['descripcion'];
        $result["precio"] = $registro['precio'];
        
        $json[] = $result;
    }
    mysqli_close($conexion);
    echo json_encode($json);
} else {
    echo "Error de conexión";
}
?>