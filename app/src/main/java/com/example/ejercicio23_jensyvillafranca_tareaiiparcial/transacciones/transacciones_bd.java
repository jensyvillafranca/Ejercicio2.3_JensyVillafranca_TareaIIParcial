package com.example.ejercicio23_jensyvillafranca_tareaiiparcial.transacciones;

public class transacciones_bd {

    //nombre de la BD
    public static final String nombre_bd = "bd_fotos";

    //Tablas de la BD
    public static final String tabla = "fotos";

    //Campos de la tabla
    public static final String id = "id";
    public static final String imagen = "imagen";
    public static final String descripcion = "descripcion";

    //Consultas de base de datos DDL
    //Creando la tabla con sus campos
    public static final String CreateTablePhotos = "CREATE TABLE fotos "+"(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "imagen BLOB, " +
            "descripcion TEXT)";

    //Consultas de base de datos DML
    public static final String SelectTableFotos = "SELECT imagen, descripcion from " + transacciones_bd.tabla;
}
