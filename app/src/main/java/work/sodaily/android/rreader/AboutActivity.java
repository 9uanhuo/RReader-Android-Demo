package work.sodaily.android.rreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .enableDarkMode(false)
                .setImage(R.drawable.icon)
                .setDescription("A simple Android application. " +
                                "It can show the RSS feed on the home screen " +
                                "and support to change the default feed in the setting. " +
                                "Also it will show the network status on the bottom " +
                                "when the network is broken or get to normal.")
                .addItem(new Element().setTitle("Version 0.0.1"))
                .addGroup("Contact")
                .addEmail("!@example.com")
                .addWebsite("https://blog.guanhuo.link")
                .addGitHub("Sandothers")
                .addGroup("License")
                .addItem(new Element().setTitle("medyo/android-about-page")
                        .setIntent(AccessUrl("https://github.com/ShirwaM/Simplistic-RSS/blob/master/LICENSE")))
                .addItem(new Element().setTitle("ShirwaM/Simplistic-RSS modified by @kukosek")
                        .setIntent(AccessUrl("https://github.com/medyo/android-about-page#license")))
                .addItem(getCopyRightsElement())
                .create();

        setContentView(aboutPage);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = "Sandothers";
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.icon);
        copyRightsElement.setAutoApplyIconTint(true);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(v -> Toast.makeText(v.getContext(), copyrights, Toast.LENGTH_SHORT).show());
        return copyRightsElement;
    }

    private Intent AccessUrl(String destUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(destUrl));
        return intent;
    }
}
