package fi.arcada.projekt_chi2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Deklarera 4 Button-objekt
    Button btn1, btn2, btn3, btn4;
    // Deklarera 4 heltalsvariabler för knapparnas värden
    int val1, val2, val3, val4;

    // TextView
    TextView textPercentage, textPercentage2, statsField, welcomeField, textViewCol1, textViewCol2, textViewRow1, textViewRow2, textExtra;
    SharedPreferences sharedPref;
    SharedPreferences.Editor prefEditor;
    int welcome = 0;
    boolean welcomeMessage;
    String col1, col2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            // Koppla samman Button-objekten med knapparna i layouten
            btn1 = findViewById(R.id.button1);
            btn2 = findViewById(R.id.button2);
            btn3 = findViewById(R.id.button3);
            btn4 = findViewById(R.id.button4);

            textViewCol1 = findViewById(R.id.textViewCol1);
            textViewCol2 = findViewById(R.id.textViewCol2);
            textViewRow1 = findViewById(R.id.textViewRow1);
            textViewRow2 = findViewById(R.id.textViewRow2);
            textExtra = findViewById(R.id.textView2);

            textPercentage = findViewById(R.id.textView3);
            textPercentage2 = findViewById(R.id.textView4);
            statsField = findViewById(R.id.textView5);
            sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

            prefEditor = sharedPref.edit();
            prefEditor.putInt("welcome", sharedPref.getInt("welcome", 0)+1);
            prefEditor.apply();

            col1 = sharedPref.getString("col1", "Children");
            col2 = sharedPref.getString("col2", "Adults");
            textViewCol1.setText(col1);
            textViewCol2.setText(col2);
            textViewRow1.setText(sharedPref.getString("row1", "Excercise regularly"));
            textViewRow2.setText(sharedPref.getString("row2", "Don't excercise regularly"));
            textExtra.setText(sharedPref.getString("row1", "Excercise regularly"));

            welcome = sharedPref.getInt("welcome", 0);

            welcomeField = findViewById(R.id.textView6);
            welcomeMessage = sharedPref.getBoolean("welcomeMsg", true);

            val1 = sharedPref.getInt("s1", 0);
            val2 = sharedPref.getInt("s2", 0);
            val3 = sharedPref.getInt("s3", 0);
            val4 = sharedPref.getInt("s4", 0);

            btn1.setText(String.valueOf(sharedPref.getInt("s1", 0)));
            btn2.setText(String.valueOf(sharedPref.getInt("s2", 0)));
            btn3.setText(String.valueOf(sharedPref.getInt("s3", 0)));
            btn4.setText(String.valueOf(sharedPref.getInt("s4", 0)));

            if (val1 > 0) {
                calculate();
            }

            if (welcomeMessage == true) {
                if (welcome <= 0) {
                    welcomeField.setText("Welcome! It's your first time here!");
                } else {
                    welcomeField.setText(String.format("Welcome back! You have opened this app %d times. ", welcome));
                }
            } else {
                welcomeField.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  Klickhanterare för knapparna
     */
    public void buttonClick(View view) {

        // Skapa ett Button-objekt genom att type-casta (byta datatyp)
        // på det View-objekt som kommer med knapptrycket
        Button btn = (Button) view;

        // Kontrollera vilken knapp som klickats, öka värde på rätt vaiabel

        if (view.getId() == R.id.button2) val2++;
        if (view.getId() == R.id.button3) val3++;
        if (view.getId() == R.id.button4) val4++;
        if (view.getId() == R.id.button1) val1++;

        // Slutligen, kör metoden som ska räkna ut allt!
        calculate();
    }

    public void settingsClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void buttonClear(View view) {
        val1 = 0; val2 = 0; val3 = 0; val4 = 0;
        btn1.setText(String.valueOf(val1));
        btn2.setText(String.valueOf(val2));
        btn3.setText(String.valueOf(val3));
        btn4.setText(String.valueOf(val4));

        prefEditor.putInt("s1", val1);
        prefEditor.putInt("s2", val2);
        prefEditor.putInt("s3", val3);
        prefEditor.putInt("s4", val4);
        prefEditor.commit();

        textPercentage.setText("");
        textPercentage2.setText("");
        statsField.setText("");
    }

    /**
     * Metod som uppdaterar layouten och räknar ut själva analysen. €
     */
    public void calculate() {
        try {
            // Uppdatera knapparna med de nuvarande värdena
            prefEditor.putInt("s1", val1);
            prefEditor.putInt("s2", val2);
            prefEditor.putInt("s3", val3);
            prefEditor.putInt("s4", val4);
            prefEditor.commit();

            btn1.setText(String.valueOf(sharedPref.getInt("s1", 0)));
            btn2.setText(String.valueOf(sharedPref.getInt("s2", 0)));
            btn3.setText(String.valueOf(sharedPref.getInt("s3", 0)));
            btn4.setText(String.valueOf(sharedPref.getInt("s4", 0)));

            // Mata in värdena i Chi-2-uträkningen och ta emot resultatet
            // i en Double-variabel
            double chi2 = Significance.chiSquared(val1, val2, val3, val4);

            // Mata in chi2-resultatet i getP() och ta emot p-värdet
            double pValue = Significance.getP(chi2);

            String prob = (100 - (pValue * 100)) + "%";

            double sign = Double.parseDouble(sharedPref.getString("significance", "0.05"));

            double tot1 = val1 + val3;
            double tot2 = val2 + val4;
            double p1 = Math.round(val1/tot1 * 100);
            double p2 = Math.round(val2/tot2 * 100);

            String childP = String.format(": %.1f", p1);
            String adultP = String.format(": %.1f", p2);
            String statsF = String.format("Chi-2 result: %.2f\nSignificance: %.2f\nP-value: %.3f\n\nThe result is with the probability of %s not dependant.",
            chi2, sign, pValue, prob
            );

            textPercentage.setText(col1 + childP + "%");
            textPercentage2.setText(col2 + adultP + "%");
            statsField.setText(statsF);

            /**
             *  - Visa chi2 och pValue åt användaren på ett bra och tydligt sätt!
             *
             *  - Visa procentuella andelen jakande svar inom de olika grupperna.
             *    T.ex. (val1 / (val1+val3) * 100) och (val2 / (val2+val4) * 100
             *
             *  - Analysera signifikansen genom att jämföra p-värdet
             *    med signifikansnivån, visa reultatet åt användaren
             *
             */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}