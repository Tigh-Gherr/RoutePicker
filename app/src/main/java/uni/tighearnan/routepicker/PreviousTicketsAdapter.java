package uni.tighearnan.routepicker;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by tighearnan on 16/04/16.
 */
public class PreviousTicketsAdapter extends RecyclerView.Adapter<PreviousTicketsAdapter.PreviousTicketsViewHolder> {

    private ArrayList<Ticket> mTickets;

    public PreviousTicketsAdapter(ArrayList<Ticket> tickets) {
        mTickets = tickets;
    }

    @Override
    public PreviousTicketsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_previous_ticket, parent, false);

        return new PreviousTicketsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PreviousTicketsViewHolder holder, int position) {
        Ticket current = mTickets.get(position);
        holder.mRouteName.setText(current.getFromTitle() + " - " + current.getToTitle());
    }

    @Override
    public int getItemCount() {
        return mTickets.size();
    }

    public class PreviousTicketsViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView mRouteName;

        public PreviousTicketsViewHolder(View itemView) {
            super(itemView);

            mRouteName = (AppCompatTextView) itemView.findViewById(R.id.text_view_routeName);
        }
    }
}
