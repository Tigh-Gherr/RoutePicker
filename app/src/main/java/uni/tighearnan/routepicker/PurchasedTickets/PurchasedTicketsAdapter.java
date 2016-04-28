package uni.tighearnan.routepicker.PurchasedTickets;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import uni.tighearnan.routepicker.AdapterItemSelectedListener;
import uni.tighearnan.routepicker.CurrentTicketSingleton;
import uni.tighearnan.routepicker.R;
import uni.tighearnan.routepicker.Ticket.Ticket;
import uni.tighearnan.routepicker.Ticket.TicketActivity;
import uni.tighearnan.routepicker.User;
import uni.tighearnan.routepicker.UserSingleton;

/**
 * Created by tighearnan on 28/04/16.
 */
public class PurchasedTicketsAdapter extends RecyclerView.Adapter<PurchasedTicketsAdapter.PurchasedTicketViewHolder> {

    private ArrayList<Ticket> mTickets;
    private AdapterItemSelectedListener mItemSelectedListener;

    public PurchasedTicketsAdapter(ArrayList<Ticket> tickets) {
        mTickets = tickets;
    }

    public void setItemSelectedListener(AdapterItemSelectedListener itemSelectedListener) {
        mItemSelectedListener = itemSelectedListener;
    }

    @Override
    public PurchasedTicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_purchased_ticket, parent, false);

        return new PurchasedTicketViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PurchasedTicketViewHolder holder, int position) {
        Ticket ticket = mTickets.get(position);

        holder.mFromTextView.setText("From: " + ticket.getFrom());
        holder.mToTextView.setText("To: " + ticket.getTo());
        holder.mUsedCheckBox.setChecked(ticket.isUsed());
    }

    @Override
    public int getItemCount() {
        return mTickets.size();
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
                    mItemSelectedListener.onAdapterItemSelected(getAdapterPosition());
                }
            });
        }
    }
}
