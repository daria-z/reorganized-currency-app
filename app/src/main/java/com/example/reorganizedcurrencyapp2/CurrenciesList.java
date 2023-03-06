package com.example.reorganizedcurrencyapp2;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class CurrenciesList {
    private final static String ARRAY_FILENAME = "arrayList";

    static LocalDate updatingDate;

    static ArrayList<Currency> currenciesArray;

    public static void writeArrayList(Context context, ArrayList arrayList) {
        try {
            FileOutputStream fos = context.openFileOutput(ARRAY_FILENAME, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            Log.v("ЗАПИСАТЬ", "array list");

            oos.writeObject(arrayList);

            oos.close();
            fos.close();

        } catch (FileNotFoundException e) {
            Log.e("File not found : ", String.valueOf(e));
            throw new RuntimeException(e);
        } catch (IOException ioe) {
            Log.e("Error writing data : ", String.valueOf(ioe));
            ioe.printStackTrace();
        }
    }

    public static ArrayList<Currency> readArrayList(Context context) {
        try {
            FileInputStream fis = context.openFileInput(ARRAY_FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);

            ArrayList<Currency> currencies = (ArrayList<Currency>) ois.readObject();
;

            return currencies;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
