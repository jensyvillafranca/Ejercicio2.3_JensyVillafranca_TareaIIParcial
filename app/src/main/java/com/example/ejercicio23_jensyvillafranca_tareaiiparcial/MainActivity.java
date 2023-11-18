package com.example.ejercicio23_jensyvillafranca_tareaiiparcial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ejercicio23_jensyvillafranca_tareaiiparcial.transacciones.connection;
import com.example.ejercicio23_jensyvillafranca_tareaiiparcial.transacciones.transacciones_bd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    /*Declaracion de las variables*/
    Button guardarFotos, verFotosGuardadas, tomarFoto;
    TextView descripcion;

    static final int request_image = 101;
    static final int access_camera = 201;
    String currentPhotoPath;
    ImageView imageView;
    connection conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            /*Casteando los valores y colocando los eventos para botones*/
            guardarFotos = (Button) findViewById(R.id.btnGuardar);
            tomarFoto = (Button) findViewById(R.id.btnTomarFoto);
            verFotosGuardadas = (Button) findViewById(R.id.btnVerFotos);
            descripcion = (TextView) findViewById(R.id.txtDescripcion);
            imageView = (ImageView) findViewById(R.id.photo);

            tomarFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Metódo de permisos de la camara*/
                    permisosCamara();
                }
            });

            guardarFotos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(validar() == true){
                        savePhotos();
                    }else{
                        mensajesVacios();
                    }
                }
            });

        verFotosGuardadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lista_fotos = new Intent(getApplicationContext(), lista_fotos.class);
                startActivity(lista_fotos);
            }
        });
    }
    private void permisosCamara() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            //Request a la API del sistema operativo, esta es la peticion del permiso a la API del sistema operativo
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},access_camera);
        }
        else{
            //si ya tenemos el permiso y esta otorgado, entonces vamos a tener la fotografia.
            //TomarFoto();
            dispatchTakePictureIntent();
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == access_camera)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                dispatchTakePictureIntent();
                //TomarFoto();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "se necesita el permiso de la camara",Toast.LENGTH_LONG).show();
            }
        }
    }
    private File createImageFile() throws IOException {

        // Create an image file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";

        //Environment es para obtener variables de entorno del sistema//con esto podemos acceder a los directorios del celular
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES); //obtener directorio de las imagenes

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        //Log.d("El path",currentPhotoPath);
        //currentPhotoPath permite obtener la url donde está ubicada nuestra imagen
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //para tomar la foto

        // Ensure that there's a camera activity to handle the intent

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(); //cargamos la imagen directamente de de la url
            } catch (IOException ex) {

                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                //Obtener URL de nuestra imagen
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.ejercicio23_jensyvillafranca_tareaiiparcial.fileprovider", photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, request_image);
            }
        }
    }
    //Capturar lo que viene desde el ActivityForResult.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Obtener toda la informacion de la data, pueden ser imagenes, texto, vide etc.
        //viene como respuesta al callback de la respuesta de la API del sistema operativo.
        if(requestCode == request_image){
            /*Obtener la información que viene de la data
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");
            imageView.setImageBitmap(image);*/
            try {
                File foto = new File(currentPhotoPath); //trae toda la url
                //mandar el objeto
                imageView.setImageURI(Uri.fromFile(foto));
            }
            catch (Exception ex){
                ex.toString();
            }
        }
    }
    public boolean validar(){
        boolean retorna = true;

        if(descripcion.getText().toString().isEmpty()){
            retorna = false;
        }
        if( imageView.getDrawable() == null){
            retorna = false;
        }
        //validar si la foto esta vacia
        return retorna;
    }

    private void savePhotos()
    {
        try {
            conexion = new connection(this, transacciones_bd.nombre_bd, null, 1);
            SQLiteDatabase db =  conexion.getWritableDatabase();
            byte[] photoData = obtenerFoto();

            ContentValues valores = new ContentValues();
            valores.put(transacciones_bd.descripcion, descripcion.getText().toString());
            valores.put(transacciones_bd.imagen, photoData);

            Long Result = db.insert(transacciones_bd.tabla, transacciones_bd.id, valores);
            message();
            //Utilizando AlertDialog.Builder
            db.close();
        }
        catch (Exception exception)
        {
            //Utilizando AlertDialog.Builder
            Log.d("El error",""+exception);
            error();
        }

    }

    private void message() {
        //Creando el AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Configurar el AlertDialog.Builder
        builder.setTitle("Registro exitoso");
        builder.setMessage("La fotografía ha sido creada correctamente");

        //Botón para cerrar el cuadro de dialogo
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Cerrar el cuadro de diálogo
                dialog.dismiss();
            }
        });

        // Mostrar el cuadro de diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void error() {
        //Creando el AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Configurar el AlertDialog.Builder
        builder.setTitle("Error al registrar");
        builder.setMessage("La fotografía no se ha podido guardar.");

        //Botón para cerrar el cuadro de dialogo
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Cerrar el cuadro de diálogo
                dialog.dismiss();
            }
        });
        // Mostrar el cuadro de diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private byte[] obtenerFoto() {
        try {
            File photoFile = new File(currentPhotoPath);
            FileInputStream file = new FileInputStream(photoFile);
            byte[] photoData = new byte[(int) photoFile.length()];
            file.read(photoData);
            file.close();
            return photoData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void mensajesVacios() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(descripcion.getText().toString().isEmpty()){
            builder.setTitle("Advertencia");
            builder.setMessage("Escriba su descripción por favor");
        }
        if(imageView.getDrawable() == null){
            builder.setTitle("Advertencia");
            builder.setMessage("Ingrese su imagen por favor");
        }
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Cerrar el cuadro de diálogo
                dialog.dismiss();
            }
        });
        // Mostrar el cuadro de diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}