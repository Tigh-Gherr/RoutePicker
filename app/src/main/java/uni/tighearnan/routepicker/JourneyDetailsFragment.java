package uni.tighearnan.routepicker;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
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

    private AppCompatImageView mTestBarcodeImageView;

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
        mDisabilitySwitch = (SwitchCompat) v.findViewById(R.id.switch_disability);

        mReturnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateCost(isChecked);
            }
        });

        mConfirmButton = (AppCompatButton) v.findViewById(R.id.button_confirm);
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedToPayment();
            }
        });

//        mTestBarcodeImageView = (AppCompatImageView) v.findViewById(R.id.image_view_testBarcode);

        MapView routeMap = (MapView) v.findViewById(R.id.map_view_route);
        routeMap.onCreate(savedInstanceState);
        routeMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                try {
                    // TODO: 14/04/16 Dynamic zoom updates.
                    LatLng from = addMarker(googleMap, getStringIntent("FROM"));
                    LatLng to = addMarker(googleMap, getStringIntent("TO"));
                    LatLngBounds bounds = new LatLngBounds.Builder().include(from).include(to).build();
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(midpoint(from, to)));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        setupJourneyDetails();

//        generateBarcodeZxing();

//        DisplayMetrics metrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        int width = metrics.widthPixels - ((metrics.widthPixels / 16) * 2);

//        mTestBarcodeImageView.setImageBitmap(BarcodeGenerator.generateBarcodeBitmap("012345678901",
//                BarcodeFormat.ITF, metrics.widthPixels, 400));
        return v;
    }

    private LatLng midpoint(LatLng from, LatLng to) {
        double totalLat = from.latitude + to.latitude;
        double totalLong = from.longitude + to.longitude;

        return new LatLng(totalLat / 2, totalLong / 2);
    }

    private LatLng addMarker(GoogleMap googleMap, String place) throws IOException {
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> addresses;
        String search = place + " UK";
        addresses = geocoder.getFromLocationName(search, 1);
//        Toast.makeText(getActivity(), search, Toast.LENGTH_SHORT).show();
        LatLng location = null;
        if(addresses.size() > 0) {
//            Toast.makeText(getActivity(), addresses.get(0).getFeatureName(), Toast.LENGTH_SHORT).show();
            location = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            googleMap.addMarker(new MarkerOptions().position(location));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        }

        return location;
    }

/*    public void toggleBarcode() {
        boolean isVisible = mTestBarcodeImageView.getVisibility() == View.VISIBLE;

        mTestBarcodeImageView.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }*/

    private void setupJourneyDetails() {
        String from = getStringIntent("FROM");
        String to = getStringIntent("TO");

        mFromTextView.setText(getString(R.string.journey_detail_from, from));
        mToTextView.setText(getString(R.string.journey_detail_to, to));

        Random rn = new Random();

        int low = 8;
        int high = 16;
        boolean half = rn.nextBoolean();

        mBaseCost = rn.nextInt(high - low) + low + (half ? 0.5d : 0d);

        updateCost(mReturnSwitch.isChecked());
    }

    private String getStringIntent(String to) {
        return getActivity().getIntent().getStringExtra(to);
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

    private void proceedToPayment() {
        Intent i = new Intent(getActivity(), PaymentActivity.class);
        DecimalFormat df = new DecimalFormat("0.00");
        boolean isReturn = mReturnSwitch.isChecked();

        i.putExtra("FROM", getStringIntent("FROM"));
        i.putExtra("TO", getStringIntent("TO"));
        i.putExtra("COST", df.format(isReturn ? mBaseCost * 1.5 : mBaseCost));
        i.putExtra("RETURN", isReturn);

        startActivity(i);
    }
}
