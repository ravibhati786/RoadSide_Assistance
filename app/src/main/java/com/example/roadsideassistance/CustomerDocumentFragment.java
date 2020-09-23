package com.example.roadsideassistance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    Bitmap bitmap;
    List<Document> documentList;
    TextView txtMessage;
    AdapterCustomerDocumentListView adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_customer_document, container, false);


      //  ivImage = v.findViewById(R.id.ivImage);
         uplloadcard = v.findViewById(R.id.upload_card);
         uploadbtn = v.findViewById(R.id.uploadbtn);
        documentList = new ArrayList<>();
        txtMessage = v.findViewById(R.id.txtMessage);

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

        fillListView();






        return v;
    }

    private void fillListView() {

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_FETCH_DOCUMENTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray documentArray = obj.getJSONArray("Data");

                            if(documentArray.length()>0)
                            {
                                txtMessage.setVisibility(View.GONE);

                            }

                            //now looping through all the elements of the json array
                            for (int i = 0; i < documentArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject documentObject = documentArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                Document document = new Document(documentObject.getString("DocumentNumber"), documentObject.getString("DocumentType"), documentObject.getString("DocumentFile"));

                                //adding the hero to herolist
                                documentList.add(document);
                            }

                            //creating custom adapter object
                            adapter = new AdapterCustomerDocumentListView(documentList, getContext());

                            //adding the adapter to listview
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Id", String.valueOf(new SharedPrefManager(getContext()).getLoggedUserId()));
                return params;
            }
        };

        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);


    }

    private void UploadDocumentCustomer() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        final View mview = getLayoutInflater().inflate(R.layout.content_add_image,null);
        selectedItem = mview.findViewById(R.id.selectedItem);

        documentName = mview.findViewById(R.id.edittext_docnumber);
        mspinner = mview.findViewById(R.id.spinnerdoctype);
        String[] value = {"Select document type","Pan Card","Aadhaar Card"," Driving Licence","Registration Certificate(RC)"};
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
                     selectedItem.setText(String.valueOf(i));
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
                final LoadingDialog loadingDialog = new LoadingDialog(getActivity());
                final String dNumber = documentName.getText().toString();
                loadingDialog.startLoadingDialog();
                Toast.makeText(getActivity(), "This is run block", Toast.LENGTH_SHORT).show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,Constants.URL_UPLOAD_DOCUMENTS,
                                new Response.Listener<NetworkResponse>() {
                                    @Override
                                    public void onResponse(NetworkResponse response) {
                                        try {
                                            String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                                            JSONObject obj = new JSONObject(res);
                                            Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                            adapter.notifyDataSetChanged();
                                            adapter.clear();
                                            documentList.clear();
                                            fillListView();
                                            dialog.dismiss();
                                            loadingDialog.dismissDialog();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getActivity(), "This is Error Block", Toast.LENGTH_SHORT).show();
                                    }
                                }) {

                            /* If you want to add more parameters with the image
                             * you can do it here
                             * here we have only one parameter with the image
                             * which is tags
                             */
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("DocumentNumber", dNumber);
                                params.put("DocumentType",selectedItem.getText().toString());
                                params.put("UserId",String.valueOf(new SharedPrefManager(getContext()).getLoggedUserId()));


                                return params;
                            }

                            /* Here we are passing image by renaming it with a unique name
                             */
                            @Override
                            protected Map<String, DataPart> getByteData() {
                                Map<String, DataPart> params = new HashMap<>();
                                params.put("DocumentFile", new DataPart(".png", getFileDataFromDrawable(bitmap)));
                                return params;
                            }
                        };
                        //adding the request to volley
                        Volley.newRequestQueue(getContext()).add(volleyMultipartRequest);

                    }
                }).start();

            }
        });


        mBuilder.setView(mview);
         dialog = mBuilder.create();
         dialog.show();

         ivImage = mview.findViewById(R.id.selectImage);
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

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
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
          /*String filePath = getRealPathFromUri(data.getData(),getActivity());
          Log.d("File path :"," "+filePath);

          this.file_path = filePath;
           file = new File(filePath);
          filename.setText(file.getName());
            */
          Uri imageUri = data.getData();
          try {
              //getting bitmap object from uri

              //InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
              //bitmap = BitmapFactory.decodeStream(inputStream);

              bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageUri);

              //displaying selected image to imageview
              ivImage.setImageBitmap(bitmap);

              //calling the method uploadBitmap to upload image
              //uploadBitmap(bitmap);
          } catch (Exception e) {
              e.printStackTrace();
          }
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