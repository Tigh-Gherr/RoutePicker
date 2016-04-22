package uni.tighearnan.routepicker;

import android.content.Context;

/**
 * Created by tighearnan on 22/04/16.
 */
public class UserSingleton {
    private static UserSingleton sUserSingleton;
    private User mUser;
    private Context mApplicationContext;

    private UserSingleton(Context c) {
        mApplicationContext = c.getApplicationContext();
    }

    public static UserSingleton get(Context c) {
        if(sUserSingleton == null) {
            sUserSingleton = new UserSingleton(c);
        }

        return sUserSingleton;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public User getUser() {
        return mUser;
    }
}
