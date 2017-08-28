package id.momokun.viverestorelocator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ElmoTan on 8/23/2017.
 */

public class StoreDatabaseHandler extends SQLiteOpenHelper {

    private static final int DB_VER = 1;
    private static final String DB_NAME = "storeDataManager";
    private static final String TABLE_STORELIST = "storeData";
    private static final String TABLE_PROMOLIST = "promoData";
    private static final String TABLE_STOREPROMO = "storePromoData";

    //col for global use
    private static final String KEY_ID = "id";

    //col for store
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_CITY = "city";
    private static final String KEY_POSTALCODE = "postal_code";
    private static final String KEY_PHONENUM = "phone_num";
    private static final String KEY_WORKDAY = "work_day";
    private static final String KEY_WORKTIME = "work_time";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_STOREIMG = "imagename";

    //col for promo
    private static final String KEY_PROMONAME = "name";
    private static final String KEY_PROMODESC = "desc";
    private static final String KEY_IMG = "image";
    private static final String KEY_DATE = "date";

    //col for store-promo
    private static final String KEY_STORE_ID = "store_id";
    private static final String KEY_PROMO_ID = "promo_id";

    //create table
    protected static final String CREATE_STORE_TABLE = "CREATE TABLE " + TABLE_STORELIST + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_CITY + " TEXT,"
            + KEY_POSTALCODE + " TEXT,"
            + KEY_PHONENUM + " TEXT,"
            + KEY_WORKDAY + " TEXT,"
            + KEY_WORKTIME + " TEXT,"
            + KEY_LONGITUDE + " TEXT,"
            + KEY_LATITUDE + " TEXT,"
            + KEY_STOREIMG + " TEXT" + ")";

    protected static final String CREATE_PROMO_TABLE = "CREATE TABLE " + TABLE_PROMOLIST + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PROMONAME + " TEXT,"
            + KEY_PROMODESC + " TEXT,"
            + KEY_IMG + " TEXT,"
            + KEY_DATE + " TEXT" + ")";

    protected static final String CREATE_STOREPROMO_TABLE = "CREATE TABLE " + TABLE_STOREPROMO + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_STORE_ID + " INTEGER,"
            + KEY_PROMO_ID + " INTEGER" + ")";


