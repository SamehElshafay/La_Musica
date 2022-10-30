package com.example.lamusica;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class setting extends AppCompatActivity {
    private MyDatabase2 myDatabase2 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        myDatabase2 = new MyDatabase2(getApplicationContext() , "mediarules" , "rules") ;

        if(myDatabase2.getMethodSort().equals("by character")){
            ((RadioButton)findViewById(R.id.charac)).setChecked(true);
        }
        else if(myDatabase2.getMethodSort().equals("by time")){
            ((RadioButton)findViewById(R.id.time)).setChecked(true);
        }
        else {
            ((RadioButton)findViewById(R.id.dataAdded)).setChecked(true);
        }

        if(myDatabase2.getSkip().equals("true")){
            ((Switch)findViewById(R.id.skipSwitch)).setChecked(true);
        }else {
            ((Switch)findViewById(R.id.skipSwitch)).setChecked(false);
        }

        ((TextView)findViewById(R.id.numberOfSongs)).setText("number of songs is " + getIntent().getExtras().getString("numberOfSong"));

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RadioGroup rg = (RadioGroup) findViewById(R.id.buttonG);
        ((Switch)findViewById(R.id.skipSwitch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                myDatabase2.changeSkip(String.valueOf(((Switch)findViewById(R.id.skipSwitch)).isChecked()));
            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                myDatabase2.changeMethodSort(((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString());
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}