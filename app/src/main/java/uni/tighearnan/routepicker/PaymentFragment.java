package uni.tighearnan.routepicker;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {

    private AppCompatTextView mFromTextView;
    private AppCompatTextView mToTextView;
    private AppCompatTextView mCostTextView;
    private AppCompatTextView mReturnTextView;

    private AppCompatEditText mFirstNameEditText;
    private AppCompatEditText mSurnameEditText;

    private AppCompatSpinner mCardTypeSpinner;

    public PaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_payment, container, false);

        mFromTextView = (AppCompatTextView) v.findViewById(R.id.text_view_from);
        mToTextView = (AppCompatTextView) v.findViewById(R.id.text_view_to);
        mCostTextView = (AppCompatTextView) v.findViewById(R.id.text_view_totalCost);
        mReturnTextView = (AppCompatTextView) v.findViewById(R.id.text_view_return);

        mFirstNameEditText = (AppCompatEditText) v.findViewById(R.id.edit_text_firstName);
        mSurnameEditText = (AppCompatEditText) v.findViewById(R.id.edit_text_surname);

        mCardTypeSpinner = (AppCompatSpinner) v.findViewById(R.id.spinner_cardType);

        setupInformation();

        return v;
    }

    private void setupInformation() {
        Intent sender = getActivity().getIntent();

        String from = sender.getStringExtra("FROM");
        String to = sender.getStringExtra("TO");

        String cost = sender.getStringExtra("COST");
        boolean isReturn = sender.getBooleanExtra("RETURN", false);

        mFromTextView.setText(getString(R.string.journey_detail_from, from));
        mToTextView.setText(getString(R.string.journey_detail_to, to));
        mCostTextView.setText(getString(R.string.journey_detail_cost, cost));
        mReturnTextView.setVisibility(isReturn ? View.VISIBLE : View.GONE);

    }

}
