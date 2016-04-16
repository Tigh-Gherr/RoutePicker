package uni.tighearnan.routepicker;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentFragment extends Fragment {

    private AppCompatTextView mFromTextView;
    private AppCompatTextView mToTextView;
    private AppCompatTextView mCostTextView;
    private AppCompatTextView mReturnTextView;

    private AppCompatEditText mFirstNameEditText;
    private AppCompatEditText mSurnameEditText;

    private AppCompatSpinner mCardTypeSpinner;

    private AppCompatEditText mCardNumberEditText;
    private AppCompatEditText mCardExpMonthEditText;
    private AppCompatEditText mCardExpYearEditText;
    private AppCompatEditText mCardCVCNumberEditText;

    private AppCompatEditText mBillingAddressLine1EditText;
    private AppCompatEditText mBillingAddressLine2EditText;

    private AppCompatButton mPayButton;

    private Ticket mTicket;

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

        mCardNumberEditText = (AppCompatEditText) v.findViewById(R.id.edit_text_cardNumber);
        mCardExpMonthEditText = (AppCompatEditText) v.findViewById(R.id.edit_text_expMonth);
        mCardExpYearEditText = (AppCompatEditText) v.findViewById(R.id.edit_text_expYear);
        mCardCVCNumberEditText = (AppCompatEditText) v.findViewById(R.id.edit_text_CVCnumber);

        mBillingAddressLine1EditText = (AppCompatEditText) v.findViewById(R.id.edit_text_addressLine1);
        mBillingAddressLine2EditText = (AppCompatEditText) v.findViewById(R.id.edit_text_addressLine2);

        mPayButton = (AppCompatButton) v.findViewById(R.id.button_pay);
        mPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBillingInfo();
            }
        });

        setupInformation();

        return v;
    }

    private void setupInformation() {
        Intent sender = getActivity().getIntent();

        mTicket = TicketSingleton.get(getActivity()).getTicket();

//        String from = sender.getStringExtra("FROM");
//        String to = sender.getStringExtra("TO");

//        String cost = sender.getStringExtra("COST");
//        boolean isReturn = sender.getBooleanExtra("RETURN", false);

        mFromTextView.setText(getString(R.string.journey_detail_from, mTicket.getFromTitle()));
        mToTextView.setText(getString(R.string.journey_detail_to, mTicket.getToTitle()));
        mCostTextView.setText(getString(R.string.journey_detail_cost, mTicket.getCostRounded()));
        mReturnTextView.setVisibility(mTicket.isReturn() ? View.VISIBLE : View.GONE);
    }

    private void checkBillingInfo() {
        mFirstNameEditText.setError(null);
        mSurnameEditText.setError(null);
        mCardNumberEditText.setError(null);
        mCardExpMonthEditText.setError(null);
        mCardExpYearEditText.setError(null);
        mBillingAddressLine1EditText.setError(null);
        mBillingAddressLine2EditText.setError(null);
        mCardCVCNumberEditText.setError(null);

        boolean cancel = false;

        String firstName = mFirstNameEditText.getText().toString();
        String surname = mSurnameEditText.getText().toString();
        String cardType = mCardTypeSpinner.getSelectedItem().toString();
        String cardNumber = mCardNumberEditText.getText().toString();
        String cvcNum = mCardCVCNumberEditText.getText().toString();

        String billingLine1 = mBillingAddressLine1EditText.getText().toString();
        String billingLine2 = mBillingAddressLine2EditText.getText().toString();

        View errorView = null;

        if(isEmpty(firstName)) {
            mFirstNameEditText.setError("Field cannot be left empty.");
            errorView = mFirstNameEditText;
            cancel = true;
        } else if(!isValid(firstName) || containsNumbers(firstName)) {
            mFirstNameEditText.setError("Name is not valid.");
            errorView = mFirstNameEditText;
            cancel = true;
        }

        if(isEmpty(surname)) {
            mSurnameEditText.setError("Field cannot be left empty");
            errorView = mSurnameEditText;
            cancel = true;
        } else if(!isValid(surname) || containsNumbers(surname)) {
            mSurnameEditText.setError("Name is not valid.");
            if(errorView == null) {
                errorView = mSurnameEditText;
            }
            cancel = true;
        }

        if(!isValidCardNumber(cardNumber)) {
            mCardNumberEditText.setError("Card number is invalid.");
            if(errorView == null) {
                errorView = mCardNumberEditText;
            }
            cancel = true;
        }

        int expMonth = 0;
        int expYear = 0;

        if(!isEmpty(mCardExpMonthEditText)) {
            expMonth = Integer.parseInt(mCardExpMonthEditText.getText().toString());
        }

        if(!isEmpty(mCardExpYearEditText)) {
            expYear = Integer.parseInt(mCardExpYearEditText.getText().toString());
        }

        Date today = new Date();
        DateFormat dateFormat = new DateFormat();
        int currentMonth = Integer.parseInt(dateFormat.format("M", today).toString());
        int currentYear = Integer.parseInt(dateFormat.format("yy", today).toString());

        if(expMonth > 12 || 1 > expMonth) {
            mCardExpMonthEditText.setError("Invalid month.");

            if(errorView == null) {
                errorView = mCardExpMonthEditText;
            }
            cancel = true;

        }

        if(expYear == currentYear) {
            if(expMonth <= currentMonth) {
                mCardExpYearEditText.setError("Date has expired.");
                mCardExpMonthEditText.setError("Date has expired.");
                if(errorView == null) {
                    errorView = mCardExpMonthEditText;
                }
                cancel = true;
            }

        } else if (expYear < currentYear) {
            mCardExpYearEditText.setError("Invalid year.");

            if(errorView == null) {
                errorView = mCardExpMonthEditText;
            }
            cancel = true;
        }

        if(isEmpty(billingLine1)) {
            mBillingAddressLine1EditText.setError("Cannot be left empty.");

            if(errorView == null) {
                errorView = mBillingAddressLine1EditText;
            }

            cancel = true;
        }

        if(!isValidCVCNumber(cvcNum)) {
            mCardCVCNumberEditText.setError("CVC number must be 3 digits long.");

            if(errorView == null) {
                errorView = mCardCVCNumberEditText;
            }

            cancel = true;
        }

        if(cancel) {
            errorView.requestFocus();
        } else {
            startActivity(new Intent(getActivity(), TicketActivity.class));
        }
    }

    private boolean isValidCardNumber(String s) {
        return s.length() == 16;
    }

    private boolean isValidCVCNumber(String s) {
        return s.length() == 3;
    }

    private boolean containsNumbers(String s) {
        return s.matches(".*[0-9].*");
    }

    private boolean isValid(String s) {
        return s.length() > 2;
    }

    private boolean isEmpty(String s) {
        return s.length() == 0;
    }

    private boolean isEmpty(AppCompatEditText editText) {
        return editText.getText().length() == 0;
    }

}
