package com.example.anta3.mapper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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

public class PlaceDetails extends AppCompatActivity implements View.OnClickListener {
    String website ;
    ImageView imgPlace;
    TextView txtName;
    TextView txtAddress;
    RatingBar rbRating;
    TextView txtLatitude;
    TextView txtLongitude;
    TextView txtCity ;
    TextView txtStateCountry ;

    TextView txtPhone;

    ImageView imgBack ;
    ImageView imgWebsite ;
    TextView txtBack ;
    TextView txtWebsite ;

    String url;
    String apiKey = "AIzaSyAayHbuSoq5PqhLcUS8j2O6P8iM604gvFE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        imgPlace = (ImageView) findViewById(R.id.placeImg);
        txtName = (TextView) findViewById(R.id.placeName);
        txtAddress = (TextView) findViewById(R.id.placeAddress);
        rbRating = (RatingBar) findViewById(R.id.placeRating);
        txtLatitude = (TextView) findViewById(R.id.placeLat);
        txtLongitude = (TextView) findViewById(R.id.placeLng);
        txtCity = (TextView)findViewById(R.id.placeCity);
        txtPhone = (TextView) findViewById(R.id.placePhone);
        txtStateCountry = (TextView)findViewById(R.id.placeStateCountry);

        rbRating.setStepSize(0.1f);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgWebsite = (ImageView)findViewById(R.id.imgWebsite);
        txtBack = (TextView) findViewById(R.id.txtBack);
        txtWebsite = (TextView)findViewById(R.id.txtWebsite);
        imgBack.setOnClickListener(this);
        imgWebsite.setOnClickListener(this);
        txtBack.setOnClickListener(this);
        txtWebsite.setOnClickListener(this);

        String name = getIntent().getExtras().getString("strName");
        String address = getIntent().getExtras().getString("strAddress");
        Double lat = getIntent().getExtras().getDouble("strLat");
        Double lng = getIntent().getExtras().getDouble("strLng");
        String ref = getIntent().getExtras().getString("strRef");

        url = "https://maps.googleapis.com/maps/api/place/details/json?reference=" + ref + "&key=" + apiKey;
        Log.d("hitUrl", url);
        txtName.setText(name);
        String addressArray[] = address.split(" ::: ");
        txtAddress.setText("Address : " + addressArray[0]);
        txtLatitude.setText("Latitude   : " + lat.toString());
        txtLongitude.setText("Longitude : " + lng.toString());
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
        }
    }

    class JsonTask extends AsyncTask<String, String, String> {

        PlaceDetails placeDetails = new PlaceDetails();
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
            String cityName = "" ;
            String stateName = "" ;
            String countryName = "" ;
            String rating = null ;
            String phone = "" ;
            String iconUrl = "" ;
            String vicinity = "" ;
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
                website = parentJson.getJSONObject("result").getString("website");
                Log.i("antalog",addressComponentArray.toString());
                Log.i("antalog",cityName);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            txtCity.setText("City : "+cityName);
            txtStateCountry.setText("State/Country : "+stateName+"/"+countryName);
            rating = (rating!=null) ? rating : "0";
            rbRating.setRating(Float.parseFloat(rating));
            txtPhone.setText("Phone Number : "+phone);
            txtAddress.setText("Address : "+vicinity);

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
            bmImage.setImageBitmap(result);
        }
    }

}

