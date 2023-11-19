package com.example.vacationscheduler.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.vacationscheduler.R;
import com.example.vacationscheduler.database.Repository;
import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.entities.Vacation;

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
                this.finish();
            }
            else{
                excursion = new Excursion(excursionID, editTitle.getText().toString(), editDate.getText().toString(), vacationID);
                repository.update(excursion);
                this.finish();
            }
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
        return true;
    }
}