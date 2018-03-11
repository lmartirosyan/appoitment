package org.app.time;



import org.app.util.Constants;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by lilit on 3/4/18.
 */
public class TimeUtil {


    /**
     * Returns total working hours per week
     *
     * @param startTime
     * @param endTime
     * @param now
     * @return
     */
    public static List<LocalDateTime> getWorkingHoursForWeek(LocalTime startTime, LocalTime endTime, LocalDateTime now) {
        List<LocalDateTime> workingHoursPerWeek=new ArrayList<>();
        LocalDateTime monday=getDayOfNextWeek(now, DayOfWeek.MONDAY);
        LocalDateTime friday=getDayOfNextWeek(now, DayOfWeek.FRIDAY);
        Stream<LocalDateTime> weekDays = new DateTimeRange(monday, friday).dateStream();
        weekDays.forEach(date->{
            List<LocalDateTime> hoursOfDay = getHoursForDay(startTime, endTime, date);
            workingHoursPerWeek.addAll(hoursOfDay);
        });
        return  workingHoursPerWeek;
    }
    /**
     * Returns total working hours per day
     *
     * @param startTime
     * @param endTime
     * @param date
     * @return
     */

    public static List<LocalDateTime> getHoursForDay(LocalTime startTime, LocalTime endTime, LocalDateTime date) {
        LocalDateTime startDateTime=LocalDateTime.of(date.toLocalDate(),startTime);
        LocalDateTime endDateTime=LocalDateTime.of(date.toLocalDate(),endTime);
        Stream<LocalDateTime> workingHoursPerDay = new DateTimeRange(startDateTime, endDateTime).timeStream();
        return workingHoursPerDay.collect(Collectors.toList());//todo:review filter

    }
    /**
     * Returns day of week
     * if dayOfWeek is Monday we shift to the next week
     *
     * @param date
     * @param dayOfWeek
     * @return
     */
    private static LocalDateTime getDayOfNextWeek(LocalDateTime date, DayOfWeek dayOfWeek) {
        return   date.plusWeeks(1).with(TemporalAdjusters.next(dayOfWeek));
    }

    /**
     * Returns appointment start and end time
     *
     * @param timestamp
     * @param format
     * @return
     */
    public static String[] getAppointmentTimeRange(long timestamp, String format) {
        String[] result = new String[2];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        Instant instant = Instant.ofEpochMilli(timestamp);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
         result[0] = dateTime.format(formatter);
        dateTime.plusHours(Constants.APPOINTMENT_INTERVAL);
        result[1] = dateTime.format(formatter);
        return result;

    }
}

