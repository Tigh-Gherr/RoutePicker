package uni.tighearnan.routepicker;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

import uni.tighearnan.routepicker.Ticket.Journey;

/**
 * Created by tighearnan on 16/04/16.
 */
public class PreviousJourneysAdapter extends RecyclerView.Adapter<PreviousJourneysAdapter.PreviousTicketsViewHolder> {

    // TODO: Wire up buttons

    private ArrayList<Journey> mJourneys;
    private AdapterItemSelectedListener mItemSelectedListener;

    public PreviousJourneysAdapter(ArrayList<Journey> journeys) {
        mJourneys = journeys;
    }

    public void setItemSelectedListener(AdapterItemSelectedListener listener) {
        mItemSelectedListener = listener;
    }

    @Override
    public PreviousTicketsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_previous_ticket, parent, false);

        return new PreviousTicketsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PreviousTicketsViewHolder holder, int position) {
        Journey current = mJourneys.get(position);
//        holder.mRouteName.setText(current.getFromTitle() + " - " + current.getToTitle());
        holder.mRouteFrom.setText("From: " + current.getFromTitle());
        holder.mRouteTo.setText("To: " + current.getToTitle());
    }

    @Override
    public int getItemCount() {
        return mJourneys.size();
    }

    public class PreviousTicketsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

//        AppCompatTextView mRouteName;
        AppCompatTextView mRouteFrom;
        AppCompatTextView mRouteTo;

        public PreviousTicketsViewHolder(View itemView) {
            super(itemView);

//            mRouteName = (AppCompatTextView) itemView.findViewById(R.id.text_view_routeName);
            mRouteFrom = (AppCompatTextView) itemView.findViewById(R.id.text_view_from);
            mRouteTo = (AppCompatTextView) itemView.findViewById(R.id.text_view_to);

            FrameLayout layout = (FrameLayout) itemView.findViewById(R.id.frame_layout_content);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemSelectedListener.onAdapterItemSelected(getLayoutPosition());
        }
    }
}