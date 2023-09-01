package com.straccion.chat.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.straccion.chat.R;
import com.straccion.chat.models.Post;
import com.straccion.chat.models.User;
import com.straccion.chat.providers.AuthProvider;
import com.straccion.chat.providers.ImageProvider;
import com.straccion.chat.providers.UserProvider;
import com.straccion.chat.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class EditarPerfilActivity extends AppCompatActivity {

    CircleImageView mCircleImageView;
    CircleImageView mCicleImagenProfile;
    ImageView mImageViewCover;
    TextInputEditText mtxtNombreUsuarioEditar;
    TextInputEditText mtxtTelefonoEditar;
    Button mButtonEditperfil;
    String Telefono ="";
    String Nombre ="";
    String imageProfile = "";
    String imageCover = "";
    ImageProvider mImagePrivider;
    AuthProvider mAuthProvider;


    UserProvider mUserprovider;

    AlertDialog mDialog;
    CharSequence options[];
    AlertDialog.Builder mBuilderSelector;
    String mAbsolutePhotoPath1;
    String mAbsolutePhotoPath2;
    String mPhotoPath1;
    String mPhotoPath2;
    File mPhotoFile1;
    File mPhotoFile2;
    File mImageFile;
    File mImageFile2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        mImagePrivider = new ImageProvider();
        mUserprovider = new UserProvider();
        mAuthProvider = new AuthProvider();
        mtxtNombreUsuarioEditar = findViewById(R.id.txtNombreUsuarioEditar);
        mtxtTelefonoEditar = findViewById(R.id.txtTelefonoEditar);
        mImageViewCover = findViewById(R.id.imageViewCover);
        mCicleImagenProfile = findViewById(R.id.cicleImagenProfile);
        mCircleImageView = findViewById(R.id.circleVolverEdit);
        mButtonEditperfil = findViewById(R.id.btneditarPerfil);
        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();
        mCicleImagenProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(1);
            }
        });
        mImageViewCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(2);
            }
        });
        mButtonEditperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicEditProfile();
            }
        });

        mBuilderSelector =new AlertDialog.Builder(this);
        mBuilderSelector.setTitle("Selecciona una opcion");
        options = new CharSequence[]{
                "Imagen de Galeria",
                "Tomar Foto"
        };


        mCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getUser();

    }
    private void selectOptionImage(int requestCode) {
        mBuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    if (requestCode ==1){
                        openGallery();
                    } else if (requestCode ==2) {
                        openGallery2();
                    }
                }else if(which == 1) {
                    TakePhoto(requestCode);
                }
            }
        });
        mBuilderSelector.show();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        GalleryLauncher.launch(galleryIntent);
    }
    private void openGallery2() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        GalleryLauncher2.launch(galleryIntent);
    }

    ActivityResultLauncher<Intent> GalleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        try {
                            mPhotoFile1 = null;
                            mImageFile = FileUtil.from(EditarPerfilActivity.this, result.getData().getData());
                            mCicleImagenProfile.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
                        }catch (Exception e){
                            Log.d("Error", "Se produjo un error" + e.getMessage());
                            Toast.makeText(EditarPerfilActivity.this, "Se produjo un error" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
    );

    ActivityResultLauncher<Intent> GalleryLauncher2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        try {
                            mPhotoFile2 = null;
                            mImageFile2 = FileUtil.from(EditarPerfilActivity.this, result.getData().getData());
                            mImageViewCover.setImageBitmap(BitmapFactory.decodeFile(mImageFile2.getAbsolutePath()));
                        }catch (Exception e){
                            Log.d("Error", "Se produjo un error" + e.getMessage());
                            Toast.makeText(EditarPerfilActivity.this, "Se produjo un error" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
    );



    private void TakePhoto(int numero) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createPhotoFile(numero);

            }catch (Exception e){
                Toast.makeText(this, "Hubo un error con el archivo" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            if (numero == 1){
                if (photoFile != null) {
                    Uri photoUri = FileProvider.getUriForFile(EditarPerfilActivity.this, "com.straccion.chat", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    cameraLauncher.launch(takePictureIntent);
                }
            } else if (numero == 2) {

                if (photoFile != null) {
                    Uri photoUri = FileProvider.getUriForFile(EditarPerfilActivity.this, "com.straccion.chat", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    cameraLauncher2.launch(takePictureIntent);
                }
            }
        }
    }
    ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    mImageFile = null;
                    mPhotoFile1 = new File(mAbsolutePhotoPath1);
                    Picasso.get().load(mPhotoPath1).into(mCicleImagenProfile);
                }
            }
    );
    ActivityResultLauncher<Intent> cameraLauncher2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    mImageFile2 = null;
                    mPhotoFile2 = new File(mAbsolutePhotoPath2);
                    Picasso.get().load(mPhotoPath2).into(mImageViewCover);
                }
            }
    );
    private File createPhotoFile(int numero) throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(
                new Date() + "_Photo",
                "jpg",
                storageDir
        );
        if (numero == 1){
            mPhotoPath1 = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath1 = photoFile.getAbsolutePath();
        } else if (numero == 2) {
            mPhotoPath2 = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath2 = photoFile.getAbsolutePath();
        }
        return photoFile;
    }

    private void clicEditProfile() {
        Nombre = mtxtNombreUsuarioEditar.getText().toString();
        Telefono = mtxtTelefonoEditar.getText().toString();
        if (!Nombre.isEmpty() && !Telefono.isEmpty()){
            if (mImageFile != null && mImageFile2 != null){
                saveImageCoverAndProfile(mImageFile, mImageFile2);
            }
            //Tomo las dos fotos de la camara
            else if (mPhotoFile1 != null && mPhotoFile2 != null){
                saveImageCoverAndProfile(mPhotoFile1, mPhotoFile2);
            }
            //una imagen de galeria y otra de la camara
            else if (mImageFile != null && mPhotoFile2 != null){
                saveImageCoverAndProfile(mImageFile, mPhotoFile2);
            }
            //una foto de camara y otra de la galeria
            else if (mPhotoFile1 != null && mImageFile2 != null){
                saveImageCoverAndProfile(mPhotoFile1, mImageFile2);
            }
            else if (mPhotoFile1 != null) {
                saveImage(mPhotoFile1, true);
            }
            else if (mPhotoFile2 != null) {
                saveImage(mPhotoFile2, false);
            }
            else if (mPhotoFile1 != null) {
                saveImage(mPhotoFile1, true);
            }
            else if (mPhotoFile2 != null) {
                saveImage(mPhotoFile2, false);
            }
            else {
                User user = new User();
                user.setNombreUsuario(Nombre);
                user.setTelefono(Telefono);
                user.setId(mAuthProvider.getUid());
                updateInfo(user);
            }

        }else {
            Toast.makeText(this, "Ingrese el nombre de usuario y numero de telefono", Toast.LENGTH_SHORT).show();
        }
    }
    private void saveImageCoverAndProfile(File imageFile1, File imageFile2) {
        mDialog.show();
        mImagePrivider.save(EditarPerfilActivity.this, imageFile1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    mImagePrivider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String urlprofile = uri.toString();
                            mImagePrivider.save(EditarPerfilActivity.this, imageFile2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImage2) {
                                    if (taskImage2.isSuccessful()){
                                        mImagePrivider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri2) {
                                                String UrlCover = uri2.toString();
                                                User user = new User();
                                                user.setNombreUsuario(Nombre);
                                                user.setTelefono(Telefono);
                                                user.setImageProfile(urlprofile);
                                                user.setImageCover(UrlCover);
                                                user.setId(mAuthProvider.getUid());
                                                updateInfo(user);
                                            }
                                        });
                                    }else{
                                        mDialog.dismiss();
                                        Toast.makeText(EditarPerfilActivity.this, "La imagen numero 2 no se pudo guardar", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }else {
                    mDialog.dismiss();
                    Toast.makeText(EditarPerfilActivity.this, "Hubo un error al almacenar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private  void saveImage(File Image, boolean IsProfileImage){
        mDialog.show();
        mImagePrivider.save(EditarPerfilActivity.this, Image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    mImagePrivider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String url = uri.toString();
                            User user = new User();
                            user.setNombreUsuario(Nombre);
                            user.setTelefono(Telefono);
                            if (IsProfileImage){
                                user.setImageProfile(url);
                                user.setImageCover(imageCover);
                            }else {
                                user.setImageCover(url);
                                user.setImageProfile(imageProfile);
                            }
                            user.setId(mAuthProvider.getUid());
                            updateInfo(user);
                        }
                    });
                }else {
                    mDialog.dismiss();
                    Toast.makeText(EditarPerfilActivity.this, "Hubo un error al almacenar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateInfo(User user){
        if (mDialog.isShowing()){
            mDialog.show();
        }
        mUserprovider.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> tasked) {
                mDialog.dismiss();
                if (tasked.isSuccessful()){
                    Toast.makeText(EditarPerfilActivity.this, "La información se actualizo correctamente ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(EditarPerfilActivity.this, "La informacion no se pudo actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getUser(){
        mUserprovider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("nombreUsuario")){
                        Nombre = documentSnapshot.getString("nombreUsuario");
                        mtxtNombreUsuarioEditar.setText(Nombre);
                    }
                    if (documentSnapshot.contains("telefono")){
                        Telefono = documentSnapshot.getString("telefono");
                        mtxtTelefonoEditar.setText(Telefono);
                    }
                    if (documentSnapshot.contains("imageProfile")){
                        imageProfile = documentSnapshot.getString("imageProfile");
                        if (imageProfile!= null){
                            if (!imageProfile.isEmpty()){
                                Picasso.get().load(imageProfile).into(mCicleImagenProfile);
                            }
                        }

                    }
                    if (documentSnapshot.contains("imageCover")){
                        imageCover = documentSnapshot.getString("imageCover");
                        if (imageCover!= null){
                            if (!imageCover.isEmpty()){
                                Picasso.get().load(imageCover).into(mImageViewCover);
                            }
                        }

                    }
                }
            }
        });
    }

}