package com.streamliners.myecom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.TextView;

import com.streamliners.myecom.databinding.ActivityMainBinding;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private int qty = 0;

    //enabling the viewBinding feature in gradle automatically creates a binding class of all layouts present.
    //b is the object of that class

    private ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //we then instantiate b by calling static fxn "inflate" of AMB
        // which creates that layout and
        // that inflate fxn needs an Inflater (LayoutInflater) to create/inflate the layout.

        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        //method 1: using xml onClick


        //method 2: handling event using java
        setupEventHandlers();
    }


/*

    //long method -> make class of OnClickListener [static interface]
    // then override onClick and then
    // create its object[as we couldn't create an interface's object directly]
    // and pass to setOnClickListener


    class MyClickListener implements View.OnClickListener{


        //as it is an interface we have to implement all its methods
        // and there is only one method i.e onClick

        @Override
        public void onClick(View v) {
            incQty();
        }
    }


    //use this in setUpEventHandlers()
    //instead of line 76 and 83
    //->   b.incBtn.setOnClickListener(new MyClickListener());
*/


    private void setupEventHandlers() {

/*

        // short method-> anonymous class
        // i.e implement interface, override methods & and without having to give the class a name, used it one single time

        b.incBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incQty();
            }
        });


*/


        //shortest-> lambda method
        //here we get a view as input and thus we call a function, corresponding to it

        b.decBtn.setOnClickListener(v -> decQty());

        b.incBtn.setOnClickListener(v -> incQty());

    }

    public void decQty() {
        /*
        qty--;
        b.qty.setText("" + qty);
        */

        b.qty.setText("" + --qty);
    }

    public void incQty() {
        b.qty.setText("" + ++qty);
    }
}