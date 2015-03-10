package infrared5.com.red5proandroid;

import com.infrared5.secondscreen.client.SecondScreen;

import infrared5.com.red5proandroid.publish.Publish;
import infrared5.com.red5proandroid.secondscreen.SecondScreenActivity;
import infrared5.com.red5proandroid.subscribe.Subscribe;

/**
 * Created by toddanderson on 10/23/14.
 */
public enum AppState {
    PUBLISH(0),
    SUBSCRIBE(1),
    SECONDSCREEN(2);

    final int value;

    private AppState(int num) {
        this.value = num;
    }

    public int getValue() {
        return this.value;
    }

    public static AppState fromValue(int value) {
        for(AppState state : values()) {
            if(state.getValue() == value) {
                return state;
            }
        }
        return null;
    }
}