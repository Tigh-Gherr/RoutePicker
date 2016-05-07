package uni.tighearnan.routepicker.PurchasedTickets;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import uni.tighearnan.routepicker.AdapterItemSelectedListener;
import uni.tighearnan.routepicker.R;
import uni.tighearnan.routepicker.Ticket.Ticket;

/**
 * Created by tighearnan on 28/04/16.
 */
public class PurchasedTicketsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER = 0;
    private static final int TICKET = 1;

    private ArrayList<Object> mData;
    private AdapterItemSelectedListener mItemSelectedListener;

    public PurchasedTicketsAdapter(ArrayList<Ticket> tickets) {
        mData = new ArrayList<>();
        mData.add(R.drawable.golden_gate_bridge);
        mData.addAll(tickets);
        mData.add(R.drawable.bus);
    }

    public void setItemSelectedListener(AdapterItemSelectedListener itemSelectedListener) {
        mItemSelectedListener = itemSelectedListener;
    }

    private void configureHeader(HeaderViewHolder holder, int position) {
        holder.mHeader.setImageResource((Integer) mData.get(position));
    }

    private void configureTicket(PurchasedTicketViewHolder holder, int position) {
        Ticket ticket = (Ticket) mData.get(position);
        holder.mFromTextView.setText("From: " + ticket.getFrom());
        holder.mToTextView.setText("To: " + ticket.getTo());
        holder.mUsedCheckBox.setChecked(ticket.isUsed());
    }

    @Override
    public int getItemViewType(int position) {
        if(mData.get(position) instanceof Ticket) {
            return TICKET;
        } else if(mData.get(position) instanceof Integer) {
            return HEADER;
        }

        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh = null;
        switch (viewType) {
            case HEADER:
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_purchased_ticket_header, parent, false);
                vh = new HeaderViewHolder(v);
                break;
            case TICKET:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_purchased_ticket, parent, false);

                vh = new PurchasedTicketViewHolder(v);
                break;
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case HEADER:
                configureHeader((HeaderViewHolder) holder, position);
                break;
            case TICKET:
                configureTicket((PurchasedTicketViewHolder) holder, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class PurchasedTicketViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView mFromTextView;
        AppCompatTextView mToTextView;
        AppCompatCheckBox mUsedCheckBox;

        public PurchasedTicketViewHolder(View itemView) {
            super(itemView);

            mFromTextView = (AppCompatTextView) itemView.findViewById(R.id.text_view_from);
            mToTextView = (AppCompatTextView) itemView.findViewById(R.id.text_view_to);
            mUsedCheckBox = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox_used);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemSelectedListener.onObjectSelected(mData.get(getAdapterPosition()));
                }
            });
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView mHeader;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            mHeader = (AppCompatImageView) itemView.findViewById(R.id.image_view_header);
        }
    }
}
