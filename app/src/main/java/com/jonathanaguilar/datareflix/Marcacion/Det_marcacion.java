package com.jonathanaguilar.datareflix.Marcacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jonathanaguilar.datareflix.Controllers.Alert_dialog;
import com.jonathanaguilar.datareflix.Controllers.Progress_dialog;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;

import java.util.Objects;

public class Det_marcacion extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    TextView fecha_hora_marcacion, txt_nombre, tipo_marcacion, estado_marcacion, fecha_hora_ingreso, fecha_hora_salida;
    Button btn_del_marcacion;
    String uid = "", uid_empleado = "", empleado = "", fecha_hora;
    Alert_dialog alertDialog;
    Progress_dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_marcacion);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        txt_nombre = findViewById(R.id.txt_nombre);
        tipo_marcacion = findViewById(R.id.tipo_marcacion);
        estado_marcacion = findViewById(R.id.estado_marcacion);
        fecha_hora_marcacion = findViewById(R.id.fecha_hora_marcacion);
        btn_del_marcacion = findViewById(R.id.btn_del_marcacion);

        fecha_hora_ingreso = findViewById(R.id.fecha_hora_ingreso);
        fecha_hora_salida = findViewById(R.id.fecha_hora_salida);

        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        uid = Objects.requireNonNull(getIntent().getExtras()).getString("uid","");
        uid_empleado = Objects.requireNonNull(getIntent().getExtras()).getString("uid_empleado","");
        empleado = Objects.requireNonNull(getIntent().getExtras()).getString("empleado","");
        fecha_hora = Objects.requireNonNull(getIntent().getExtras()).getString("fecha_hora","");

        assert uid!=null;
        if(!uid.isEmpty() && ! uid_empleado.isEmpty()) {

            initMap();

            txt_nombre.setText(empleado);
            fecha_hora_marcacion.setText(fecha_hora);

            if(Principal.rol.equals("Administrador")){
                btn_del_marcacion.setVisibility(View.VISIBLE);
            }else{
                btn_del_marcacion.setVisibility(View.GONE);
            }

            Principal.databaseReference.child("horarios").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()) {

                        for (DataSnapshot datos : snapshot.getChildren()) {

                            if (datos.child("fecha").exists() && datos.child("hora_inicio").exists() && datos.child("hora_fin").exists()) {

                                if(Objects.requireNonNull(datos.child("fecha").getValue()).toString().equalsIgnoreCase(fecha_hora.split(" ")[0])){

                                    if(datos.child("hora_inicio").exists()) {
                                        String h_inicio = Objects.requireNonNull(datos.child("hora_inicio").getValue()).toString();
                                        fecha_hora_ingreso.setText(fecha_hora.split(" ")[0] +" - "+h_inicio);
                                    }
                                    if(datos.child("hora_fin").exists()) {
                                        String h_fin = Objects.requireNonNull(datos.child("hora_fin").getValue()).toString();
                                        fecha_hora_salida.setText(fecha_hora.split(" ")[0] +" - "+h_fin);
                                    }
                                }
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            btn_del_marcacion.setOnClickListener(view -> {

                alertDialog.crear_mensaje("¿Estás Seguro de Eliminar la marcación?", "¡Esta acción no es reversible!", builder -> {
                    builder.setPositiveButton("Aceptar", (dialogInterface, i) -> {
                        dialog.mostrar_mensaje("Eliminando Marcación...");
                        Principal.databaseReference.child("usuarios").child(uid_empleado).child("marcaciones").child(uid).removeValue();
                        dialog.ocultar_mensaje();
                        finish();
                    });
                    builder.setNeutralButton("Cancelar", (dialogInterface, i) -> {});
                    builder.setCancelable(false);
                    builder.create().show();
                });

            });

        }

    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        Principal.databaseReference.child("usuarios").child(uid_empleado).child("marcaciones").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    mMap.clear();

                    MarkerOptions ubi = new MarkerOptions();

                    if(snapshot.child("latitud").exists() && snapshot.child("longitud").exists() ) {

                        double latitud = Double.parseDouble(Objects.requireNonNull(snapshot.child("latitud").getValue()).toString());
                        double longitud = Double.parseDouble(Objects.requireNonNull(snapshot.child("longitud").getValue()).toString());

                        ubi.position(new LatLng(latitud,longitud)).title("Ubicación de Marcación").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).draggable(false);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud,longitud),15f));
                        Objects.requireNonNull(mMap.addMarker(ubi)).showInfoWindow();
                    }

                    if(snapshot.child("tipo").exists()){
                        tipo_marcacion.setText(Objects.requireNonNull(snapshot.child("tipo").getValue()).toString());
                    }
                    if(snapshot.child("estado").exists()){
                        estado_marcacion.setText(Objects.requireNonNull(snapshot.child("estado").getValue()).toString());
                        switch (estado_marcacion.getText().toString().toLowerCase()){
                            case "asistencia":
                            case "salida":
                                estado_marcacion.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.success));
                                break;
                            case "atraso":
                            case "salida temprano":
                                estado_marcacion.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.danger));
                                break;
                            case "horas extras":
                                estado_marcacion.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.warning));
                                break;
                            default:
                                estado_marcacion.setTextColor(ContextCompat.getColor(getApplicationContext() ,R.color.black));
                                break;
                        }

                    }

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}