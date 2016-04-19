package uni.tighearnan.routepicker;

/**
 * Created by tighearnan on 19/04/16.
 */
public class User {

    private int mId;
    private String mEmail;
    private String mFirstName;
    private String mSurnameName;

    public User(String email) {
        mEmail = email;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public void setSurnameName(String surnameName) {
        mSurnameName = surnameName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getSurnameName() {
        return mSurnameName;
    }
}
