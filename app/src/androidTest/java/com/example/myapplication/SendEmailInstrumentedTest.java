package com.example.myapplication;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myapplication.activities.EmailActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SendEmailInstrumentedTest {

    @Test
    public void testIntentCreationWithFields() {

        try (ActivityScenario<EmailActivity> scenario =
                     ActivityScenario.launch(EmailActivity.class)) {

            scenario.onActivity(activity -> {

                EditText receiver = activity.findViewById(R.id.emailReceiver);
                EditText subject  = activity.findViewById(R.id.emailSubject);
                EditText message  = activity.findViewById(R.id.emailMessage);

                receiver.setText("test@example.com");
                subject.setText("Subject");
                message.setText("Hello world!");

                activity.findViewById(R.id.emailButton).performClick();

                Intent expected = new Intent(Intent.ACTION_SENDTO);
                expected.setData(Uri.parse("mailto:"));
                expected.putExtra(Intent.EXTRA_EMAIL, new String[]{"test@example.com"});
                expected.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                expected.putExtra(Intent.EXTRA_TEXT, "Hello world!");

                Intent actual = activity.lastSentIntent;

                assertEquals(expected.getAction(), actual.getAction());
                assertEquals(expected.getData().toString(), actual.getData().toString());

                assertArrayEquals(
                        expected.getStringArrayExtra(Intent.EXTRA_EMAIL),
                        actual.getStringArrayExtra(Intent.EXTRA_EMAIL)
                );

                assertEquals(
                        expected.getStringExtra(Intent.EXTRA_SUBJECT),
                        actual.getStringExtra(Intent.EXTRA_SUBJECT)
                );

                assertEquals(
                        expected.getStringExtra(Intent.EXTRA_TEXT),
                        actual.getStringExtra(Intent.EXTRA_TEXT)
                );
            });
        }
    }
}
