package com.example.vacationscheduler.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vacationscheduler.R;
import com.example.vacationscheduler.database.Repository;
import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    String title;
    String date;
    String vacationStart;
    String vacationEnd;
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

    private Date parseDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isDateInRange(Date date, Date startDate, Date endDate) {
        return date != null && !date.before(startDate) && !date.after(endDate);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.excursionsave){
            Vacation vacation = repository.getVacationByID(vacationID);

            Date excursionDate = parseDate(editDate.getText().toString());
            Date vacationStartDate = parseDate(vacation.getVacationStartDate());
            Date vacationEndDate = parseDate(vacation.getVacationEndDate());

            if (excursionDate != null && vacationStartDate != null && vacationEndDate != null) {
                // Check if the excursion date is within the vacation range
                if (!isDateInRange(excursionDate, vacationStartDate, vacationEndDate)) {
                    Toast.makeText(this, "Excursion date must be within the vacation range", Toast.LENGTH_LONG).show();
                    return true;
                }
            }

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

            Intent mainIntent = new Intent(ExcursionDetails.this, VacationList.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the activity stack
            startActivity(mainIntent);
            finish();

        }
        else if(item.getItemId() == R.id.excursiondelete){
            if(excursionID != -1){
                Excursion excursion = new Excursion(excursionID);
                repository.delete(excursion);
                this.finish();
            }
            Intent mainIntent = new Intent(ExcursionDetails.this, VacationList.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the activity stack
            startActivity(mainIntent);
            finish();
        }
        else if (item.getItemId() == R.id.excursionnotify) {
            String dateFromScreen = editDate.getText().toString();
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try{
                myDate = sdf.parse(dateFromScreen);
            }catch (ParseException e){

            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
            int requestCode = RequestCodeGenerator.getNextRequestCode();
            intent.putExtra("key", title + " starts today!");
            PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            return true;
        }
        else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return true;
    }

}