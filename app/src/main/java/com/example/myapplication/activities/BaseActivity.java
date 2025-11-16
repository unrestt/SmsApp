package com.example.myapplication.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;

public abstract class BaseActivity extends AppCompatActivity {

    protected static final String TAG = "Bartosz Kutnik";
    private ExtendedFloatingActionButton fabMain;
    private FloatingActionButton fabOption1, fabOption2, fabOption3, fabOption4;
    private boolean isFabMenuOpen = false;
    private Animation fabOpen, fabClose;

    protected Button wyzerujButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupDynamicShortcuts();
    }
    protected void setupFabMenu() {
        fabMain = findViewById(R.id.fab_main);
        fabOption1 = findViewById(R.id.fab_option1);
        fabOption2 = findViewById(R.id.fab_option2);
        fabOption3 = findViewById(R.id.fab_option3);
        fabOption4 = findViewById(R.id.fab_option4);
        wyzerujButton = findViewById(R.id.wyzeruj);


        if (fabMain == null) return;

        fabMain.setOnClickListener(v -> toggleFabMenu());

        fabOption1.setOnClickListener(v -> {
            startActivity(new Intent(this, SmsActivity.class));
            finish();
        });

        fabOption2.setOnClickListener(v -> {
            startActivity(new Intent(this, MmsActivity.class));
            finish();
        });

        fabOption3.setOnClickListener(v -> {
            startActivity(new Intent(this, EmailActivity.class));
            finish();
        });
        fabOption4.setOnClickListener(v -> {
            startActivity(new Intent(this, CallActivity.class));
            finish();
        });

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);

    }

    private void toggleFabMenu() {
        if (!isFabMenuOpen) {
            fabOption1.startAnimation(fabOpen);
            fabOption2.startAnimation(fabOpen);
            fabOption3.startAnimation(fabOpen);
            fabOption4.startAnimation(fabOpen);

            fabOption1.setVisibility(View.VISIBLE);
            fabOption2.setVisibility(View.VISIBLE);
            fabOption3.setVisibility(View.VISIBLE);
            fabOption4.setVisibility(View.VISIBLE);

            fabMain.extend();
            isFabMenuOpen = true;
        } else {

            fabOption1.startAnimation(fabClose);
            fabOption2.startAnimation(fabClose);
            fabOption3.startAnimation(fabClose);
            fabOption4.startAnimation(fabClose);

            new Handler().postDelayed(() -> {
                fabOption1.setVisibility(View.GONE);
                fabOption2.setVisibility(View.GONE);
                fabOption3.setVisibility(View.GONE);
                fabOption4.setVisibility(View.GONE);
            }, 200);

            fabMain.shrink();
            isFabMenuOpen = false;
        }
    }

    protected void backToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // --------------------------------- PERMISJE ---------------------------------------------------
    protected void requestAppPermission(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    protected boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean allGranted = true;

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }

        onPermissionsResult(requestCode, allGranted);
    }

    protected abstract void onPermissionsResult(int requestCode, boolean granted);

    // --------------------------------- SHORTCUTY DYNAMICZNE ---------------------------------------------------
    private void setupDynamicShortcuts() {
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

        if (shortcutManager != null) {
            ShortcutInfo mmsShortcut = new ShortcutInfo.Builder(this, "dynamic_mms")
                    .setShortLabel("Wyślij Mms")
                    .setLongLabel("Otwórz ekran wysyłki SMS")
                    .setIcon(Icon.createWithResource(this, R.drawable.baseline_mms_24))
                    .setIntent(new Intent(this, MmsActivity.class).setAction(Intent.ACTION_VIEW))
                    .build();

            ShortcutInfo callShortCut = new ShortcutInfo.Builder(this, "dynamis.call")
                    .setShortLabel("Zadzwoń")
                    .setLongLabel("Otwórz ekran dzwonienia")
                    .setIcon(Icon.createWithResource(this, R.drawable.baseline_call_24))
                    .setIntent(new Intent(this, CallActivity.class).setAction(Intent.ACTION_VIEW))
                    .build();
            shortcutManager.setDynamicShortcuts(Arrays.asList(mmsShortcut, callShortCut));
        }
    }
}
