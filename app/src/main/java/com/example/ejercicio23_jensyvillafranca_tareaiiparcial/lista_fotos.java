package com.example.ejercicio23_jensyvillafranca_tareaiiparcial;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.ejercicio23_jensyvillafranca_tareaiiparcial.Modelo.Photograh;
import com.example.ejercicio23_jensyvillafranca_tareaiiparcial.transacciones.connection;
import com.example.ejercicio23_jensyvillafranca_tareaiiparcial.transacciones.transacciones_bd;

import java.util.ArrayList;
import java.util.List;

public class lista_fotos extends AppCompatActivity {
    private ListView miListView;
    private List<Photograh> miList = new ArrayList<>();
    ListAdapter myAdapter;
    private connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_fotos);
        miListView = findViewById(R.id.listViewFotos);

        connection = new connection(this, transacciones_bd.nombre_bd, null, 1);
        getDatos();
        // Obtén todos los modelos de la base de datos
        List<Photograh> modelos = getAllPhotos();

        // Usa un adaptador para mostrar los modelos en el ListView
        myAdapter = new LisAdapter(this, R.layout.item_row, modelos);
        miListView.setAdapter(myAdapter);
    }

    public List<Photograh> getAllPhotos() {
        List<Photograh> modeloList = new ArrayList<>();
        // Selecciona todos los query

        SQLiteDatabase db = connection.getReadableDatabase();
        Cursor cursor = db.rawQuery(transacciones_bd.SelectTableFotos, null);

        // Recorre todas las filas y añade a la lista
        if (cursor.moveToFirst()) {
            do {
                int columnIndexImagen = cursor.getColumnIndex(transacciones_bd.imagen);
                int columnIndexDescripcion = cursor.getColumnIndex(transacciones_bd.descripcion);

                Photograh modelo = new Photograh();
                modelo.setImagen(cursor.getBlob(columnIndexImagen));
                modelo.setDescripcion(cursor.getString(columnIndexDescripcion));
                // Agrega el modelo a la lista
                modeloList.add(modelo);
            } while (cursor.moveToNext());
        }

        // Cierra el cursor y la base de datos
        cursor.close();
        db.close();

        // Retorna la lista de modelos
        return modeloList;
    }
    private void getDatos() {
        SQLiteDatabase db = connection.getReadableDatabase();
        Photograh photograh = null;
        Cursor cursor = db.rawQuery(transacciones_bd.SelectTableFotos,null);
        while (cursor.moveToNext()){
            photograh = new Photograh();
            photograh.setDescripcion(cursor.getString(1));
            miList.add(photograh);
        }
        cursor.close();
    }
}