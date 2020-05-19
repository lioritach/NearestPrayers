package com.example.group21;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ContactUs extends AppCompatActivity {

    private EditText ourEmailData;
    private EditText messageData;
    private EditText subjectData;
    private Button send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        ourEmailData = (EditText) findViewById(R.id.ourmailID);
        subjectData = (EditText) findViewById(R.id.subjectID);
        messageData = (EditText) findViewById(R.id.messageID);

        send = (Button) findViewById(R.id.btn_sendID);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ourEmail = ourEmailData.getText().toString();
                String ourSubject = subjectData.getText().toString();
                String ourMessage = messageData.getText().toString();

                Intent send = new Intent(Intent.ACTION_SEND);
                send.putExtra(Intent.EXTRA_EMAIL, ourEmail);
                send.putExtra(Intent.EXTRA_SUBJECT, ourSubject);
                send.putExtra(Intent.EXTRA_TEXT, ourMessage);
                send.setType("message/rfc822");
                send.setPackage("com.google.android.gm");
                startActivity(send);
            }
        });
    }
}
