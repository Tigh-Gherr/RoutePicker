package uni.tighearnan.routepicker.JourneyPlanner;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uni.tighearnan.routepicker.AdapterItemSelectedListener;
import uni.tighearnan.routepicker.CurrentJourneySingleton;
import uni.tighearnan.routepicker.JourneyDetails.JourneyDetailsActivity;
import uni.tighearnan.routepicker.PreviousJourneysSingleton;
import uni.tighearnan.routepicker.PurchasedTickets.PurchasedTicketsActivity;
import uni.tighearnan.routepicker.R;
import uni.tighearnan.routepicker.Ticket.Journey;


/**
 * A simple {@link Fragment} subclass.
 */
public class JourneyPlannerFragment extends Fragment {

    private AppCompatEditText mFromEditText;
    private AppCompatEditText mToEditText;
    private AppCompatImageButton mClearFromImageButton;
    private AppCompatImageButton mClearToImageButton;
    private AppCompatButton mGoButton;
    private AppCompatButton mTicketsButton;

    private RecyclerView mPreviousJourneysRecyclerView;
    private PreviousJourneysAdapter mPreviousJourneysAdapter;

    private View.OnClickListener mClearListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clearText(getCoupledEditText(v));
        }
    };

    private AppCompatEditText getCoupledEditText(View v) {
        return v == mClearFromImageButton ? mFromEditText : mToEditText;
    }

    private void clearText(AppCompatEditText editText) {
        editText.setText("");
        editText.requestFocus();
    }

    private View.OnFocusChangeListener mFocusChangeListener
            = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            AppCompatImageButton button = null;
            if (v == mFromEditText) {
                button = mClearFromImageButton;
            } else if (v == mToEditText) {
                button = mClearToImageButton;
            }

            if (hasFocus) {
                DrawableCompat.setTint(button.getDrawable(), ContextCompat.getColor(getActivity(), R.color.colorControlActivated));
            } else {
                DrawableCompat.setTint(button.getDrawable(), ContextCompat.getColor(getActivity(), R.color.md_white_1000));
            }
        }
    };

    public JourneyPlannerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_journey_planner, container, false);

        mFromEditText = (AppCompatEditText) v.findViewById(R.id.edit_text_travellingFrom);
        mFromEditText.setOnFocusChangeListener(mFocusChangeListener);
        mToEditText = (AppCompatEditText) v.findViewById(R.id.edit_text_travellingTo);
        mToEditText.setOnFocusChangeListener(mFocusChangeListener);

        mClearFromImageButton = (AppCompatImageButton) v.findViewById(R.id.image_button_clearTravellingFrom);
        mClearFromImageButton.setOnClickListener(mClearListener);
        mClearToImageButton = (AppCompatImageButton) v.findViewById(R.id.image_button_clearTravellingTo);
        mClearToImageButton.setOnClickListener(mClearListener);

        mGoButton = (AppCompatButton) v.findViewById(R.id.button_go);
        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryLocations();
            }
        });

        mPreviousJourneysRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_previousJourneys);
        mPreviousJourneysAdapter = new PreviousJourneysAdapter(PreviousJourneysSingleton.get(getActivity()).getPreviousJourneys());

        mPreviousJourneysAdapter.setItemSelectedListener(new AdapterItemSelectedListener() {
            @Override
            public void onAdapterItemSelected(int position) {
                Journey journey = PreviousJourneysSingleton.get(getActivity()).getPreviousJourneys().get(position);
                CurrentJourneySingleton.get(getActivity()).setJourney(journey);

                startActivity(new Intent(getActivity(), JourneyDetailsActivity.class));
            }
        });

        mPreviousJourneysRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPreviousJourneysRecyclerView.setAdapter(mPreviousJourneysAdapter);

        mTicketsButton = (AppCompatButton) v.findViewById(R.id.button_purchasedTickets);
        mTicketsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PurchasedTicketsActivity.class));
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPreviousJourneysAdapter.notifyDataSetChanged();
    }

    private void tryLocations() {
        mFromEditText.setError(null);
        mToEditText.setError(null);
        boolean cancel = false;

        String from = mFromEditText.getText().toString();
        String to = mToEditText.getText().toString();

        View errorView = null;

        if (!isValid(mFromEditText)) {
            mFromEditText.setError("\"" + from + "\" is not a valid location.");
            errorView = mFromEditText;
            cancel = true;
        }

        if (!isValid(mToEditText)) {
            mToEditText.setError("\"" + to + "\" is not a valid location.");
            if (errorView == null) {
                errorView = mToEditText;
            }
            cancel = true;
        }

        if (cancel) {
            errorView.requestFocus();
        } else {
            Intent i = new Intent(getActivity(), JourneyDetailsActivity.class);
            i.putExtra("FROM", from);
            i.putExtra("TO", to);
            i.putExtra("NEW", true);

            startActivity(i);
        }
    }

    private boolean isValid(AppCompatEditText editText) {
        return editText.getText().toString().length() > 3;
    }

}
