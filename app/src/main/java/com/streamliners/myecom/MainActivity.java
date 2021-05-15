package com.streamliners.myecom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.streamliners.myecom.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private int qty = 0;

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

    }

    private void getInitialCount() {

        Bundle bundle = getIntent().getExtras();

        if (bundle == null)
            return;
        //get data from intent
        qty = bundle.getInt(Constants.INITIAL_COUNT_KEY, 0);
        minVal = bundle.getInt(Constants.MIN_Value, Integer.MIN_VALUE);
        maxVal = bundle.getInt(Constants.MAX_Value, Integer.MAX_VALUE);

        b.qty.setText(String.valueOf(qty));

        if (qty != 0){
            b.sendBackBtn.setVisibility(View.VISIBLE);
        }
    }


    private void setupEventHandlers() {

        //lambda method -> here we get a view as input and thus we call a function, corresponding to it

        b.decBtn.setOnClickListener(v -> decQty());

        b.incBtn.setOnClickListener(v -> incQty());

    }

    public void decQty() {
        b.qty.setText("" + --qty);
    }

    public void incQty() {
        b.qty.setText("" + ++qty);
    }

    public void sendBack(View view) {

        //validate count
        if (qty >= minVal && qty <= maxVal){

            //send the data
            Intent intent = new Intent();
            intent.putExtra(Constants.FINAL_COUNT, qty);
            setResult(RESULT_OK, intent);

            //close the activity
            finish();
        }

        //not in range
        else{
            Toast.makeText(this, "Not in range!", Toast.LENGTH_SHORT).show();
        }
    }
}