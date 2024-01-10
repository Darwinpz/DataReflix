package com.jonathanaguilar.datareflix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.Objects;

public class Vi_fotos extends AppCompatActivity {

    PhotoView photoView;
    String link, titulo;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vi_fotos);

        photoView = (PhotoView) findViewById(R.id.imagen);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        link = Objects.requireNonNull(getIntent().getExtras()).getString("url");
        titulo = getIntent().getExtras().getString("titulo");

        toolbar.setTitle(titulo);
        Glide.with(this).load(link).into(photoView);

        toolbar.setOnClickListener(view -> finish());

    }
}