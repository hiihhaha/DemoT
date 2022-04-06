package com.superqrcode.scan.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CommonUtils {
    private static final String POLICY_URL = "https://firebasestorage.googleapis.com/v0/b/compass-app-df4f4.appspot.com/o/Privacy_Policy_QRCodeReader.html?alt=media&token=eace85bf-ef92-4c2d-b7ec-ba1f0d4e41c7";
    private static CommonUtils instance;

    private static final String TAG = CommonUtils.class.getName();
    private static final String EMAIL = "verifiedapps.help@gmail.com";
    private static final String SUBJECT = "Feedback for App QRCode";
    private static final String PUBLISH_NAME = "";

    private CommonUtils() {
    }


    public static CommonUtils getInstance() {
        if (instance == null) {
            instance = new CommonUtils();
        }
        return instance;
    }


    public void shareApp(Context context) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody =
                "https://play.google.com/store/apps/details?id=" + context.getPackageName();
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, "Share to"));
    }

    public void support(Context context) {
        Intent mailIntent = new Intent(Intent.ACTION_VIEW);
        Uri data =
                Uri.parse("mailto:?SUBJECT=" + SUBJECT + "&body=" + "" + "&to=" + EMAIL);
        mailIntent.setData(data);
        context.startActivity(Intent.createChooser(mailIntent, "Send mail..."));
    }

    public void rateApp(Context context) {
        try {
            context.startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + context.getPackageName())
                    )
            );
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())
                    )
            );
        }
    }

    public void showPolicy(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                    POLICY_URL)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moreApp(Context context) {
        try {
            context.startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://search?q=pub:" + PUBLISH_NAME)
                    )
            );
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/developer?id=" + PUBLISH_NAME)
                    )
            );
        }
    }


    public void log(String text) {
        Log.d(TAG, text);
    }


    public void saveFile(InputStream fin, String savePath, String nameFile) {
        File file = new File(savePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(new File(savePath + "/" + nameFile));
            int lenght = 0;
            byte buff[] = new byte[1024];
            lenght = fin.read(buff);
            while (lenght > 0) {
                fout.write(buff, 0, lenght);
                lenght = fin.read(buff);
            }
            fin.close();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String formatTime(long duration) {
        return formatTime(duration, false);
    }

    public String formatTime(long duration, boolean isHour) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        if (!isHour) {
            formatter = new SimpleDateFormat("mm:ss");
        }
        Date date = new Date(duration);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }

    public String formatDate(long duration) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        Date date = new Date(duration);
//        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }

    public String formatDate2(long duration) {
        SimpleDateFormat formatter = new SimpleDateFormat("_ddMMyyyy_hhmmss");
        Date date = new Date(duration);
//        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }

    public void shareFile(Context context, File file) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.setType(URLConnection.guessContentTypeFromName(file.getName()));
        intentShareFile.putExtra(Intent.EXTRA_STREAM,
                Uri.parse("file://" + file.getAbsolutePath()));
        context.startActivity(Intent.createChooser(intentShareFile, "Share File"));
    }

    public File getDocumentDirPath(String FolderName) {
        File dir = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + FolderName);
        } else {
            dir = new File(Environment.getExternalStorageDirectory() + "/" + FolderName);
        }

        // Make sure the path directory exists.
        if (!dir.exists()) {
            // Make it, if it doesn't exit
            boolean success = dir.mkdirs();
            if (!success) {
                dir = null;
            }
        }
        return dir;
    }
}
