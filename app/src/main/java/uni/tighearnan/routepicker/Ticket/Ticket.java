package uni.tighearnan.routepicker.Ticket;

/**
 * Created by tighearnan on 27/04/16.
 */
public class Ticket {

    private String mFrom;
    private String mTo;
    private boolean mIsReturn;
    private String mBarcode;
    private boolean isUsed;
    private int mId;

    public Ticket(String from, String to, boolean isReturn) {
        mFrom = from;
        mTo = to;
        generateBarcode();
    }

    public void setId(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public String getFrom() {
        return mFrom;
    }

    public String getTo() {
        return mTo;
    }

    public boolean isReturn() {
        return mIsReturn;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    private void generateBarcode() {
        StringBuilder builder = new StringBuilder();
        builder.append(mFrom.substring(0, 4).toUpperCase())
                .append("-").append(mIsReturn ? 1 : 0).append("-")
                .append(mTo.substring(0, 4).toUpperCase());

        mBarcode = builder.toString();
    }

    public void setBarcode(String barcode) {
        mBarcode = barcode;
    }

    public String getBarcode() {
        return mBarcode;
    }
}
