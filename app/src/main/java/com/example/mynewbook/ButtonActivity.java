package com.example.mynewbook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

public class ButtonActivity extends AppCompatActivity {

    ImageView selectImageView;
    EditText bookNameText;
    EditText novelistText;
    EditText commentText;
    Button saveButton;
    SQLiteDatabase database;
    Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
        Intent intent = getIntent();
        selectImageView = findViewById(R.id.selectImageView);
        bookNameText = findViewById(R.id.bookNameEditText);
        novelistText = findViewById(R.id.novelistEditText);
        commentText = findViewById(R.id.commentEditText);
        saveButton = findViewById(R.id.saveButton);

    }

    public void save(View view) {

    }

    public void selectImage(View view) {

        //kullanıcı iznini kontrol etmek için

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);

        } else {
            Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(imageIntent, 1);
        }


    }

    //eğer requestCode 1 ise bu işlemleri yap diye kontrol etmek için
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //kontrolleri yapacağımız bir if durumu oluşturuyoruz. requestcode verdiğimiz değerde ise , sonuçta bır verimiz varsa ve data boş değilse şeklinde.
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            //seçilen dataya göre görsel oluşturmak
            Uri imageData = data.getData();
            //try and catch'e bitmapten dolayı kendi aldı
            try {
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    selectImageView.setImageBitmap(selectedImage);

                } else {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData);
                    selectImageView.setImageBitmap(selectedImage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    //kullanıcıdan görseli izin aldık,bunu her seferinde izin almamak için yeni bir kod oluşturuyoruz


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==2){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(imageIntent, 1);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
