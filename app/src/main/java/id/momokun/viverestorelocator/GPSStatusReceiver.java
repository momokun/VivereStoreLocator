package id.momokun.viverestorelocator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by ElmoTan on 8/26/2017.
 */

public class GPSStatusReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(final Context context, Intent intent) {
        Toast.makeText(context, "Turn on Location for check nearest store location", Toast.LENGTH_SHORT).show();
    }
}
