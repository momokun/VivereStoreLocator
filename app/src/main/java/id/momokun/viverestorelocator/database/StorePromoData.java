package id.momokun.viverestorelocator.database;

/**
 * Created by ElmoTan on 8/23/2017.
 */

public class StorePromoData {

    int _id;
    String _promo_name;
    String _promo_desc;
    String _promo_image;
    String _promo_date;

    public StorePromoData(int _id, String _promo_name, String _promo_desc, String _promo_image, String _promo_date) {
        this._id = _id;
        this._promo_name = _promo_name;
        this._promo_desc = _promo_desc;
        this._promo_image = _promo_image;
        this._promo_date = _promo_date;
    }

    public StorePromoData(String _promo_name, String _promo_desc, String _promo_image, String _promo_date) {
        this._promo_name = _promo_name;
        this._promo_desc = _promo_desc;
        this._promo_image = _promo_image;
        this._promo_date = _promo_date;
    }

    public StorePromoData(){

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_promo_name() {
        return _promo_name;
    }

    public void set_promo_name(String _promo_name) {
        this._promo_name = _promo_name;
    }

    public String get_promo_desc() {
        return _promo_desc;
    }

    public void set_promo_desc(String _promo_desc) {
        this._promo_desc = _promo_desc;
    }

    public String get_promo_image() {
        return _promo_image;
    }

    public void set_promo_image(String _promo_image) {
        this._promo_image = _promo_image;
    }

    public String get_promo_date() {
        return _promo_date;
    }

    public void set_promo_date(String _promo_date) {
        this._promo_date = _promo_date;
    }
}
