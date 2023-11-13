package com.example.ejercicio23_jensyvillafranca_tareaiiparcial;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ejercicio23_jensyvillafranca_tareaiiparcial.transacciones.connection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class VerImage{
    public File convertByteArrToFile(Context context, byte[] fileBytes) throws IOException {
        File tempFile = File.createTempFile("tempImage", null, context.getCacheDir());
        tempFile.deleteOnExit();

        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(fileBytes);
        fos.close();

        return tempFile;
    }
}
