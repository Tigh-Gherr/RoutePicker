package uni.tighearnan.routepicker;

import android.content.Context;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by tighearnan on 16/04/16.
 */
public class PreviousTicketsSingleton {
    private static PreviousTicketsSingleton sPreviousTicketsSingleton;
    private ArrayList<Ticket> mPreviousTickets;
    private Context mApplicationContext;
    private TicketIO mIO;

    private PreviousTicketsSingleton(Context c) {
        mApplicationContext = c;
        mIO = new TicketIO(mApplicationContext, "previoustickets.json");

        try {
            mPreviousTickets = mIO.loadTickets();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            mPreviousTickets = new ArrayList<>();
        }
    }

    public static PreviousTicketsSingleton get(Context c) {
        if(sPreviousTicketsSingleton == null) {
            sPreviousTicketsSingleton = new PreviousTicketsSingleton(c.getApplicationContext());
        }

        return sPreviousTicketsSingleton;
    }

    public ArrayList<Ticket> getPreviousTickets() {
        return mPreviousTickets;
    }

    public boolean saveTickets() {
        if(mPreviousTickets == null) {
            return false;
        }

        try {
            mIO.saveTickets(mPreviousTickets);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
