package uni.tighearnan.routepicker.Login;

import android.content.Context;

import uni.tighearnan.routepicker.R;

/**
 * Created by tighearnan on 19/04/16.
 */
public class LoginAuth {

    private String mEmail;
    private String mPassword;
    private String mUrl;

    public LoginAuth(Context c, String email, String password) {
        mEmail = email;
        mPassword = password;
        mUrl = c.getString(R.string.login_url, mEmail, mPassword);
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getUrl() {
        return mUrl;
    }
}
