package com.example.roadsideassistance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MechanicDocumentActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST = 1 ;
    private static final int REQUEST_GALLERY = 200;
    ImageView ivImage;
    TextView filename;
    Button uploaduImage;
    String file_path = null;
    CardView uplloadcard;
    Button uploadbtn;
    FloatingActionButton btnfab;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_document);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Document");


        FloatingActionButton btnfab = findViewById(R.id.mfabbtn);
        btnfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Relace with your own action", Toast.LENGTH_SHORT).show();

                //   SelectImage();

                UploadDocumentMechanic();

            }
        });

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void UploadDocumentMechanic() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mview = getLayoutInflater().inflate(R.layout.content_add_image,null);

        Spinner mspinner = mview.findViewById(R.id.spinnerdoctype);
        String[] value = {"Select document type","Pan Card","Adhar Card"," Driving Licence","Registration Certificate(RC)"};
        ArrayList<String> arrayList = new  ArrayList<>(Arrays.asList(value));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.style_spinner,arrayList);
        mspinner.setAdapter(arrayAdapter);

        mBuilder.setView(mview);
        dialog = mBuilder.create();
        dialog.show();

        uploaduImage = mview.findViewById(R.id.upload_Img);
        uploaduImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>=23){
                    if(checkPermission()){
                        filepicker();
                    }else {
                        requestPermission();
                    }
                }else{
                    filepicker();
                }
                // uploaduImage.setVisibility(View.INVISIBLE);
            }
        });
        filename = mview.findViewById(R.id.filename);

    }

    private void filepicker() {
        Toast.makeText(this, "File Picker Call", Toast.LENGTH_SHORT).show();

        Intent openGallery = new Intent(Intent.ACTION_PICK);
        openGallery.setType("image/*");
        startActivityForResult(openGallery,REQUEST_GALLERY);



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK){
            String filePath = getRealPathFromUri(data.getData(),MechanicDocumentActivity.this);
            Log.d("File path :"," "+filePath);

            this.file_path = filePath;
            File file = new File(filePath);
            filename.setText(file.getName());
        }

    }

    public String getRealPathFromUri(Uri uri,Activity activity){
        Cursor cursor = activity.getContentResolver().query(uri,null,null,null,null);
        if (cursor==null){
            return uri.getPath();
        }else {
            cursor.moveToFirst();
            int id = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(id);
        }
    }


    private void requestPermission(){

        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(this, "please Give Permission to Upload File", Toast.LENGTH_SHORT).show();
        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        }
    }



    private boolean checkPermission(){
        int result =  ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Successful", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Permission Failed", Toast.LENGTH_SHORT).show();
                }
        }
    }


}