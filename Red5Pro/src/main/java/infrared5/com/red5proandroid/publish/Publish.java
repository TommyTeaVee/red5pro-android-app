package infrared5.com.red5proandroid.publish;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.event.R5ConnectionEvent;
import com.red5pro.streaming.event.R5ConnectionListener;
import com.red5pro.streaming.source.R5Camera;
import com.red5pro.streaming.source.R5Microphone;

import java.util.ArrayList;
import java.util.List;

import infrared5.com.red5proandroid.AppState;
import infrared5.com.red5proandroid.ControlBarFragment;
import infrared5.com.red5proandroid.R;
import infrared5.com.red5proandroid.settings.SettingsDialogFragment;

public class Publish extends Activity implements SurfaceHolder.Callback, View.OnClickListener,
        ControlBarFragment.OnFragmentInteractionListener, SettingsDialogFragment.OnFragmentInteractionListener {

    private int cameraSelection = 0;
    private Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
    private List<Camera.Size> sizes = new ArrayList<Camera.Size>();
    public static Camera.Size selected_size = null;
    public static String selected_item = null;
    public static int preferedResolution = 0;
    public static PublishStreamConfig config = null;
    private R5Camera r5Cam;
    private R5Microphone r5Mic;
    private SurfaceView surfaceForCamera;

    static {
        if(config==null){
            config = new PublishStreamConfig();
        }
    }

    protected Camera camera;
    protected boolean isPublishing = false;

    R5Stream stream;

    public final static String TAG = "Preview";

    public void onStateSelection(AppState state) {
        this.finish();
    }

    public void onSettingsClick() {
        openSettings();
    }

    public String getStringResource(int id) {
        return getResources().getString(id);
    }

    public void onSettingsDialogClose() {
        configure();
    }

    //grab user data to be used in R5Configuration
    private void configure() {
        SharedPreferences preferences = getPreferences(MODE_MULTI_PROCESS);
        config.host = preferences.getString(getStringResource(R.string.preference_host), getStringResource(R.string.preference_default_host));
        config.port = preferences.getInt(getStringResource(R.string.preference_port), Integer.parseInt(getStringResource(R.string.preference_default_port)));
        config.app = preferences.getString(getStringResource(R.string.preference_app), getStringResource(R.string.preference_default_app));
        config.name = preferences.getString(getStringResource(R.string.preference_name), getStringResource(R.string.preference_default_name));
        config.bitrate = preferences.getInt(getStringResource(R.string.preference_bitrate), Integer.parseInt(getStringResource(R.string.preference_default_bitrate)));
        config.audio = preferences.getBoolean(getStringResource(R.string.preference_audio), true);
        config.video = preferences.getBoolean(getStringResource(R.string.preference_video), true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //assign the layout view
        setContentView(R.layout.activity_publish);

        //setup properties in configuration
        configure();

        final View v = findViewById(android.R.id.content);

        v.setKeepScreenOn(true);

        ControlBarFragment controlBar = (ControlBarFragment)getFragmentManager().findFragmentById(R.id.control_bar);
        controlBar.setSelection(AppState.PUBLISH);
        controlBar.displayPublishControls(true);

        //activate the camera
        showCamera();

        ImageButton rButton = (ImageButton) findViewById(R.id.btnRecord);
        rButton.setOnClickListener(this);

        ImageButton cameraButton = (ImageButton) findViewById(R.id.btnCamera);
        cameraButton.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        openSettings();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        stopPublishing();
        stopCamera();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.publish, menu);
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

    private void openSettings(){
        try {
            SettingsDialogFragment newFragment = SettingsDialogFragment.newInstance(AppState.PUBLISH);
            newFragment.show(getFragmentManager().beginTransaction(), "settings_dialog");

            List<String> sb = new ArrayList<String>();
            for(Camera.Size size:this.sizes){
                if((size.width/2)%16!=0){
                    continue;
                }

                String potential = String.valueOf(size.width).trim() +"x"+  String.valueOf(size.height).trim();
                sb.add(potential);
            }
            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,sb) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    ((TextView) v).setTextColor(0xffff0000);
                    return v;
                }

                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View v = super.getDropDownView(position, convertView, parent);
                    ((TextView) v).setTextColor(0xffff0000);
                    ((TextView) v).setGravity(Gravity.CENTER);
                    return v;
                }
            };

            newFragment.setSpinnerAdapter(adapter);

        }
        catch(Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Can't open settings: " + e.getMessage());
        }
    }

    private void toggleCamera() {
        cameraSelection = (cameraSelection + 1) % 2;
        try {
            Camera.getCameraInfo(cameraSelection, cameraInfo);
            cameraSelection = cameraInfo.facing;
        }
        catch(Exception e) {
            // can't find camera at that index, set default
            cameraSelection = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        stopCamera();
        showCamera();
    }

    private void showCamera() {
        if(camera == null) {
            camera = Camera.open(cameraSelection);
            camera.setDisplayOrientation(90);
            sizes=camera.getParameters().getSupportedPreviewSizes();
            SurfaceView sufi = (SurfaceView) findViewById(R.id.surfaceView);
            if(sufi.getHolder().isCreating()) {
                sufi.getHolder().addCallback(this);
            }
            else {
                sufi.getHolder().addCallback(this);
                this.surfaceCreated(sufi.getHolder());
            }
        }
    }

    private void stopCamera() {
        if(camera != null) {
            SurfaceView sufi = (SurfaceView) findViewById(R.id.surfaceView);
            sufi.getHolder().removeCallback(this);
            sizes.clear();

            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    //called by record button
    private void startPublishing() {
        if(!isPublishing) {

            Handler mHand = new Handler();

            stream = new R5Stream(new R5Connection(new R5Configuration(R5StreamProtocol.RTSP, Publish.config.host,  Publish.config.port, Publish.config.app, Publish.config.app, 1.0f)));
            stream.setLogLevel(R5Stream.LOG_LEVEL_DEBUG);

            stream.connection.addListener(new R5ConnectionListener() {
                @Override
                public void onConnectionEvent(R5ConnectionEvent event) {
                    Log.d("publish","connection event code "+event.value()+"\n");
                    switch(event.value()){
                        case 0://open
                            break;
                        case 1://close
                            break;
                        case 2://error
                            break;

                    }
                }
            });

            stream.setListener(new R5ConnectionListener() {
                @Override
                public void onConnectionEvent(R5ConnectionEvent event) {
                    switch (event) {
                        case CONNECTED:
                            break;
                        case DISCONNECTED:
                            break;
                        case START_STREAMING:
                            break;
                        case STOP_STRAMING:
                            break;
                        case CLOSE:
                            break;
                        case TIMEOUT:
                            break;
                        case ERROR:
                            break;
                    }
                }
            });

            camera.stopPreview();

            //assign the surface to show the camera output
            this.surfaceForCamera = (SurfaceView) findViewById(R.id.surfaceView);
            stream.setView((SurfaceView) findViewById(R.id.surfaceView));

            //add the camera for streaming
            if(selected_item != null) {
                Log.d("publisher","selected_item "+selected_item);
                String bits[] = selected_item.split("x");
                int pW= Integer.valueOf(bits[0]);
                int pH=  Integer.valueOf(bits[1]);
                if((pW/2) %16 !=0){
                    pW=320;
                    pH=240;
                }
                Camera.Parameters parameters = camera.getParameters();
                parameters.setPreviewSize(pW, pH);
                camera.setParameters(parameters);
                r5Cam = new R5Camera(camera,pW,pH);
                r5Cam.setBitrate(Publish.config.bitrate);
            }
            else {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setPreviewSize(320, 240);

                camera.setParameters(parameters);
                r5Cam = new R5Camera(camera,320,240);
                r5Cam.setBitrate(config.bitrate);
            }

            if(cameraSelection==1) {
                r5Cam.setOrientation(270);
            }
            else {
                r5Cam.setOrientation(90);
            }
            r5Mic = new R5Microphone();

            if(config.video) {
                stream.attachCamera(r5Cam);
            }

            if(config.audio) {
                stream.attachMic(r5Mic);
            }


                    isPublishing = true;
                    stream.publish(Publish.config.name, R5Stream.RecordType.Live);
                    camera.startPreview();

        }
    }

    private void stopPublishing() {
        if(stream!=null) {
            stream.stop();
        }
        isPublishing = false;
    }

    public void onClick(View view) {
        ImageButton rButton = (ImageButton) findViewById(R.id.btnRecord);
        ImageButton cameraButton = (ImageButton) findViewById(R.id.btnCamera);

        if(view.getId() == R.id.btnRecord) {
            if(isPublishing) {
                stopPublishing();
                rButton.setImageResource(R.drawable.empty_red);
                cameraButton.setVisibility(View.VISIBLE);

            }
            else {
                startPublishing();
                rButton.setImageResource(R.drawable.empty);
                cameraButton.setVisibility(View.GONE);
            }
        }
        else if(view.getId() == R.id.btnCamera) {
            toggleCamera();
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        try{
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }
        catch(Exception e){
            e.printStackTrace();
        };

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {}

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {}

}
