package uni.tighearnan.routepicker;

import android.content.Context;

import uni.tighearnan.routepicker.Ticket.Journey;

/**
 * Created by tighearnan on 16/04/16.
 */
public class CurrentJourneySingleton {
    private static CurrentJourneySingleton sCurrentJourneySingleton;
    private Journey mJourney;
    private Context mApplicationContext;

    private CurrentJourneySingleton(Context c) {
        mApplicationContext = c;
    }

    public static CurrentJourneySingleton get(Context c) {
        if(sCurrentJourneySingleton == null) {
            sCurrentJourneySingleton = new CurrentJourneySingleton(c.getApplicationContext());
        }

        return sCurrentJourneySingleton;
    }

    public Journey getJourney() {
        return mJourney;
    }

    public void setJourney(Journey journey) {
        mJourney = journey;
    }
}
