package com.cherish.firebasemessaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddBookActivity extends AppCompatActivity {
    private static int PERMISSION_CODE = 100;
    private  static int REQUEST_CODE = 200;
    TextInputEditText bookName, bookDescription;
    AutoCompleteTextView bookCategory;
    Button submitBook;
    String bookNameText, bookDescriptionText, bookCategoryText;
    Uri imageUrl;
    ProgressDialog progressDialog;
    private StorageReference reference;
    private CollectionReference collectionReference;
    ImageView bookImage;
    private String base_url = "https://fcm.googleapis.com/fcm/";
    FirebaseFirestore firestore;
    private String key = "AAAAIaqI6_I:APA91bGlVNA3G92YOpdftHdnevfUlF0MZzk_WCfl9Rh0SyXH93rDnR_X7Zr9ld1DCQr9iigHf66xoRnlauyZFkeRb4Ke3_2UBxdOAHL8eyZVScxkqThfOv96dwoVPy4x798vdHB5nX6u";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection("books");
        reference = FirebaseStorage.getInstance().getReference();
        bookCategory = findViewById(R.id.bookCategory);
        bookName = findViewById(R.id.bookName);

        bookDescription = findViewById(R.id.bookDescription);
        bookImage = findViewById(R.id.bookImage);
        submitBook = findViewById(R.id.submitBook);
        String[] category = getResources().getStringArray(R.array.category_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, category);
        bookCategory.setThreshold(1);
        bookCategory.setCompletionHint("Select category");
        bookCategory.setAdapter(adapter);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        submitBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ImageURI", String.valueOf(imageUrl));
                bookNameText = bookName.getText().toString();
                bookDescriptionText = bookDescription.getText().toString();
                bookCategoryText = bookCategory.getText().toString();
                if (TextUtils.isEmpty(bookNameText)){
                    bookName.setError("Required");
                    bookName.requestFocus();
                }else  if (TextUtils.isEmpty(bookDescriptionText)){
                    bookDescription.setError("Required");
                    bookDescription.requestFocus();
                }else if (TextUtils.isEmpty(bookCategoryText)){
                    bookCategory.setError("Required");
                    bookCategory.requestFocus();
                }else if (imageUrl == null){
                    Toast.makeText(AddBookActivity.this, "Image is Required", Toast.LENGTH_LONG).show();
                    bookImage.requestFocus();
                }else {
                    progressDialog.show();
                    StorageReference storageReference = reference.child("practice/" + System.currentTimeMillis());
                    storageReference.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                   Log.i("IMAGEURL", String.valueOf(uri));
                                   BookItem bookItem = new BookItem(bookNameText,bookDescriptionText,uri.toString(),bookCategoryText);
                                   collectionReference.document().set(bookItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void aVoid) {
                                           Alert.showSuccess(AddBookActivity.this, "Successful");
                                           progressDialog.cancel();
                                           sendNotification(bookNameText,"A new Book has been added to "+ bookCategoryText);
                                           Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                                           startActivity(intent);



                                       }
                                   }).addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           Alert.showFailed(AddBookActivity.this, "Failed");
                                       }
                                   });
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Alert.showFailed(AddBookActivity.this, "Failed");

                               }
                           });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Alert.showFailed(AddBookActivity.this, "Failed");
                        }
                    });
                }

            }
        });

        bookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();

            }
        });




    }



    public void chooseImage(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE);
            } else {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUrl = data.getData();
            Bitmap bitmap;
            try {
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUrl);
                } else {
                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageUrl);
                    bitmap = ImageDecoder.decodeBitmap(source);
                }
                bookImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }else {
                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
            }
        }

    }


    public  void  sendNotification(String title, String message ){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("Authorization", "key=" + key);

        Data data = new Data(title,message);
        FCMessage fcMessage = new FCMessage("/topics/all", data);



        apiService.sendMessage(header,fcMessage).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               Alert.showSuccess(AddBookActivity.this, "MESSAGE SENT");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Alert.showFailed(AddBookActivity.this, "MESSAGE Failed");

            }
        });


    }
}
