package uni.tighearnan.routepicker.Login;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import uni.tighearnan.routepicker.JourneyPlanner.JourneyPlannerActivity;
import uni.tighearnan.routepicker.PreviousJourneysSingleton;
import uni.tighearnan.routepicker.R;
import uni.tighearnan.routepicker.User;
import uni.tighearnan.routepicker.UserSingleton;
import uni.tighearnan.routepicker.utils.EncryptionUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    private AppCompatEditText mEmailEditText;
    private AppCompatEditText mPasswordEditText;
    private AppCompatButton mSignInButton;
    private ProgressBar mSignInProgressBar;

    private AppCompatButton mCreateAccountButton;

    private boolean mDialogShowing;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        PreviousJourneysSingleton.get(getActivity()).reset();
        UserSingleton.get(getActivity()).reset();

        if(!mDialogShowing) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mEmailEditText.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mEmailEditText, InputMethodManager.SHOW_IMPLICIT);
                }
            }, 50);
        }
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

        mCreateAccountButton = (AppCompatButton) v.findViewById(R.id.button_createAccount);

        mDialogShowing = false;
        return v;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_GO) {
                    mSignInButton.performClick();
                }

                return false;
            }
        });

        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccountDialog dialog = CreateAccountDialog.newInstance();
                dialog.setListener(new OnUserConfirmedListener() {
                    @Override
                    public void onConfirmed(String name) {
                        Snackbar.make(view, name + ", your account has been created!", Snackbar.LENGTH_SHORT)
                                .show();
                        mDialogShowing = false;
                    }
                });
                mDialogShowing = true;
                dialog.show(getActivity().getSupportFragmentManager(), "NEWUSER");

            }
        });
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
            // TODO: Network checks.
            mSignInProgressBar.setVisibility(View.VISIBLE);

            String hashPass = EncryptionUtils.sha256(password);

            LoginASyncTask loginASyncTask = new LoginASyncTask(getActivity());
            loginASyncTask.setResultListener(new LoginResultListener() {
                @Override
                public void onLoginSuccess(User user) {
                    UserSingleton.get(getActivity()).setUser(user);
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
                                    mEmailEditText.requestFocus();
                                    dialog.dismiss();
                                }
                            }).show();

                }
            });
            loginASyncTask.execute(new LoginAuth(getActivity(), email.toLowerCase(), hashPass));
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    private boolean isValidPassword(String password) {
        return password.length() > 6;
    }
}
