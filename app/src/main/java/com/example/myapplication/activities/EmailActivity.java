package com.example.myapplication.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;

public class EmailActivity extends BaseActivity {

    EditText emailSubject;
    EditText emailMessage;

    EditText emailReceiver;
    Button backButton;
    Button sendEmail;

    public Intent lastSentIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        setupFabMenu();
        emailSubject = findViewById(R.id.emailSubject);
        emailMessage = findViewById(R.id.emailMessage);
        emailReceiver = findViewById(R.id.emailReceiver);


        sendEmail = findViewById(R.id.emailButton);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v-> backToMainActivity());

        sendEmail.setOnClickListener(v -> sendEmail(
                emailSubject.getText().toString(),
                emailMessage.getText().toString(),
                emailReceiver.getText().toString()
                ));
    }

    @Override
    protected void onPermissionsResult(int requestCode, boolean granted) {

    }

    public void sendEmail(String emailSubject, String emailMessage, String emailReceiver) {
        if(emailMessage.isEmpty() || emailReceiver.isEmpty()){
            Toast.makeText(this, "Adres Email i odbiorca nie może być pusty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(emailSubject.isEmpty())
            emailSubject = "temat";

        Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
        mailIntent.setData(Uri.parse("mailto:"));
        mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailReceiver});
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        mailIntent.putExtra(Intent.EXTRA_TEXT, emailMessage);

        lastSentIntent = mailIntent;

        startActivity(mailIntent);
    }
}
