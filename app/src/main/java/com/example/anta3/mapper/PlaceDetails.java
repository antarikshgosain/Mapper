package com.example.anta3.mapper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class PlaceDetails extends AppCompatActivity {
    ImageView placeImg ;
    TextView txtName ;
    TextView txtAddress ;
    RatingBar rbRating ;
    TextView txtLatitude ;
    TextView txtLongitude ;
    Button btnBack;
    TextView txtJson;

    String apiKey = "AIzaSyAayHbuSoq5PqhLcUS8j2O6P8iM604gvFE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        placeImg = (ImageView)findViewById(R.id.placeImg);
        txtName = (TextView)findViewById(R.id.placeName);
        txtAddress = (TextView)findViewById(R.id.placeAddress);
        rbRating = (RatingBar)findViewById(R.id.placeRating);
        txtLatitude = (TextView)findViewById(R.id.placeLat);
        txtLongitude = (TextView)findViewById(R.id.placeLng);
        btnBack = (Button)findViewById(R.id.btn_back);
        txtJson = (TextView)findViewById(R.id.placeJson);

        String name = getIntent().getExtras().getString("strName");
        String address = getIntent().getExtras().getString("strAddress");
        Double lat = getIntent().getExtras().getDouble("strLat");
        Double lng = getIntent().getExtras().getDouble("strLng");
        String ref = getIntent().getExtras().getString("strRef");

        String url = "https://maps.googleapis.com/maps/api/place/details/json?reference="+ref+"&key="+apiKey;
        Log.d("hitUrl",url);
        txtName.setText(name);
        txtAddress.setText("Address : "+address);
        txtLatitude.setText("Latitude   : "+lat.toString());
        txtLongitude.setText("Longitude : "+lng.toString());
        //txtLongitude.append("\n\n"+url);

        new JsonTask().execute(url);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}

class JsonTask extends AsyncTask<String, String, String> {

    //ProgressDialog pd;
    MapsActivity maps = new MapsActivity();
    PlaceDetails pd = new PlaceDetails();

    protected void onPreExecute() {
        super.onPreExecute();

//        pd = new ProgressDialog(maps);
//        pd.setMessage("Please wait");
//        pd.setCancelable(false);
//        pd.show();
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
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

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
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
//        if (pd.isShowing()){
//            pd.dismiss();
//        }
        //txtJson.setText(result);
        Log.i("myjson",result);
        pd.txtJson.setText("result");
    }

}
