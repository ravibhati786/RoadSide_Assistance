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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MechanicDocumentActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST = 1;
    private static final int REQUEST_GALLERY = 200;
    ImageView ivImage;
    Button uploaduImage;
    String file_path = null;
    CardView uplloadcard;
    Button uploadbtn;
    FloatingActionButton btnfab;
    AlertDialog dialog;
    TextView filename, selectedItem;
    EditText documentName;
    Bitmap bitmap;
    List<Document> documentList;
    TextView txtMessage;
    AdapterCustomerDocumentListView adapter;
    ListView listView;
    Spinner  mspinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_document);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = findViewById(R.id.Mechaniclistview);
        txtMessage = findViewById(R.id.txtMessage);
        documentList = new ArrayList<>();

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

        fillListView();

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void UploadDocumentMechanic() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mview = getLayoutInflater().inflate(R.layout.content_add_image, null);
        selectedItem = mview.findViewById(R.id.selectedItem);


        documentName = mview.findViewById(R.id.edittext_docnumber);


        mspinner = mview.findViewById(R.id.spinnerdoctype);
        String[] value = {"Select document type","Pan Card","Aadhaar Card"," Driving Licence","Registration Certificate(RC)"};
        ArrayList<String> arrayList = new  ArrayList<>(Arrays.asList(value));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.style_spinner,arrayList);
        mspinner.setAdapter(arrayAdapter);

        mspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(adapterView.getItemAtPosition(i).equals("Select document"))
                {
                    Toast.makeText(getApplicationContext(), "Please select document", Toast.LENGTH_SHORT).show();
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


        mBuilder.setView(mview);
        dialog = mBuilder.create();
        dialog.show();

        ivImage = mview.findViewById(R.id.selectImage);
        uploaduImage = mview.findViewById(R.id.upload_Img);
        uploaduImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if(checkPermission())
                    {
                        filepicker();
                    }
                    else{
                        requestPermission();
                    }
                } else {
                    filepicker();
                }
                // uploaduImage.setVisibility(View.INVISIBLE);
            }
        });


        mview.findViewById(R.id.savedocumentbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT).show();
                final LoadingDialog loadingDialog = new LoadingDialog(MechanicDocumentActivity.this);
                final String dNumber = documentName.getText().toString();
                loadingDialog.startLoadingDialog();
                Toast.makeText(getApplicationContext(), "This is run block", Toast.LENGTH_SHORT).show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constants.URL_UPLOAD_DOCUMENTS,
                                new Response.Listener<NetworkResponse>() {
                                    @Override
                                    public void onResponse(NetworkResponse response) {
                                        try {
                                            String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                                            Log.i("StringValue",res);
                                            JSONObject obj = new JSONObject(res);
                                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                                        Log.i("Error",error.toString());
                                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getApplicationContext(), "This is Error Block", Toast.LENGTH_SHORT).show();
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
                                params.put("DocumentType", selectedItem.getText().toString());
                                params.put("AssistantId", String.valueOf(new SharedPrefManager(getApplicationContext()).getLoggedUserId()));
                                Log.i("AssitantID", String.valueOf(new SharedPrefManager(getApplicationContext()).getLoggedUserId()));


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
                        Volley.newRequestQueue(getApplicationContext()).add(volleyMultipartRequest);

                    }
                }).start();
            }
        });


    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    private void fillListView() {
        final LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_FETCH_DOCUMENTS_MECHANIC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            documentList.clear();
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray documentArray = obj.getJSONArray("Data");

                            if (documentArray.length() > 0) {
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
                            adapter = new AdapterCustomerDocumentListView(documentList, getApplicationContext());

                            //adding the adapter to listview
                            listView.setAdapter(adapter);

                            loadingDialog.dismissDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Id", String.valueOf(new SharedPrefManager(getApplicationContext()).getLoggedUserId()));
                Log.i("MechanicId",String.valueOf(new SharedPrefManager(getApplicationContext()).getLoggedUserId()));
                return params;
            }
        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    private void filepicker() {
        Toast.makeText(this, "File Picker Call", Toast.LENGTH_SHORT).show();

        Intent openGallery = new Intent(Intent.ACTION_PICK);
        openGallery.setType("image/*");
        startActivityForResult(openGallery, REQUEST_GALLERY);



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
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

                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageUri);

                //displaying selected image to imageview
                ivImage.setImageBitmap(bitmap);

                //calling the method uploadBitmap to upload image
                //uploadBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


        private void requestPermission(){

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "please Give Permission to Upload File", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
            }
        }


        private boolean checkPermission(){
            int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }

        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch (requestCode) {
                case PERMISSION_REQUEST:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Permission Failed", Toast.LENGTH_SHORT).show();
                    }
            }
        }
        }