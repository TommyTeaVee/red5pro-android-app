package infrared5.com.red5proandroid.hostlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.infrared5.secondscreen.client.HostInfo;
import com.infrared5.secondscreen.client.HostListListener;
import com.infrared5.secondscreen.client.SecondScreenService;

import java.util.ArrayList;
import java.util.List;

import infrared5.com.red5proandroid.R;

class HostListAdaptor extends BaseAdapter implements HostListListener {

	private final List<HostInfo> mHosts;
	private final LayoutInflater mInflater;
	private final SecondScreenService mService;

	public HostListAdaptor(Context context, List<HostInfo> hostList, SecondScreenService service) {
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mHosts = new ArrayList<HostInfo>(hostList);
		mService = service;
	}

	@Override
	public void onHostListChanged(List<HostInfo> hosts) {
		mHosts.clear();
		mHosts.addAll(hosts);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mHosts.size();
	}

	@Override
	public Object getItem(int position) {
		return mHosts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mHosts.get(position).getSlotId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.template_host_entry, null);
            convertView.setMinimumHeight(48);
            // Creates a ViewHolder and store references to the children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.hostName = (TextView) convertView.findViewById(R.id.hostName);
            holder.slotId = (TextView) convertView.findViewById(R.id.slotId);
            holder.playerCount = (TextView) convertView.findViewById(R.id.playerCount);

            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }
        
        HostInfo info = mHosts.get(position);

        // Bind the data efficiently with the holder.
		String name = mService.isConnectingToHost(info)
				? "Connection Requested..."
				: info.getName();
        holder.hostName.setText(name);
        holder.slotId.setText(""+info.getSlotId());
        holder.playerCount.setText(info.getCurrentPlayers() + "/" + info.getMaxPlayers());

        return convertView;
	}
	
	static class ViewHolder {
        TextView hostName;
        TextView slotId;
        TextView playerCount;
    }

}
