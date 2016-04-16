package uni.tighearnan.routepicker;


import android.content.Intent;
import android.os.Bundle;
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

    private Ticket mTicket;

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
                mTicket.setReturn(isChecked);
                updateCost();
            }
        });

        mDisabilitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTicket.setDisability(isChecked);
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
            mTicket = CurrentTicketSingleton.get(getActivity()).getTicket();
            setupFromPreviousTicket();
        }

        return v;
    }

    private void setupFromPreviousTicket() {
        String from = mTicket.getFromTitle();
        String to = mTicket.getToTitle();

        mFromTextView.setText(from);
        mToTextView.setText(to);

        mReturnSwitch.setChecked(mTicket.isReturn());

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
            public void onMapReady(GoogleMap googleMap) {
                // TODO: 14/04/16 Dynamic zoom updates.

                if (mTicket.setAddresses()) {
                    googleMap.addMarker(new MarkerOptions().title(mTicket.getFromTitle()).position(mTicket.getFromLatLng()));
                    googleMap.addMarker(new MarkerOptions().title(mTicket.getToTitle()).position(mTicket.getToLatLng()));

                    googleMap.moveCamera(CameraUpdateFactory
                            .newLatLngBounds(mTicket.createLatLngBounds(), 5));
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

        mTicket = new Ticket(getActivity().getApplicationContext(), from, to);

        Random rn = new Random();
        int low = 8;
        int high = 16;
        boolean half = rn.nextBoolean();

        double cost = rn.nextInt(high - low) + low + (half ? 0.5d : 0d);

        mTicket.setCost(cost);
        mTicket.setReturn(mReturnSwitch.isChecked());
        mTicket.setDisability(mDisabilitySwitch.isChecked());
        updateCost();
    }

    private String getStringIntent(String to) {
        return getActivity().getIntent().getStringExtra(to);
    }

    private void updateCost() {
        mCostTextView.setText(getString(R.string.journey_detail_cost, mTicket.getCostRounded()));
    }


    private void proceedToPayment() {
        CurrentTicketSingleton.get(getActivity()).setTicket(mTicket);
        startActivity(new Intent(getActivity(), PaymentActivity.class));
    }
}
