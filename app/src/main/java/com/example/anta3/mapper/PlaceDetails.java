package com.example.anta3.mapper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PlaceDetails extends AppCompatActivity {
    ImageView placeImg ;
    TextView txtName ;
    TextView txtAddress ;
    RatingBar rating ;
    TextView latitude ;
    TextView longitude ;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        placeImg = (ImageView)findViewById(R.id.placeImg);
        txtName = (TextView)findViewById(R.id.placeName);
        txtAddress = (TextView)findViewById(R.id.placeAddress);
        rating = (RatingBar)findViewById(R.id.placeRating);
        latitude = (TextView)findViewById(R.id.placeLat);
        longitude = (TextView)findViewById(R.id.placeLng);
        btnBack = (Button)findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
