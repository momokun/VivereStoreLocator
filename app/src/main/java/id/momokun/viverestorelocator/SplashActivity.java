package id.momokun.viverestorelocator;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import id.momokun.viverestorelocator.database.StoreData;
import id.momokun.viverestorelocator.database.StoreDatabaseHandler;
import id.momokun.viverestorelocator.database.StorePromoData;

public class SplashActivity extends AppCompatActivity {

    private StoreDatabaseHandler sdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        sdb = new StoreDatabaseHandler(this);

        if (sdb.getStoreCount() <= 0 && sdb.getPromoCount() <= 0) {
            dataUp();
        }

        boolean x = BuildConfig.BUILD_TYPE.equals("debug");
        Toast.makeText(this, String.valueOf(x), Toast.LENGTH_SHORT).show();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkLocationUsagePermission();
        }else {

            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, 5000);
        }
    }

    //PermissionCheck
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationUsagePermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);


        }
    }


    @Override
    public void onRequestPermissionsResult(int code, String permission[], int[] grantResult){
        switch(code) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if(grantResult.length>0 && grantResult[0] == PackageManager.PERMISSION_GRANTED){
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
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
                return ;
            }
        }
    }



    private void dataUp(){
        StorePromoData promo1 = new StorePromoData("Special Independence Promo",
                "Special Independence Promo",
                "promo1",
                "15 Agustus 2017 - 25 Agustus 2017");

        StorePromoData promo2 = new StorePromoData("Kitchen & Wardrobe Set Limited Offer",
                "Kitchen & Wardrobe Set Limited Offer",
                "promo2",
                "15 Agustus 2017 - 25 Agustus 2017");

        StorePromoData promo3 = new StorePromoData("Vivere Anniversary Superb Treat",
                "Special Price on Accesories",
                "promo3",
                "15 Agustus 2017 - 25 Oktober 2017");

        StorePromoData promo4 = new StorePromoData("Vivere Home Furnishing Package",
                "The most valuable offer for your home furnishing",
                "promo4",
                "15 Agustus 2017 - 25 Oktober 2017");

        long id_promo1 = sdb.addPromoData(promo1);
        long id_promo2 = sdb.addPromoData(promo2);
        long id_promo3 = sdb.addPromoData(promo3);
        long id_promo4 = sdb.addPromoData(promo4);

        StoreData store1 = new StoreData("Vivere Central Park",
                "Level 2, Unit #130,210,211",
                "West Jakarta",
                "11120",
                "021-5698-5353",
                "Monday - Sunday",
                "10.00 am - 10.00 pm",
                "-6.175861",
                "106.791528",
                "image1");

        StoreData store2 = new StoreData("Vivere Mall Kelapa Gading 3",
                "Ground Floor, Unit G#03",
                "North Jakarta",
                "14350",
                "021-4585-3908",
                "Monday - Sunday",
                "10.00 am - 10.00 pm",
                "-6.157350",
                "106.908690",
                "image2");

        StoreData store3 = new StoreData("Vivere Pondok Indah Mall 2",
                "Level 2, Unit #230",
                "South Jakarta",
                "12310",
                "021-7592-0925",
                "Monday - Sunday",
                "10.00 am - 10.00 pm",
                "-6.267460",
                "106.784500",
                "image3");

        StoreData store4 = new StoreData("Lippo Mall Kemang",
                "Level 2, Kav. 36",
                "South Jakarta",
                "12730",
                "021-2905-6889",
                "Monday - Sunday",
                "10.00 am - 10.00 pm",
                "-6.26121",
                "106.81265",
                "image4");

        StoreData store5 = new StoreData("Sumarecon Mall Serpong",
                "Ground Floor #08",
                "Tangerang, Banten",
                "12730",
                "021-2931-0517",
                "Monday - Sunday",
                "10.00 am - 10.00 pm",
                "-6.2407",
                "106.62835",
                "image5");

        StoreData store6 = new StoreData("Goodrich Building",
                "Pakuwon Square Ak 1\n" +
                        "Jl. Mayjen Yono Soewoyo No. 35-36",
                "Surabaya",
                "60217",
                "031-994-212-49",
                "Monday - Friday, Saturday - Sunday",
                "10.00 am - 07.00 pm, 10.00 am - 08.00 pm",
                "-7.25747",
                "112.75209",
                "image6");

        StoreData store7 = new StoreData("Grand City Mall Surabaya",
                "1st Level - 1.24B",
                "Surabaya",
                "60217",
                "031-524-059-08",
                "Monday - Sunday",
                "10.00 am - 22.30 pm",
                "-7.262213",
                "112.7494",
                "image7");

        StoreData store8 = new StoreData("Seminyak Bali",
                "Jl. Kayu Aya No. 6",
                "Denpasar",
                "60217",
                "0361-734-785",
                "Monday - Sunday",
                "09.00 am - 09.00 pm",
                "-8.68418",
                "115.15957",
                "image8");

        long id_store1 = sdb.addStoreData(store1, new long[] { id_promo1 });
        long id_store2 = sdb.addStoreData(store2, new long[] { id_promo1 });
        long id_store3 = sdb.addStoreData(store3, new long[] { id_promo1 });
        long id_store4 = sdb.addStoreData(store4, new long[] { id_promo1 });
        long id_store5 = sdb.addStoreData(store5, new long[] { id_promo1 });
        long id_store6 = sdb.addStoreData(store6, new long[] { id_promo1 });
        long id_store7 = sdb.addStoreData(store7, new long[] { id_promo1 });
        long id_store8 = sdb.addStoreData(store8, new long[] { id_promo1 });

        sdb.assignPromoToStore(id_store1,id_promo2);
        sdb.assignPromoToStore(id_store1,id_promo3);
        sdb.assignPromoToStore(id_store1,id_promo4);

        sdb.assignPromoToStore(id_store2,id_promo2);
        sdb.assignPromoToStore(id_store2,id_promo3);

        sdb.assignPromoToStore(id_store3,id_promo3);
        sdb.assignPromoToStore(id_store3,id_promo4);


        sdb.closeDatabase();

    }
}
