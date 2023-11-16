package com.example.vacationscheduler.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.vacationscheduler.R;
import com.example.vacationscheduler.database.Repository;
import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import kotlin.collections.ArrayDeque;

public class VacationDetails extends AppCompatActivity {
    String title;
    String hotel;
    String startDate;
    String endDate;
    int vacationID;
    EditText editTitle;
    EditText editHotel;
    EditText editStartDate;
    EditText editEndDate;
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);

        editTitle = findViewById(R.id.titletext);
        editHotel = findViewById(R.id.hoteltext);
        vacationID = getIntent().getIntExtra("ID", -1);
        editStartDate = findViewById(R.id.startdatetext);
        editEndDate = findViewById(R.id.enddatetext);
        title = getIntent().getStringExtra("Title");
        hotel = getIntent().getStringExtra("Hotel");
        startDate = getIntent().getStringExtra("StartDate");
        endDate = getIntent().getStringExtra("EndDate");
        editTitle.setText(title);
        editHotel.setText(hotel);
        editStartDate.setText(startDate);
        editEndDate.setText(endDate);
        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository= new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayDeque<>();
        for (Excursion e : repository.getAllExcursions()){
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.vacationsave){
            Vacation vacation;
            if (vacationID == -1){
                if(repository.getAllVacations().size() == 0) vacationID = 1;
                else vacationID = repository.getAllVacations().get(repository.getAllVacations().size() -1).getVacationID() + 1;
                vacation = new Vacation(vacationID, editTitle.getText().toString(), editHotel.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                repository.insert(vacation);
                this.finish();
            }
            else{
                vacation = new Vacation(vacationID, editTitle.getText().toString(), editHotel.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                repository.update(vacation);
                this.finish();
            }
        }
        return true;
    }
}