    public StoreDatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STORE_TABLE);
        db.execSQL(CREATE_PROMO_TABLE);
        db.execSQL(CREATE_STOREPROMO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORELIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMOLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOREPROMO);
        onCreate(db);
    }

    public long addStoreData(StoreData sd, long[] promo_ids){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, sd.get_name());
        values.put(KEY_ADDRESS, sd.get_address());
        values.put(KEY_CITY, sd.get_city());
        values.put(KEY_POSTALCODE, sd.get_postal_code());
        values.put(KEY_PHONENUM, sd.get_phone_num());
        values.put(KEY_WORKDAY, sd.get_work_day());
        values.put(KEY_WORKTIME, sd.get_work_time());
        values.put(KEY_LONGITUDE, sd.get_store_latitude());
        values.put(KEY_LATITUDE, sd.get_store_longitude());
        values.put(KEY_STOREIMG, sd.get_store_image());


        //add to store table
        long store_id = db.insert(TABLE_STORELIST,null,values);

        //promo->store
        for(long promo_id : promo_ids){
            addStorePromoLink(store_id, promo_id);
        }

        return store_id;
    }

    public List<StoreData> getAllStoreData(){
        List<StoreData> storeDataList = new ArrayList<StoreData>();
        String query = "SELECT  * FROM " + TABLE_STORELIST;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()){
            do{
                StoreData sd = new StoreData();
                sd.set_id(Integer.parseInt(c.getString(0)));
                sd.set_name(c.getString(1));
                sd.set_address(c.getString(2));
                sd.set_city(c.getString(3));
                sd.set_postal_code(c.getString(4));
                sd.set_phone_num(c.getString(5));
                sd.set_work_day(c.getString(6));
                sd.set_work_time(c.getString(7));
                sd.set_store_longitude(c.getString(8));
                sd.set_store_latitude(c.getString(9));
                sd.set_store_image(c.getString(10));
                storeDataList.add(sd);
            }while(c.moveToNext());
        }
        c.close();
        return storeDataList;
    }

    public List<StorePromoData> getAllPromoByStore(int store_id){
        List<StorePromoData> storePromoList = new ArrayList<StorePromoData>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM storePromoData tokopromo JOIN storeData toko " +
                "on tokopromo.store_id = toko.id JOIN promoData promo on tokopromo.promo_id" +
                "= promo.id where toko.id = " + store_id;

        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()){
            do {
                StorePromoData spd = new StorePromoData();
                spd.set_id(c.getInt(c.getColumnIndex(KEY_ID)));
                spd.set_promo_name(c.getString(c.getColumnIndex(KEY_PROMONAME)));
                spd.set_promo_desc(c.getString(c.getColumnIndex(KEY_PROMODESC)));
                spd.set_promo_image(c.getString(c.getColumnIndex(KEY_IMG)));
                spd.set_promo_date(c.getString(c.getColumnIndex(KEY_DATE)));
                storePromoList.add(spd);
            }while(c.moveToNext());
        }


        return storePromoList;
    }

    //CRUD Promo
    public long addPromoData(StorePromoData spd){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(KEY_PROMONAME, spd.get_promo_name());
        cv.put(KEY_PROMODESC, spd.get_promo_desc());
        cv.put(KEY_IMG, spd.get_promo_image());
        cv.put(KEY_DATE, spd.get_promo_date());

        return db.insert(TABLE_PROMOLIST, null, cv);
    }

    public List<StorePromoData> getAllPromoData(){
        List<StorePromoData> promoDataList = new ArrayList<StorePromoData>();
        String query = "SELECT  * FROM " + TABLE_PROMOLIST;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()){
            do{
                StorePromoData spd = new StorePromoData();
                spd.set_id(Integer.parseInt(c.getString(0)));
                spd.set_promo_name(c.getString(1));
                spd.set_promo_desc(c.getString(2));
                spd.set_promo_image(c.getString(3));
                spd.set_promo_date(c.getString(4));

                promoDataList.add(spd);
            }while(c.moveToNext());
        }

        return promoDataList;
    }

    //assignPromo
    public long assignPromoToStore(long store_id, long promo_id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(KEY_STORE_ID, store_id);
        cv.put(KEY_PROMO_ID, promo_id);

        return db.insert(TABLE_STOREPROMO, null, cv);
    }

    //close
    public void closeDatabase(){
        SQLiteDatabase db = this.getReadableDatabase();
        if(db!=null & db.isOpen()){
            db.close();
        }
    }

    //count

    public int getStoreCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_STORELIST;
        Cursor c = db.rawQuery(query, null);

        int total = c.getCount();
        c.close();

        return total;
    }

    public int getPromoCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PROMOLIST;
        Cursor c = db.rawQuery(query, null);

        int total = c.getCount();
        c.close();

        return total;
    }

    public int get(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PROMOLIST;
        Cursor c = db.rawQuery(query, null);

        int total = c.getCount();
        c.close();

        return total;
    }



    //ignore

    public StoreData getSingleStoreData(long store_id){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_STORELIST + " WHERE " + KEY_ID + " = " + store_id;
        //perform query
        Cursor c = db.rawQuery(query, null);

        if(c!=null){
            c.moveToFirst();
        }

        StoreData sd = new StoreData();
        sd.set_id(Integer.parseInt(c.getString(0)));
        sd.set_name(c.getString(1));

        return sd;
    }

    public long addStorePromoLink(long store_id, long promo_id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(KEY_STORE_ID, store_id);
        cv.put(KEY_PROMO_ID, promo_id);

        long id_storepromo = db.insert(TABLE_STOREPROMO,null,cv);

        return id_storepromo;
    }
}
