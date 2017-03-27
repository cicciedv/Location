package com.work;

import java.util.ArrayList;
import java.util.List;

public class WorkWeek {
    private int weekNo;
    private List<WorkDay> workDayList = new ArrayList<WorkDay>();

    public WorkWeek(int weekNo) {
        this.weekNo = weekNo;
    }

    public int getWeekNo() {
        return weekNo;
    }

    public List<WorkDay> getWorkDayList() {
        return workDayList;
    }
}
