package uni.tighearnan.routepicker;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tighearnan on 27/04/16.
 */
public class PostASyncTask extends AsyncTask<String, Void, Void> {

    private PostListener mListener;

    public void setListener(PostListener listener) {
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
        super.onPostExecute(aVoid);

        if(mListener != null) {
            mListener.onPostComplete();
        }
    }
}
