package com.clark.steparc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private StepArcView stepArcView;
    private static int currentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stepArcView = (StepArcView) findViewById(R.id.id_stepArcView);
        currentStep = 4000;
        stepArcView.setCurrentCount(7000, currentStep);
        Button button_clear = (Button) findViewById(R.id.id_clear);
        Button button_add = (Button) findViewById(R.id.id_add);
        Button button_reset = (Button) findViewById(R.id.id_reset);
        button_add.setOnClickListener(this);
        button_clear.setOnClickListener(this);
        button_reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_clear:
                currentStep = 0;
                stepArcView.setCurrentCount(7000, currentStep);
                break;
            case R.id.id_add:
                currentStep += 100;
                stepArcView.setCurrentCount(7000, currentStep);
                break;
            case R.id.id_reset:
                currentStep  = 4000;
                stepArcView.setCurrentCount(7000, currentStep);
                break;
        }
    }
}
