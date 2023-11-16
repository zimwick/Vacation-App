package com.example.vacationscheduler.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.vacationscheduler.dao.ExcursionDAO;
import com.example.vacationscheduler.dao.VacationDAO;
import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.entities.Vacation;

@Database(entities = {Vacation.class, Excursion.class}, version = 2, exportSchema = false)
public abstract class TripDatabaseBuilder extends RoomDatabase {
    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();
    private static volatile TripDatabaseBuilder INSTANCE;

    static TripDatabaseBuilder getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (TripDatabaseBuilder.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),TripDatabaseBuilder.class, "MyTripDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
