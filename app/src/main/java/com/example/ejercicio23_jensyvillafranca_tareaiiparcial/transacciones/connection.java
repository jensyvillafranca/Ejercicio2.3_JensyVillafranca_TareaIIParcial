package com.example.ejercicio23_jensyvillafranca_tareaiiparcial.transacciones;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/*Esta clase es para la creación y la actualización de la base de datos.*/
public class connection extends SQLiteOpenHelper
{
    public connection(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //este es para crear los objetos de base de datos
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) { //se llama cuando la BD se crea por primera vez
        sqLiteDatabase.execSQL(transacciones_bd.CreateTablePhotos); //Creando la tabla como tal.
    }

    //este es para actualizarlo o destruir los objetos
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
