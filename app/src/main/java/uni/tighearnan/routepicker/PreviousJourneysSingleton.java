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
            moveToTop(journey);
        }

        if(mPreviousJourneys.size() > 4) {
            mPreviousJourneys.remove(mPreviousJourneys.size() - 1);
        }
    }

    private void moveToTop(Journey journey) {
        int index = mPreviousJourneys.indexOf(journey);
        for(int i = index; i > 0; i--) {
            Collections.swap(mPreviousJourneys, i, i - 1);
        }
    }

    public void addJourneyAndPost(Journey journey) {
        if(!journeyIsUnique(journey)) {
            return;
        }
        addJourney(journey);

        JourneyUploadASyncTask aSyncTask = new JourneyUploadASyncTask(mApplicationContext);
        aSyncTask.execute(journey);
    }

    private boolean journeyIsUnique(Journey journey) {
        String from = journey.getFromTitle();
        String to = journey.getToTitle();

        for(int i = 0; i < mPreviousJourneys.size(); i++) {
            Journey j = mPreviousJourneys.get(i);
            if(from.equalsIgnoreCase(j.getFromTitle()) || from.equalsIgnoreCase(j.getToTitle())) {
                if (to.equalsIgnoreCase(j.getFromTitle()) || to.equalsIgnoreCase(j.getToTitle())) {
                    moveToTop(j);
                    return false;
                }
            }
        }

        return true;
    }

    public void reset() {
        sPreviousJourneysSingleton = null;
        mApplicationContext = null;
        mPreviousJourneys = null;
    }
}
