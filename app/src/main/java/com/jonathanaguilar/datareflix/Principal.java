package com.jonathanaguilar.datareflix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jonathanaguilar.datareflix.databinding.ActivityPrincipalBinding;

import java.util.Objects;

public class Principal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static String id = MainActivity.preferences.getString("uid","");
    public static String rol = MainActivity.preferences.getString("rol","");
    public static DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!id.isEmpty()) {

            ActivityPrincipalBinding binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            databaseReference = MainActivity.DB.getReference();

            setSupportActionBar(binding.appBarPrincipal.toolbar);
            binding.appBarPrincipal.fab.setOnClickListener(view -> Snackbar.make(view, "Mi Accion", Snackbar.LENGTH_LONG)
                    .setAction("Accion", null).show());
            DrawerLayout drawer = binding.drawerLayout;
            NavigationView navigationView = binding.navView;

            View headerView = navigationView.getHeaderView(0);
            TextView headerTextView = headerView.findViewById(R.id.header_username);
            ImageView headerImageView = headerView.findViewById(R.id.header_imagen);

            databaseReference.child("usuarios").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {

                        headerTextView.setText(Objects.requireNonNull(snapshot.child("nombre").getValue()).toString());

                        if (snapshot.child("url_foto").exists()) {
                            String foto = Objects.requireNonNull(snapshot.child("url_foto").getValue()).toString();
                            Glide.with(getBaseContext()).load(foto).centerCrop().into(headerImageView);
                        } else {
                            headerImageView.setImageResource(R.drawable.perfil);
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_schedules, R.id.nav_profile)
                    .setOpenableLayout(drawer)
                    .build();

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_principal);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);

        }else{
            MainActivity.preferences.edit().clear().apply();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_principal);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}