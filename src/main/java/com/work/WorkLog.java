package com.work;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class WorkLog {

    private static final int WORKDAYS_IN_WEEK = 5;

    public static void main(String[] args) throws Exception {

        int year;
        int weekNo;

        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        weekNo = cal.get(Calendar.WEEK_OF_YEAR);

        if (args.length == 1) {
            weekNo = new Integer(args[0]);
        } else if(args.length > 1) {
            year = Integer.parseInt(args[0]);
            weekNo = Integer.parseInt(args[1]);
        }
        WorkWeek workWeek = downloadFile(year, weekNo);
        parseXml(workWeek);

    }

    private static WorkWeek downloadFile(int year, int weekNo) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        WorkWeek workWeek = new WorkWeek(weekNo);
        for (int i = 0; i < WORKDAYS_IN_WEEK; i++) {
            cal.set(Calendar.DAY_OF_WEEK, i + 2);
            int thisYear = cal.get(Calendar.YEAR);
            int thisDate = cal.get(Calendar.DAY_OF_MONTH);
            int thisMonth = cal.get(Calendar.MONTH);
            WorkDay workDay = new WorkDay(thisYear, thisMonth + 1, thisDate, i + 1);
            workWeek.getWorkDayList().add(workDay);
            File file = new File(getFileName(workDay));
            if (!file.exists() || file.isDirectory()) {
                downloadFile(thisYear, thisMonth, thisDate);
            }
        }
        Runtime.getRuntime().exec("killall firefox");
        return workWeek;
    }

    private static void downloadFile(int year, int month, int day) throws IOException, InterruptedException {
        System.out.println("Downloading " + year + "-" + (month + 1) + "-" + day);
        String url = "https://www.google.com/maps/timeline/kml?authuser=0&pb=!1m8" +
                "!1m3!1i" + year + "!2i" + month + "!3i" + day +
                "!2m3!1i" + year + "!2i" + month + "!3i" + day;
        System.out.println(url);
        Runtime.getRuntime().exec("firefox -new-window " + url);
        Thread.sleep(2000);
    }

    private static void parseXml(WorkWeek workWeek) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        for (WorkDay workDay : workWeek.getWorkDayList()) {
            Document dom = dbf.newDocumentBuilder().parse(getFileName(workDay));
            Element docEle = dom.getDocumentElement();
            NodeList placemark = docEle.getElementsByTagName("Placemark");
            double duration = 0;
            System.out.println(workDay.getDateStr());
            for (int i = 0; i < placemark.getLength(); i++) {
                Element element = (Element)placemark.item(i);
                if(element.getElementsByTagName("name").item(0).getTextContent().contains("Johan Willins gata 6")) {

                    Element timeSpan = (Element)element.getElementsByTagName("TimeSpan").item(0);
                    String begin = timeSpan.getElementsByTagName("begin").item(0).getTextContent();
                    String end = timeSpan.getElementsByTagName("end").item(0).getTextContent();

                    Instant beginInstant = Instant.parse(begin);
                    Instant endInstant = Instant.parse(end);

                    duration += beginInstant.until(endInstant, ChronoUnit.MINUTES);

                }
            }
            System.out.println(Math.floor((duration / 60) * 10) / 10);
        }
    }

    private static String getFileName(WorkDay workDay) {
        return "/home/cecilia/Downloads/Firefox/history-" + workDay.getDateStr() + ".kml";
    }
}
