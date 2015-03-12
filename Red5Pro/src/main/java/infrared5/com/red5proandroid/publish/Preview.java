package infrared5.com.red5proandroid.publish;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;

import infrared5.com.red5proandroid.Main;
import infrared5.com.red5proandroid.R;

public class Preview extends Activity implements SurfaceHolder.Callback,View.OnClickListener{

    public static PublishStreamConfig config = null;

    static{
        if(config==null){
            config = new PublishStreamConfig();
        }
    }

    protected Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(camera!=null)
            camera.release();
    }

    public void onClick(View view) {}

    @Override
    protected void onPause() {
        super.onPause();
        if(camera!=null){
            camera.release();
            camera=null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(camera==null){
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        camera.setDisplayOrientation(90);
        SurfaceView sufi = (SurfaceView) findViewById(R.id.surfaceView);
        sufi.getHolder().addCallback(this);
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

    @Override
    public void onBackPressed() {

        if(camera!=null) {
           camera.stopPreview();
           camera.release();
           camera = null;
       }

        Intent startMain=new Intent(this, Main.class);
        startMain .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(startMain);

    }
}
