package uni.tighearnan.routepicker.Payment;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import uni.tighearnan.routepicker.CreditCard;

/**
 * Created by tighearnan on 27/04/16.
 */
public class CreditCardPostASyncTask extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        HttpURLConnection connection;

        try {
            URL url = new URL(params[0]);
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
