package com.streamliners.myecom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.streamliners.myecom.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private int count = 0;

    //enabling the viewBinding feature in gradle automatically creates a binding class of all layouts present.
    //b is the object of that class

    private ActivityMainBinding b;
    private int minVal, maxVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        setupEventHandlers();
        getInitialCount();


        //restore instance state

        //get from instance state
        if (savedInstanceState != null){
            count = savedInstanceState.getInt(Constants.COUNT, 0);
        }

        /*
        //get from shared preferences
        else {
            SharedPreferences prefs = getPreferences(MODE_PRIVATE);
            count = prefs.getInt(Constants.COUNT, 0);
        }*/
        b.countTv.setText(String.valueOf(count));
    }

    private void getInitialCount() {

        //get data from intent
        Bundle bundle = getIntent().getExtras();

        //no data received
        if (bundle == null)
            return;

        count = bundle.getInt(Constants.INITIAL_COUNT_KEY, 0);
        minVal = bundle.getInt(Constants.MIN_Value, Integer.MIN_VALUE);
        maxVal = bundle.getInt(Constants.MAX_Value, Integer.MAX_VALUE);

        b.countTv.setText(String.valueOf(count));

        if (count != 0){
            b.sendBackBtn.setVisibility(View.VISIBLE);
        }
    }


    private void setupEventHandlers() {

        //lambda method -> here we get a view as input and thus we call a function, corresponding to it

        b.decBtn.setOnClickListener(v -> decQty());

        b.incBtn.setOnClickListener(v -> incQty());

    }

    public void decQty() {
        b.countTv.setText("" + --count);
    }

    public void incQty() {
        b.countTv.setText("" + ++count);
    }

    public void sendBack(View view) {

        //validate count
        if (count >= minVal && count <= maxVal){

            //send the data
            Intent intent = new Intent();
            intent.putExtra(Constants.FINAL_COUNT, count);
            setResult(RESULT_OK, intent);

            //close the activity
            finish();
        }

        //not in range
        else{
            Toast.makeText(this, "Not in range!", Toast.LENGTH_SHORT).show();
        }
    }


    //Instance State

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(Constants.COUNT, count);
    }


    @Override
    protected void onPause() {
        super.onPause();

        //create preferences reference i.e create object of preferences
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        //save and commit data
        prefs.edit()
                .putInt(Constants.COUNT, count)
                .apply();


        //shared preferences changed listener
        //for strong reference, preferred way is to create listener object as a field in Activity class
        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            }
        };

        prefs.registerOnSharedPreferenceChangeListener(listener);

    }


    /*
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null){
            count = savedInstanceState.getInt(Constants.COUNT, 0);
            b.countTv.setText(String.valueOf(count));
        }
    }

    */
}