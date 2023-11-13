package com.example.ejercicio23_jensyvillafranca_tareaiiparcial;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ejercicio23_jensyvillafranca_tareaiiparcial.Modelo.Photograh;
import com.example.ejercicio23_jensyvillafranca_tareaiiparcial.transacciones.connection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class LisAdapter extends ArrayAdapter<Photograh> {

    private List<Photograh> mList;

    /*De donde vamos a asignar los datos*/
    private Context mContext;

    /*Conexión con la base de datos*/
    connection conexion;
    private int resourceLayout;
    public LisAdapter(@NonNull Context context, int resource, List<Photograh> objects) {
        super(context, resource, objects);
        this.mList = objects;
        this.mContext = context;
        this.resourceLayout = resource;
    }

    /*Buscar las vistas que vamos a poner en cada elemento de la lista*/

    @NonNull
    @Override
    /*Vista inflada de cada elemento que viene en el listview*/
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        /*View*/
        View view = convertView;
        if(view == null){
            /*Que cree el row con el layout*/
            view = LayoutInflater.from(mContext).inflate(R.layout.item_row, null);

            Photograh modelo = mList.get(position);
            ImageView imagen = view.findViewById(R.id.imageView);
            VerImage verimagen = new VerImage();
            imagen.setImageBitmap(verimagen.obtenerImagen());
            //imagen.setImageResource(modelo.getImagen());

            TextView descripcion = view.findViewById(R.id.textView);
            descripcion.setText(modelo.getDescripcion());
        }
        return view;
    }
}
