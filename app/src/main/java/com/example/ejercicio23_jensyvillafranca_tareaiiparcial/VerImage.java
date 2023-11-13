package com.example.ejercicio23_jensyvillafranca_tareaiiparcial;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ejercicio23_jensyvillafranca_tareaiiparcial.Modelo.Photograh;
import com.example.ejercicio23_jensyvillafranca_tareaiiparcial.transacciones.connection;
import com.example.ejercicio23_jensyvillafranca_tareaiiparcial.transacciones.transacciones_bd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VerImage extends AppCompatActivity {
    connection conexion;


    public Bitmap obtenerImagen() {
        conexion = new connection(this, transacciones_bd.nombre_bd, null, 1);
        SQLiteDatabase db = conexion.getReadableDatabase();
        Bitmap bitmap = null;
        byte[] imagen = null;

        try {
            Cursor cursor = db.rawQuery(transacciones_bd.SelectTableFotos, null);
            if (cursor.moveToNext()) {
                imagen = cursor.getBlob(0);
                int size = imagen.length;
                Log.e("tamaño", "" + size);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        if (imagen != null) {
            try {
                File imageFile = convertByteArrToFile(imagen);
                bitmap = reducirCalidadImagen(imageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    private File convertByteArrToFile(byte[] fileBytes) throws IOException {
        File tempFile = File.createTempFile("tempImage", null, getCacheDir());
        tempFile.deleteOnExit();

        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(fileBytes);
        fos.close();

        return tempFile;
    }

    private Bitmap reducirCalidadImagen(File imageFile) {
        int quality = 90;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }
}
