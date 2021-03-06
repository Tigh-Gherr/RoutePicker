package uni.tighearnan.routepicker.Ticket;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;

import uni.tighearnan.routepicker.CurrentJourneySingleton;
import uni.tighearnan.routepicker.CurrentTicketSingleton;
import uni.tighearnan.routepicker.PostASyncTask;
import uni.tighearnan.routepicker.PostListener;
import uni.tighearnan.routepicker.PreviousJourneysSingleton;
import uni.tighearnan.routepicker.R;
import uni.tighearnan.routepicker.User;
import uni.tighearnan.routepicker.UserSingleton;


/**
 * A simple {@link Fragment} subclass.
 */
public class TicketFragment extends Fragment {

    private AppCompatTextView mFromTextView;
    private AppCompatTextView mToTextView;
    private AppCompatImageView mBarcodeImageView;
    private AppCompatTextView mBarcodeTextView;

    private Ticket mTicket;

    public TicketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ticket, container, false);

        mTicket = CurrentTicketSingleton.get(getActivity()).getTicket();

        mFromTextView = (AppCompatTextView) v.findViewById(R.id.text_view_from);
        mFromTextView.setText(mTicket.getFrom());

        mToTextView = (AppCompatTextView) v.findViewById(R.id.text_view_to);
        mToTextView.setText(mTicket.getTo());

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        mBarcodeImageView = (AppCompatImageView) v.findViewById(R.id.image_view_barcode);
        mBarcodeImageView.setImageBitmap(BarcodeGenerator.generateBarcodeBitmap(
                mTicket.getBarcode(),
                BarcodeFormat.CODE_39,
                metrics.widthPixels,
                400
        ));

        mBarcodeTextView = (AppCompatTextView) v.findViewById(R.id.text_view_barcode);
        mBarcodeTextView.setText(mTicket.getBarcode());

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean purchased = getActivity().getIntent().getBooleanExtra("PURCHASED", false);

        if (purchased) {
            int id = UserSingleton.get(getActivity()).getUser().getId();

            String url = getString(R.string.upload_ticket_url, id,
                    mTicket.getFrom(),
                    mTicket.getTo(),
                    mTicket.isReturn() ? "1" : "0",
                    mTicket.getBarcode(),
                    mTicket.isUsed() ? "1" : "0");

            UploadTicketASyncTask aSyncTask = new UploadTicketASyncTask(mTicket);
            aSyncTask.setPostListener(new PostListener() {
                @Override
                public void onPostComplete() {
                    User user = UserSingleton.get(getActivity()).getUser();
                    user.getTickets().add(0, mTicket);
                }
            });
            aSyncTask.execute(url);


        } else {
            String url = getString(R.string.upload_ticket_used_url, mTicket.getId());
            PostASyncTask aSyncTask = new PostASyncTask();
            aSyncTask.execute(url);

            mTicket.setUsed(true);
        }
    }
}
