package uni.tighearnan.routepicker;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tighearnan on 19/04/16.
 */
public class LoginASyncTask extends AsyncTask<LoginAuth, Void, Integer> {

    private LoginResultListener mResultListener;

    public void setResultListener(LoginResultListener resultListener) {
        mResultListener = resultListener;
    }

    @Override
    protected Integer doInBackground(LoginAuth... params) {
        HttpURLConnection connection;
        int result = -1;
        try {
            URL url = new URL(params[0].getUrl());
            connection = (HttpURLConnection) url.openConnection();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream inputStream = connection.getInputStream();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];

            while ((bytesRead = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();

            result = Integer.valueOf(new String(out.toByteArray()));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if(integer == 0) {
            mResultListener.onLoginSuccess();
        } else {
            mResultListener.onLoginFailed();
        }
    }
}
