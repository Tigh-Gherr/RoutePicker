package uni.tighearnan.routepicker.Login;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import uni.tighearnan.routepicker.CreditCard;
import uni.tighearnan.routepicker.PreviousJourneysSingleton;
import uni.tighearnan.routepicker.Ticket.Journey;
import uni.tighearnan.routepicker.User;

/**
 * Created by tighearnan on 19/04/16.
 */
public class LoginASyncTask extends AsyncTask<LoginAuth, Void, User> {

    private LoginResultListener mResultListener;

    private Context mContext;

    public LoginASyncTask(Context c) {
        mContext = c;
    }

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
            JSONObject creditCardJson = jsonObject.getJSONObject("cc");
            String number = creditCardJson.getString("number");
            int cardType = creditCardJson.getInt("card_type");
            int expMonth = creditCardJson.getInt("exp_month");
            int expYear = creditCardJson.getInt("exp_year");
            String addrl1 = creditCardJson.getString("addr_line_one");
            String addrl2 = creditCardJson.getString("addr_line_two");
            String cvc = creditCardJson.getString("cvc");
            creditCard = new CreditCard(number, cardType, expMonth, expYear, addrl1, addrl2, cvc);
        }

        if(!jsonObject.isNull("journeys")) {
            JSONArray jsonPreviousJourneys = jsonObject.getJSONArray("journeys");
            for(int i = 0; i < jsonPreviousJourneys.length(); i++) {
                JSONObject journey = jsonPreviousJourneys.getJSONObject(i);

                String from = journey.getString("where_from");
                String to = journey.getString("where_to");
                boolean isReturn = journey.getInt("is_return") == 1;
                double cost = journey.getDouble("base_cost");

                Journey j = new Journey(mContext, from, to);
                j.setCost(cost);
                j.setReturn(isReturn);

                PreviousJourneysSingleton.get(mContext).addJourney(j);

            }
        }

        return new User(id, email, fname, sname, creditCard);
    }
}
