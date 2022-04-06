package com.superqrcode.scan.utils;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.zxing.BarcodeFormat;
import com.superqrcode.scan.App;
import com.superqrcode.scan.Const;
import com.superqrcode.scan.R;
import com.superqrcode.scan.Type;
import com.superqrcode.scan.model.ResultOfTypeAndValue;
import com.superqrcode.scan.view.OnActionCallback;

import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QRCodeUtils {


    public static void createQRCode(final String input, OnActionCallback callback) {
        CodeGenerator codeGenerator = new CodeGenerator();
        CodeGenerator.setColor(Color.BLACK);
        codeGenerator.generateQRFor(input);
        codeGenerator.setResultListener(bitmap -> callback.callback(Const.K_RESULT, bitmap));
        codeGenerator.execute();
    }

    public static void createBarCode(final String input, BarcodeFormat barcodeFormat, OnActionCallback callback) {
        CodeGenerator codeGenerator = new CodeGenerator();
        CodeGenerator.setColor(Color.BLACK);
        codeGenerator.generateBarFor(input);
        codeGenerator.setBarcodeFormat(BarcodeFormat.CODE_128);
        codeGenerator.setResultListener(bitmap -> {
            callback.callback(Const.K_RESULT, bitmap);
        });
        codeGenerator.execute();
    }

    public static String getName(String contentType) {
        if (contentType.equals(Type.T_URL)) {
            return App.getInstance().getString(R.string.url);
        }
        if (contentType.equals(Type.T_PHONE)) {
            return App.getInstance().getString(R.string.phone);
        }
        if (contentType.equals(Type.T_CALENDAR)) {
            return App.getInstance().getString(R.string.calendar);
        }
        if (contentType.equals(Type.T_CONTACT)) {
            return App.getInstance().getString(R.string.contact);
        }
        if (contentType.equals(Type.T_EMAIL)) {
            return App.getInstance().getString(R.string.email);
        }
        if (contentType.equals(Type.T_SMS)) {
            return App.getInstance().getString(R.string.sms);
        }
        if (contentType.equals(Type.T_WIFI)) {
            return App.getInstance().getString(R.string.wifi);
        }
        if (contentType.equals(Type.T_GEO)) {
            return App.getInstance().getString(R.string.geo);
        }
        return App.getInstance().getString(R.string.text);
    }

    public static int getIcon(String contentType) {
        if (contentType.equals(Type.T_URL)) {
            return R.drawable.ic_url;
        }
        if (contentType.equals(Type.T_CALENDAR)) {
            return R.drawable.ic_calendar;
        }
        if (contentType.equals(Type.T_PHONE)) {
            return R.drawable.ic_phone;
        }
        if (contentType.equals(Type.T_CONTACT)) {
            return R.drawable.ic_contact;
        }
        if (contentType.equals(Type.T_EMAIL)) {
            return R.drawable.ic_email;
        }
        if (contentType.equals(Type.T_SMS)) {
            return R.drawable.ic_sms;
        }
        if (contentType.equals(Type.T_WIFI)) {
            return R.drawable.ic_wifi;
        }
        if (contentType.equals(Type.T_GEO)) {
            return R.drawable.ic_geo;
        }
        return R.drawable.ic_text;
    }


    public static ResultOfTypeAndValue getResourceType(String result) {
        if (result != null && (result.contains("https://youtu.be") || result.contains("https://www.youtube.com"))) {
            return new ResultOfTypeAndValue(Type.T_URL, result);
        } else if (result != null && Patterns.WEB_URL.matcher(result).matches()) {
            if (!result.startsWith("http://") && !result.startsWith("https://")) {
                result = "http://" + result;
            }
            return new ResultOfTypeAndValue(Type.T_URL, result);
        } else if (result != null && (result.contains("BEGIN:VCARD") || result.contains("begin:vcard")
                || result.contains("MECARD") || result.contains("mecard"))) {
            String name = "";
            String org = "";
            String title = "";
            StringBuilder tel = new StringBuilder();
            String url = "";
            String email = "";
            StringBuilder adr = new StringBuilder();
            String birthDay = "";
            String note = "";
            try {
                Matcher m = Pattern.compile("N:(.*)", Pattern.CASE_INSENSITIVE).matcher(result);
                while (m.find()) {
                    name = m.group(1);
                    if (result.contains("MECARD") || result.contains("mecard")) {
                        assert name != null;
                        name = name.substring(0, name.indexOf(";"));
                    }
                }
                m = Pattern.compile("FN:(.*)", Pattern.CASE_INSENSITIVE).matcher(result);
                while (m.find()) {
                    name = m.group(1);
                    if (result.contains("MECARD") || result.contains("mecard")) {
                        assert name != null;
                        name = name.substring(0, name.indexOf(";"));
                    }
                }
                m = Pattern.compile("ORG:(.*)", Pattern.CASE_INSENSITIVE).matcher(result);
                while (m.find()) {
                    org = m.group(1);
                    if (result.contains("MECARD") || result.contains("mecard")) {
                        assert org != null;
                        org = org.substring(0, org.indexOf(";"));
                    }
                }
                m = Pattern.compile("TITLE:(.*)", Pattern.CASE_INSENSITIVE).matcher(result);
                while (m.find()) {
                    title = m.group(1);
                    if (result.contains("MECARD") || result.contains("mecard")) {
                        assert title != null;
                        title = title.substring(0, title.indexOf(";"));
                    }
                }
                m = Pattern.compile("URL:(.*)", Pattern.CASE_INSENSITIVE).matcher(result);
                while (m.find()) {
                    url = m.group(1);
                    if (result.contains("MECARD") || result.contains("mecard")) {
                        assert url != null;
                        url = url.substring(0, url.indexOf(";"));
                    }
                }
                if (result.contains("BEGIN:VCARD") || result.contains("begin:vcard")) {
                    m = Pattern.compile("TEL.*:(.*)", Pattern.CASE_INSENSITIVE).matcher(result);
                    while (m.find()) {
                        tel.append("\n").append(m.group(1));
                    }
                    m = Pattern.compile("EMAIL.*:(.*)", Pattern.CASE_INSENSITIVE).matcher(result);
                    while (m.find()) {
                        email = m.group(1);
                    }
                    m = Pattern.compile("ADR.*:(.*)", Pattern.CASE_INSENSITIVE).matcher(result);
                    while (m.find()) {
                        adr.append("\n").append(m.group(1));
                    }
                    if (adr.toString().contains(";")) {
                        String[] adrArray = adr.toString().split(";");
                        adr = new StringBuilder();
                        for (String a : adrArray) {
                            adr.append(a).append("\n");
                        }
                    }
                } else {
                    m = Pattern.compile("TEL:(.*)", Pattern.CASE_INSENSITIVE).matcher(result);
                    while (m.find()) {
                        tel = new StringBuilder(Objects.requireNonNull(m.group(1)));
                        tel = new StringBuilder(tel.substring(0, tel.indexOf(";")));
                    }
                    m = Pattern.compile("EMAIL:(.*)", Pattern.CASE_INSENSITIVE).matcher(result);
                    while (m.find()) {
                        email = m.group(1);
                        assert email != null;
                        email = email.substring(0, email.indexOf(";"));

                    }
                    m = Pattern.compile("ADR:(.*)", Pattern.CASE_INSENSITIVE).matcher(result);
                    while (m.find()) {
                        adr = new StringBuilder(Objects.requireNonNull(m.group(1)));
                        adr = new StringBuilder(adr.substring(0, adr.indexOf(";")));

                    }
                }

                m = Pattern.compile("BDAY:(.*)", Pattern.CASE_INSENSITIVE).matcher(result);
                while (m.find()) {
                    birthDay = m.group(1);
                    if (result.contains("MECARD") || result.contains("mecard")) {
                        assert birthDay != null;
                        birthDay = birthDay.substring(0, adr.indexOf(";"));
                    }
                }
                m = Pattern.compile("NOTE:(.*)", Pattern.CASE_INSENSITIVE).matcher(result);
                while (m.find()) {
                    note = m.group(1);
                    if (result.contains("MECARD") || result.contains("mecard")) {
                        assert note != null;
                        note = note.substring(0, note.indexOf(";"));
                    }
                }
                result = name + "\n" + org + "\n" + title + "\n" + tel + "\n" + url + "\n" + email + "\n" + adr + "\n" + birthDay + "\n" + note;
                String[] s = result.split("\n");
                result = "";
                StringBuilder resultBuilder = new StringBuilder(result);
                for (String i : s) {
                    if (resultBuilder.toString().equals("")) {
                        if (!i.equals("")) {
                            resultBuilder = new StringBuilder(i);
                        }
                    } else {
                        if (!i.equals("")) {
                            resultBuilder.append("\n").append(i);
                        }
                    }
                }
                result = resultBuilder.toString();
            } catch (Exception e) {
                return new ResultOfTypeAndValue(Type.T_CONTACT, result);
            }
            return new ResultOfTypeAndValue(Type.T_CONTACT, result);
        } else if (result != null && ((Patterns.PHONE.matcher(result).matches() ||
                result.contains("tel:") || Patterns.PHONE.matcher(result).matches() ||
                result.contains("TEL:"))) && !result.contains("barcode:")) {
            try {
                result = result.replace("tel:", "");
                result = result.replace("TEL:", "");
            } catch (Exception e) {
                return new ResultOfTypeAndValue(Type.T_PHONE, result);
            }
            return new ResultOfTypeAndValue(Type.T_PHONE, result);
        } else if (result != null && (Patterns.EMAIL_ADDRESS.matcher(result).matches() ||
                result.contains("mailto:") || result.contains("MAILTO:") ||
                result.contains("matmsg:") || result.contains("MATMSG:"))) {
            if (result.contains("mailto:") || result.contains("MAILTO:")) {
                try {
                    result = result.replace("mailto:", "");
                    result = result.replace("MAILTO:", "");
                } catch (Exception e) {
                    return new ResultOfTypeAndValue(Type.T_EMAIL, result);
                }
                return new ResultOfTypeAndValue(Type.T_EMAIL, result);
            } else if (result.contains("matmsg:") || result.contains("MATMSG:")) {
                try {
                    String email = "", sub = "", body = "";
                    Matcher m = Pattern.compile("to:(.*)", Pattern.CASE_INSENSITIVE).matcher(result);
                    while (m.find()) {
                        email = m.group(1);
                        assert email != null;
                        email = email.substring(0, email.indexOf(";"));
                        Log.d("EMAIL", email);
                    }
                    m = Pattern.compile("sub:(.*)", Pattern.CASE_INSENSITIVE).matcher(result);
                    while (m.find()) {
                        sub = m.group(1);
                        assert sub != null;
                        sub = sub.substring(0, sub.indexOf(";"));
                    }
                    result = result.replace("\n", " ");
                    m = Pattern.compile("body:(.*)", Pattern.CASE_INSENSITIVE).matcher(result);
                    while (m.find()) {
                        body = m.group(1);
                        assert body != null;
                        body = body.substring(0, body.indexOf(";"));
                    }
                    result = email + "\n" + sub + "\n" + body;
                } catch (Exception e) {
                    return new ResultOfTypeAndValue(Type.T_EMAIL, result);
                }
                return new ResultOfTypeAndValue(Type.T_EMAIL, result);
            } else {
                return new ResultOfTypeAndValue(Type.T_EMAIL, result);
            }

        } else if (result != null && result.contains("barcode:")) {
            result = result.replace("barcode:", "");
            return new ResultOfTypeAndValue(Type.T_BARCODE, result);
        } else if (result != null && (result.contains("WIFI:") || result.contains("wifi:"))) {
            try {
                String ssid = "", type = "", password = "";
                Matcher m = Pattern.compile("s:(.*)", Pattern.CASE_INSENSITIVE).matcher(result);
                while (m.find()) {
                    ssid = m.group(1);
                    assert ssid != null;
                    ssid = ssid.substring(0, ssid.indexOf(";"));
                }
                m = Pattern.compile("t:(.*)", Pattern.CASE_INSENSITIVE).matcher(result);
                while (m.find()) {
                    type = m.group(1);
                    assert type != null;
                    type = type.substring(0, type.indexOf(";"));
                }
                m = Pattern.compile("p:(.*)", Pattern.CASE_INSENSITIVE).matcher(result);
                while (m.find()) {
                    password = m.group(1);
                    assert password != null;
                    password = password.substring(0, password.indexOf(";"));
                }
                if (type.equals("")) type = "nopass";

                result = ssid + "\n" + type + "\n" + password;
            } catch (Exception e) {
                return new ResultOfTypeAndValue(Type.T_WIFI, result);
            }
            return new ResultOfTypeAndValue(Type.T_WIFI, result);
        } else if (result != null && (result.contains("SMSTO:") || result.contains("smsto:"))) {
            try {
                String number;
                StringBuilder message = new StringBuilder();
                result = result.replace("SMSTO:", "");
                result = result.replace("smsto:", "");
                if (result.contains(":")) {
                    String[] str = result.split(":");
                    number = str[0];
                    if (str.length > 1) {
                        for (int i = 1; i < str.length; i++) {
                            if (message.toString().equals("")) {
                                message = new StringBuilder(str[i]);
                            } else {
                                message.append(":").append(str[i]);
                            }
                        }
                    }
                    result = number + "\n" + message;
                }
            } catch (Exception e) {
                return new ResultOfTypeAndValue(Type.T_SMS, result);
            }
            return new ResultOfTypeAndValue(Type.T_SMS, result);
        } else if (result != null && (result.contains("GEO:") || result.contains("geo:"))) {
            try {
                result = result.replace("GEO:", "");
                result = result.replace("geo:", "");
            } catch (Exception e) {
                return new ResultOfTypeAndValue(Type.T_GEO, result);
            }
            return new ResultOfTypeAndValue(Type.T_GEO, result);
        } else {
            return new ResultOfTypeAndValue(Type.T_TEXT, result);
        }

    }


    public static String getURLCode(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        text = text.toLowerCase().trim();
        return (text.contains("http://") || text.contains("https://")) ? text : "http://" + text;
    }

    public static String getTextCode(String text) {
        String data = text.trim();
        if (TextUtils.isEmpty(data)) return "";
        return data;
    }

    public static String REGEX_NUMBER_ONLY = "^[0-9]*$";

    public static void call(Context context, String scannedResult, boolean isSMS) {
        if (isSMS) {
            String number = "", message = "";
            scannedResult = scannedResult.replace("SMSTO:", "");
            scannedResult = scannedResult.replace("smsto:", "");
            //scannedResult = "rrrrr";
            number = scannedResult;
            if (scannedResult.contains(":")) {
                String[] str = scannedResult.split(":");
                number = str[0];
                if (str.length > 1) {
                    for (int i = 1; i < str.length; i++) {
                        if (message.equals("")) {
                            message = str[i];
                        } else {
                            message = message + ":" + str[i];
                        }
                    }
                }
                //scannedResult = number + "\n" + message;
            }
            context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null)));
            return;
        }

        Matcher m;
        String tel = "";
        if (scannedResult.contains("BEGIN:VCARD") || scannedResult.contains("begin:vcard")) {
            m = Pattern.compile("TEL.*:(.*)", Pattern.CASE_INSENSITIVE).matcher(scannedResult);
            while (m.find()) {
                tel = m.group(1);
            }
        } else {
            m = Pattern.compile("TEL:(.*)", Pattern.CASE_INSENSITIVE).matcher(scannedResult);
            while (m.find()) {
                tel = m.group(1);
                if (scannedResult.contains("MECARD") || scannedResult.contains("mecard")) {
                    assert tel != null;
                    tel = tel.substring(0, tel.indexOf(";"));
                }
            }
        }
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", tel, null)));


