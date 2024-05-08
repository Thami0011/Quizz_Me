package com.example.quizme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.quizme.Quiz2;

public class Quiz1 extends AppCompatActivity {
    Button  next;
    RadioGroup rg;
    RadioButton rb;
    String correctresponse="Paris";
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1);
        next=findViewById(R.id.button1);
        rg=findViewById(R.id.rg);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(rg.getCheckedRadioButtonId()==-1)
                {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
                else{
                    rb=findViewById(rg.getCheckedRadioButtonId());
                    if(rb.getText().toString().equals(correctresponse)) score+=1;
                    Intent i1=new Intent(getApplicationContext(), Quiz2.class);
                    i1.putExtra("score",score);
                    startActivity(i1);
                    //overridePendingTransition(R.anim,R.anim.entry) Transition
                    finish();

                }


            }
        });
    }
}