package com.example.reorganizedcurrencyapp2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "MyStorage";
    String content = "";
    String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    ArrayList<Currency> currenciesList;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences dariaStorage = getSharedPreferences(PREFS_NAME, 0);

        LocalDate lastUpdate = LocalDate.parse(dariaStorage.getString("last_update", null));
        LocalDate now = LocalDate.now();

        TextView textView = findViewById(R.id.textView1);
        textView.setText("Последнее скачивание данных было: ".concat(lastUpdate.toString()));

        Log.v("LastUpdate", lastUpdate.toString());
        Log.v("now", now.toString());


        if (!now.isEqual(lastUpdate)) {
            new Thread(() -> {
                try {
                    content = getContent(url);

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(new StringReader(content));

                    CurrencyXmlParser currencyParser = new CurrencyXmlParser(xpp);
                    currenciesList = currencyParser.getCurrencyList();
                    CurrenciesList.writeArrayList(this, currenciesList);
                    setLastUpdate(LocalDate.now(), dariaStorage);


                } catch (XmlPullParserException | IOException e) {
                    throw new RuntimeException(e);
                }

            }).start();
        }

    }

    public void goToSecondActivity(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
        CurrenciesList.readArrayList(this);
    }

    private String getContent(String path) throws IOException {
        BufferedReader reader=null;
        InputStream stream = null;
        HttpsURLConnection connection = null;
        try {
            URL url=new URL(path);
            connection =(HttpsURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.connect();
            stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            Log.v("Reader", String.valueOf(reader));
            StringBuilder buf=new StringBuilder();
            String line;
            while ((line=reader.readLine()) != null) {
                buf.append(line).append("\n");
            }

            return(buf.toString());
        }
        finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public void setLastUpdate(LocalDate date, SharedPreferences storage) {
        SharedPreferences.Editor storedDataEditor = storage.edit();
        storedDataEditor.putString("last_update", String.valueOf(date));
        storedDataEditor.commit();
    }
}