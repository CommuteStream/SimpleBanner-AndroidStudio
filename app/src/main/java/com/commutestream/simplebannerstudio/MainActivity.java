package com.commutestream.simplebannerstudio;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.commutestream.ads.*;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup some location stuff that we'll use in an example below
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        String NetworkLocationProvider = LocationManager.NETWORK_PROVIDER;
        Location lastKnownLocation = locationManager
                .getLastKnownLocation(NetworkLocationProvider);

        CommuteStream.init();
        CommuteStream.setTestingFlag(true);
        CommuteStream.setBaseURL("https://api.commutestream.com:3000/");
        CommuteStream.setTheme("dark");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        AdRequest adRequest = new AdRequest.Builder()
                .setLocation(lastKnownLocation).build();
        mAdView.loadAd(adRequest);

        /*
		 * CommuteStream pays you more when you supply more information about
		 * your users. Most importantly, what transit information they are viewing,
		 * and also location, and when they do things like bookmarking or planning a trip.
		 */

        // Let's pretend that our app just displayed some arrival time information for two different
        // train stops, and also displayed some alert information for the "Red" line route. This is
        // how we would tell CommuteStream about it.
        CommuteStream.trackingDisplayed("cta", "Red", "41420"); // Addison
        CommuteStream.trackingDisplayed("cta", "Red", "41450"); // Chicago
        CommuteStream.alertDisplayed("cta", "Red", null);
        TimerTask myTimerTask = new TimerTask() {
            @Override
            public void run() {
                Log.v("SimpleBanner", "adding displayed tracking");
                CommuteStream.trackingDisplayed("cta", "Red", "41420");
            }
        };
        Timer myTimer = new Timer();
        myTimer.scheduleAtFixedRate(myTimerTask, 5000, 5000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
