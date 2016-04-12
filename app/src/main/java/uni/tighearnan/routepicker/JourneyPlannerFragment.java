package uni.tighearnan.routepicker;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class JourneyPlannerFragment extends Fragment {

    private AppCompatEditText mFromEditText;
    private AppCompatEditText mToEditText;
    private AppCompatImageButton mFromImageButton;
    private AppCompatImageButton mToImageButton;
    private AppCompatButton mGoButton;

    private View.OnFocusChangeListener mFocusChangeListener
            = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            AppCompatImageButton search = null;
            if(v == mFromEditText) {
                search = mFromImageButton;
            } else if (v == mToEditText){
                search = mToImageButton;
            }

            if(hasFocus) {
                DrawableCompat.setTint(search.getDrawable(), ContextCompat.getColor(getActivity(), R.color.colorControlActivated));
            } else {
                DrawableCompat.setTint(search.getDrawable(), ContextCompat.getColor(getActivity(), R.color.md_white_1000));
            }
        }
    };

    public JourneyPlannerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_journey_planner, container, false);

        mFromEditText = (AppCompatEditText) v.findViewById(R.id.edit_text_travellingFrom);
        mFromEditText.setOnFocusChangeListener(mFocusChangeListener);
        mToEditText = (AppCompatEditText) v.findViewById(R.id.edit_text_travellingTo);
        mToEditText.setOnFocusChangeListener(mFocusChangeListener);

        mFromImageButton = (AppCompatImageButton) v.findViewById(R.id.image_button_travellingFrom);
        mToImageButton = (AppCompatImageButton) v.findViewById(R.id.image_button_travellingTo);

        mGoButton = (AppCompatButton) v.findViewById(R.id.button_go);
        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryLocations();
            }
        });

        return v;
    }

    private void tryLocations() {
        mFromEditText.setError(null);
        mToEditText.setError(null);
        boolean cancel = false;

        String from = mFromEditText.getText().toString();
        String to = mToEditText.getText().toString();

        View errorView = null;

        if(!isValid(mFromEditText)) {
            mFromEditText.setError("\"" + from + "\" is not a valid location.");
            errorView = mFromEditText;
            cancel = true;
        }

        if(!isValid(mToEditText)) {
            mToEditText.setError("\"" + to + "\" is not a valid location.");
            if(errorView == null) {
                errorView = mToEditText;
            }
            cancel = true;
        }

        if (cancel) {
            errorView.requestFocus();
        } else {
            Intent i = new Intent(getActivity(), JourneyDetailsActivity.class);
            i.putExtra("FROM",from);
            i.putExtra("TO", to);

            startActivity(i);
        }
    }

    private boolean isValid(AppCompatEditText editText) {
        return editText.getText().toString().length() > 3;
    }

}
