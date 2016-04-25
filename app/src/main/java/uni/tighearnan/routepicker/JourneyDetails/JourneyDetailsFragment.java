package uni.tighearnan.routepicker.JourneyDetails;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

import uni.tighearnan.routepicker.CurrentTicketSingleton;
import uni.tighearnan.routepicker.Payment.PaymentActivity;
import uni.tighearnan.routepicker.R;
import uni.tighearnan.routepicker.Ticket.Journey;


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

    private Journey mJourney;

//    private double mBaseCost;

    public JourneyDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_journey_details, container, false);

        mFromTextView = (AppCompatTextView) v.findViewById(R.id.text_view_from);
        mToTextView = (AppCompatTextView) v.findViewById(R.id.text_view_to);
        mCostTextView = (AppCompatTextView) v.findViewById(R.id.text_view_totalCost);

        mReturnSwitch = (SwitchCompat) v.findViewById(R.id.switch_return);
        mDisabilitySwitch = (SwitchCompat) v.findViewById(R.id.switch_disability);

        mReturnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mJourney.setReturn(isChecked);
                updateCost();
            }
        });

        mDisabilitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mJourney.setDisability(isChecked);
            }
        });

        mConfirmButton = (AppCompatButton) v.findViewById(R.id.button_confirm);
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedToPayment();
            }
        });

        if (getActivity().getIntent().getBooleanExtra("NEW", false)) {
            setupJourneyDetails();
        } else {
            mJourney = CurrentTicketSingleton.get(getActivity()).getJourney();
            setupFromPreviousTicket();
        }

        return v;
    }

    private void setupFromPreviousTicket() {
        String from = mJourney.getFromTitle();
        String to = mJourney.getToTitle();

        mFromTextView.setText(from);
        mToTextView.setText(to);

        mReturnSwitch.setChecked(mJourney.isReturn());

        updateCost();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupMap();
    }

    private void setupMap() {
        MapView routeMap = (MapView) getView().findViewById(R.id.map_view_route);
        routeMap.onCreate(null);
        routeMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                // TODO: 14/04/16 Dynamic zoom updates.

                if (mJourney.setAddresses()) {
                    googleMap.addMarker(new MarkerOptions().title(mJourney.getFromTitle()).position(mJourney.getFromLatLng()));
                    googleMap.addMarker(new MarkerOptions().title(mJourney.getToTitle()).position(mJourney.getToLatLng()));

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            googleMap.moveCamera(CameraUpdateFactory
                                    .newLatLngBounds(mJourney.createLatLngBounds(), 5));
                        }
                    }, 100);
                }

            }
        });
    }

    private LatLng midpoint(LatLng from, LatLng to) {
        double totalLat = from.latitude + to.latitude;
        double totalLong = from.longitude + to.longitude;

        return new LatLng(totalLat / 2, totalLong / 2);
    }

    private void setupJourneyDetails() {
        String from = getStringIntent("FROM");
        String to = getStringIntent("TO");

        mFromTextView.setText(getString(R.string.journey_detail_from, from));
        mToTextView.setText(getString(R.string.journey_detail_to, to));

        mJourney = new Journey(getActivity().getApplicationContext(), from, to);

        Random rn = new Random();
        int low = 8;
        int high = 16;
        boolean half = rn.nextBoolean();

        double cost = rn.nextInt(high - low) + low + (half ? 0.5d : 0d);

        mJourney.setCost(cost);
        mJourney.setReturn(mReturnSwitch.isChecked());
        mJourney.setDisability(mDisabilitySwitch.isChecked());
        updateCost();
    }

    private String getStringIntent(String to) {
        return getActivity().getIntent().getStringExtra(to);
    }

    private void updateCost() {
        mCostTextView.setText(getString(R.string.journey_detail_cost, mJourney.getCostRounded()));
    }


    private void proceedToPayment() {
        CurrentTicketSingleton.get(getActivity()).setJourney(mJourney);
        startActivity(new Intent(getActivity(), PaymentActivity.class));
    }
}
