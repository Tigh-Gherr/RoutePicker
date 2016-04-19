package uni.tighearnan.routepicker;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    private AppCompatEditText mEmailEditText;
    private AppCompatEditText mPasswordEditText;
    private AppCompatButton mSignInButton;
    private ProgressBar mSignInProgressBar;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);

        mEmailEditText = (AppCompatEditText) v.findViewById(R.id.edit_text_email);
        mPasswordEditText = (AppCompatEditText) v.findViewById(R.id.edit_text_password);

        mSignInButton = (AppCompatButton) v.findViewById(R.id.button_signIn);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        mSignInProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar_signIn);

        return v;
    }

    private void signIn() {
        mEmailEditText.setError(null);
        mPasswordEditText.setError(null);

        boolean shouldCancel = false;

        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        View errorView = null;

        if (!isValidEmail(email)) {
            mEmailEditText.setError("Email is invalid.");
            errorView = mEmailEditText;
            shouldCancel = true;
        }

        if (!isValidPassword(password)) {
            mPasswordEditText.setError("Password is too short.");
            if (errorView == null) {
                errorView = mPasswordEditText;
                shouldCancel = true;
            }
        }

        if (shouldCancel) {
            errorView.requestFocus();
        } else {
            mSignInProgressBar.setVisibility(View.VISIBLE);

            LoginASyncTask loginASyncTask = new LoginASyncTask();
            loginASyncTask.setResultListener(new LoginResultListener() {
                @Override
                public void onLoginSuccess() {
                    startActivity(new Intent(getActivity(), JourneyPlannerActivity.class));
                    getActivity().finish();
                }

                @Override
                public void onLoginFailed() {
                    mSignInProgressBar.setVisibility(View.INVISIBLE);

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Login Error")
                            .setMessage("Email or password is incorrect.")
                            .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            });
            loginASyncTask.execute(new LoginAuth(getActivity(), email.toLowerCase(), password));
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    private boolean isValidPassword(String password) {
        return password.length() > 6;
    }
}
