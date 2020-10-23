package com.example.kidstodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class TrophyCase extends AppCompatActivity {

    private static ArrayList<Trophy> earnedTrophy = new ArrayList<>();
    private static ArrayList<Trophy> availableTrophy = new ArrayList<>();
    private TrophyAdapter adapter;
    private GridLayout earnedGrid;
    private GridLayout availableGrid;
    //private ToDoAdapter adapter;  change todoadapter
    private Button homeBtn;
    private Button createNewTrophy;
    private TextView numPt;
    public int points;
    private MainActivity main = new MainActivity();
   // Boolean inParentMode = main.inParentMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy_case);
        numPt.findViewById(R.id.numPt);
        homeBtn.findViewById(R.id.homeBtn);
        earnedGrid.findViewById(R.id.earnedGrid);
        availableGrid.findViewById(R.id.availableGrid);
        TextView earnedText = (TextView) findViewById(R.id.earnedText);
        TextView availableText = (TextView) findViewById(R.id.availableText);
        ImageButton earnedPrize = (ImageButton) findViewById(R.id.earnedPrize);
        ImageButton availablePrize = (ImageButton) findViewById(R.id.availablePrize);
        numPt.setText(String.format(Locale.US, "$%d", points));
        adapter = new TrophyAdapter(earnedTrophy, (TrophyAdapter.OnEntryListener) this);
       // earnedGrid.setAdapter(adapter);

        createNewTrophy = findViewById(R.id.AddTrophy);
        createNewTrophy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddTrophy.class);
                //startActivityForResult(intent, NEW);
            }
        });

        earnedPrize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TrophyCase.this, "You already have this one!", Toast.LENGTH_LONG).show();
            }
        });
        availablePrize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrophyCase.this, BuyingTrophy.class));
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startIntent);
            }
        });
    }
}