package work.sodaily.android.rreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import work.sodaily.android.rreader.rssparser.RssItem;
import work.sodaily.android.rreader.rssparser.RssReader;

public class MainActivity extends AppCompatActivity {

    private ListView articleList;
    private ArrayAdapter<String> titleAdapter;
    private ArrayList<String> linkList;
    private final NetReceiver netReceiver = new NetReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        articleList = findViewById(R.id.list);
        titleAdapter = new ArrayAdapter<>(this, R.layout.basic_list_item);
        linkList = new ArrayList<>();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String url = sp.getString("FeedUrl", null);
        if (url == null) {
            url = "https://sspai.com/feed";
            Toast.makeText(this, "RSS订阅链接为空！已显示预设订阅，请在设置中修改并重启应用，以加载修改: (", Toast.LENGTH_SHORT).show();
        }
        new GetRssFeed().execute(url);
        articleList.setOnItemClickListener((parent, view, position, id) -> ReadArticle(linkList.get((int) id)));
    }

    @Override
    protected void onResume() {
        IntentFilter netIntentFilter = new IntentFilter();
        netIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        netIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        netIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netReceiver, netIntentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (netReceiver != null) {
            unregisterReceiver(netReceiver);
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 1, R.string.app_setting);
        menu.add(1, 2, 2, R.string.about_me);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    private class GetRssFeed extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                RssReader rssReader = new RssReader(params[0]);
                for (RssItem item : rssReader.getItems()) {
                    titleAdapter.add(item.getTitle());
                    linkList.add(item.getLink());
                }
            } catch (Exception e) {
                Log.v("Error Parsing Data", e + "");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            titleAdapter.notifyDataSetChanged();
            articleList.setAdapter(titleAdapter);
        }
    }

    private void ReadArticle(String destUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (isURL(destUrl)) {
            intent.setData(Uri.parse(destUrl));
            startActivity(intent);
        } else {
            Toast.makeText(this, "链接解析结果为：" + destUrl
                    + "\nRSS订阅内容不规范，暂无法进行解析", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isURL(String str){
        str = str.toLowerCase();
        String regex = "^((https|http|ftp|rtsp|mms)?://)"  //https、http、ftp、rtsp、mms
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 例如：199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "[a-z]{2,6})" // first level domain- .com or .museum
                + "(:[0-9]{1,5})?" // 端口号最大为65535,5位数
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        return  str.matches(regex);
    }
}