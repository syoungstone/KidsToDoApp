package com.example.kidstodoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

// This comment was made by Sean Youngstone
public class MainActivity extends AppCompatActivity implements ToDoAdapter.OnEntryListener {

    private ToDoAdapter adapter = new ToDoAdapter(toDoEntries, this);

    private TextView pointsDisplay;

    private static ArrayList<ToDoEntry> toDoEntries = new ArrayList<>();
    private static ArrayList<ToDoEntry> completedEntries = new ArrayList<>();
    private static int pointsEarned = 0;

    private final int NEW_ENTRY_REQUEST = 1;
    private final int VIEW_ENTRY_REQUEST = 2;
    private final int EDIT_ENTRY_REQUEST = 3;

    private String password;
    private String phoneNumber;
    private Boolean notFirstTime = null;         //Used to determine if the parent needs to set up a password
    private Boolean inParentMode = false;        //Used to determine if the parent is in parent mode or not
    private Button parentModeButton;
    private Button addEntryButton;
    private ImageButton setPhoneNumberButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        pointsDisplay = findViewById(R.id.points_display);
        setPointsDisplay();

        addEntryButton = findViewById(R.id.add_entry_button);
        addEntryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreateToDoEntryActivity.class);
                startActivityForResult(intent, NEW_ENTRY_REQUEST);
            }
        });

        parentModeButton = findViewById(R.id.pM);
        setPhoneNumberButton = findViewById(R.id.phone);

        addEntryButton.setVisibility(View.GONE);
        setPhoneNumberButton.setVisibility(View.GONE);

        parentModeButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if(!inParentMode) {openDialog(notFirstTime);}
                  else
                  {
                      inParentMode = false;
                      Toast.makeText(MainActivity.this,
                              "Logged Out",
                              Toast.LENGTH_SHORT).show();
                      addEntryButton.setVisibility(View.GONE);
                      setPhoneNumberButton.setVisibility(View.GONE);
                      parentModeButton.setText(getResources().getString(R.string.login));
                  }
              }
        });

        setPhoneNumberButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if(inParentMode)
                  {
                      phoneNumberDialog phoneDialog = new phoneNumberDialog();
                      phoneDialog.show(getSupportFragmentManager(), "Set Phone Number");
                  }
                  else {
                      Toast.makeText(MainActivity.this,
                              "Please Enter Parent Mode to Add a Phone Number",
                              Toast.LENGTH_SHORT).show();
                  }
              }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == NEW_ENTRY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle extras = result.getExtras();
                ToDoEntry newToDoEntry = (ToDoEntry) extras.getSerializable("ToDoEntry");
                toDoEntries.add(newToDoEntry);
            }
        }
        if (requestCode == VIEW_ENTRY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle extras = result.getExtras();
                int position = extras.getInt("position");
                ToDoEntry entry = toDoEntries.remove(position);
                adapter.notifyItemRemoved(position);
                entry.setCompleted(true);
                completedEntries.add(entry);
                pointsEarned += entry.getPointValue();
                setPointsDisplay();
            }
        }
    }

    @Override
    public void onEntryClick(int position) {
        Intent intent = new Intent(this, ToDoEntryActivity.class);
        intent.putExtra("ToDoEntry", toDoEntries.get(position));
        intent.putExtra("position", position);
        startActivityForResult(intent, VIEW_ENTRY_REQUEST);
    }

    public void setPointsDisplay() {
        pointsDisplay.setText(String.format(Locale.US, "$%d", pointsEarned));
    }

    public void openDialog(Boolean passwordAlreadySet)
    {
        if(passwordAlreadySet==null)
        {
            parentModeSetPassword parentSetPass = new parentModeSetPassword();
            parentSetPass.show(getSupportFragmentManager(), "Create Password");
        }
        else
        {
            parentModeDialog parentMode = new parentModeDialog();
            parentMode.show(getSupportFragmentManager(), "Enter Password");
        }
    }

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public Boolean getNotFirstTime() {return notFirstTime;}
    public void setNotFirstTime(Boolean notFirstTime) {this.notFirstTime = notFirstTime;}
    public Boolean getInParentMode() {return inParentMode;}
    public void setInParentMode(Boolean inParentMode) {
        this.inParentMode = inParentMode;
        addEntryButton.setVisibility(View.VISIBLE);
        setPhoneNumberButton.setVisibility(View.VISIBLE);
        parentModeButton.setText(getResources().getString(R.string.logout));
    }
    public String getPhoneNumber() {return phoneNumber;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
}
