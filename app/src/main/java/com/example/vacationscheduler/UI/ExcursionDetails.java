package com.example.vacationscheduler.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.vacationscheduler.R;
import com.example.vacationscheduler.database.Repository;
import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.entities.Vacation;

import java.util.Calendar;
import java.util.Locale;

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
        //editNote = findViewById(R.id.note);
        //editDate1 = findViewById(R.id.date);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editDate);
            }
        });

    }

    private void showDatePickerDialog(final EditText dateEditText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ExcursionDetails.this,
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
        getMenuInflater().inflate(R.menu.menu_excursiondetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.excursionsave){
            Excursion excursion;
            if (excursionID == -1){
                if(repository.getAllExcursions().size()== 0) excursionID = 1;
                else excursionID = repository.getAllExcursions().get(repository.getAllExcursions().size() -1).getExcursionID() + 1;
                excursion = new Excursion(excursionID, editTitle.getText().toString(), editDate.getText().toString(), vacationID);
                repository.insert(excursion);
            }
            else{
                excursion = new Excursion(excursionID, editTitle.getText().toString(), editDate.getText().toString(), vacationID);
                repository.update(excursion);
            }
            Intent data = new Intent();
            setResult(RESULT_OK, data);
            finish();

        }
        else if(item.getItemId() == R.id.excursiondelete){
            if(excursionID != -1){
                Excursion excursion = new Excursion(excursionID);
                repository.delete(excursion);
                this.finish();
            }
        }
        else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        /*else if (item.getItemId() == android.R.id.share){
            Intent sentIntent = new Intent();
            sentIntent.setAction(Intent.ACTION_SEND);
            sentIntent.putExtra(Intent.EXTRA_TEXT, editNote.getText().toString() + "EXTRA_TEXT");
            sentIntent.putExtra(Intent.EXTRA_TITLE, editNote.getText().toString() + "EXTRA_TITLE");
            sentIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sentIntent, null);
            startActivity(shareIntent);
            return true;
        }
        else if (item.getItemId() == android.R.id.notify){

        }*/
        return true;
    }

}