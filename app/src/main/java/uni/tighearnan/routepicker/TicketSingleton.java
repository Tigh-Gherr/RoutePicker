package uni.tighearnan.routepicker;

import android.content.Context;

/**
 * Created by tighearnan on 16/04/16.
 */
public class TicketSingleton {
    private static TicketSingleton sTicketSingleton;
    private Ticket mTicket;
    private Context mApplicationContext;

    private TicketSingleton(Context c) {
        mApplicationContext = c;
    }

    public static TicketSingleton get(Context c) {
        if(sTicketSingleton == null) {
            sTicketSingleton = new TicketSingleton(c.getApplicationContext());
        }

        return sTicketSingleton;
    }

    public Ticket getTicket() {
        return mTicket;
    }

    public void setTicket(Ticket ticket) {
        mTicket = ticket;
    }
}
