package com.dinesh.resolib;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Objects;
public class uploadfile extends AppCompatActivity
{
    ImageView imagebrowse,imageupload,filelogo,cancelfile;
    Uri filepath;

    EditText filetitle;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    static String ACTV_Value;
    static String ACTV_Value2;
    String[] programs = { "BTech","BDes","BBA","Masters"};
    String[] sems = {"Sem-1", "Sem-2","Sem-3","Sem-4","Sem-5"};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        filetitle=findViewById(R.id.filetitle);

        imagebrowse=findViewById(R.id.imagebrowse);
        imageupload=findViewById(R.id.imageupload);

        filelogo=findViewById(R.id.filelogo);
        cancelfile=findViewById(R.id.cancelfile);

        filelogo.setVisibility(View.INVISIBLE);
        cancelfile.setVisibility(View.INVISIBLE);

        cancelfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filelogo.setVisibility(View.INVISIBLE);
                cancelfile.setVisibility(View.INVISIBLE);
                imagebrowse.setVisibility(View.VISIBLE);
            }
        });
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,R.layout.dropdown_item,programs);
        AutoCompleteTextView autocompleteTV = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autocompleteTV.setThreshold(1);
        autocompleteTV.setAdapter(adapter);

        autocompleteTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ACTV_Value = (String) parent.getItemAtPosition(position);
                Toast.makeText(uploadfile.this, "Selected Item: " + ACTV_Value, Toast.LENGTH_SHORT).show();
                if(ACTV_Value.isEmpty()){
                    Toast.makeText(uploadfile.this,"Please select your program",Toast.LENGTH_SHORT).show();
                }else {
                    databaseReference = FirebaseDatabase.getInstance().getReference().child(ACTV_Value);
                }
            }
        });

        ArrayAdapter<String>adapter2=new ArrayAdapter<String>(this,R.layout.dropdown_item,sems);
        AutoCompleteTextView autocompleteTV2 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        autocompleteTV2.setThreshold(1);
        autocompleteTV2.setAdapter(adapter2);
        autocompleteTV2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ACTV_Value2 = (String) parent.getItemAtPosition(position);
                Toast.makeText(uploadfile.this, "Selected Item: " + ACTV_Value, Toast.LENGTH_SHORT).show();
//                if(ACTV_Value2.isEmpty()){
//                    Toast.makeText(uploadfile.this,"Please select your program",Toast.LENGTH_SHORT).show();
//                }else {
//                    databaseReference = FirebaseDatabase.getInstance().getReference().child(ACTV_Value);
//                }
            }
        });


        storageReference= FirebaseStorage.getInstance().getReference();
//        databaseReference= FirebaseDatabase.getInstance().getReference(ACTV_Value);


        imagebrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"Select Pdf Files"),101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                            }
                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        imageupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processupload(filepath);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        filetitle=findViewById(R.id.filetitle);
        if(requestCode==101 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            if (filetitle.getText().toString().isEmpty()) {
                Toast.makeText(uploadfile.this, "Fill the File name", Toast.LENGTH_SHORT).show();
            }
            else {
                filepath = data.getData();
                filelogo.setVisibility(View.VISIBLE);
                cancelfile.setVisibility(View.VISIBLE);
                imagebrowse.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void processupload(Uri filepath)
    {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("File Uploading....!!!");
        pd.show();

        final StorageReference reference=storageReference.child("uploads/"+System.currentTimeMillis()+".pdf");
        reference.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                model obj=new model(filetitle.getText().toString(),uri.toString(),0,0);
                                databaseReference.child(Objects.requireNonNull(databaseReference.push().getKey())).setValue(obj);

                                pd.dismiss();
                                Toast.makeText(getApplicationContext(),"File Uploaded",Toast.LENGTH_LONG).show();

                                filelogo.setVisibility(View.INVISIBLE);
                                cancelfile.setVisibility(View.INVISIBLE);
                                imagebrowse.setVisibility(View.VISIBLE);
                                filetitle.setText("");
                            }
                        });

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        pd.setMessage("Uploaded :"+(int)percent+"%");
                    }
                });


    }
}