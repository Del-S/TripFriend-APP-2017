package com.tripfriend.model.api;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class DownloadFileService extends IntentService {

    public DownloadFileService(String name) {
        super(name);
    }

    public DownloadFileService() {
        super("DownloadFileService");
    }

    // Download files
    @Override
    protected void onHandleIntent(Intent intent) {
        File dir = getFilesDir();
        List<String> downloads_url = intent.getStringArrayListExtra("downloads_url");

        for( String download_url : downloads_url ) {
            boolean download = true;
            String filename = download_url;
            filename = filename.substring(filename.lastIndexOf("/") + 1);
            for (File file : dir.listFiles()) {
                if (file.getName().equals(filename)) download = false;
            }

            if (download) {
                try {
                    URL url = new URL(download_url);
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    String filename_download = url.getFile();
                    filename = filename_download.substring(filename_download.lastIndexOf("/") + 1);

                    InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                    OutputStream outputStream = openFileOutput(filename, MODE_PRIVATE);

                    byte data[] = new byte[1024];
                    int count;
                    while ((count = inputStream.read(data)) != -1) {
                        outputStream.write(data, 0, count);
                    }
                    outputStream.close();
                    inputStream.close();

                    showNotification(true, "File downloaded");

                } catch (Exception e) {
                    e.printStackTrace();
                    showNotification(false, e.getMessage());
                    showNotification(false, download_url);
                }
            }
        }
        this.stopSelf();
    }

    private void showNotification(boolean success, String title) {
        String text;
        if(success) {
            text = "Everything alright.";
        } else {
            text = "Error occurred.";
        }

        // Notification in top bar
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(android.R.drawable.ic_dialog_info);

        // Build and show this notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}

