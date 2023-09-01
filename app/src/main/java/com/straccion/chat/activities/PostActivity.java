package com.straccion.chat.activities;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.straccion.chat.R;
import com.straccion.chat.models.Post;
import com.straccion.chat.providers.AuthProvider;
import com.straccion.chat.providers.ImageProvider;
import com.straccion.chat.providers.PostProvider;
import com.straccion.chat.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class PostActivity extends AppCompatActivity {

    ImageView mImageViewPost1;
    ImageView mImageViewPost2;
    File mImageFile;
    File mImageFile2;
    Button mButtonPost;
    ImageProvider mImagePrivider;
    TextInputEditText mtxtVideoGame;
    TextInputEditText mtxtDescripcion;
    ImageView mImageViewPC;
    ImageView mImageViewPs4;
    ImageView mImageViewXbox;
    ImageView mImageViewNintendo;
    TextView mtxtCategory;
    String mCategoria = "";
    PostProvider mPostProvider;
    String mTitulo = "";
    String mDescripcion = "";
    AuthProvider mAuthProvider;
    CircleImageView mCircleImagenBack;
    AlertDialog mDialog;
    CharSequence options[];
    AlertDialog.Builder mBuilderSelector;

    String mAbsolutePhotoPath1;
    String mAbsolutePhotoPath2;
    String mPhotoPath1;
    String mPhotoPath2;
    File mPhotoFile1;
    File mPhotoFile2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mImageViewPost1 = findViewById(R.id.ImageViwePost1);
        mImageViewPost2 = findViewById(R.id.ImageViwePost2);
        mButtonPost = findViewById(R.id.btnPost);
        mtxtVideoGame = findViewById(R.id.txtVideoGame);
        mtxtDescripcion = findViewById(R.id.txtDescripcion);
        mImageViewPC = findViewById(R.id.ImageViewPC);
        mImageViewPs4 = findViewById(R.id.ImageViewPs4);
        mImageViewXbox = findViewById(R.id.ImageViewXbox);
        mImageViewNintendo = findViewById(R.id.ImageViewNintendo);
        mtxtCategory = findViewById(R.id.txtCategory);
        mCircleImagenBack = findViewById(R.id.btncircleVolver);
        mCircleImagenBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAuthProvider = new AuthProvider();
        mImagePrivider = new ImageProvider();
        mPostProvider = new PostProvider();
        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();
        mBuilderSelector =new AlertDialog.Builder(this);
        mBuilderSelector.setTitle("Selecciona una opcion");
        options = new CharSequence[]{
          "Imagen de Galeria",
          "Tomar Foto"
        };
        

        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPost();
            }
        });

        mImageViewPost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int requestCode = 1;
                selectOptionImage(requestCode);
            }
        });

        mImageViewPost2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int requestCode = 2;
                selectOptionImage(requestCode);
            }
        });


        mImageViewPC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategoria = "PC";
                mtxtCategory.setText(mCategoria);
            }
        });
        mImageViewPs4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategoria = "PS4";
                mtxtCategory.setText(mCategoria);
            }
        });
        mImageViewXbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategoria = "XBOX";
                mtxtCategory.setText(mCategoria);
            }
        });
        mImageViewNintendo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategoria = "NINTENDO";
                mtxtCategory.setText(mCategoria);
            }
        });
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
                    Uri photoUri = FileProvider.getUriForFile(PostActivity.this, "com.straccion.chat", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    cameraLauncher.launch(takePictureIntent);
                }
            } else if (numero == 2) {

                if (photoFile != null) {
                    Uri photoUri = FileProvider.getUriForFile(PostActivity.this, "com.straccion.chat", photoFile);
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
                    Picasso.get().load(mPhotoPath1).into(mImageViewPost1);
                }
            }
    );
    ActivityResultLauncher<Intent> cameraLauncher2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    mImageFile2 = null;
                    mPhotoFile2 = new File(mAbsolutePhotoPath2);
                    Picasso.get().load(mPhotoPath2).into(mImageViewPost2);
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



    private void clickPost() {
         mTitulo = mtxtVideoGame.getText().toString();
         mDescripcion = mtxtDescripcion.getText().toString();

        if (!mTitulo.isEmpty() && !mDescripcion.isEmpty() && !mCategoria.isEmpty()){
            // selecciono mabas de galeria
            if (mImageFile != null && mImageFile2 != null){
                saveImage(mImageFile, mImageFile2);
            }
            //Tomo las dos fotos de la camara
            else if (mPhotoFile1 != null && mPhotoFile2 != null){
                saveImage(mPhotoFile1, mPhotoFile2);
            }
            //una imagen de galeria y otra de la camara
            else if (mImageFile != null && mPhotoFile2 != null){
                saveImage(mImageFile, mPhotoFile2);
            }
            //una foto de camara y otra de la galeria
            else if (mPhotoFile1 != null && mImageFile2 != null){
                saveImage(mPhotoFile1, mImageFile2);
            }
            else {
                Toast.makeText(this, "Debes seleccionar una imagen", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(this, "Completa los campos para publicar", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImage(File imageFile1, File imageFile2) {
        mDialog.show();
        mImagePrivider.save(PostActivity.this, imageFile1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    mImagePrivider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String url = uri.toString();

                            mImagePrivider.save(PostActivity.this, imageFile2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImage2) {
                                    if (taskImage2.isSuccessful()){
                                        mImagePrivider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri2) {
                                                String Url2 = uri2.toString();
                                                Post post = new Post();
                                                post.setImagen1(url);
                                                post.setImagen2(Url2);
                                                post.setTitulo(mTitulo);
                                                post.setDescription(mDescripcion);
                                                post.setCategoria(mCategoria);
                                                post.setIdUser(mAuthProvider.getUid());
                                                post.setTimestamp(new Date().getTime());
                                                mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> taskSave) {
                                                        mDialog.dismiss();
                                                        if (taskSave.isSuccessful()){
                                                            clearForm();
                                                            Toast.makeText(PostActivity.this, "La informacion se almaceno correctamente", Toast.LENGTH_SHORT).show();
                                                        }else {
                                                            Toast.makeText(PostActivity.this, "No se puedo almacenar correctamente", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }else{
                                        mDialog.dismiss();
                                        Toast.makeText(PostActivity.this, "La imagen numero 2 no se pudo guardar", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }
                    });
                }else {
                    mDialog.dismiss();
                    Toast.makeText(PostActivity.this, "Hubo un error al almacenar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void clearForm() {
        mtxtVideoGame.setText("");
        mtxtDescripcion.setText("");
        mtxtCategory.setText("CATEGORIA");
        mImageViewPost1.setImageResource(R.drawable.upload_image);
        mImageViewPost2.setImageResource(R.drawable.upload_image);
        mTitulo="";
        mDescripcion ="";
        mCategoria = "";
        mImageFile = null;
        mImageFile2 = null;
    }

    ActivityResultLauncher<Intent> GalleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        try {
                            mPhotoFile1 = null;
                            mImageFile = FileUtil.from(PostActivity.this, result.getData().getData());
                            mImageViewPost1.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
                        }catch (Exception e){
                            Log.d("Error", "Se produjo un error" + e.getMessage());
                            Toast.makeText(PostActivity.this, "Se produjo un error" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
    );


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

    ActivityResultLauncher<Intent> GalleryLauncher2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        try {
                            mPhotoFile2 = null;
                            mImageFile2 = FileUtil.from(PostActivity.this, result.getData().getData());
                            mImageViewPost2.setImageBitmap(BitmapFactory.decodeFile(mImageFile2.getAbsolutePath()));
                        }catch (Exception e){
                            Log.d("Error", "Se produjo un error" + e.getMessage());
                            Toast.makeText(PostActivity.this, "Se produjo un error" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
    );
}