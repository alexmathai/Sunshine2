package com.mathai.alex.sunshine2.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.ShareActionProvider;
import android.widget.ImageView;
import android.widget.TextView;

import com.mathai.alex.sunshine2.app.data.WeatherContract;
//import com.mathai.alex.sunshine2.app.data.WeatherContract.WeatherEntry;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;


/**
 * A placeholder fragment containing a simple view.
 *//**/
public class DetailFragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    //public class DetailFragment extends Fragment {

    private static final int DETAIL_LOADER = 14;

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private static final String FORECAST_SHARE_HASHTAG = "#Sunshine2";

    private ShareActionProvider mShareActionProvider;

    private String mForecast;


    public DetailFragment() {
        setHasOptionsMenu(true);
    }


    //Projection for CursorLoader
    private static final String[]DETAIL_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING

    };

    static final int COL_ID = 0;
    static final int COL_DATE = 1;
    static final int COL_SHORT_DESC = 2;
    static final int COL_MAX_TEMP = 3;
    static final int COL_MIN_TEMP = 4;
    public static final int COL_WEATHER_HUMIDITY = 5;
    public static final int COL_WEATHER_PRESSURE = 6;
    public static final int COL_WEATHER_WIND_SPEED = 7;
    public static final int COL_WEATHER_DEGREES = 8;
    public static final int COL_WEATHER_CONDITION_ID = 9;

    private ImageView mIconView;
    private TextView mFriendlyDateView;
    private TextView mDateView;
    private TextView mHighTempView;
    private TextView mLowTempView;
    private TextView mDescriptionView;
    private TextView mHumidityView;
    private TextView mPressureView;
    private TextView mWindView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


        mFriendlyDateView = (TextView) rootView.findViewById(R.id.detail_day_textview);
        mDateView = (TextView)rootView.findViewById(R.id.detail_date_textview);
        mHighTempView = (TextView)rootView.findViewById(R.id.detail_high_textview);
        mLowTempView = (TextView)rootView.findViewById(R.id.detail_low_textview);
        mIconView =  (ImageView) rootView.findViewById(R.id.detail_icon);
        mDescriptionView = (TextView) rootView.findViewById(R.id.detail_description_textview);
        mHumidityView = (TextView)rootView.findViewById(R.id.detail_humidity_textview);
        mPressureView = (TextView)rootView.findViewById(R.id.detail_pressure_textview);
        mWindView = (TextView)rootView.findViewById(R.id.detail_wind_textview);

        return rootView;
    }

    private Intent createShareForecastIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.detailfragment, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mForecast != null){
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");
        Intent intent = getActivity().getIntent();
        if (intent == null || intent.getData() == null) {
            return null;
        }

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                intent.getData(),
                DETAIL_COLUMNS,
                null,
                null,
                null
        );
    }


    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader,Cursor data){
        Log.v(LOG_TAG," In onLoadFinished");

        if (data.moveToFirst() && data != null){

            int weatherID = data.getInt(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID));
            mIconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherID));
            mFriendlyDateView.setText(Utility.getFriendlyDayString(getActivity(),data.getLong(COL_DATE)));
            mDateView.setText(Utility.getFormattedMonthDay(getActivity(), data.getLong(COL_DATE)));
            mHighTempView.setText(Utility.formatTemperature(getActivity(), data.getDouble(COL_MAX_TEMP), Utility.isMetric(getActivity())));
            mLowTempView.setText(Utility.formatTemperature(getActivity(), data.getDouble(COL_MIN_TEMP), Utility.isMetric(getActivity())));
            mDescriptionView.setText(data.getString(COL_SHORT_DESC));
            mHumidityView.setText(getActivity().getString(R.string.format_humidity, data.getFloat(COL_WEATHER_HUMIDITY)));
            mPressureView.setText(getActivity().getString(R.string.format_pressure, data.getFloat(COL_WEATHER_PRESSURE)));
            mWindView.setText(Utility.getFormattedWind(getActivity(),data.getFloat(COL_WEATHER_WIND_SPEED),data.getFloat(COL_WEATHER_DEGREES)));

            return;
        }

        String dateString = Utility.formatDate(data.getLong(COL_DATE));
        String description = data.getString(COL_SHORT_DESC);

        boolean isMetric = Utility.isMetric(getActivity());

        String maxTemp = Utility.formatTemperature(getActivity(), data.getDouble(COL_MAX_TEMP), isMetric);
        String minTemp = Utility.formatTemperature(getActivity(), data.getDouble(COL_MIN_TEMP), isMetric);

        mForecast = String.format("%s - %s - %s/%s",dateString,description,maxTemp,minTemp);

        //TextView detailTextView = (TextView)getView().findViewById(R.id.detail_text);
        //detailTextView.setText(mForecast);

        if(mShareActionProvider != null){
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader){

    }

}
