package uni.tighearnan.routepicker.Login;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tighearnan on 24/04/16.
 */
public class CreateAccountASyncTask extends AsyncTask<String, Void, Void> {

    private PostToDatabaseListener mListener;

    public void setListener(PostToDatabaseListener listener) {
        mListener = listener;
    }

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

    @Override
    protected void onPostExecute(Void aVoid) {
        mListener.onPostComplete();
    }
}
