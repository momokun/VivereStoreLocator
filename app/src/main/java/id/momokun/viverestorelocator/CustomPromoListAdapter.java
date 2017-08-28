package id.momokun.viverestorelocator;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ElmoTan on 8/24/2017.
 */

public class CustomPromoListAdapter extends BaseAdapter {

    private ArrayList<String> promoName;
    private ArrayList<String> promoDesc;
    private ArrayList<String> promoDate;
    private ArrayList<String> promoImage;
    private Activity context;



    private TextView pName, pDesc, pDate;
    private ImageView pImage;

    public CustomPromoListAdapter(Activity c, ArrayList<String> promoName, ArrayList<String> promoDesc, ArrayList<String> promoDate, ArrayList<String> promoImage) {
        this.promoName = promoName;
        this.promoDesc = promoDesc;
        this.promoDate = promoDate;
        this.promoImage = promoImage;
        this.context = c;

    }

    @Override
    public int getCount() {
        return promoName.size();
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
    public View getView(int i, View v, ViewGroup viewGroup) {
        LayoutInflater inflater = context.getLayoutInflater();
        v = inflater.inflate(R.layout.custom_promo_row, null, true);


        pName = v.findViewById(R.id.promo_name);
        pImage = v.findViewById(R.id.bg_promo);


        String imageResource = promoImage.get(i);
        int resourceId = context.getResources().getIdentifier(imageResource, "drawable", context.getPackageName());
        BitmapDrawable bd = new BitmapDrawable(context.getResources(),decodeSampledBitmapFromResource(context.getResources(),resourceId,100,100));
        pImage.setScaleType(ImageView.ScaleType.CENTER);
        pImage.setAdjustViewBounds(true);
        //pImage.setBackground(bd);
        pImage.setImageResource(resourceId);


        pName.setText(promoName.get(i));


        return v;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
