package com.example.myapplication.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.*;
import java.util.ArrayList;
import android.os.Handler;
import android.os.Looper;

import com.example.myapplication.R;
import com.example.myapplication.adapters.MessageAdapter;
import com.example.myapplication.models.MessageModel;

public class SmsActivity extends BaseActivity {


    private static final int REQ_SMS = 100;

    SmsManager smsManager;
    String phone_number = "";
    String message_content = "";
    EditText form_phone;
    EditText form_content;
    Button sendSms;
    Button sendIntent;
    Button sendMultiPart;
    Button wyzerujButton;

    Button backButton;

    private ArrayList<MessageModel> messageList = new ArrayList<>();
    private MessageAdapter adapter;

    RecyclerView messageRecyclerView;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        setupFabMenu();
        form_phone = findViewById(R.id.tel);
        form_content = findViewById(R.id.message);
        sendSms = findViewById(R.id.smsButton);
        sendIntent = findViewById(R.id.intentButton);
        sendMultiPart = findViewById(R.id.mpButton);
        wyzerujButton = findViewById(R.id.wyzeruj);
        backButton = findViewById(R.id.backButton);


        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        messageRecyclerView = findViewById(R.id.recycler_view_messages);
        messageRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MessageAdapter(messageList);
        messageRecyclerView.setAdapter(adapter);



        sendSms.setOnClickListener(v -> {
            boolean canSend = hasPermission(Manifest.permission.SEND_SMS)
                    && hasPermission(Manifest.permission.RECEIVE_SMS);

            if (canSend) {
                sendWithSmsManager();
            } else {
                requestAppPermission(
                        new String[]{
                                Manifest.permission.SEND_SMS,
                                Manifest.permission.RECEIVE_SMS
                        },
                        REQ_SMS
                );
            }
        });

        sendIntent.setOnClickListener(v -> sendSmsWithIntent());
        sendMultiPart.setOnClickListener(v -> sendMultipartSms());
        backButton.setOnClickListener(v-> backToMainActivity());
        wyzerujButton.setOnClickListener(v -> clearMessagesFile("messages.txt"));



        loadMessagesFromFile();
    }

    @Override
    protected void onPermissionsResult(int requestCode, boolean granted) {
        if (requestCode == REQ_SMS) {
            if (granted) sendWithSmsManager();
            else Toast.makeText(this, "Brak uprawnień SMS", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendWithSmsManager() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            phone_number = form_phone.getText().toString();
            message_content = form_content.getText().toString();

            if (!phone_number.equals("") && !message_content.equals("")) {
                smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone_number, null, message_content, null, null);
                Toast.makeText(getApplicationContext(), "SMS wysłany.", Toast.LENGTH_SHORT).show();
                form_phone.setText("");
                form_content.setText("");
            } else {
                Toast.makeText(getApplicationContext(), "Numer telefonu i wiadomość nie mogą być puste!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Brak permisji SMS", Toast.LENGTH_LONG).show();
        }
    }

    private void sendSmsWithIntent() {
        phone_number = form_phone.getText().toString();
        message_content = form_content.getText().toString();

        if (phone_number.isEmpty() || message_content.isEmpty()) {
            Toast.makeText(this, "Numer telefonu i wiadomość nie mogą być puste!", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = Uri.parse("smsto:" + phone_number);
        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", message_content);

        try {
            startActivity(intent);
            Toast.makeText(this, "SMS intent zrealizowany.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Błąd wysyłania SMS intent: " + e);
        }
    }

    private void sendMultipartSms() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            phone_number = form_phone.getText().toString();
            message_content = form_content.getText().toString();

            if (!phone_number.equals("") && !message_content.equals("")) {
                smsManager = SmsManager.getDefault();
                ArrayList<String> multipartText = smsManager.divideMessage(message_content);
                smsManager.sendMultipartTextMessage(phone_number, null, multipartText, null, null);
                Toast.makeText(getApplicationContext(), "Multipart wiadomość wysłana.", Toast.LENGTH_LONG).show();
                form_phone.setText("");
                form_content.setText("");
            }
        }
    }

    private void loadMessagesFromFile() {
        ArrayList<MessageModel> list = new ArrayList<>();

        try {
            FileInputStream fis = openFileInput("messages.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));


            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    list.add(new MessageModel(parts[0], parts[1]));
                }
            }
            br.close();
        } catch (IOException e) {
            Log.e(TAG, "Brak pliku lub błąd odczytu -> SMS");
        }
        adapter.updateList(list);

        new Handler(Looper.getMainLooper()).postDelayed(this::loadMessagesFromFile, 1000);
    }

    private void clearMessagesFile(String fileName) {
        try {
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write("".getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
