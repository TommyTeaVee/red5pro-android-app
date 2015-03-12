package infrared5.com.red5proandroid.secondscreen;

import android.content.Context;

import com.infrared5.secondscreen.client.ConnectFailureType;
import com.infrared5.secondscreen.client.ConnectListener;
import com.infrared5.secondscreen.client.HostInfo;
import com.infrared5.secondscreen.client.HostListListener;
import com.infrared5.secondscreen.client.HostSession;
import com.infrared5.secondscreen.client.RegistryConnectionListener;
import com.infrared5.secondscreen.client.SecondScreen;
import com.infrared5.secondscreen.client.SecondScreenConfig;
import com.infrared5.secondscreen.client.SecondScreenConnection;
import com.infrared5.secondscreen.client.SecondScreenService;
import com.infrared5.secondscreen.client.SecondScreenView;

import java.util.List;

public class SecondScreenHandler extends SecondScreenConnection implements RegistryConnectionListener, HostListListener,ConnectListener {
    /**
     * The SecondScreen configuration
     */
    private SecondScreenConfig config;
    private SecondScreenView view;
    private Context context;
    private HostInfo info;
    private HostSession hostSession;
    private SecondScreenClientAdapter adapter;
    private SecondScreenConnection s;

    public SecondScreenHandler(SecondScreenClientAdapter adapter,Context context){
        this.context = context;
        this.adapter = adapter;
    }

    public boolean hasHostSession(){
        return hostSession != null;
    }

    public void setConfig(SecondScreenConfig conf){
        config = conf;
    }

    public void setView(SecondScreenView view){
        this.view = view;
        if(hostSession != null) {
            this.hostSession.bindToView(view);
        }
    }

    public void start() {
        SecondScreen.init(config);
        bind(context);
    }


    public void stop() {
        unbind(context);
        hostSession=null;
    }

    @Override
    public void onRegistryConnectionChanged(boolean b) {
        adapter.registryConnected(b);
    }

    protected void onBound(SecondScreenService service) {
        service.addRegistryConnectionListener(this);
        service.addHostListListener(this);
        service.addConnectListener(this);
    }

    @Override
    protected void onUnbound(SecondScreenService service) {

    }

    @Override
    public void onHostListChanged(List<HostInfo> hostInfos) {
        adapter.onHostListChanged(hostInfos);
    }

    @Override
    public void onHostConnected(HostSession hostSession) {
        this.hostSession = hostSession;
        adapter.showControls(true);
    }

    @Override
    public void onHostConnectFailed(ConnectFailureType connectFailureType) {
        adapter.showControls(false);
    }

}
