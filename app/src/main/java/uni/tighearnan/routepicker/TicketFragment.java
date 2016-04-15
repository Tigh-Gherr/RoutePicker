package uni.tighearnan.routepicker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.BarcodeFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class TicketFragment extends Fragment {

    private AppCompatTextView mFromTextView;
    private AppCompatTextView mToTextView;
    private AppCompatImageView mBarcodeImageView;
    private AppCompatTextView mBarcodeTextView;

    public TicketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ticket, container, false);

        String from = getStringIntent("FROM");
        String to = getStringIntent("TO");
        boolean isReturn = getActivity().getIntent().getBooleanExtra("RETURN", false);

        mFromTextView = (AppCompatTextView) v.findViewById(R.id.text_view_from);
        mFromTextView.setText(from);

        mToTextView = (AppCompatTextView) v.findViewById(R.id.text_view_to);
        mToTextView.setText(to);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        String hash = hash(from, to, isReturn);

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

    private String getStringIntent(String key) {
        return getActivity().getIntent().getStringExtra(key);
    }

    private String hash(String from, String to, boolean isReturn) {
        StringBuilder builder = new StringBuilder();
        builder.append(from.substring(0, 4).toUpperCase())
                .append("-").append(isReturn ? 1 : 0).append("-")
                .append(to.substring(0, 4).toUpperCase());

        return builder.toString();
    }

}
