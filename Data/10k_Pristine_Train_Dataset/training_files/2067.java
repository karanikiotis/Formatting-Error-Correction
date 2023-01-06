package com.github.florent37.slidr;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.florent37.androidslidr.Slidr;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        {
            final Slidr slidr = (Slidr) findViewById(R.id.slideure);
            if (slidr != null) {
                slidr.setMax(500);
                slidr.addStep(new Slidr.Step("test", 250, Color.parseColor("#007E90"), Color.RED));
                slidr.setTextMax("max\nvalue");
                slidr.setTextMin("min\nvalue");
                slidr.setCurrentValue(300);
                slidr.setListener(new Slidr.Listener() {
                    @Override
                    public void valueChanged(Slidr slidr, float currentValue) {

                    }

                    @Override
                    public void bubbleClicked(Slidr slidr) {

                    }
                });
            }

            findViewById(R.id.max).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    slidr.setMax(1000);
                }
            });

            findViewById(R.id.current).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    slidr.setCurrentValue(100);
                }
            });

        }

        {
            final Slidr slidr = (Slidr) findViewById(R.id.slideure2);
            if (slidr != null) {
                slidr.setMax(5000);
                slidr.setCurrentValue(5000);
                slidr.addStep(new Slidr.Step("test", 1500, Color.parseColor("#007E90"), Color.parseColor("#111111")));
            }
        }
        {
            final Slidr slidr = (Slidr) findViewById(R.id.slideure_regions);
            if (slidr != null) {
                slidr.setMax(3000);
                slidr.setRegionTextFormatter(new Slidr.RegionTextFormatter() {
                    @Override
                    public String format(int region, float value) {
                        return String.format("region %d : %d", region, (int) value);
                    }
                });
                slidr.addStep(new Slidr.Step("test", 1500, Color.parseColor("#007E90"), Color.parseColor("#111111")));
            }
        }
    }
}

