package uni.tighearnan.routepicker;

/**
 * Created by tighearnan on 19/04/16.
 */
public class User {

    private int mId;
    private String mEmail;
    private String mFirstName;
    private String mSurnameName;
    private CreditCard mCreditCard;

    public User(int id, String email, String firstName, String surnameName, CreditCard creditCard) {
        mId = id;
        mEmail = email;
        mFirstName = firstName;
        mSurnameName = surnameName;
        mCreditCard = creditCard;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
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

    public CreditCard getCreditCard() {
        return mCreditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        mCreditCard = creditCard;
    }
}
