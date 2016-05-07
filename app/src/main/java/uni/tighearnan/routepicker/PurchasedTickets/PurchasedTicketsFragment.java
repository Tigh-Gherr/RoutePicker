package uni.tighearnan.routepicker.PurchasedTickets;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uni.tighearnan.routepicker.CurrentTicketSingleton;
import uni.tighearnan.routepicker.AdapterItemSelectedListener;
import uni.tighearnan.routepicker.R;
import uni.tighearnan.routepicker.Ticket.Ticket;
import uni.tighearnan.routepicker.Ticket.TicketActivity;
import uni.tighearnan.routepicker.User;
import uni.tighearnan.routepicker.UserSingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchasedTicketsFragment extends Fragment {

    private RecyclerView mTicketsRecyclerView;
    private PurchasedTicketsAdapter mTicketsAdapter;

    public PurchasedTicketsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_purchased_tickets, container, false);

        final User user = UserSingleton.get(getActivity()).getUser();

        mTicketsRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_purchasedTickets);
        mTicketsAdapter = new PurchasedTicketsAdapter(user.getTickets());
        mTicketsAdapter.setObjectSelectedListener(new AdapterItemSelectedListener() {
            @Override
            public void onObjectSelected(Object o) {
                if(!(o instanceof Ticket)) {
                    return;
                }

                Intent i = new Intent(getActivity(), TicketActivity.class);
                i.putExtra("PURCHASED", false);

                CurrentTicketSingleton.get(getActivity()).setTicket((Ticket)o);
                startActivity(i);
            }
        });
        mTicketsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTicketsRecyclerView.setAdapter(mTicketsAdapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTicketsAdapter.notifyDataSetChanged();
    }
}
