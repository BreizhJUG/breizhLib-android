package org.breizhjug.breizhlib.utils;


import android.content.Context;
import android.content.Intent;

public class IntentSupport {

    private static final String MIME_TYPE_EMAIL = "message/rfc822";
    private static final String MIME_TYPE_TEXT = "text/plain";

    public static Intent newShareIntent(Context context, String subject, String message,
                                        String chooserDialogTitle) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        shareIntent.setType(MIME_TYPE_TEXT);
        return Intent.createChooser(shareIntent, chooserDialogTitle);
    }
}
