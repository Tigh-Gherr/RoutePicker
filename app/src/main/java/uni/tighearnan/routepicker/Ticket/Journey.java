package uni.tighearnan.routepicker.Ticket;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by tighearnan on 16/04/16.
 */
public class Journey {

    private String mFromTitle;
    private String mToTitle;

    private double mCost;
    private boolean mReturn;
    private boolean mDisability;

    private Address mFromAddress;
    private Address mToAddress;

    private LatLng mFromLatLng;
    private LatLng mToLatLng;

    private Context mApplicationContext;

    public Journey(Context applicationContext, String from, String to) {
        mFromTitle = from;
        mToTitle = to;

        mApplicationContext = applicationContext.getApplicationContext();
    }

    public boolean setAddresses() {
        mFromAddress = setAddress(mFromTitle);
        mToAddress = setAddress(mToTitle);

        return mFromAddress != null && mToAddress != null;
    }

    private Address setAddress(String place) {
        Geocoder geocoder = new Geocoder(mApplicationContext);
        String searchTerm = place + " UK";

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocationName(searchTerm, 1);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if(addresses != null && addresses.size() > 0) {
            return  addresses.get(0);
        }

        return null;
    }

    public LatLng getFromLatLng() {
        if(mFromLatLng == null) {
            mFromLatLng = new LatLng(mFromAddress.getLatitude(), mFromAddress.getLongitude());
        }
        return mFromLatLng;
    }

    public LatLng getToLatLng() {
        if(mToLatLng == null) {
            mToLatLng = new LatLng(mToAddress.getLatitude(), mToAddress.getLongitude());
        }
        return mToLatLng;
    }

    public LatLngBounds createLatLngBounds() {
        return new LatLngBounds.Builder().include(getFromLatLng()).include(getToLatLng()).build();
    }

    public String getFromTitle() {
        return mFromTitle;
    }

    public String getToTitle() {
        return mToTitle;
    }

    public double getCost() {
        return isReturn() ? mCost * 1.5 : mCost;
    }

    public double getBaseCost() {
        return mCost;
    }

    public String getCostRounded() {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(getCost());
    }

    public boolean isReturn() {
        return mReturn;
    }

    public void setDisability(boolean disability) {
        mDisability = disability;
    }

    public boolean isDisability() {
        return mDisability;
    }

    public void setReturn(boolean aReturn) {
        mReturn = aReturn;
    }

    public void setCost(double cost) {
        mCost = cost;
    }

}
