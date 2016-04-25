package uni.tighearnan.routepicker.Ticket;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.BarcodeFormat;

import uni.tighearnan.routepicker.CurrentTicketSingleton;
import uni.tighearnan.routepicker.PreviousJourneysSingleton;
import uni.tighearnan.routepicker.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TicketFragment extends Fragment {

    private AppCompatTextView mFromTextView;
    private AppCompatTextView mToTextView;
    private AppCompatImageView mBarcodeImageView;
    private AppCompatTextView mBarcodeTextView;

    private Journey mJourney;

    public TicketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ticket, container, false);

        mJourney = CurrentTicketSingleton.get(getActivity()).getJourney();

        mFromTextView = (AppCompatTextView) v.findViewById(R.id.text_view_from);
        mFromTextView.setText(mJourney.getFromTitle());

        mToTextView = (AppCompatTextView) v.findViewById(R.id.text_view_to);
        mToTextView.setText(mJourney.getToTitle());

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        String hash = hash(mJourney.getFromTitle(), mJourney.getToTitle(), mJourney.isReturn());

        mBarcodeImageView = (AppCompatImageView) v.findViewById(R.id.image_view_barcode);
        mBarcodeImageView.setImageBitmap(BarcodeGenerator.generateBarcodeBitmap(
                hash,
                BarcodeFormat.CODE_39,
                metrics.widthPixels,
                400
        ));

        mBarcodeTextView = (AppCompatTextView) v.findViewById(R.id.text_view_barcode);
        mBarcodeTextView.setText(hash);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PreviousJourneysSingleton.get(getActivity()).addJourneyAndPost(mJourney);

        /*if (!previousTickets.contains(mJourney)) {
            previousTickets.add(mJourney);
        } else {
            int index = previousTickets.indexOf(mJourney);
            if(index != 0) {
                Collections.swap(previousTickets, index, 0);
            }
        }*/

//        PreviousJourneysSingleton.get(getActivity()).saveTickets();
    }

    private String hash(String from, String to, boolean isReturn) {
        StringBuilder builder = new StringBuilder();
        builder.append(from.substring(0, 4).toUpperCase())
                .append("-").append(isReturn ? 1 : 0).append("-")
                .append(to.substring(0, 4).toUpperCase());

        return builder.toString();
    }

}
