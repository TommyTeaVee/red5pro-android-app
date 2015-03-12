package infrared5.com.red5proandroid.secondscreen;

import com.infrared5.secondscreen.client.HostInfo;

import java.util.List;

public interface SecondScreenClientAdapter {

    public void showControls(boolean hasHost);

    public void onHostListChanged(List<HostInfo> list);

    public void registryConnected(boolean isConnected);
}
