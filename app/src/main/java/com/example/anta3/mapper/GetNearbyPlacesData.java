package com.example.anta3.mapper;

/**
 * Created by anta3 on 5/4/2017.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by anta3 on 5/4/2017.
 */

public class GetNearbyPlacesData extends AsyncTask<Object, String, String>{

    String googlePlacesData;
    GoogleMap mMap;
    String url;
    MapsActivity mapsActivity;

    public GetNearbyPlacesData(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);

        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute","Entered into showing locations");
            final MarkerOptions markerOptions = new MarkerOptions();
            final HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            final String placeName = googlePlace.get("place_name");
            final String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            mMap.addMarker(markerOptions);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(final Marker marker) {
                    //Log.i("logger",marker.getTitle());
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mapsActivity);
                    String fullAddress = marker.getTitle();
                    String[] fullAddressArray = fullAddress.split(" : ");
                    String title = fullAddressArray[0] ;
                    Log.i("data",title);
                    alertDialogBuilder.setTitle("Name : "+title.replace(" : ",""))
                            .setMessage("Address : "+vicinity
                            +"\nLatitude : "+marker.getPosition().latitude
                            +"\nLongitude : "+marker.getPosition().longitude)
                            .setPositiveButton("Ok",null)
                            .setNegativeButton("Details", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(mapsActivity.getApplicationContext(), PlaceDetails.class);
                                    intent.putExtra("strName", marker.getTitle());
                                    intent.putExtra("strLatitude", marker.getPosition().latitude);
                                    intent.putExtra("strLongitude", marker.getPosition().longitude);
                                    mapsActivity.startActivity(intent);
                                    //Toast.makeText(mapsActivity.getApplicationContext(),"Loading...",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                    return false;
                }
            });
//            mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);

        }
    }

}
