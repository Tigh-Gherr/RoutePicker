package uni.tighearnan.routepicker.Ticket;

/**
 * Created by tighearnan on 27/04/16.
 */
public class Ticket {

    private Journey mJourney;
    private String mBarcode;
    private boolean isUsed;

    public Ticket(Journey journey) {
        mJourney = journey;
        generateBarcode();
    }

    private void generateBarcode() {
        String from = mJourney.getFromTitle();
        String to = mJourney.getToTitle();
        boolean isReturn = mJourney.isReturn();

        StringBuilder builder = new StringBuilder();
        builder.append(from.substring(0, 4).toUpperCase())
                .append("-").append(isReturn ? 1 : 0).append("-")
                .append(to.substring(0, 4).toUpperCase());

        mBarcode = builder.toString();
    }

    public String getBarcode() {
        return mBarcode;
    }

    public Journey getJourney() {
        return mJourney;
    }
}
