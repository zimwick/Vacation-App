package com.example.vacationscheduler.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.vacationscheduler.R;
import com.example.vacationscheduler.database.Repository;

public class ExcursionDetails extends AppCompatActivity {

    String title;
    String date;
    int excursionID;
    int vacationID;
    EditText editTitle;
    EditText editDate;
    Repository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);
        repository = new Repository(getApplication());
        title = getIntent().getStringExtra("Title");
        editTitle = findViewById(R.id.extitletext);
        editTitle.setText(title);
        date = getIntent().getStringExtra("Date");
        editDate = findViewById(R.id.exdatetext);
        editDate.setText(date);
        excursionID = getIntent().getIntExtra("ID", -1);
        vacationID = getIntent().getIntExtra("vacID", -1);

    }
}