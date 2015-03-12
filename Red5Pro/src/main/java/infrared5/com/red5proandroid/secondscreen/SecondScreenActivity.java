package infrared5.com.red5proandroid.secondscreen;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.infrared5.secondscreen.client.HostInfo;
import com.infrared5.secondscreen.client.SecondScreenConfig;
import com.infrared5.secondscreen.client.SecondScreenView;

import java.util.List;

import infrared5.com.red5proandroid.AppState;
import infrared5.com.red5proandroid.ControlBarFragment;
import infrared5.com.red5proandroid.R;
import infrared5.com.red5proandroid.hostlist.HostListFragment;
import infrared5.com.red5proandroid.settings.SettingsDialogFragment;

public class SecondScreenActivity extends FragmentActivity implements SecondScreenClientAdapter, ControlBarFragment.OnFragmentInteractionListener, SettingsDialogFragment.OnFragmentInteractionListener {

    HostListFragment hostListFragment;
    SecondScreenHandler secondScreenHandler;
    SecondScreenConfig config = SecondScreenConfig.createDefault();

    private static final String TAG = "SecondScreen";

    public void onStateSelection(AppState state) {
        this.finish();
    }

    public void onSettingsClick() {
        openSettings();
    }

    public void onSettingsDialogClose() {
        start();
    }

    public String getStringResource(int id) {
        return getResources().getString(id);
    }

    private void configure() {
        SharedPreferences preferences = getPreferences(MODE_MULTI_PROCESS);
        config.setRegistryHostname(preferences.getString(getStringResource(R.string.preference_host), getStringResource(R.string.preference_default_host)));
        config.setRegistryPort(preferences.getInt(getStringResource(R.string.preference_ss_port), Integer.parseInt(getStringResource(R.string.preference_default_ss_port))));
        config.setRed5AppName(preferences.getString(getStringResource(R.string.preference_ss_app), getStringResource(R.string.preference_default_ss_app)));
    }

    private void start() {
        configure();
        displayHosts();

        secondScreenHandler = new SecondScreenHandler(this, getApplicationContext());
        secondScreenHandler.setConfig(config);
        secondScreenHandler.start();
        hostListFragment.start();

        final View v = findViewById(android.R.id.content);

        v.setKeepScreenOn(true);
    }

    private void stop() {

        if (secondScreenHandler != null) {
            secondScreenHandler.getService().disconnectFromHost();
            secondScreenHandler.setView(null);
            secondScreenHandler.stop();
            secondScreenHandler = null;
        }
        if (hostListFragment != null) {
            hostListFragment.stop();
        }
        displayHosts();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second_screen);

        ControlBarFragment controlBar = (ControlBarFragment) getFragmentManager().findFragmentById(R.id.control_bar);
        controlBar.setSelection(AppState.SECONDSCREEN);
        controlBar.displayPublishControls(false);

        hostListFragment = (HostListFragment) getSupportFragmentManager().findFragmentById(R.id.host_list);

    }

    @Override
    protected void onResume() {
        super.onResume();
        openSettings();
    }

    @Override
    protected void onDestroy() {
        stop();
        super.onDestroy();
    }

    public void onDisconnectClick(View v){
        secondScreenHandler.getService().disconnectFromHost();
        displayHosts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.second_screen_settings) {
            openSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void showControls(boolean hasHost) {

        if(hasHost) {
            displaySecondScreen();
            SecondScreenView view = (SecondScreenView) findViewById(R.id.control_content);
            if(view != null){
                secondScreenHandler.setView(view);
            }
        }

    }

    @Override
    public void onHostListChanged(List<HostInfo> list) {

        if(secondScreenHandler.hasHostSession()) {

        }
        else {

        }
    }

    @Override
    public void registryConnected(boolean isConnected) {
        Log.i(TAG, "Registry Connected "+isConnected);
    }

    public void openSettings() {
        try {
            DialogFragment newFragment = SettingsDialogFragment.newInstance(AppState.SECONDSCREEN);
            newFragment.show(getFragmentManager().beginTransaction(), "settings_dialog");
            stop();
        }
        catch(Exception e) {
            Log.i(TAG, "Can't open settings: " + e.getMessage());
        }
    }

    private void displayHosts() {
        ViewGroup secondScreen = (ViewGroup) findViewById(R.id.second_screen_panel);
        ViewGroup hostList = (ViewGroup) findViewById(R.id.host_list);

        hostList.setVisibility(View.VISIBLE);
        secondScreen.setVisibility(View.GONE);
    }

    private void displaySecondScreen() {
        ViewGroup secondScreen = (ViewGroup) findViewById(R.id.second_screen_panel);
        ViewGroup hostList = (ViewGroup) findViewById(R.id.host_list);

        hostList.setVisibility(View.GONE);
        secondScreen.setVisibility(View.VISIBLE);
    }
}
