package id.momokun.viverestorelocator.database;

/**
 * Created by ElmoTan on 8/23/2017.
 */

public class StoreData {

    int _id;
    String _name;
    String _address;
    String _city;
    String _postal_code;
    String _phone_num;
    String _work_day;
    String _work_time;
    String _store_latitude;
    String _store_longitude;
    String _store_image;



    public StoreData(int _id, String _name, String _address, String _city, String _postal_code, String _phone_num, String _work_day, String _work_time, String _store_longitude, String _store_latitude, String _store_image) {
        this._id = _id;
        this._name = _name;
        this._address = _address;
        this._city = _city;
        this._postal_code = _postal_code;
        this._phone_num = _phone_num;
        this._work_day = _work_day;
        this._work_time = _work_time;
        this._store_latitude = _store_latitude;
        this._store_longitude = _store_longitude;
        this._store_image = _store_image;
    }

    public StoreData(String _name, String _address, String _city, String _postal_code, String _phone_num, String _work_day, String _work_time, String _store_longitude, String _store_latitude, String _store_image) {

        this._name = _name;
        this._address = _address;
        this._city = _city;
        this._postal_code = _postal_code;
        this._phone_num = _phone_num;
        this._work_day = _work_day;
        this._work_time = _work_time;
        this._store_latitude = _store_latitude;
        this._store_longitude = _store_longitude;
        this._store_image = _store_image;
    }

    public StoreData(){

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_address() {
        return _address;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public String get_city() {
        return _city;
    }

    public void set_city(String _city) {
        this._city = _city;
    }

    public String get_postal_code() {
        return _postal_code;
    }

    public void set_postal_code(String _postal_code) {
        this._postal_code = _postal_code;
    }

    public String get_phone_num() {
        return _phone_num;
    }

    public void set_phone_num(String _phone_num) {
        this._phone_num = _phone_num;
    }

    public String get_work_day() {
        return _work_day;
    }

    public void set_work_day(String _work_day) {
        this._work_day = _work_day;
    }

    public String get_work_time() {
        return _work_time;
    }

    public void set_work_time(String _work_time) {
        this._work_time = _work_time;
    }

    public String get_store_latitude() {
        return _store_latitude;
    }

    public void set_store_latitude(String _store_latitude) {
        this._store_latitude = _store_latitude;
    }

    public String get_store_longitude() {
        return _store_longitude;
    }

    public void set_store_longitude(String _store_longitude) {
        this._store_longitude = _store_longitude;
    }

    public String get_store_image() {
        return _store_image;
    }

    public void set_store_image(String _store_image) {
        this._store_image = _store_image;
    }
}
