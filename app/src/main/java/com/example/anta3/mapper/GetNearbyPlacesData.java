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
import android.widget.LinearLayout;
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
    String markerType ;
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
            markerType = (String) params[2];
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
            final String ref = googlePlace.get("reference");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName);
            markerOptions.snippet(vicinity + " ::: " + ref);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            if (markerType=="restaurants") {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.food40));
            }else if(markerType=="hospitals"){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.doctor40));
            }else if (markerType=="schools"){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.school40));
            }else{
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.game32));
            }

            mMap.addMarker(markerOptions);
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(final Marker marker) {
                    //Log.i("logger",marker.getTitle());
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mapsActivity);
                    final String fullAddress = marker.getTitle();
                    String[] fullAddressArray = markerOptions.getSnippet().split(" : ");
                    final String title = fullAddress ;
                    final String address = fullAddressArray[0];

                    Log.i("data",title);
                    Intent intent = new Intent(mapsActivity.getApplicationContext(), PlaceDetails.class);
                    intent.putExtra("strName", title);
                    intent.putExtra("strAddress",address);
                    intent.putExtra("strLat", marker.getPosition().latitude);
                    intent.putExtra("strLng", marker.getPosition().longitude);
                    intent.putExtra("strRef",marker.getSnippet().substring(marker.getSnippet().indexOf(" ::: ")).replace(" ::: ",""));
                    mapsActivity.startActivity(intent);
                    Toast.makeText(mapsActivity.getApplicationContext(),"Fetching Data for "+title,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
