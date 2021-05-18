package com.streamliners.myecom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.streamliners.myecom.databinding.ActivityIntentsPlaygroundBinding;

public class IntentsPlaygroundActivity extends AppCompatActivity {

    private static final int REQUEST_COUNT = 0;
    ActivityIntentsPlaygroundBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupLayout();

        setupHideErrorForEditText();

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        b.dataEt.setText(prefs.getString(Constants.DATA,""));
        b.intentTypeRGrp.check(prefs.getInt(Constants.TYPE,-1));
        b.initialCounterTv.setText(prefs.getString(Constants.INITIAL_COUNT_KEY,""));
    }


    //Initial Setup---------------------------------------------------------

    /**
     * To set the layout of the ObjectSenderActivity
     */
    private void setupLayout() {
        b = ActivityIntentsPlaygroundBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        setTitle("Intents Playground");
    }

    /**
     *On changing of the written text in EditText, the error will be hidden
     */
    private void setupHideErrorForEditText() {

        TextWatcher myTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hideError();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        b.data.getEditText().addTextChangedListener(myTextWatcher);
        b.initialCounterEt.getEditText().addTextChangedListener(myTextWatcher);
    }



    //Event Handlers--------------------------------------------------------

    public void openMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void sendImplicitIntent(View view) {

        //validate data input
        String input = b.data.getEditText().getText().toString().trim();

        if (input.isEmpty()){
            b.data.setError("Please enter something!");
            return;
        }

        //validate intent type
        int type = b.intentTypeRGrp.getCheckedRadioButtonId();

        //handle implicit intent cases
        if (type == R.id.openWebPageRBtn)
            openWebPage(input);
        else if (type == R.id.dialNumberRBtn)
            dialNumber(input);
        else if (type == R.id.shareTextRBtn)
            shareText(input);
        else
            Toast.makeText(this, "Please select an intent type!", Toast.LENGTH_SHORT).show();

    }

    public void sendData(View view) {

        //validate data input
        String input = b.initialCounterEt.getEditText().getText().toString().trim();

        if (input.isEmpty()){
            b.initialCounterEt.setError("Please enter something!");
            return;
        }

        //get count
        int initialCount = Integer.parseInt(input);

        //create intent
        Intent intent = new Intent(this, MainActivity.class);

        //create bundles to pass
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.INITIAL_COUNT_KEY, initialCount);
        bundle.putInt(Constants.MIN_Value, -100);
        bundle.putInt(Constants.MAX_Value, 100);

        //pass bundle
        intent.putExtras(bundle);

        startActivityForResult(intent, REQUEST_COUNT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_COUNT && resultCode == RESULT_OK){

            //get data
            int count = data.getIntExtra(Constants.FINAL_COUNT, Integer.MIN_VALUE);

            //show data
            b.result.setText("Final count received: " + count);
            b.result.setVisibility(View.VISIBLE);
        }
    }



    //Implicit Intent Sender------------------------------------------------

    private void shareText(String text){

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text );
        startActivity(Intent.createChooser(intent, "Share text via"));

        //hide error if the share text operation is successful
        hideError();
    }

    private void dialNumber(String number){

        //check if input is mobile number
        if (!number.matches("^\\d{10}$")){
            b.data.setError("Invalid Mobile Number!");
            return;
        }

        //Good to go, send intent!
        Uri uri = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(intent);

        //hide error if the dial number operation is successful
        hideError();
    }

    private void openWebPage(String url){

        //validating user input using RegEx (check if input is URL)
        if (!url.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")){
            b.data.setError("Invalid URL!");
            return;
        }

        //Good to go, send intent!
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

        //hide error if the open webPage operation is successful
        hideError();
    }



    //Utility Functions or Utils---------------------------------------------

    /**
     * To hide error
     */
    private void hideError(){
        b.data.setError(null);
    }



    //Instance State

    @Override
    protected void onPause() {
        super.onPause();

        //create preferences reference i.e create object of preferences
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        //save and commit data
        prefs.edit()
                .putString(Constants.DATA, b.dataEt.getText().toString().trim())
                .putInt(Constants.TYPE, b.intentTypeRGrp.getCheckedRadioButtonId())
                .putString(Constants.INITIAL_COUNT_KEY, b.initialCounterEt.getEditText().getText().toString().trim())
                .apply();
    }

}