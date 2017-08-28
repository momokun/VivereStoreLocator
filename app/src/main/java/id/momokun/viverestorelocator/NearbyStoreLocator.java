package id.momokun.viverestorelocator;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ElmoTan on 8/23/2017.
 */

public class NearbyStoreLocator extends AsyncTask<Object, String, String>{

    String placeData;
    GoogleMap vMap;
    String url;

    @Override
    protected String doInBackground(Object... params) {
        try {
            vMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownloadStream data = new DownloadStream();
            placeData = data.readUrl(url);
        }catch (Exception e){
            Log.d("StoreLocator",e.toString());
        }

        Log.v("StoreLocator",placeData);
        return placeData;
    }

    @Override
    protected void onPostExecute(String result){
        List<HashMap<String, String>> storeList = null;
        StoreListParser slp = new StoreListParser();
        storeList = StoreListParser.parse(result);
        showStore(storeList);
    }

    private void showStore(List<HashMap<String, String>> nearbyPlacesList) {
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute","Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            vMap.addMarker(markerOptions);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            //move map camera
            vMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            vMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
    }
}
