
package com.mathai.alex.sunshine2.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    //.add(R.id.container, new DetailActivityFragment())
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
   public static class PlaceholderFragment extends Fragment {

        private final String LOG_TAG = PlaceholderFragment.class.getSimpleName();

        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Intent intent = getActivity().getIntent();
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
           if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
               Log.v(LOG_TAG, "intent not null");
                String forecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);
               Toast.makeText(getActivity(),forecastStr, Toast.LENGTH_SHORT).show();
                ((TextView) rootView.findViewById(R.id.detail_text))
                        .setText(forecastStr);
                 }

            return rootView;
        }
    }
}
