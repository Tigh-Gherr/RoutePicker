package uni.tighearnan.routepicker;

/**
 * Created by tighearnan on 22/04/16.
 */
public class CreditCard {
    private String mNumber;
    private int mCardType;
    private int mExpMonth;
    private int mExpYear;
    private String mAddressLine1;
    private String mAddressLine2;
    private String mCVC;

    public CreditCard(String number, int cardType, int expMonth, int expYear, String addressLine1, String addressLine2, String CVC) {
        mNumber = number;
        mCardType = cardType;
        mExpMonth = expMonth;
        mExpYear = expYear;
        mAddressLine1 = addressLine1;
        mAddressLine2 = addressLine2;
        mCVC = CVC;
    }

    public String getNumber() {
        return mNumber;
    }

    public int getCardType() {
        return mCardType;
    }

    public int getExpMonth() {
        return mExpMonth;
    }

    public int getExpYear() {
        return mExpYear;
    }

    public String getAddressLine1() {
        return mAddressLine1;
    }

    public String getAddressLine2() {
        return mAddressLine2;
    }

    public String getCVC() {
        return mCVC;
    }
}
