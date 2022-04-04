package fi.arcada.projekt_chi2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Deklarera 4 Button-objekt
    Button btn1, btn2, btn3, btn4;
    // Deklarera 4 heltalsvariabler för knapparnas värden
    double val1, val2, val3, val4;
    // TextView
    TextView textPercentage, textPercentage2, statsField;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Koppla samman Button-objekten med knapparna i layouten
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);

        textPercentage = findViewById(R.id.textView3);
        textPercentage2 = findViewById(R.id.textView4);
        statsField = findViewById(R.id.textView5);
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

    public void buttonClear(View view) {
        val1 = 0; val2 = 0; val3 = 0; val4 = 0;
        btn1.setText(String.valueOf(val1));
        btn2.setText(String.valueOf(val2));
        btn3.setText(String.valueOf(val3));
        btn4.setText(String.valueOf(val4));

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
            btn1.setText(String.valueOf(val1));
            btn2.setText(String.valueOf(val2));
            btn3.setText(String.valueOf(val3));
            btn4.setText(String.valueOf(val4));

            // Mata in värdena i Chi-2-uträkningen och ta emot resultatet
            // i en Double-variabel
            double chi2 = Significance.chiSquared(val1, val2, val3, val4);

            // Mata in chi2-resultatet i getP() och ta emot p-värdet
            double pValue = Significance.getP(chi2);

            double prob = 100 - (pValue * 100);
            if (prob < 50) {
                prob = 0;
            }

            double sign = 0.05;

            double tot1 = val1 + val3;
            double tot2 = val2 + val4;
            double p1 = val1/tot1 * 100;
            double p2 = val2/tot2 * 100;

            String childP = String.format("Children: %.1f", p1);
            String adultP = String.format("Adults: %.1f", p2);
            String statsF = String.format("Chi-2 result: %.2f\nSignificance: %.2f\nP-value: %.3f\n\nConfidence level: %.1f",
            chi2, sign, pValue, prob
            );

            textPercentage.setText(childP + "%");
            textPercentage2.setText(adultP + "%");
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