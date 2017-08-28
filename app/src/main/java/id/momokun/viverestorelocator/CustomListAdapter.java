package id.momokun.viverestorelocator;

import android.app.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.momokun.viverestorelocator.database.StoreDatabaseHandler;
import id.momokun.viverestorelocator.database.StorePromoData;

/**
 * Created by ElmoTan on 8/23/2017.
 */

public class CustomListAdapter extends BaseAdapter {

    private ArrayList<String> storeName;
    private ArrayList<String> storeAddress;
    private ArrayList<String> storeCity;
    private ArrayList<String> storePostalCode;
    private ArrayList<String> storePhnum;
    private ArrayList<String> storeWorkday;
    private ArrayList<String> storeWorktime;
    private ArrayList<String> storeImage;
    private Activity context;
    private TextView vName, vAddr, vCity, vPhnum, vWorkday, vWorktime, pDialogTitle;
    private ImageView vImage;
    private Button vPromo;

    private ArrayList<String> promoName = new ArrayList<>();
    private ArrayList<String> promoDesc = new ArrayList<>();
    private ArrayList<String> promoDate = new ArrayList<>();
    private ArrayList<String> promoImage = new ArrayList<>();
    private ArrayList<String> storeTName = new ArrayList<>();

    private StoreDatabaseHandler sdb;
    private CustomPromoListAdapter customPromoListAdapter;
    private ListView listPromoView;


    public CustomListAdapter(StoreDatabaseHandler sdb, Activity c, ArrayList<String> storeName, ArrayList<String> storeAddress,
                             ArrayList<String> storeCity, ArrayList<String> storePostalCode,
                             ArrayList<String> storePhnum, ArrayList<String> storeWorkday, ArrayList<String> storeWorktime, ArrayList<String> storeImage) {
        this.context = c;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeCity = storeCity;
        this.storePostalCode = storePostalCode;
        this.storePhnum = storePhnum;
        this.storeWorkday = storeWorkday;
        this.storeWorktime = storeWorktime;
        this.storeImage = storeImage;
        this.sdb = sdb;
    }

    @Override
    public int getCount() {
        return storeName.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View v, ViewGroup viewGroup) {
        LayoutInflater inflater = context.getLayoutInflater();
        v = inflater.inflate(R.layout.custom_row, null, true);

        vName = v.findViewById(R.id.store_name);
        vAddr = v.findViewById(R.id.store_address);
        vCity = v.findViewById(R.id.store_city);
        vPhnum = v.findViewById(R.id.store_phnum);
        vWorkday = v.findViewById(R.id.store_workday);
        vWorktime = v.findViewById(R.id.store_worktime);
        vImage = v.findViewById(R.id.store_image);
        vPromo = v.findViewById(R.id.btn_promo);


        vName.setText(storeName.get(i));
        vAddr.setText(storeAddress.get(i));
        vCity.setText(storeCity.get(i) + " " + storePostalCode.get(i));
        vPhnum.setText(storePhnum.get(i));
        vWorkday.setText(storeWorkday.get(i));
        vWorktime.setText(storeWorktime.get(i));

        String imageResource = storeImage.get(i);
        int resourceId = context.getResources().getIdentifier(imageResource, "drawable", context.getPackageName());
        vImage.setImageResource(resourceId);



        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);


        final View promoView = inflater.inflate(R.layout.custom_promo_dialog,null,false);
        // set dialog message
        alertDialogBuilder
                .setView(promoView)
                .setCancelable(true)
                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();


        vPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                promoName.clear();
                promoImage.clear();
                List<StorePromoData> t = sdb.getAllPromoByStore(i+1);
                for (StorePromoData spd : t) {
                    promoName.add(spd.get_promo_name());
                    promoImage.add(spd.get_promo_image());

                }


                customPromoListAdapter = new CustomPromoListAdapter(context,promoName,promoDesc,promoDate,promoImage);
                listPromoView = promoView.findViewById(R.id.list_promo);
                pDialogTitle = promoView.findViewById(R.id.promo_dialog_title);
                pDialogTitle.setText(storeName.get(i));
                listPromoView.setAdapter(customPromoListAdapter);




                // show it
                alertDialog.show();


                /*final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setView(promoView);
                alertDialog.setCancelable(true);
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();*/
            }
        });


        return v;
    }



    /*
     public CustomListAdapter(Activity context, String[] storeName, String[] storeAddress){
        super(context, R.layout.custom_row, storeName);
        this.context = context;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup p){
        LayoutInflater i = context.getLayoutInflater();
        View v = i.inflate(R.layout.custom_row, null, true);
        TextView vName = v.findViewById(R.id.store_name);
        TextView vAddr = v.findViewById(R.id.store_address);

        vName.setText(storeName[pos]);
        vAddr.setText(storeAddress[pos]);
        return v;
    }
     */
}
