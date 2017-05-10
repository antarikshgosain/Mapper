package com.example.anta3.mapper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class PlaceDetails extends AppCompatActivity implements View.OnClickListener {
    ImageView btnCall ;
    String website ;
    String cityName = "" ;
    String stateName = "" ;
    String countryName = "" ;
    String rating = null ;
    String phone = "" ;
    String iconUrl = "" ;
    String vicinity = "" ;

    ScrollView parentLayout ;
    ImageView imgPlace;
    TextView txtName;
    TextView txtAddress;
    RatingBar rbRating;
    TextView txtLatitude;
    TextView txtLongitude;
    TextView txtCity ;
    TextView txtProvince;

    TextView txtGetDirections;
    TextView txtPhone;
    JSONArray types;
    ImageView imgBack ;
    ImageView imgWebsite ;
    TextView txtBack ;
    TextView txtWebsite ;

    String url;
    String strTypes;
    String getDirections;
    String apiKey = "AIzaSyAayHbuSoq5PqhLcUS8j2O6P8iM604gvFE";

    String name ;
    String address ;
    Double lat ;
    Double lng ;
    String ref ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        imgPlace = (ImageView) findViewById(R.id.placeImg);
        txtName = (TextView) findViewById(R.id.placeName);
        txtAddress = (TextView) findViewById(R.id.placeAddress);
        rbRating = (RatingBar) findViewById(R.id.placeRating);
        rbRating.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        Drawable drawable = rbRating.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#d6a700"), PorterDuff.Mode.SRC_ATOP);
        txtLatitude = (TextView) findViewById(R.id.placeLat);
        txtLongitude = (TextView) findViewById(R.id.placeLng);
        txtCity = (TextView)findViewById(R.id.placeCity);
        txtPhone = (TextView) findViewById(R.id.placePhone);
        txtProvince = (TextView)findViewById(R.id.placeStateCountry);
        txtGetDirections = (TextView)findViewById(R.id.placeGetDirections);
        parentLayout = (ScrollView)findViewById(R.id.parentLayout);
        btnCall = (ImageView)findViewById(R.id.btn_call);

        rbRating.setStepSize(0.5f);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgWebsite = (ImageView)findViewById(R.id.imgWebsite);
        txtBack = (TextView) findViewById(R.id.txtBack);
        txtWebsite = (TextView)findViewById(R.id.txtWebsite);

        imgBack.setOnClickListener(this);
        imgWebsite.setOnClickListener(this);
        txtBack.setOnClickListener(this);
        txtWebsite.setOnClickListener(this);
        txtGetDirections.setOnClickListener(this);
        btnCall.setOnClickListener(this);

        name = getIntent().getExtras().getString("strName");
        address = getIntent().getExtras().getString("strAddress");
        lat = getIntent().getExtras().getDouble("strLat");
        lng = getIntent().getExtras().getDouble("strLng");
        ref = getIntent().getExtras().getString("strRef");


        url = "https://maps.googleapis.com/maps/api/place/details/json?reference=" + ref + "&key=" + apiKey;
        Log.d("hitUrl", url);


        txtName.setText(name);
        txtName.setTypeface(null, Typeface.BOLD);
        txtName.setTextColor(Color.parseColor("#000000"));
        String addressArray[] = address.split(" ::: ");

        txtAddress.append(addressArray[0]);
        txtAddress.setTextColor(Color.parseColor("#000000"));

        txtLatitude.append(lat.toString());
        txtLatitude.setTextColor(Color.parseColor("#000000"));

        txtLongitude.append(lng.toString());
        txtLongitude.setTextColor(Color.parseColor("#000000"));
        //txtLongitude.append("\n\n"+url);


        new JsonTask().execute(url);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.txtBack:{
                finish();
                break;
            }
            case R.id.imgBack:{
                finish();
                break;
            }
            case R.id.imgWebsite:{
                final String newWebsite = website ;
                Intent intent = new Intent(PlaceDetails.this,WebViewDemo.class);
                intent.putExtra("url",newWebsite);
                startActivity(intent);
                break;
            }
            case R.id.txtWebsite:{
                final String newWebsite = website ;
                Intent intent = new Intent(PlaceDetails.this,WebViewDemo.class);
                intent.putExtra("url",newWebsite);
                startActivity(intent);
                break;
            }
            case R.id.placeGetDirections:{
                String uri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + name + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
                break;
            }
            case R.id.btn_call:{
                if(phone=="" || phone==null){
                    Toast.makeText(getApplicationContext(),"Phone Number Unavailable",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+phone)); startActivity(intent);
                }
                break;
            }
        }
    }

    class JsonTask extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(PlaceDetails.this, "Loading", "Please wait...");
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.d("Response: ", "> " + line);

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "abc";
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            //txtPhone.setText(result);

            try {
                JSONObject parentJson = new JSONObject(result);
                //Log.i("resultjson",result);
                JSONArray addressComponentArray = parentJson.getJSONObject("result").getJSONArray("address_components");
                cityName = addressComponentArray.getJSONObject(0).getString("long_name");
                stateName = addressComponentArray.getJSONObject(1).getString("long_name");
                countryName = addressComponentArray.getJSONObject(2).getString("long_name");
                rating = parentJson.getJSONObject("result").getString("rating");
                phone = parentJson.getJSONObject("result").getString("international_phone_number");
                iconUrl = parentJson.getJSONObject("result").getString("icon");
                vicinity = parentJson.getJSONObject("result").getString("vicinity");
                types = parentJson.getJSONObject("result").getJSONArray("types");
                website = parentJson.getJSONObject("result").getString("website");
                getDirections = parentJson.getJSONObject("result").getString("url");
                Log.i("antalog",addressComponentArray.toString());
                Log.i("antalog",cityName);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(types!=null){
                strTypes = types.toString();
                Log.i("types",strTypes);

                if(strTypes.contains("hospital")){
                    parentLayout.setBackgroundResource(R.drawable.back_hospital);
                }else if(strTypes.contains("school")){
                    parentLayout.setBackgroundResource(R.drawable.back_school);
                }else if(strTypes.contains("restaurant")){
                    parentLayout.setBackgroundResource(R.drawable.back_rest);
                }else{
                    parentLayout.setBackgroundResource(R.drawable.back);
                }
            }else {
                parentLayout.setBackgroundResource(R.drawable.back);
            }

            txtCity.append(cityName);
            txtCity.setTextColor(Color.parseColor("#000000"));

            txtProvince.append(stateName);
            txtProvince.setTextColor(Color.parseColor("#000000"));

            rating = (rating!=null) ? rating : "0";
            rbRating.setRating(Float.parseFloat(rating));

            txtPhone.append(phone);
            txtPhone.setTextColor(Color.parseColor("#000000"));

            txtAddress.append(vicinity);
            txtAddress.setTextColor(Color.parseColor("#000000"));

            new DownloadImageTask((ImageView) findViewById(R.id.placeImg))
                    .execute(iconUrl);

        }
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageDrawable(null);
            bmImage.setImageBitmap(result);
        }
    }

}

