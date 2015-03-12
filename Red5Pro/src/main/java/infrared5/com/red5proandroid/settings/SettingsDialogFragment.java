package infrared5.com.red5proandroid.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import infrared5.com.red5proandroid.AppState;
import infrared5.com.red5proandroid.R;
import infrared5.com.red5proandroid.publish.Publish;

public class SettingsDialogFragment extends DialogFragment {

    private AppState state;
    private OnFragmentInteractionListener mListener;
    private Spinner resolutionPicker ;
    private ArrayAdapter adapter;
    public static int defaultResolution = 0;
    public static int bitRate = 56;

    public static SettingsDialogFragment newInstance(AppState state) {
        SettingsDialogFragment fragment = new SettingsDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.state = state;
        return fragment;
    }
    public void setSpinnerAdapter(ArrayAdapter list) {
        this.adapter=list;
        defaultResolution=-1;
        boolean has352 = false;
        boolean has160 = false;

        int c = adapter.getCount();
        for(int j=0; j < c; j++){
            String rez = String.valueOf(adapter.getItem(j));
            if("352x288".equals(rez)) {
                defaultResolution=j;//not ideal but...
            }
            else  if("160x120".equals(rez)&& defaultResolution<0) {
              //  defaultResolution=j;//not good enough...
            }
            else  if("176x144".equals(rez)&& defaultResolution<0) {
              //  defaultResolution=j;//not good enough...
            }

            if("320x240".equals(rez)) {
                defaultResolution=j;//perfect
                break;
            }

        }

        Log.d("publisher", "setting default resolution "+defaultResolution);

        if(defaultResolution < 0) {
            defaultResolution=0;
            Log.e("publisher","no currently supported resolution");
        }

        if(resolutionPicker!=null) {
            resolutionPicker.setAdapter(adapter);
            resolutionPicker.setSelection(defaultResolution);
            resolutionPicker.setOnItemSelectedListener(getItemSelectedHandlerForResolution());
        }
    }

    public SettingsDialogFragment() {

    }

    private EditText getField(View v, int id) {
        return (EditText) v.findViewById(id);
    }

    private String getPreferenceValue(int id) {
        return getResources().getString(id);
    }

    private void saveSettings(View v) {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = preferences.edit();

        EditText host = getField(v, R.id.settings_host);
        EditText port = getField(v, R.id.settings_port);
        EditText app = getField(v, R.id.settings_appname);
        EditText name = getField(v, R.id.settings_streamname);

        editor.putString(getPreferenceValue(R.string.preference_host), host.getText().toString());
        editor.putInt(getPreferenceValue(R.string.preference_port), Integer.parseInt(port.getText().toString()));
        editor.putString(getPreferenceValue(R.string.preference_app), app.getText().toString());
        editor.putString(getPreferenceValue(R.string.preference_name), name.getText().toString());

        if(state == AppState.PUBLISH) {
            CheckBox cb = (CheckBox)v.findViewById(R.id.settings_audio);
            CheckBox cbv = (CheckBox)v.findViewById(R.id.settings_video);
            EditText br = getField(v, R.id.settings_bitrate);
            bitRate = Integer.valueOf(br.getText().toString().trim());
            editor.putInt(getPreferenceValue(R.string.preference_bitrate),bitRate);
            editor.putBoolean(getPreferenceValue(R.string.preference_audio), cb.isChecked());
            editor.putBoolean(getPreferenceValue(R.string.preference_video), cbv.isChecked());
        }

        editor.commit();
    }

    private void showUserSettings(View v) {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_MULTI_PROCESS);

        EditText host = getField(v, R.id.settings_host);
        EditText port = getField(v, R.id.settings_port);
        EditText app = getField(v, R.id.settings_appname);
        EditText name = getField(v, R.id.settings_streamname);
        EditText bitrate = getField(v, R.id.settings_bitrate);

        host.setText(preferences.getString(getPreferenceValue(R.string.preference_host), getPreferenceValue(R.string.preference_default_host)));
        name.setText(preferences.getString(getPreferenceValue(R.string.preference_name), getPreferenceValue(R.string.preference_default_name)));


        switch (state) {
            case PUBLISH:
            case SUBSCRIBE:
                bitrate.setText((""+preferences.getInt(getPreferenceValue(R.string.preference_bitrate), Integer.parseInt(getPreferenceValue(R.string.preference_default_bitrate)))));
                port.setText(""+preferences.getInt(getPreferenceValue(R.string.preference_port), Integer.parseInt(getPreferenceValue(R.string.preference_default_port))));
                app.setText(preferences.getString(getPreferenceValue(R.string.preference_app), getPreferenceValue(R.string.preference_default_app)));
                break;
            case SECONDSCREEN:
                port.setText(""+preferences.getInt(getPreferenceValue(R.string.preference_port), Integer.parseInt(getPreferenceValue(R.string.preference_default_ss_port))));
                app.setText(preferences.getString(getPreferenceValue(R.string.preference_app), getPreferenceValue(R.string.preference_default_ss_app)));
                break;
        }
    }

    private AdapterView.OnItemSelectedListener getItemSelectedHandlerForResolution(){
         return new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 Log.d("publisher"," selected item "+String.valueOf(adapterView.getSelectedItem()+"  i:" +i+"  l :"+l));
                 Publish.selected_item = String.valueOf(adapterView.getSelectedItem());
                 Publish.preferedResolution = adapterView.getSelectedItemPosition();
             }

             @Override
             public void onNothingSelected(AdapterView<?> adapterView) {

             }
         };
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.fragment_settings_dialog, null, false);

        resolutionPicker = (Spinner) v.findViewById(R.id.resolutionPicker);
        if(adapter!=null) {
            resolutionPicker.setAdapter(adapter);

            resolutionPicker.setSelection(defaultResolution);
            resolutionPicker.setOnItemSelectedListener(getItemSelectedHandlerForResolution());
        }

        ViewGroup streamSettings = (ViewGroup) v.findViewById(R.id.subscribe_settings);
        ViewGroup publishSettings = (ViewGroup) v.findViewById(R.id.publishing_settings);

        switch (state) {
            case SUBSCRIBE:
                publishSettings.setVisibility(View.GONE);
                break;
            case SECONDSCREEN:
                publishSettings.setVisibility(View.GONE);
                streamSettings.setVisibility(View.GONE);
                break;
        }

        ContextThemeWrapper ctx = new ContextThemeWrapper(getActivity(), R.style.AppTheme );
        AlertDialog dialog =  new AlertDialog.Builder(ctx)
                                    .setView(v)
                                    .setPositiveButton("DONE", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int id) {
                                            saveSettings(v);
                                            mListener.onSettingsDialogClose();
                                            dialog.cancel();
                                        }

                                    })
                                    .create();

        showUserSettings(v);
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;

            ImageButton settingsButton = (ImageButton) activity.findViewById(R.id.btnSettings);
            if(settingsButton != null) {
                settingsButton.setImageResource(R.drawable.settings_red);
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        ImageButton settingsButton = (ImageButton) getActivity().findViewById(R.id.btnSettings);
        if(settingsButton != null) {
            settingsButton.setImageResource(R.drawable.settings_grey);
        }
    }

    public interface OnFragmentInteractionListener {
        public void onSettingsDialogClose();
    }

}
