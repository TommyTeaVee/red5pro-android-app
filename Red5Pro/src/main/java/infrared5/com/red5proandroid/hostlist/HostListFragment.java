package infrared5.com.red5proandroid.hostlist;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;

import com.infrared5.secondscreen.client.HostInfo;
import com.infrared5.secondscreen.client.RegistryConnectionListener;
import com.infrared5.secondscreen.client.SecondScreenConnection;
import com.infrared5.secondscreen.client.SecondScreenService;

import infrared5.com.red5proandroid.AppState;
import infrared5.com.red5proandroid.ControlBarFragment;
import infrared5.com.red5proandroid.R;
import infrared5.com.red5proandroid.secondscreen.SecondScreenActivity;

public class HostListFragment extends ListFragment implements RegistryConnectionListener, ControlBarFragment.OnFragmentInteractionListener {

	private HostListAdaptor mListAdaptor;

    private final SecondScreenConnection mConnection = new SecondScreenConnection() {

        @Override
        protected void onBound(SecondScreenService service) {
            onRegistryConnectionChanged(service.isConnectedToRegistry());

            mListAdaptor = new HostListAdaptor(getActivity(),
                    service.getAvailableHosts(),
                    service);
            service.addHostListListener(mListAdaptor);
            service.addRegistryConnectionListener(HostListFragment.this);
            setListAdapter(mListAdaptor);
        }

        @Override
        protected void onUnbound(SecondScreenService service) {
            service.removeHostListListener(mListAdaptor);
            mListAdaptor.notifyDataSetInvalidated();
            service.removeRegistryConnectionListener(HostListFragment.this);
        }
    };

    private final BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
        updateWifiIndicator(context);
        }
    };

    public void onStateSelection(AppState state) {
        this.getActivity().finish();
    }

    public void onSettingsClick() {
        ((SecondScreenActivity) getActivity()).openSettings();
    }

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		SecondScreenService service = mConnection.getService();
		HostInfo host = service.getAvailableHosts().get(position);
		service.connectToHost(host);
		mListAdaptor.notifyDataSetChanged();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_host_list, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onRegistryConnectionChanged(boolean connected) {
		if (getView() != null) {

			CheckBox indicator = (CheckBox)getView().findViewById(R.id.indicator_registry);
			indicator.setChecked(connected);

		}
	}

    public void start() {
        mConnection.bind(getActivity());
        getActivity().registerReceiver(mWifiReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        updateWifiIndicator(getActivity());
    }

    public void stop() {
        if(getActivity() != null) {
            getActivity().unregisterReceiver(mWifiReceiver);
        }
        if(mConnection != null) {
            try {
                mConnection.unbind(getActivity());
            }
            catch(Exception e) {
                Log.i("HostListFragement", "Could not unbind to service: " + e.getMessage());
            }
        }
    }

	private void updateWifiIndicator(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		boolean hasWifi = info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI;

		if (getView() != null) {

			CheckBox indicator = (CheckBox)getView().findViewById(R.id.indicator_wifi);
			indicator.setChecked(hasWifi);

		}
	}
}
