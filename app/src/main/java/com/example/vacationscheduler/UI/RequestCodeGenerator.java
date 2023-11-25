package com.example.vacationscheduler.UI;

public class RequestCodeGenerator
{
    private static int nextRequestCode = 0;

    public static int getNextRequestCode() {
        return nextRequestCode++;
    }
}
