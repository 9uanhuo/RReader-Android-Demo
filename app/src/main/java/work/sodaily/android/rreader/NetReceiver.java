package work.sodaily.android.rreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class NetReceiver extends BroadcastReceiver {

    private boolean hasNet;
    private boolean hasShow;

    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, hasShow + "", Toast.LENGTH_SHORT).show();
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = manager.getAllNetworkInfo();
        int netType = -1;
        for (NetworkInfo info : networkInfo) {
            if (info != null && info.isConnected()) {
                hasNet = true;
                netType = info.getType();
                if (netType == ConnectivityManager.TYPE_MOBILE && info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE) {
                    netType = ConnectivityManager.TYPE_WIFI;
                }
                break;
            } else {
                hasNet = false;
            }
        }
        if (hasNet) {
            if (!hasShow) {
                hasShow = true;
                Toast.makeText(context, "网络已连接", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "网络已断开，请检查WiFi或蜂窝网络设置", Toast.LENGTH_SHORT).show();
            hasShow = false;
        }
    }

}
