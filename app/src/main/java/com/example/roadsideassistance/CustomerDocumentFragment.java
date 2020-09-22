package com.example.roadsideassistance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;


public class CustomerDocumentFragment extends Fragment {

    private static final int PERMISSIOM_REQUEST =1 ;
    private static final int REQUEST_GALLERY = 200;
    ImageView ivImage;
    EditText documentName;
    TextView filename, selectedItem;
    Button uploaduImage;
    String file_path = null;
    Integer REQUEST_CAMERA = 1,SELECT_FILE = 0;
    CardView uplloadcard;
    Button uploadbtn,savedocumentbtn;
    FloatingActionButton btnfab;
    AlertDialog dialog;
    ListView listView;
    File file;
    Spinner  mspinner;
    String[] DocumentName = {"Pan card"};
    String[] DocumentNumber = {"TY123T45"};
    Integer[] DocumentImage  = {R.drawable.profileimg};



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_customer_document, container, false);


      //  ivImage = v.findViewById(R.id.ivImage);
         uplloadcard = v.findViewById(R.id.upload_card);
         uploadbtn = v.findViewById(R.id.uploadbtn);


        FloatingActionButton btnfab = v.findViewById(R.id.fabbtn);
        btnfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Relace with your own action", Toast.LENGTH_SHORT).show();

             //   SelectImage();

                UploadDocumentCustomer();

            }
        });

        listView = v.findViewById(R.id.custvehiclelistview);

        AdapterCustomerDocumentListView adapter = new AdapterCustomerDocumentListView(getActivity(),DocumentName,DocumentNumber,DocumentImage);
        listView.setAdapter(adapter);





        return v;
    }

    private void UploadDocumentCustomer() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View mview = getLayoutInflater().inflate(R.layout.content_add_image,null);
        selectedItem = mview.findViewById(R.id.selectedItem);

        documentName = mview.findViewById(R.id.edittext_docnumber);
        mspinner = mview.findViewById(R.id.spinnerdoctype);
        String[] value = {"Select document type","Pan Card","Adhar Card"," Driving Licence","Registration Certificate(RC)"};
        ArrayList<String> arrayList = new  ArrayList<>(Arrays.asList(value));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),R.layout.style_spinner,arrayList);
        mspinner.setAdapter(arrayAdapter);

        mspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(adapterView.getItemAtPosition(i).equals("Select document"))
                {
                    Toast.makeText(getContext(), "Please select document", Toast.LENGTH_SHORT).show();
                }
                else
                {
                     selectedItem.setText(adapterView.getSelectedItem().toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            ///
            }
        });

        mview.findViewById(R.id.savedocumentbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();




            }
        });


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
        Toast.makeText(getContext(), "File Picker Call", Toast.LENGTH_SHORT).show();

        Intent openGallery = new Intent(Intent.ACTION_PICK);
        openGallery.setType("image/*");
        startActivityForResult(openGallery,REQUEST_GALLERY);



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      if(requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK){
          String filePath = getRealPathFromUri(data.getData(),getActivity());
          Log.d("File path :"," "+filePath);

          this.file_path = filePath;
           file = new File(filePath);
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
    /*private void SelectImage(){

        final CharSequence[] items = {"Gallery", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                 if(items[i].equals("Gallery")){

                    Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent,"Select File"),SELECT_FILE);

                }else if(items[i].equals("Cancel"))
                {
                    dialogInterface.dismiss();
                }
                uplloadcard.setVisibility(View.VISIBLE);



            }
        });
        builder.show();
    }*/

   /* public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){

            if(requestCode == REQUEST_CAMERA){

                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                ivImage.setImageBitmap(bmp);

            }else if(requestCode==SELECT_FILE){

                Uri selectImageUri = data.getData();
                ivImage.setImageURI(selectImageUri);
            }
        }
    }*/

   private void requestPermission(){

       if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
           Toast.makeText(getContext(), "please Give Permission to Upload File", Toast.LENGTH_SHORT).show();
       }else {
           ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIOM_REQUEST);
       }
   }



   private boolean checkPermission(){
       int result =  ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE);
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
            case PERMISSIOM_REQUEST:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getContext(), "Permission Successful", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "Permission Failed", Toast.LENGTH_SHORT).show();
                }
        }
    }
}