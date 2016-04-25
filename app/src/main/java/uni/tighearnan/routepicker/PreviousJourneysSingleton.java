package uni.tighearnan.routepicker;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;

import uni.tighearnan.routepicker.Ticket.Journey;
import uni.tighearnan.routepicker.Ticket.JourneyUploadASyncTask;

/**
 * Created by tighearnan on 16/04/16.
 */
public class PreviousJourneysSingleton {
    private static PreviousJourneysSingleton sPreviousJourneysSingleton;
    private ArrayList<Journey> mPreviousJourneys;
    private Context mApplicationContext;

    private PreviousJourneysSingleton(Context c) {
        mApplicationContext = c;

        if(mPreviousJourneys == null) {
            mPreviousJourneys = new ArrayList<>();
        }
    }

    public static PreviousJourneysSingleton get(Context c) {
        if(sPreviousJourneysSingleton == null) {
            sPreviousJourneysSingleton = new PreviousJourneysSingleton(c.getApplicationContext());
        }

        return sPreviousJourneysSingleton;
    }

    public ArrayList<Journey> getPreviousJourneys() {
        return mPreviousJourneys;
    }

    public void addJourney(Journey journey) {
        if(!mPreviousJourneys.contains(journey)) {
            mPreviousJourneys.add(0, journey);
        } else {
            int index = mPreviousJourneys.indexOf(journey);
            for(int i = index; i > 0; i--) {
                Collections.swap(mPreviousJourneys, i, i - 1);
            }
        }

        if(mPreviousJourneys.size() > 4) {
            mPreviousJourneys.remove(mPreviousJourneys.size() - 1);
        }
    }

    public void addJourneyAndPost(Journey journey) {
        addJourney(journey);

        JourneyUploadASyncTask aSyncTask = new JourneyUploadASyncTask(mApplicationContext);
        aSyncTask.execute(journey);
    }

    public void reset() {
        sPreviousJourneysSingleton = null;
        mApplicationContext = null;
        mPreviousJourneys = null;
    }
}
