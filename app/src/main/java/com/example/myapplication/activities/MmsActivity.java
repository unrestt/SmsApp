package com.example.myapplication.activities;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.myapplication.R;


public class MmsActivity extends BaseActivity {
        private ActivityResultLauncher<String> pickImageLauncher;
        private Uri selectedImageUri;
        Button backButton;
        EditText phone_number;
        ImageView pickedImageView;
        Button pickImageButton;
        Button sendMmsButton;

        @Override protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_mms);
            setupFabMenu();
            pickImageButton = findViewById(R.id.pickImageButton);
            phone_number = findViewById(R.id.tel);
            pickedImageView = findViewById(R.id.pickedImageView);
            sendMmsButton = findViewById(R.id.sendMmsButton);
            pickImageLauncher = registerForActivityResult(
                    new ActivityResultContracts.GetContent(), uri -> {
                        if (uri != null) {
                            pickedImageView.setImageURI(uri);
                            selectedImageUri = uri;
                        }
                    });
            pickImageButton.setOnClickListener(v -> {
                pickImageLauncher.launch("image/*");
            });

            sendMmsButton.setOnClickListener(v -> {
                if (selectedImageUri != null && !phone_number.getText().toString().isEmpty()) {
                    sendMms(phone_number.getText().toString(), selectedImageUri);
                } else {
                    Toast.makeText(this, "Wybierz zdjęcie i wpisz numer", Toast.LENGTH_SHORT).show();
                }
            });

            backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener(v-> backToMainActivity());
        }
    private void sendMms(String phoneNumber, Uri imageUri) {
            try {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("image/*");
                sendIntent.putExtra("address", phoneNumber);
                sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(sendIntent, "Wyślij MMS"));
            } catch (Exception e) {
                Toast.makeText(this, "Nie można wysłać MMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        @Override protected void onPermissionsResult(int requestCode, boolean granted) { } }