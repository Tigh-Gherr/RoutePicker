package uni.tighearnan.routepicker;

import android.content.Context;

import uni.tighearnan.routepicker.Ticket.Journey;

/**
 * Created by tighearnan on 16/04/16.
 */
public class CurrentTicketSingleton {
    private static CurrentTicketSingleton sCurrentTicketSingleton;
    private Journey mJourney;
    private Context mApplicationContext;

    private CurrentTicketSingleton(Context c) {
        mApplicationContext = c;
    }

    public static CurrentTicketSingleton get(Context c) {
        if(sCurrentTicketSingleton == null) {
            sCurrentTicketSingleton = new CurrentTicketSingleton(c.getApplicationContext());
        }

        return sCurrentTicketSingleton;
    }

    public Journey getJourney() {
        return mJourney;
    }

    public void setJourney(Journey journey) {
        mJourney = journey;
    }
}
