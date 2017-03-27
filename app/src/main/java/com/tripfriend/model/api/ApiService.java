package com.tripfriend.model.api;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;

public class ApiService {

    private Context context;
    private static final String appKey = "2ck15x21-m2k5-37su-5a89-42sfu28f5c61";
    //private ArrayList<String> download_send;

    public ApiService(Context context) {
        this.context = context;
        checkListener();
        //download_send = new ArrayList<>();
    }

    public void sendPostApi(String urlString, JSONObject jsonSend) {
        ApiAsyncTask asyncTaskApi = new ApiAsyncTask(context);
        asyncTaskApi.execute("POST", urlString, jsonSend.toString());
    }

    public void sendGetApi(String urlString) {
        ApiAsyncTask asyncTaskApi = new ApiAsyncTask(context);
        asyncTaskApi.execute("GET", urlString);
    }

    static HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.put("App-key", appKey);

        return headers;
    }

    private void checkListener() {
        try {
            ApiResultListener arl = (ApiResultListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ApiResultListener.");
        }
    }

    /*public List<String> downloadFiles(String[] downloadUrls) {
        List<String> files = new ArrayList<>();
        for(String downloadUrl : downloadUrls) {
            String downloaded_file = downloadFile(downloadUrl);
            files.add(downloaded_file);
        }
        return files;
    }

    public String downloadFile(String downloadUrl) {
        // Alter url from localhost to 10.0.2.2 for localhost only
        if(downloadUrl.contains("localhost")) {
            downloadUrl = downloadUrl.replace("localhost", "10.0.2.2");
        }

        File dir = context.getFilesDir();
        boolean download = true;
        String filename = downloadUrl;
        filename = filename.substring(filename.lastIndexOf("/") + 1);
        for(File file : dir.listFiles()) {
            if( file.getName().equals(filename) ) {
                download = false;
                break;
            }
        }

        if (download) {
            download_send.add(downloadUrl);
        }
        return filename;
    }

    public void initiateDownload() {
        if(download_send != null) {
            if (!download_send.isEmpty()) {
                Intent intent = new Intent(context, DownloadFileService.class);
                intent.putStringArrayListExtra("downloads_url", download_send);

                context.startService(intent);
            }
        }
        download_send = new ArrayList<>();
    }*/
}
