package com.example.myapplication.utills;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileManager {
    private static final String TAG = "Bartosz Kutnik";

    public static void appendMessageToFile(Context context, String fileName, String phoneNumber, String message) {
        message = message.replace("\n", " ");
        String toInsert = phoneNumber + "|" + message + "\n";

        try (FileOutputStream fos = context.openFileOutput(fileName + ".txt", Context.MODE_PRIVATE | Context.MODE_APPEND)) {
            fos.write(toInsert.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            Log.v(TAG, "Data couldn't be appended to the file.");
            e.printStackTrace();
        }
    }

}
