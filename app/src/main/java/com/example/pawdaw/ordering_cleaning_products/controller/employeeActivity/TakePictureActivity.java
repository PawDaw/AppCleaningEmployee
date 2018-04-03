package com.example.pawdaw.ordering_cleaning_products.controller.employeeActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pawdaw.ordering_cleaning_products.R;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.Image;
import com.example.pawdaw.ordering_cleaning_products.model.mainClasses.User;
import com.example.pawdaw.ordering_cleaning_products.model.service.PermissionService;
import com.example.pawdaw.ordering_cleaning_products.model.service.Service;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;


/**
 * Created by pawdaw on 12/06/17.
 */

public class TakePictureActivity extends AppCompatActivity{


    private ImageView imageview;
    private Button button_Save;
    private String employeeName,randomId,exisitngUUIDImage;
    private User userExtras;
    private Uri uri,url,file;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALLERY_REQUEST_CODE = 2;
    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 3;
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "camera";





    //--- FIREBASE -----
    // Set up Cloud Storage
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    // Create a storage reference from our app
    private StorageReference storageRef = storage.getReference();


    // Create instance of Firebase
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    // Create main reference - parent
    private DatabaseReference dbRefEmployee = firebaseDatabase.getReference("employees");

    //----------


    private ProgressDialog progressDialog;
    private static final String CIRCLE_IMAGE = "circle_image";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_picture);

        imageview = (ImageView) findViewById(R.id.imageView);

        progressDialog = new ProgressDialog(this);

        button_Save  = (Button) findViewById(R.id.save_upload);
        button_Save.setVisibility(View.GONE);


        // Getting extras from the Admin WorkplaceActivityEmployee
        userExtras = (User) getIntent().getSerializableExtra("user");

        employeeName = userExtras.getUsername();


        // 1/3 BACK BUTTON
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    // 2/3 BACK BUTTON
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void on_click_button_Camera(View view) {

        if(PermissionService.getInstance().askForCameraWriteReadPermission(this)){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,CAMERA_REQUEST_CODE);
        }

    }

    public void on_click_button_Gallery(View view) {

        if(PermissionService.getInstance().askForCameraWriteReadPermission(this)){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent,GALLERY_REQUEST_CODE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // get picture from the CAMERA
        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){

            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            imageview.setImageBitmap(bitmap);
            button_Save.setVisibility(View.VISIBLE);
        }

        // get picture from the GALLERY
        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            uri = data.getData();

            //Picasso.with(this).load(uri).into(imageview);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                imageview.setImageBitmap(bitmap);
                button_Save.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                Toast.makeText(TakePictureActivity.this, R.string.error_Toast, Toast.LENGTH_LONG).show();
            }
        }
    }


    public void on_Click_Button_Save_upolad_Image(View view) {

        byte[] compressedImage = new byte[0];


        imageview.setDrawingCacheEnabled(true);
        imageview.buildDrawingCache();

        progressDialog.setMessage("Uploading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();

        //Random UUID.randomUUID()
        randomId = String.valueOf(UUID.randomUUID());
        exisitngUUIDImage = Service.getInstance().existEmployeeProfileImage(employeeName,userExtras.getEmail());

        // Create a child reference
        // imagesRef now points to "images"  UUID.randomUUID()+".png" or uri.getLastPathSegment()
        StorageReference filepath = storageRef.child(CIRCLE_IMAGE).child(randomId+".jpg");
        StorageReference filepathDelete = storageRef.child(CIRCLE_IMAGE).child(exisitngUUIDImage+".jpg");

        // EXTRA TEXT
        StorageMetadata storageMetadata = new StorageMetadata.Builder().setCustomMetadata("text",employeeName).build();

        //Compress Image by method placed in the Service class
        compressedImage = Service.getInstance().compressImage(imageview.getDrawingCache());

        // FIREBASE new TASK
        UploadTask uploadTask = filepath.putBytes(compressedImage,storageMetadata);

        //UploadTask uploadTask = filepath.putFile(uri,storageMetadata);

        // Deleting image from the FIREBASE
        if(exisitngUUIDImage != null){

            filepathDelete.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TakePictureActivity.this, R.string.error_Toast, Toast.LENGTH_LONG).show();
                }
            });
        }

        // ADDING new image to the FIREBASE
        uploadTask.addOnSuccessListener(TakePictureActivity.this,new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                url = taskSnapshot.getDownloadUrl();
                progressDialog.hide();
                Toast.makeText(TakePictureActivity.this,"Uploading Finished..",Toast.LENGTH_SHORT).show();

                // update value in the Employee "image"
                HashMap<String,Object> newImage = new HashMap<String, Object>();
                Image image = new Image(url.toString(),randomId);
                image.toMap();
                newImage.put("image",image);
                dbRefEmployee.child(employeeName).updateChildren(newImage);

                Intent intent = new Intent(TakePictureActivity.this,WorkplaceActivityEmployee.class);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TakePictureActivity.this, R.string.error_Toast, Toast.LENGTH_LONG).show();

            }
        });


    }

}
