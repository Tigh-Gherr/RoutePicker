package uni.tighearnan.routepicker.Ticket;

import android.content.Intent;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import uni.tighearnan.routepicker.PostListener;

/**
 * Created by tighearnan on 29/04/16.
 */
public class UploadTicketASyncTask extends AsyncTask<String, Void, Integer> {

    private Ticket mTicket;
    private PostListener mPostListener;

    public UploadTicketASyncTask(Ticket ticket) {
        mTicket = ticket;
    }

    public void setPostListener(PostListener postListener) {
        mPostListener = postListener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        HttpURLConnection connection = null;
        int id = -1;

        try {
            URL url = new URL(params[0]);
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

            id = Integer.parseInt(new String(out.toByteArray()));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return id;
    }

    @Override
    protected void onPostExecute(Integer id) {
        if(id != null) {
            mTicket.setId(id);
        }

        if(mPostListener != null) {
            mPostListener.onPostComplete();
        }
    }
}
