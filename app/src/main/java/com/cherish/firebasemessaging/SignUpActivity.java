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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    private static final int PERMISSION_CODE = 200;
    private static final int REQUEST_CODE = 300;
    TextInputEditText name, password, email;
    ImageView image;
    FirebaseAuth auth;
    GoogleSignInClient client;
    Uri imageUrl;
    Button submitBtn;
    String nameText, passwordText, emailText, imageText;
   private StorageReference reference;
   private CollectionReference collectionReference;
   private FirebaseFirestore firebaseFirestore;
   SignInButton signUpButton;
   GoogleSignInClient signInClient;
   LinearLayout wrapperLayout;
   ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = findViewById(R.id.nameEdit);
        password = findViewById(R.id.passwordEdit);
        email = findViewById(R.id.emailEdit);
        submitBtn = findViewById(R.id.submitBtn);
        image = findViewById(R.id.image);
        signUpButton = findViewById(R.id.signUpBtn);
        wrapperLayout  = findViewById(R.id.wrapperLayout);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("token");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this,signInOptions);



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ImageUrl", String.valueOf(imageUrl));
                nameText = name.getText().toString();
                passwordText = password.getText().toString();
                emailText = email.getText().toString();
                if (TextUtils.isEmpty(nameText)){
                    name.setError("Name is required");
                    name.requestFocus();
                }else if (TextUtils.isEmpty(passwordText)){
                    password.setError("Password is required");
                    password.requestFocus();
                }else  if (TextUtils.isEmpty(emailText)){
                    email.setError("Email is required");
                    email.requestFocus();
                }else  if (imageUrl == null){
                    Toast.makeText(SignUpActivity.this, "Image is Required", Toast.LENGTH_LONG).show();
                    image.requestFocus();
                }else  if (passwordText.length() < 6){
                    password.setError("Password-length must be greater than 6");
                    password.requestFocus();
                }
                else  {
                    progressDialog.show();
                        auth.createUserWithEmailAndPassword(emailText,passwordText).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                progressDialog.cancel();
                              StorageReference storageReference = reference.child("profile/" + System.currentTimeMillis());
                              storageReference.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                  @Override
                                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                     taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                         @Override
                                         public void onSuccess(Uri uri) {
                                             final FirebaseUser user = auth.getCurrentUser();
                                             UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                                     .setDisplayName(nameText)
                                                     .setPhotoUri(uri)
                                                     .build();

                                             user.updateProfile(userProfileChangeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                 @Override
                                                 public void onSuccess(Void aVoid) {
                                                     FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                         @Override
                                                         public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                             if (task.isSuccessful()){
                                                                 HashMap<String, String > token = new HashMap<>();
                                                                 token.put("token", Objects.requireNonNull(task.getResult().getToken()));
                                                                 collectionReference.document(user.getUid()).set(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                     @Override
                                                                     public void onSuccess(Void aVoid) {
                                                                         Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                                         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                         startActivity(intent);
                                                                         finish();
                                                                     }
                                                                 });

                                                             }
                                                         }
                                                     });
                                                 }
                                             });
                                         }
                                     });
                                  }
                              });


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Alert.showFailed(SignUpActivity.this, "Authentication Failed");
                                Log.i("ERROR", e.getMessage());
                                Log.i("ERROR", e.getLocalizedMessage());
                                e.printStackTrace();
                            }
                        });
                }

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                Intent intent = signInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });


    }


    public void  selectImage(){
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
        if(requestCode == RC_SIGN_IN){
            progressDialog.cancel();
         Task<GoogleSignInAccount>task = GoogleSignIn.getSignedInAccountFromIntent(data);
         try {
             GoogleSignInAccount account = task.getResult(ApiException.class);
             if (account!=null){
                 authWithGoogle(account);
             }
         }catch (ApiException e){
             e.printStackTrace();
             progressDialog.cancel();
             Log.i("AUTHERROR", e.getMessage());
             Log.i("AUTHERROR", e.getLocalizedMessage());

         }

//            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();

        }else if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUrl = data.getData();
            Bitmap bitmap;
            try {
                if (  Build.VERSION.SDK_INT < 28) {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUrl);
                } else {
                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(),imageUrl);
                    bitmap = ImageDecoder.decodeBitmap(source);
                }
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            Alert.showFailed(this, "Failed");
         }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void authWithGoogle(final GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
               .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()){
                           final FirebaseUser user = auth.getCurrentUser();
                           UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                                   .setDisplayName(account.getDisplayName())
                                   .setPhotoUri(account.getPhotoUrl())
                                   .build();
                           Log.i("AccountName", account.getDisplayName());
                           Log.i("AccountMail", account.getEmail());
                           Log.i("AccountImafe", String.valueOf(account.getPhotoUrl()));

                           user.updateProfile(changeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                  FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                      @Override
                                      public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                          if (task.isSuccessful()){
                                            String tokenId = task.getResult().getToken();
                                              Map<String, Object> token = new HashMap<>();
                                              token.put("token", tokenId);
                                                collectionReference.document(user.getUid()).set(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                          }
                                      }
                                  });
                               }
                           });
                       }else {
                           Alert.showFailed(SignUpActivity.this, "Authentication Failed");
                           Snackbar.make(wrapperLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                       }

                   }
               });


    }

}
