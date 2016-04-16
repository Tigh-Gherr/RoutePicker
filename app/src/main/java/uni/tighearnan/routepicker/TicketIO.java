package uni.tighearnan.routepicker;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOError;
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

    public void saveTickets(ArrayList<Ticket> tickets) throws JSONException, IOException {
            JSONArray array =  new JSONArray();

            for(Ticket t : tickets) {
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

    public ArrayList<Ticket> loadTickets() throws IOException, JSONException {
        ArrayList<Ticket> tickets = new ArrayList<>();
        BufferedReader reader = null;

        try {
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            for(int i = 0; i < array.length(); i++) {
                tickets.add(new Ticket(array.getJSONObject(i), mContext));
            }

        } catch (FileNotFoundException e) {

        } finally {
            if(reader != null) {
                reader.close();
            }
        }
        return tickets;
    }
}
