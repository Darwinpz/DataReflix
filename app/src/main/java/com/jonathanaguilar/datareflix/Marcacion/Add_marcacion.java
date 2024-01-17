package com.jonathanaguilar.datareflix.Marcacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jonathanaguilar.datareflix.Controllers.Alert_dialog;
import com.jonathanaguilar.datareflix.Controllers.Progress_dialog;
import com.jonathanaguilar.datareflix.MainActivity;
import com.jonathanaguilar.datareflix.Objetos.Ob_marcacion;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executor;

public class Add_marcacion extends AppCompatActivity implements OnMapReadyCallback {

    private static Double LATITUD, LONGITUD;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    Boolean mLocationPermissionsGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    FusedLocationProviderClient fusedLocationProviderClient;
    GoogleMap mMap;
    Alert_dialog alertDialog;
    Progress_dialog dialog;
    Button btn_marcar_manual;
    Button btn_marcar_huella;

    String uid_biometric = MainActivity.preferences.getString("uid_biometric","");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marcacion);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        btn_marcar_manual = findViewById(R.id.btn_marcar_manual);
        btn_marcar_huella = findViewById(R.id.btn_marcar_huella);

        btn_marcar_manual.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        btn_marcar_huella.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        if(!uid_biometric.isEmpty()){

            if(Principal.id.equals(uid_biometric)){
                btn_marcar_huella.setVisibility(View.VISIBLE);
            }else{
                btn_marcar_huella.setVisibility(View.GONE);
            }

        }


        getLocationPermission();

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (mLocationPermissionsGranted) {

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            getDeviceLocation();
            init();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        mLocationPermissionsGranted = true;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {

                    for (int result: grantResults) {
                        if(result != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            break;
                        }
                    }

                    if(mLocationPermissionsGranted){

                        activarGPS();

                        if(mLocationPermissionsGranted){

                            initMap();
                        }

                    }else{

                        alertDialog.crear_mensaje("Advertencia", "Debes ACTIVAR el Permiso de Ubicación", builder -> {
                            builder.setNeutralButton("Cambiar Permisos de Ubicación", (dialogInterface, i) -> {
                                finish();
                                startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+getPackageName())));
                            });
                            builder.setCancelable(false);
                            builder.create().show();
                        });

                    }

                }

            } break;

        }
    }

    private void init(){
        mMap.clear();
        getDeviceLocation();
    }

    private void initMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }


    private void moveCamera(LatLng latLng, float zoom, String title) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions options = new MarkerOptions().position(latLng).title(title).draggable(false);
        Objects.requireNonNull(mMap.addMarker(options)).showInfoWindow();

    }


    //Activar permisos de locación
    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                activarGPS();

                if(mLocationPermissionsGranted){
                    initMap();
                }

            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    //Activación del GPS
    private void activarGPS(){

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )) {

            mLocationPermissionsGranted = false;

            alertDialog.crear_mensaje("Advertencia", "¡El GPS está DESACTIVADO!", builder -> {
                builder.setPositiveButton("Activar GPS", (dialogInterface, i) -> {
                    finish();
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                });
                builder.setNegativeButton("Cancelar", (dialogInterface, i) -> {});
                builder.setCancelable(false);
                builder.create().show();
            });

        }else{
            mLocationPermissionsGranted = true;

        }

    }


    //Obtener ubicacion del dispositivo
    private void getDeviceLocation() {
        try {

            if (mLocationPermissionsGranted) {

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Location currLocation = (Location) task.getResult();
                        LATITUD = currLocation.getLatitude();
                        LONGITUD = currLocation.getLongitude();

                        moveCamera(new LatLng(currLocation.getLatitude(), currLocation.getLongitude()), DEFAULT_ZOOM,"Mi Ubicación");

                        btn_marcar_manual.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.success)));
                        btn_marcar_huella.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.proyecto_light)));

                        btn_marcar_manual.setOnClickListener(view -> {

                            dialog.mostrar_mensaje("Guardando Marcación...");

                            Ob_marcacion marcacion = new Ob_marcacion();
                            marcacion.fecha_hora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                            marcacion.latitud = LATITUD;
                            marcacion.longitud =  LONGITUD;

                            Ver_marcaciones.ctlMarcacion.crear_marcacion(Principal.id,marcacion);

                            dialog.ocultar_mensaje();
                            alertDialog.crear_mensaje("Correcto", "Marcación Creada Correctamente", builder -> {
                                builder.setCancelable(false);
                                builder.setNeutralButton("Aceptar", (dialogInterface, i) -> finish());
                                builder.create().show();
                            });

                        });

                        btn_marcar_huella.setOnClickListener(view -> {

                            Executor executor = ContextCompat.getMainExecutor(this);

                            BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
                                @Override
                                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                                    super.onAuthenticationError(errorCode, errString);
                                    Toast.makeText(getApplicationContext(), "ERROR "+errString,Toast.LENGTH_SHORT).show();

                                    alertDialog.crear_mensaje("No está Configurado el Biométrico", "Configura y vuelve a Intentar", builder -> {
                                        builder.setCancelable(false);
                                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {

                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                                                Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                                                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL);
                                                startActivity(enrollIntent);
                                            }

                                        });
                                        builder.create().show();
                                    });

                                }

                                @Override
                                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                                    super.onAuthenticationSucceeded(result);

                                    if(!uid_biometric.isEmpty()) {

                                        dialog.mostrar_mensaje("Guardando Marcación...");

                                        Ob_marcacion marcacion = new Ob_marcacion();
                                        marcacion.fecha_hora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                                        marcacion.latitud = LATITUD;
                                        marcacion.longitud =  LONGITUD;

                                        Ver_marcaciones.ctlMarcacion.crear_marcacion(Principal.id,marcacion);

                                        dialog.ocultar_mensaje();
                                        alertDialog.crear_mensaje("Correcto", "Marcación Creada Correctamente", builder -> {
                                            builder.setCancelable(false);
                                            builder.setNeutralButton("Aceptar", (dialogInterface, i) -> finish());
                                            builder.create().show();
                                        });

                                    }else{
                                        Toast.makeText(getApplicationContext(),"No hay Registro Biometrico guardado",Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onAuthenticationFailed() {
                                    super.onAuthenticationFailed();
                                    Toast.makeText(getApplicationContext(), "Error al Autenticar",Toast.LENGTH_SHORT).show();
                                }
                            });


                            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                                    .setTitle("Verificación DataReflix")
                                    .setSubtitle("Ingresa tu huella para registrar la marcación")
                                    .setNegativeButtonText("Cancelar")
                                    .setConfirmationRequired(false)
                                    .build();

                            biometricPrompt.authenticate(promptInfo);


                        });

                    }

                });

            }

        } catch (SecurityException e) {
            alertDialog.crear_mensaje("Error al Obtener la Ubicación", Objects.requireNonNull(e.getLocalizedMessage()), builder -> {
                builder.setCancelable(false);
                builder.setNeutralButton("Aceptar", (dialogInterface, i) -> finish());
                builder.create().show();
            });
        }

    }
}