package uni.tighearnan.routepicker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.text.DecimalFormat;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class JourneyDetailsFragment extends Fragment {

    private AppCompatTextView mFromTextView;
    private AppCompatTextView mToTextView;

    private SwitchCompat mReturnSwitch;
    private SwitchCompat mDisabilitySwitch;

    private AppCompatTextView mCostTextView;
    private AppCompatButton mConfirmButton;

    private double mBaseCost;

    public JourneyDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_journey_details, container, false);

        mFromTextView = (AppCompatTextView) v.findViewById(R.id.text_view_from);
        mToTextView = (AppCompatTextView) v.findViewById(R.id.text_view_to);
        mCostTextView = (AppCompatTextView) v.findViewById(R.id.text_view_totalCost);

        mReturnSwitch = (SwitchCompat) v.findViewById(R.id.switch_return);
        mReturnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateCost(isChecked);
            }
        });

        setupJourneyDetails();

        return v;
    }

    private void setupJourneyDetails() {
        String from = getActivity().getIntent().getStringExtra("FROM");
        String to = getActivity().getIntent().getStringExtra("TO");

        mFromTextView.setText(getString(R.string.journey_detail_from, from));
        mToTextView.setText(getString(R.string.journey_detail_to, to));

        Random rn = new Random();

        int low = 8;
        int high = 16;
        boolean half = rn.nextBoolean();

        mBaseCost = rn.nextInt(high - low) + low + (half ? 0.5d : 0d);

        updateCost(mReturnSwitch.isChecked());
    }

    private void updateCost(boolean isReturn) {
        DecimalFormat df = new DecimalFormat("0.00");
        String price;

        if(isReturn) {
            price = df.format(mBaseCost * 1.5);
            mCostTextView.setText(getString(R.string.journey_detail_cost, price));
        } else {
            price = df.format(mBaseCost);
            mCostTextView.setText(getString(R.string.journey_detail_cost, price));
        }
    }
}
