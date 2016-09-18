package com.spartan.xxx.walayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    CircleImageView fotoProfil;
    FloatingActionButton camera;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALERI = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fotoProfil = (CircleImageView) findViewById(R.id.fotoProfil);

    }

    public void dialogCall(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle(R.string.title_dialog);
        builder.setMessage(R.string.text_dialog);
        builder.setPositiveButton("Kamera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dispatchTakePictureIntent();
            }
        }).setNegativeButton("Galeri", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fotoDariGaleri();
            }
        }).setNeutralButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void fotoDariGaleri(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            fotoProfil.setImageBitmap(imageBitmap);
            fotoProfil.setMinimumWidth(275);
            fotoProfil.setMinimumHeight(275);

            View view =  findViewById(R.id.root_view);
            Snackbar snackbar = Snackbar.make(view, R.string.foto_changed, Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), R.string.fitur_undo, Toast.LENGTH_SHORT).show();
                        }
                    });
            snackbar.show();
        }else if(requestCode == REQUEST_IMAGE_GALERI && resultCode == RESULT_OK){
            Bitmap bitmap = null;
            if (data != null){
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(
                            getApplicationContext().getContentResolver(),
                            data.getData());
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if(fotoProfil != null){
                fotoProfil.setImageBitmap(bitmap);
                View view =  findViewById(R.id.root_view);
                Snackbar snackbar = Snackbar.make(view, R.string.foto_changed, Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.RED)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), R.string.fitur_undo, Toast.LENGTH_SHORT).show();
                            }
                        });
                snackbar.show();
            }
        }
    }
}
