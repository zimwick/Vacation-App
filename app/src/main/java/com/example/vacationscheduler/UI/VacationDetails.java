package com.example.vacationscheduler.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.vacationscheduler.R;
import com.example.vacationscheduler.database.Repository;
import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editStartDate);
            }
        });

        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editEndDate);
            }
        });

        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                //sends the actual viewed vacation ID over
                intent.putExtra("vacID", vacationID);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository= new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getAllExcursions()){
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);
    }

    private void showDatePickerDialog(final EditText dateEditText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                VacationDetails.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
                        dateEditText.setText(selectedDate);
                    }
                }, year, month, day);
        datePickerDialog.show();
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
        } else if (item.getItemId() == R.id.vacationdelete){
            if(vacationID != -1){
                // Check if there are any associated excursions
                List<Excursion> excursionsToDelete = repository.getAssociatedExcursions(vacationID);
                if (!excursionsToDelete.isEmpty()) {
                    // Show confirmation dialog
                    new AlertDialog.Builder(this)
                            .setTitle("Delete Vacation")
                            .setMessage("Deleting this vacation will also delete all its associated excursions. Are you sure you want to proceed?")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // User clicked Confirm, so delete the excursions and vacation
                                    for(Excursion excursion: excursionsToDelete){
                                        repository.delete(excursion);
                                    }
                                    Vacation vacation = new Vacation(vacationID);
                                    repository.delete(vacation);
                                    VacationDetails.this.finish();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // User clicked Cancel, do nothing
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    // If there are no associated excursions, delete the vacation directly
                    Vacation vacation = new Vacation(vacationID);
                    repository.delete(vacation);
                    this.finish();
                }
            }
        }

        else if (item.getItemId() == R.id.vacationrefresh){
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return true;
    }
}