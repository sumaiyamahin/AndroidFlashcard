package com.example.sumaiyamahin.flashcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
       // String s1 = getIntent().getStringExtra( "stringkey1");
        //String s2 = getIntent().getStringExtra("stringkey1");

        findViewById(R.id.exitbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.exitbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCardActivity.this, MainActivity.class);
                AddCardActivity.this.startActivity(intent);
            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent(AddCardActivity.this, MainActivity.class);
                //think mailing a letter back to the main activity
                data.putExtra("questionvalue", ((EditText) findViewById(R.id.enterquestion)).getText().toString()); // puts one string into the Intent, with the key as 'string1'
                data.putExtra("answervalue", ((EditText) findViewById(R.id.enteranswer)).getText().toString()); // puts another string into the Intent, with the key as 'string2

                //actually delivering the message back to the other activity
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish(); // closes this activity and pass data to the original activity that launched this activity
            }

        });

    }
}
