package com.example.myapplication.activities;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;

public class CallActivity extends BaseActivity {

    private static final int REQ_CALL_PHONE = 200;

    EditText form_phone;
    Button callButton;
    Button backButton;

    String phone_number = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        setupFabMenu();

        form_phone = findViewById(R.id.tel);
        callButton = findViewById(R.id.callButton);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v-> backToMainActivity());

        callButton.setOnClickListener(v -> {
            boolean canCall = hasPermission(Manifest.permission.CALL_PHONE);

            if(canCall){
                makeCall();
            }else{
                requestAppPermission(
                        new String[]{
                                Manifest.permission.CALL_PHONE,
                        },
                        REQ_CALL_PHONE
                );
            }
        });

    }

    private void makeCall() {
        phone_number = form_phone.getText().toString();

        if (phone_number.isEmpty()) {
            Toast.makeText(this, "Numer telefonu nie może być pusty!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+ phone_number));
        startActivity(callIntent);
    }

    @Override
    protected void onPermissionsResult(int requestCode, boolean granted) {
        if(requestCode == REQ_CALL_PHONE){
            if(granted) makeCall();
            else Toast.makeText(this, "Brak uprawnień CALL", Toast.LENGTH_SHORT).show();
        }
    }
}
