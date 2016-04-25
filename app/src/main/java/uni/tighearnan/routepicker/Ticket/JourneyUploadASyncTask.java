package uni.tighearnan.routepicker.Ticket;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import uni.tighearnan.routepicker.R;
import uni.tighearnan.routepicker.User;
import uni.tighearnan.routepicker.UserSingleton;

/**
 * Created by tighearnan on 25/04/16.
 */
public class JourneyUploadASyncTask extends AsyncTask<Journey, Void, Void> {

    private Context mContext;

    public JourneyUploadASyncTask(Context c) {
        mContext = c;
    }

    @Override
    protected Void doInBackground(Journey... params) {
        HttpURLConnection connection;

        User user = UserSingleton.get(mContext).getUser();

        Journey journey = params[0];
        try {
            URL url = new URL(mContext.getString(R.string.upload_journey_url,
                    user.getId(), journey.getFromTitle(), journey.getToTitle(),
                    journey.isReturn() ? "1" : "0", journey.getBaseCost()));
            connection = (HttpURLConnection) url.openConnection();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
