package com.example.vacationscheduler.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "excursions")
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int excursionID;
    private String excursionTitle;
    private String excursionDate;
    private int vacationID;

    public Excursion(int excursionID, String excursionTitle, String excursionDate, int vacationID) {
        this.excursionID = excursionID;
        this.excursionTitle = excursionTitle;
        this.excursionDate = excursionDate;
        this.vacationID = vacationID;
    }

    @Ignore
    public Excursion(int excursionID) {
        this.excursionID = excursionID;
    }

    public int getExcursionID() {
        return excursionID;
    }

    public void setExcursionID(int excursionID) {
        this.excursionID = excursionID;
    }

    public String getExcursionTitle() {
        return excursionTitle;
    }

    public void setExcursionTitle(String excursionTitle) {
        this.excursionTitle = excursionTitle;
    }

    public String getExcursionDate() {
        return excursionDate;
    }

    public void setExcursionDate(String excursionDate) {
        this.excursionDate = excursionDate;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }
}
