package infrared5.com.red5proandroid.subscribe;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.event.R5ConnectionEvent;
import com.red5pro.streaming.event.R5ConnectionListener;
import com.red5pro.streaming.view.R5VideoView;

import infrared5.com.red5proandroid.AppState;
import infrared5.com.red5proandroid.ControlBarFragment;
import infrared5.com.red5proandroid.R;
import infrared5.com.red5proandroid.publish.PublishStreamConfig;
import infrared5.com.red5proandroid.settings.SettingsDialogFragment;

public class Subscribe extends Activity implements ControlBarFragment.OnFragmentInteractionListener, SettingsDialogFragment.OnFragmentInteractionListener {

    PublishStreamConfig streamParams = new PublishStreamConfig();
    R5Stream stream;

    public boolean isStreaming = false;

    public final static String TAG = "Subscribe";

    public void onStateSelection(AppState state) {
        this.finish();
    }
     public void onSettingsClick() {
        openSettings();
    }

    public void onSettingsDialogClose() {
        configure();
    }

    public String getStringResource(int id) {
        return getResources().getString(id);
    }

    //setup configuration
    private void configure() {
        SharedPreferences preferences = getPreferences(MODE_MULTI_PROCESS);
        streamParams.host = preferences.getString(getStringResource(R.string.preference_host), getStringResource(R.string.preference_default_host));
        streamParams.port = preferences.getInt(getStringResource(R.string.preference_port), Integer.parseInt(getStringResource(R.string.preference_default_port)));
        streamParams.app = preferences.getString(getStringResource(R.string.preference_app), getStringResource(R.string.preference_default_app));
        streamParams.name = preferences.getString(getStringResource(R.string.preference_name), getStringResource(R.string.preference_default_name));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subscribe);

        configure();

        ControlBarFragment controlBar = (ControlBarFragment)getFragmentManager().findFragmentById(R.id.control_bar);
        controlBar.setSelection(AppState.SUBSCRIBE);
        controlBar.displayPublishControls(false);

        View playPauseButton = findViewById(R.id.btnSubscribePlayPause);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleStream();
            }
        });

    }

    private void toggleStream() {
        if(isStreaming) {
            stopStream();
        }
        else {
            startStream();
        }
    }

    private void setStreaming(boolean ok) {
        ViewGroup playPauseButton = (ViewGroup) findViewById(R.id.btnSubscribePlayPause);
        TextView textView = (TextView) playPauseButton.getChildAt(0);

        isStreaming = ok;
        textView.setText(isStreaming ? "Stop Stream" : "Start Stream");
    }

    private void startStream() {

        //grab the main view where our video object resides
        View v = this.findViewById(android.R.id.content);

        v.setKeepScreenOn(true);

        //setup the stream with the user config settings
        stream = new R5Stream(new R5Connection(new R5Configuration(R5StreamProtocol.RTSP, streamParams.host, streamParams.port, streamParams.app,streamParams.app, 2.0f)));

        //set log level to be informative
        stream.setLogLevel(R5Stream.LOG_LEVEL_INFO);

        //set up our listener
        stream.setListener(new R5ConnectionListener() {
            @Override
            public void onConnectionEvent(R5ConnectionEvent r5event) {
                //this is getting called from the network thread, so handle appropriately
                final R5ConnectionEvent event = r5event;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Context context = getApplicationContext();
                        CharSequence text = event.message;
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });
            }
        });

        //associate the video object with the red5 SDK video view
        R5VideoView videoView = (R5VideoView)v.findViewById(R.id.video);
        //attach the stream
        videoView.attachStream(stream);
        //start the stream
        stream.play(streamParams.name);
        //update the state for the toggle button
        setStreaming(true);

    }

    private void stopStream() {

        if(stream != null) {
            View v = this.findViewById(android.R.id.content);
            R5VideoView videoView = (R5VideoView)v.findViewById(R.id.video);
            videoView.attachStream(null);
            stream.stop();

            stream = null;
        }
        setStreaming(false);

    }

    private void openSettings() {
        try {
            DialogFragment newFragment = SettingsDialogFragment.newInstance(AppState.SUBSCRIBE);
            newFragment.show(getFragmentManager().beginTransaction(), "settings_dialog");
        }
        catch(Exception e) {
            Log.i(TAG, "Can't open settings: " + e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.subscribe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            openSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        openSettings();
    }

    @Override
    protected void onPause() {
        stopStream();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        stopStream();
        super.onDestroy();
    }

}
