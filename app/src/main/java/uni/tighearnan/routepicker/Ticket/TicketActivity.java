package uni.tighearnan.routepicker.Ticket;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import uni.tighearnan.routepicker.JourneyPlanner.JourneyPlannerActivity;
import uni.tighearnan.routepicker.PurchasedTickets.PurchasedTicketsActivity;
import uni.tighearnan.routepicker.R;

public class TicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                navigateUp();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void navigateUp() {
        boolean purchased = getIntent().getBooleanExtra("PURCHASED", false);

        if(purchased) {
            NavUtils.navigateUpTo(this, new Intent(this, JourneyPlannerActivity.class));
        } else {
            NavUtils.navigateUpTo(this, new Intent(this, PurchasedTicketsActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        navigateUp();
    }
}
