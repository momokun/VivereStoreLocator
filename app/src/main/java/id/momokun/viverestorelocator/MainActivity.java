package id.momokun.viverestorelocator;

import android.Manifest;
import android.app.AlertDialog;

import android.content.DialogInterface;

import android.content.pm.PackageManager;

import android.location.Location;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import java.util.List;


import id.momokun.viverestorelocator.database.StoreData;
import id.momokun.viverestorelocator.database.StoreDatabaseHandler;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMyLocationButtonClickListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private GoogleMap vMap;
    private LocationRequest vLocRequest;
    private Location vLastLoc;
    private double cLat, cLong;
    private LatLng currPos;

    private FusedLocationProviderClient vFusedLocClient;

    protected GeoDataClient mGeoDataClient;
    protected PlaceDetectionClient mPlaceDetectionClient;

    private StoreDatabaseHandler sdb;

    private GoogleApiClient mGoogleApiClient;

    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> addr = new ArrayList<>();
    private ArrayList<String> city = new ArrayList<>();
    private ArrayList<String> postalcode = new ArrayList<>();
    private ArrayList<String> phnum = new ArrayList<>();
    private ArrayList<String> workday = new ArrayList<>();
    private ArrayList<String> worktime = new ArrayList<>();
    private ArrayList<Double> lat = new ArrayList<>();
    private ArrayList<Double> lng = new ArrayList<>();
    private ArrayList<String> img = new ArrayList<>();
    private ListView listStoreView;

    protected synchronized void GoogleAPIClientStarter() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sdb = new StoreDatabaseHandler(this);
        listStoreView = findViewById(R.id.list);

        SupportMapFragment vMapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        vMapFrag.getMapAsync(this);
        vFusedLocClient = LocationServices.getFusedLocationProviderClient(this);


        //Google Place API
        mGeoDataClient = Places.getGeoDataClient(this, null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);


        //if (sdb.getStoreCount() <= 0 && sdb.getPromoCount() <= 0) {
            populateListView();
        //} else {
        //    populateListView();
        //}

        vLocRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10)
                .setFastestInterval(1);





    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        vMap = gMap;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkLocationUsagePermission();
        } else {
            if (mGoogleApiClient == null) {
                GoogleAPIClientStarter();
            }
            vMap.setOnMyLocationButtonClickListener(this);
            vMap.setMyLocationEnabled(true);
        }



    }


    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkLocationUsagePermission();
        } else {
            Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (loc != null) {
                getNearestStore(loc.getLatitude(),loc.getLongitude());
            } else {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, vLocRequest, this);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            GoogleAPIClientStarter();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }


    private void getLastLocationDevice() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkLocationUsagePermission();
        }else {
            vFusedLocClient.getLastLocation()
                    .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                vLastLoc = task.getResult();
                                cLat = vLastLoc.getLatitude();
                                cLong = vLastLoc.getLongitude();
                                currPos = new LatLng(cLat, cLong);
                                getNearestStore(cLat,cLong);
                            } else {
                                Toast.makeText(MainActivity.this, "Location Error, Turn On Location", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        getLastLocationDevice();

        //google-place-api
        /*String url = getUrl(cLat, cLong, "upnormal");
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = vMap;
        DataTransfer[1] = url;
        NearbyStoreLocator getStore = new NearbyStoreLocator();
        getStore.execute(DataTransfer);*/
        return true;

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //google-place-api
    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + 15000);
        googlePlacesUrl.append("&keyword=" + "upnormal");
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyBsG3sQCSqiXzOysO-LML6SLUIkpSEgd2E");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }




    private void populateListView(){
        List<StoreData> sd = sdb.getAllStoreData();
        for(StoreData spd : sd){
            Log.v("StoreName",spd.get_name());
            name.add(spd.get_name());
            addr.add(spd.get_address());
            city.add(spd.get_city());
            postalcode.add(spd.get_postal_code());
            phnum.add(spd.get_phone_num());
            workday.add(spd.get_work_day());
            worktime.add(spd.get_work_time());
            lat.add(Double.valueOf(spd.get_store_latitude()));
            lng.add(Double.valueOf(spd.get_store_longitude()));
            img.add(spd.get_store_image());
        }

        CustomListAdapter customListAdapter = new CustomListAdapter(sdb, this, name, addr, city, postalcode, phnum, workday, worktime, img);
        listStoreView.setAdapter(customListAdapter);

        listStoreView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LatLng pos = new LatLng(lat.get(i),lng.get(i));
                Marker target = vMap.addMarker(new MarkerOptions().position(pos).title(name.get(i)));
                vMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos,15));
                target.showInfoWindow();
            }
        });
    }

    private void getNearestStore(double lat, double lng){
        ArrayList<Float> range= new ArrayList<>();

        float area = 10;
        int count = 1;
        float distanceRange;
        float[] distance = new float[1];

        double storeLat, storeLong;
        List<StoreData> p = sdb.getAllStoreData();
        for(StoreData spd : p){
            storeLat = Double.parseDouble(spd.get_store_latitude());
            storeLong = Double.parseDouble(spd.get_store_longitude());

            Location.distanceBetween(storeLat,storeLong,lat,lng,distance);
            distanceRange = distance[0]/1000;

            if(distanceRange < area) {
                count++;
                if(count>1){
                    area=7;
                    if(distanceRange < area){
                        LatLng pos = new LatLng(storeLat,storeLong);
                        Marker target = vMap.addMarker(new MarkerOptions().position(pos).title("Nearest store: "+spd.get_name()));
                        vMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos,15));
                        target.showInfoWindow();
                    }
                }

            }


        }
    }


    //PermissionCheck

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationUsagePermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            new AlertDialog.Builder(this)
                .setTitle("Location Permission Needed")
                .setMessage("This app needs the location permission for check nearest store location. Please Allow for application functionality")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                    }
                })
                .create()
                .show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int code, @NonNull String permission[], @NonNull int[] grantResult){
        switch(code) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if(grantResult.length>0 && grantResult[0] == PackageManager.PERMISSION_GRANTED){

                }else {
                    new AlertDialog.Builder(this)
                            .setTitle("Location Permission Needed")
                            .setMessage("This app needs the location permission for check nearest store location. Please Allow for application functionality")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Prompt the user once explanation has been shown
                                    checkLocationUsagePermission();
                                }
                            })
                            .create()
                            .show();
                }
            }
        }
    }
}
