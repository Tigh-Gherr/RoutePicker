package uni.tighearnan.routepicker.Ticket;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by tighearnan on 16/04/16.
 */
public class TicketIO {

    private Context mContext;
    private String mFilename;

    public TicketIO(Context c, String filename) {
        mContext = c;
        mFilename = filename;
    }

    public void saveTickets(ArrayList<Journey> journeys) throws JSONException, IOException {
            JSONArray array =  new JSONArray();

            for(Journey t : journeys) {
                array.put(t.toJson());
            }

        Writer writer = null;
        try {
            OutputStream out = mContext
                    .openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if(writer != null) {
                writer.close();
            }
        }
    }

    public ArrayList<Journey> loadTickets() throws IOException, JSONException {
        ArrayList<Journey> journeys = new ArrayList<>();
        BufferedReader reader = null;

        try {
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            for(int i = 0; i < array.length(); i++) {
                journeys.add(new Journey(array.getJSONObject(i), mContext));
            }

        } catch (FileNotFoundException e) {

        } finally {
            if(reader != null) {
                reader.close();
            }
        }
        return journeys;
    }
}
