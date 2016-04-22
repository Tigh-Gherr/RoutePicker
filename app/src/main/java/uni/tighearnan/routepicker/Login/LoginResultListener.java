package uni.tighearnan.routepicker.Login;

import uni.tighearnan.routepicker.User;

/**
 * Created by tighearnan on 19/04/16.
 */
public interface LoginResultListener {
    void onLoginSuccess(User user);
    void onLoginFailed();
}
