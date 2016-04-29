package uni.tighearnan.routepicker.Login;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import uni.tighearnan.routepicker.PostASyncTask;
import uni.tighearnan.routepicker.PostListener;
import uni.tighearnan.routepicker.R;

/**
 * Created by tighearnan on 24/04/16.
 */
public class CreateAccountDialog extends AppCompatDialogFragment {

    private AppCompatEditText mFirstNameEditText;
    private AppCompatEditText mSurnameEditText;
    private AppCompatEditText mEmailEditText;
    private AppCompatEditText mPasswordEditText;
    private AppCompatEditText mConfirmPasswordEditText;

    private OnUserConfirmedListener mListener;

    private View mContent;

    public static CreateAccountDialog newInstance() {
        return new CreateAccountDialog();
    }

    public void setListener(OnUserConfirmedListener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        mContent = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_account, null);

        mFirstNameEditText = (AppCompatEditText) mContent.findViewById(R.id.edit_text_firstName);
        mSurnameEditText = (AppCompatEditText) mContent.findViewById(R.id.edit_text_surname);

        mEmailEditText = (AppCompatEditText) mContent.findViewById(R.id.edit_text_email);

        mPasswordEditText = (AppCompatEditText) mContent.findViewById(R.id.edit_text_password);
        mConfirmPasswordEditText = (AppCompatEditText) mContent.findViewById(R.id.edit_text_confirm_password);

        mConfirmPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_GO) {
                    createUser();
                }

                return false;
            }
        });

        builder.setView(mContent).setTitle("Create Account")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        final AlertDialog dialog = (AlertDialog) getDialog();

        if (dialog != null) {
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createUser();
                }
            });
        }
    }

    private void createUser() {
        if(verifyFields()) {
            final String fname = mFirstNameEditText.getText().toString();
            String sname = mSurnameEditText.getText().toString();
            String email = mEmailEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();

            PostASyncTask task = new PostASyncTask();
            task.setListener(new PostListener() {
                @Override
                public void onPostComplete() {
                    mListener.onConfirmed(fname);
                    getDialog().dismiss();
                }
            });
            task.execute(getString(R.string.create_account_url, fname, sname, email, password));
        }
    }

    private boolean verifyFields() {

        mFirstNameEditText.setError(null);
        mSurnameEditText.setError(null);
        mEmailEditText.setError(null);
        mPasswordEditText.setError(null);
        mConfirmPasswordEditText.setError(null);

        String emptyfield = "Field cannot be left empty.";

        View errorView = null;
        boolean verified = true;


        if (isEmpty(mFirstNameEditText)) {
            mFirstNameEditText.setError(emptyfield);
            errorView = mFirstNameEditText;
            verified = false;
        }

        if (isEmpty(mSurnameEditText)) {
            mSurnameEditText.setError(emptyfield);
            errorView = mSurnameEditText;
            verified = false;
        }

        if (isEmpty(mEmailEditText)) {
            mEmailEditText.setError(emptyfield);
            errorView = mEmailEditText;
            verified = false;
        } else if(!isValidEmail(mEmailEditText)) {
            mEmailEditText.setError("Invalid email.");
            errorView = mEmailEditText;
            verified = false;
        }

        if (isEmpty(mPasswordEditText)) {
            mPasswordEditText.setError(emptyfield);
            errorView = mPasswordEditText;
            verified = false;
        } else if(!isValidPassword(mPasswordEditText)) {
            mPasswordEditText.setError("Password is too short.");
            errorView = mPasswordEditText;
            verified = false;
        } else {
            String pass = mPasswordEditText.getText().toString();
            String confpass = mConfirmPasswordEditText.getText().toString();

            if (!pass.equals(confpass)) {
                mPasswordEditText.setError("Passwords do not match.");
                mConfirmPasswordEditText.setError("Passwords do not match.");
                errorView = mPasswordEditText;
                verified = false;
            }
        }

        if (!verified) {
            errorView.requestFocus();
        }

        return verified;
    }

    private boolean isValidPassword(AppCompatEditText editText) {
        return editText.getText().toString().length() > 6;
    }

    private boolean isValidEmail(AppCompatEditText editText) {
        String text = editText.getText().toString();
        return text.contains("@") && text.contains(".");
    }

    private boolean isEmpty(AppCompatEditText editText) {
        return editText.getText().length() == 0;
    }

    @Override
    public void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mFirstNameEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 50);
    }
}
