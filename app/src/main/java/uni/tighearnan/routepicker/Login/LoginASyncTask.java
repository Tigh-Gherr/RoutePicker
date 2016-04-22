package uni.tighearnan.routepicker.Login;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import uni.tighearnan.routepicker.CreditCard;
import uni.tighearnan.routepicker.User;

/**
 * Created by tighearnan on 19/04/16.
 */
public class LoginASyncTask extends AsyncTask<LoginAuth, Void, User> {

    private LoginResultListener mResultListener;

    public void setResultListener(LoginResultListener resultListener) {
        mResultListener = resultListener;
    }

    @Override
    protected User doInBackground(LoginAuth... params) {
        HttpURLConnection connection;
        User user = null;
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

            try {
                user = parseUser(new String(out.toByteArray()));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    protected void onPostExecute(User user) {
        if(user == null) {
            mResultListener.onLoginFailed();
        } else {
            mResultListener.onLoginSuccess(user);
        }
    }

    private User parseUser(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        int id = jsonObject.getInt("id");
        String email = jsonObject.getString("email");
        String fname = jsonObject.getString("first_name");
        String sname = jsonObject.getString("surname");

        CreditCard creditCard;

        if(jsonObject.isNull("cc")) {
            creditCard = null;
        } else {
            jsonObject = jsonObject.getJSONObject("cc");
            String number = jsonObject.getString("number");
            int cardType = jsonObject.getInt("card_type");
            int expMonth = jsonObject.getInt("exp_month");
            int expYear = jsonObject.getInt("exp_year");
            String addrl1 = jsonObject.getString("addr_line_one");
            String addrl2 = jsonObject.getString("addr_line_two");
            String cvc = jsonObject.getString("cvc");
            creditCard = new CreditCard(number, cardType, expMonth, expYear, addrl1, addrl2, cvc);
        }

        return new User(id, email, fname, sname, creditCard);
    }
}
