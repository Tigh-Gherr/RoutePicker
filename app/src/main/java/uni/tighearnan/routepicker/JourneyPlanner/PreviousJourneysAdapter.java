package uni.tighearnan.routepicker.JourneyPlanner;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import uni.tighearnan.routepicker.AdapterItemSelectedListener;
import uni.tighearnan.routepicker.R;
import uni.tighearnan.routepicker.Ticket.Journey;

/**
 * Created by tighearnan on 16/04/16.
 */
public class PreviousJourneysAdapter extends RecyclerView.Adapter<PreviousJourneysAdapter.PreviousTicketsViewHolder> {

    private ArrayList<Journey> mJourneys;
    private AdapterItemSelectedListener mObjectSelectedListener;

    public PreviousJourneysAdapter(ArrayList<Journey> journeys) {
        mJourneys = journeys;
    }

    public void setObjectSelectedListener(AdapterItemSelectedListener objectSelectedListener) {
        mObjectSelectedListener = objectSelectedListener;
    }

    @Override
    public PreviousTicketsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_previous_journey, parent, false);

        return new PreviousTicketsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PreviousTicketsViewHolder holder, int position) {
        Journey current = mJourneys.get(position);
        holder.mRouteFrom.setText("From: " + current.getFromTitle());
        holder.mRouteTo.setText("To: " + current.getToTitle());
    }

    @Override
    public int getItemCount() {
        return mJourneys.size();
    }

    public class PreviousTicketsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        AppCompatTextView mRouteFrom;
        AppCompatTextView mRouteTo;

        public PreviousTicketsViewHolder(View itemView) {
            super(itemView);

            mRouteFrom = (AppCompatTextView) itemView.findViewById(R.id.text_view_from);
            mRouteTo = (AppCompatTextView) itemView.findViewById(R.id.text_view_to);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mObjectSelectedListener.onObjectSelected(mJourneys.get(getLayoutPosition()));
        }
    }
}
