package com.jonathanaguilar.datareflix.Controllers;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Date;

public class Ctl_Imagenes {

    Context context;
    ActivityResultRegistry resultRegistry;
    ActivityResultLauncher<Intent> getImage;
    ActivityResultLauncher<CropImageContractOptions> cropImage;
    ActivityResultLauncher<Intent> android11StoragePermission;
    ActivityResultLauncher<String> requestPermission;

    public Ctl_Imagenes(Context context) {
        this.context = context;

        //registerForActivityResult(new
         getImage = resultRegistry.register( "getImage", new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null && data.getData() != null) {
                    Uri imageUri = data.getData();
                    launchImageCropper(imageUri);
                }
            }
        });

        cropImage = resultRegistry.register( "cropImage", new CropImageContract(), result -> {
            if (result.isSuccessful()) {
                Bitmap cropped = BitmapFactory.decodeFile(result.getUriFilePath(context, true));
                saveCroppedImage(cropped);
            }
        });

        android11StoragePermission = resultRegistry.register( "android11StoragePermission", new ActivityResultContracts.StartActivityForResult(), result -> {
            if (isPermitted()) {
                getImageFile();
            } else {
                permissionDenied();
            }
        });

         requestPermission = resultRegistry.register( "requestPermission", new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                getImageFile();
            } else {
                permissionDenied();
            }
        });

    }

    public void Iniciar(){

        if (isPermitted()) {
            getImageFile();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requestAndroid11StoragePermission();
            } else {
                requestPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    private void getImageFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getImage.launch(intent);
    }

    private void saveCroppedImage(Bitmap bitmap) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/Cropped Images");

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        // Generate a unique file name
        String imageName = "Image_" + new Date().getTime() + ".jpg";

        File file = new File(myDir, imageName);
        if (file.exists()) file.delete();

        try {
            // Save the Bitmap to the file
            OutputStream outputStream;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                outputStream = Files.newOutputStream(file.toPath());
            } else {
                outputStream = new FileOutputStream(file);
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Add the image to the MediaStore
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            // Trigger a media scan to update the gallery
            MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, null);
            showSuccessMessage();
        } catch (Exception e) {
            showFailureMessage();
        }
    }

    @TargetApi(Build.VERSION_CODES.R)
    private void requestAndroid11StoragePermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse(String.format("package:%s", context.getPackageName())));
        android11StoragePermission.launch(intent);
    }
    
    private void launchImageCropper(Uri uri) {
        CropImageOptions cropImageOptions = new CropImageOptions();
        cropImageOptions.imageSourceIncludeGallery = false;
        cropImageOptions.imageSourceIncludeCamera = true;
        CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(uri, cropImageOptions);
        cropImage.launch(cropImageContractOptions);
    }
    
    private boolean isPermitted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void permissionDenied() {
        Toast.makeText(context, "Permission denied", Toast.LENGTH_LONG).show();
    }

    private void showFailureMessage() {
        Toast.makeText(context, "Cropped image not saved something went wrong", Toast.LENGTH_LONG).show();
    }

    private void showSuccessMessage() {
        Toast.makeText(context, "Image Saved", Toast.LENGTH_LONG).show();
    }

}
