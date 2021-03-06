package uni.tighearnan.routepicker;

import android.content.Context;

import uni.tighearnan.routepicker.Ticket.Ticket;

/**
 * Created by tighearnan on 27/04/16.
 */
public class CurrentTicketSingleton {
    private static CurrentTicketSingleton sCurrentTicketSingleton;
    private Ticket mTicket;
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

    public void setTicket(Ticket ticket) {
        mTicket = ticket;
    }

    public Ticket getTicket() {
        return mTicket;
    }
}
