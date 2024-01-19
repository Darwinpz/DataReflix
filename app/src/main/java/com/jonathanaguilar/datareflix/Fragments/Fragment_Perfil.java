package com.jonathanaguilar.datareflix.Fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jonathanaguilar.datareflix.Controllers.Alert_dialog;
import com.jonathanaguilar.datareflix.Controllers.Progress_dialog;
import com.jonathanaguilar.datareflix.MainActivity;
import com.jonathanaguilar.datareflix.Objetos.Ob_usuario;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;
import com.jonathanaguilar.datareflix.Vi_fotos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Fragment_Perfil extends Fragment {

    Button btn_salir, btn_update_profile;
    TextView txt_nombre, txt_cedula,txt_estado, txt_rol, cant_marcaciones, cant_solicitudes, txtfecha_ini_contrato,txtfecha_fin_contrato;
    EditText editTextEmail, editTextTextPhone;
    Progress_dialog dialog;
    ImageView img_perfil;
    Alert_dialog alertDialog;
    String URL_FOTO = "", NOMBRE = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View vista = inflater.inflate(R.layout.fragment_perfil,container,false);

        txt_nombre = vista.findViewById(R.id.txt_nombre);
        txt_cedula = vista.findViewById(R.id.txt_cedula);
        txt_estado = vista.findViewById(R.id.txt_estado);
        txt_rol = vista.findViewById(R.id.txt_rol);
        editTextEmail = vista.findViewById(R.id.editTextTextEmailAddress);
        editTextTextPhone = vista.findViewById(R.id.editTextTextPhone);
        btn_salir = vista.findViewById(R.id.btn_salir);
        img_perfil = vista.findViewById(R.id.img_perfil);
        dialog = new Progress_dialog(vista.getContext());
        alertDialog = new Alert_dialog(vista.getContext());

        cant_marcaciones = vista.findViewById(R.id.cant_marcaciones);
        cant_solicitudes = vista.findViewById(R.id.cant_solicitudes);
        txtfecha_ini_contrato = vista.findViewById(R.id.txtfecha_ini_contrato);
        txtfecha_fin_contrato = vista.findViewById(R.id.txtfecha_fin_contrato);

        btn_update_profile = vista.findViewById(R.id.btn_update_profile);

        if(!Principal.id.isEmpty()){

            btn_update_profile.setOnClickListener(view -> {

                dialog.mostrar_mensaje("Actualizando Perfil...");

                if(!editTextEmail.getText().toString().isEmpty() && !editTextTextPhone.getText().toString().isEmpty()) {

                    Ob_usuario usuario = new Ob_usuario();
                    usuario.uid = Principal.id;
                    usuario.email = editTextEmail.getText().toString();
                    usuario.telefono = editTextTextPhone.getText().toString();
                    update_perfil(usuario);

                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("Correcto", "Usuario Actualizado Correctamente", builder -> {
                        builder.setCancelable(false);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                        builder.create().show();
                    });

                }else{
                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("¡Advertencia!", "Completa todos los campos", builder -> {
                        builder.setCancelable(true);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                        builder.create().show();
                    });
                }
            });

            txt_rol.setText(Principal.rol);
            Principal.databaseReference.child("usuarios").child(Principal.id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){

                        if(snapshot.child("cedula").exists()) {
                            txt_cedula.setText(Objects.requireNonNull(snapshot.child("cedula").getValue()).toString());
                        }
                        if(snapshot.child("estado").exists()) {
                            txt_estado.setText(Objects.requireNonNull(snapshot.child("estado").getValue()).toString());
                            switch (txt_estado.getText().toString().toLowerCase()){
                                case "activo":
                                    txt_estado.setTextColor(ContextCompat.getColor(vista.getContext(),R.color.success));
                                    break;
                                case "inactivo":
                                    txt_estado.setTextColor(ContextCompat.getColor(vista.getContext(),R.color.danger));
                                    break;
                                default:
                                    txt_estado.setTextColor(ContextCompat.getColor(vista.getContext(),R.color.proyecto_night));
                                    break;
                            }
                        }
                        if(snapshot.child("nombre").exists()) {
                            NOMBRE = Objects.requireNonNull(snapshot.child("nombre").getValue()).toString();
                            txt_nombre.setText(NOMBRE);
                        }
                        if(snapshot.child("url_foto").exists()){
                            URL_FOTO = Objects.requireNonNull(snapshot.child("url_foto").getValue()).toString();
                            Glide.with(vista.getContext()).load(URL_FOTO).centerCrop().into(img_perfil);
                        }else{
                            img_perfil.setImageResource(R.drawable.perfil);
                        }
                        if(snapshot.child("email").exists()){
                            editTextEmail.setText(Objects.requireNonNull(snapshot.child("email").getValue()).toString());
                        }
                        if(snapshot.child("telefono").exists()){
                            editTextTextPhone.setText(Objects.requireNonNull(snapshot.child("telefono").getValue()).toString());
                        }

                        if(snapshot.child("fecha_ini_contrato").exists()){
                            txtfecha_ini_contrato.setText(Objects.requireNonNull(snapshot.child("fecha_ini_contrato").getValue()).toString());
                        }
                        if(snapshot.child("fecha_fin_contrato").exists()){
                            txtfecha_fin_contrato.setText(Objects.requireNonNull(snapshot.child("fecha_fin_contrato").getValue()).toString());
                        }

                        if(snapshot.child("solicitudes").exists()){
                            cant_solicitudes.setText(snapshot.child("solicitudes").getChildrenCount()+" Solicitudes");
                        }
                        if(snapshot.child("marcaciones").exists()){
                            cant_marcaciones.setText(snapshot.child("marcaciones").getChildrenCount()+" Marcaciones");
                        }

                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        img_perfil.setOnClickListener(view -> {

            alertDialog.crear_mensaje("Información", "Selecciona una opción", builder -> {

                if(!URL_FOTO.isEmpty()) {

                    builder.setPositiveButton("Ver Foto", (dialogInterface, i) -> {

                        startActivity(new Intent(getContext(), Vi_fotos.class)
                                .putExtra("url", URL_FOTO)
                                .putExtra("titulo", NOMBRE));

                    });
                    builder.setNeutralButton("Subir Foto", (dialogInterface, i) -> {

                    });

                    builder.setCancelable(true);
                    builder.create().show();

                }else{

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

            });

        });


        btn_salir.setOnClickListener(view -> {

            dialog.mostrar_mensaje("Cerrando Sesión...");

            SharedPreferences.Editor editor = MainActivity.preferences.edit();
            editor.putString("uid", "");
            editor.putString("rol", "");
            editor.apply();
            //MainActivity.preferences.edit().clear().apply();
            dialog.ocultar_mensaje();

            startActivity(new Intent(vista.getContext(), MainActivity.class));

        });

        return vista;
    }

    public void update_perfil(Ob_usuario usuario) {

        if(usuario.uid != null) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("email", usuario.email.toLowerCase());
            datos.put("telefono", usuario.telefono);
            Principal.databaseReference.child("usuarios").child(usuario.uid).updateChildren(datos);
        }

    }

    private final ActivityResultLauncher<String> requestPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            getImageFile();
        } else {
            permissionDenied();
        }
    });

    @TargetApi(Build.VERSION_CODES.R)
    private void requestAndroid11StoragePermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse(String.format("package:%s", getContext().getPackageName())));
        android11StoragePermission.launch(intent);
    }

    ActivityResultLauncher<Intent> android11StoragePermission = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (isPermitted()) {
            getImageFile();
        } else {
            permissionDenied();
        }
    });
    private boolean isPermitted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                launchImageCropper(imageUri);
            }
        }
    });

    private void launchImageCropper(Uri uri) {
        CropImageOptions cropImageOptions = new CropImageOptions();
        cropImageOptions.imageSourceIncludeGallery = false;
        cropImageOptions.imageSourceIncludeCamera = true;
        CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(uri, cropImageOptions);
        cropImage.launch(cropImageContractOptions);
    }

    ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), result -> {
        if (result.isSuccessful()) {
            Bitmap cropped = BitmapFactory.decodeFile(result.getUriFilePath(getContext(), true));
            saveCroppedImage(cropped);
        }
    });

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
            getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            // Trigger a media scan to update the gallery
            MediaScannerConnection.scanFile(getContext(), new String[]{file.getAbsolutePath()}, null, null);
            showSuccessMessage();
        } catch (Exception e) {
            showFailureMessage();
        }
    }

    private void showFailureMessage() {
        Toast.makeText(getContext(), "Cropped image not saved something went wrong", Toast.LENGTH_LONG).show();
    }
    private void permissionDenied() {
        Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_LONG).show();
    }
    private void showSuccessMessage() {
        Toast.makeText(getContext(), "Image Saved", Toast.LENGTH_LONG).show();
    }

    private void getImageFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getImage.launch(intent);
    }

}
