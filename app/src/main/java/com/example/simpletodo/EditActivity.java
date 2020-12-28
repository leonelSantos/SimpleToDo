package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText etText;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //References to views in our layout
        etText = findViewById(R.id.etText);
        btnSave = findViewById(R.id.btnSave);

        // More Descriptive Tittle for user to know where they are
        getSupportActionBar().setTitle("Edit Item");

        // Gets data passed from editing. And Then it populates Edit Text, with text from item.
        etText.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        // Button clicked when user is done editing
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Create an intent which will contain the results
                // Intent left empty because its used as shell to pass data
                Intent intent = new Intent();

                // 2. Pass the data (results of editing)
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, etText.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                // 3. Set the result of the intent
                setResult(RESULT_OK, intent);

                // 4. Finish activity, close screen and return result
                finish();
            }
        });
    }
}