//        if ((ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)) {
//        }
    }

    public static void addContact(Activity activity, String scannedResult) {
        Matcher m = Pattern.compile("N:(.*)", Pattern.CASE_INSENSITIVE).matcher(scannedResult);
        String name = "";
        while (m.find()) {
            name = m.group(1);
            if (scannedResult.contains("MECARD") || scannedResult.contains("mecard")) {
                assert name != null;
                name = name.substring(0, name.indexOf(";"));
            }
        }
        String tel = "";
        if (scannedResult.contains("BEGIN:VCARD") || scannedResult.contains("begin:vcard")) {
            m = Pattern.compile("TEL.*:(.*)", Pattern.CASE_INSENSITIVE).matcher(scannedResult);
            while (m.find()) {
                tel = m.group(1);
            }
        } else {
            m = Pattern.compile("TEL:(.*)", Pattern.CASE_INSENSITIVE).matcher(scannedResult);
            while (m.find()) {
                tel = m.group(1);
                assert tel != null;
                tel = tel.substring(0, tel.indexOf(";"));
            }
        }
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, tel);
        int PICK_CONTACT = 100;
        activity.startActivityForResult(intent, PICK_CONTACT);
    }

    public static void goURL(Context context, String scannedResult) {
        Matcher m = Pattern.compile("URL:(.*)", Pattern.CASE_INSENSITIVE).matcher(scannedResult);
        String url = "";
        while (m.find()) {
            url = m.group(1);
            if (scannedResult.contains("MECARD") || scannedResult.contains("mecard")) {
                assert url != null;
                url = url.substring(0, url.indexOf(";"));
            }
        }
        assert url != null;
        if (url.contains("http://") || url.contains("https://")) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } else {
            url = "https://" + url;
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }

    public static void sendMessage(Context activity, String scannedResult) {
        try {
            String number = "", message = "";
            scannedResult = scannedResult.replace("SMSTO:", "");
            scannedResult = scannedResult.replace("smsto:", "");
            //scannedResult = "rrrrr";
            number = scannedResult;
            if (scannedResult.contains(":")) {
                String[] str = scannedResult.split(":");
                number = str[0];
                if (str.length > 1) {
                    for (int i = 1; i < str.length; i++) {
                        if (message.equals("")) {
                            message = str[i];
                        } else {
                            message = message + ":" + str[i];
                        }
                    }
                }
                //scannedResult = number + "\n" + message;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + number));
            intent.putExtra("sms_body", message);
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.unknow_error), Toast.LENGTH_SHORT).show();
        }
    }

    public static void sendEmail(Context activity, String scannedResult) {
        if (scannedResult.contains("mailto:") || scannedResult.contains("MAILTO:")) {
            try {
                scannedResult = scannedResult.replace("mailto:", "");
                scannedResult = scannedResult.replace("MAILTO:", "");

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{scannedResult});
                //i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                //i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                activity.startActivity(Intent.createChooser(i, "Send Email"));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.no_email_client), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.unknow_error), Toast.LENGTH_SHORT).show();
            }
        } else if (scannedResult.contains("matmsg:") || scannedResult.contains("MATMSG:")) {
            try {
                String email = "", sub = "", body = "";
                Matcher m = Pattern.compile("to:(.*)", Pattern.CASE_INSENSITIVE).matcher(scannedResult);
                while (m.find()) {
                    email = m.group(1);
                    assert email != null;
                    email = email.substring(0, email.indexOf(";"));
                    Log.d("EMAIL", email);
                }
                m = Pattern.compile("sub:(.*)", Pattern.CASE_INSENSITIVE).matcher(scannedResult);
                while (m.find()) {
                    sub = m.group(1);
                    assert sub != null;
                    sub = sub.substring(0, sub.indexOf(";"));
                }
                scannedResult = scannedResult.replace("\n", " ");
                m = Pattern.compile("body:(.*)", Pattern.CASE_INSENSITIVE).matcher(scannedResult);
                while (m.find()) {
                    body = m.group(1);
                    assert body != null;
                    body = body.substring(0, body.indexOf(";"));
                }
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                i.putExtra(Intent.EXTRA_SUBJECT, sub);
                i.putExtra(Intent.EXTRA_TEXT, body);
                activity.startActivity(Intent.createChooser(i, "Send Email"));
                //result = email + "\n" + sub + "\n" + body;
            } catch (ActivityNotFoundException e) {
                Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.no_email_client), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.unknow_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void vibrateDevice(Context context) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);
    }


    public static void searchInWeb(Activity activity, String text) {
        try {
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, text);
            activity.startActivity(intent);
        } catch (Exception e) {
            try {
                String escapedQuery = URLEncoder.encode(text, "UTF-8");
                Uri uri = Uri.parse("http://www.google.com/search?q=" + escapedQuery);
                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent1);
            } catch (Exception e1) {
                Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.no_browse), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void copyToClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
//        Toast.makeText(context, context.getString(R.string.copy_in_boardcast), Toast.LENGTH_SHORT).show();
    }

    public static void connectWifi(Context activity, String scannedResult) {
        try {
            String ssid = "", typeOfWIFI = "", password = "";
            Matcher m = Pattern.compile("s:(.*)", Pattern.CASE_INSENSITIVE).matcher(scannedResult);
            while (m.find()) {
                ssid = m.group(1);
                ssid = ssid.substring(0, ssid.indexOf(";"));
            }
            m = Pattern.compile("t:(.*)", Pattern.CASE_INSENSITIVE).matcher(scannedResult);
            while (m.find()) {
                typeOfWIFI = m.group(1);
                typeOfWIFI = typeOfWIFI.substring(0, typeOfWIFI.indexOf(";"));
            }
            m = Pattern.compile("p:(.*)", Pattern.CASE_INSENSITIVE).matcher(scannedResult);
            while (m.find()) {
                password = m.group(1);
                password = password.substring(0, password.indexOf(";"));
            }
            if (typeOfWIFI.equals("")) typeOfWIFI = "nopass";

            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + ssid + "\"";

            if (typeOfWIFI.equals("WEP")) {
                conf.wepKeys[0] = "\"" + password + "\"";
                conf.wepTxKeyIndex = 0;
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            } else if (typeOfWIFI.equals("WPA")) {
                conf.preSharedKey = "\"" + password + "\"";
            } else if (typeOfWIFI.equals("nopass")) {
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            } else {
                Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.wiffi_error), Toast.LENGTH_SHORT).show();
            }
            WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.addNetwork(conf);

            WifiManager wifi = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifi.setWifiEnabled(true);

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();
                    Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.connecting), Toast.LENGTH_SHORT).show();
                    break;
                }
            }

        } catch (Exception e) {
            Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.unknow_error), Toast.LENGTH_SHORT).show();
        }
    }


    public static String getPhoneCode(String text) {
        String data = text.trim();
        if (TextUtils.isEmpty(data) || !data.matches(REGEX_NUMBER_ONLY)) return "";
        return "tel:" + data;
    }

    public static String getContactCode(String name, String org,
                                        String phone, String email, String address, String note) {
        return "MECARD:\n" +
                "N:" + name + "\n" +
                "ORG:" + org + "\n" +
                "ADR:" + address + "\n" +
                "TEL:" + phone + "\n" +
                "EMAIL:" + email + "\n" +
                "NOTE:" + note + "\n" +
                ";";
    }

    public static String getGeoCode(String latitude, String longitude, String query) {
        return "geo:" + latitude + "," + longitude + "?\n" + "q=" + query;
    }

    public static String getCalendarCode(String even, String dateStart, String timeStart, String dateEnd,
                                         String timeEnd, String location, String des, String uid) {
        //dateStart yyyymmdd
        //dayStart hhmmss
        return "BEGIN:VCALENDAR\n" +
                "VERSION:2.0\n" +
                "PRODID:" + even + "\n" +
                "BEGIN:VEVENT\n" +
                "SUMMARY:" + des + "\n" +
                "DTSTART:" + dateStart + "T" + timeStart + "\n" +
                "DTEND:" + dateEnd + "T" + timeEnd + "\n " +
                "LOCATION:" + location + "\n" +
                "UID:" + uid + "\n" +
                "DESCRIPTION:" + des + "\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR";
    }

    public static String getEmailCode(String email, String sub, String content) {
        return "MATMSG:TO:" + email +
                ";SUB:" + sub +
                ";BODY:" + content + ";;";
    }

    public static String getSMSCode(String recipient, String message) {
        return "SMSTO:" + recipient +
                ":" + message;
    }

    public static String getWifiCode(String ssid, String pass, String type, boolean hidden) {
        return "WIFI:S:" + ssid + ";T:" + type + ";P:" + pass + ";H:" + hidden + ";;";
    }


    public static void openUrl(Activity activity, String content) {
        if (activity == null) {
            return;
        }
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                    content)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
