package org.app.util;

import org.app.model.Facility;
import org.app.model.FacilityFreeSchedule;
import org.app.time.TimeUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by lilit on 3/4/18.
 */
public class FacilityFreeScheduleUtil {


    /**
     * Returns of all  Facilities Free Schedules
     *
     * @param facilities
     * @return
     */
    public static  List<FacilityFreeSchedule> generateFacilitiesFreeSchedules(List<Facility> facilities) {
        List<FacilityFreeSchedule> facilitiesFreeSchedules=new ArrayList<>();
        facilities.stream().forEach(facility-> facilitiesFreeSchedules.addAll(generateFacilityFreeSchedules(facility)));
        return facilitiesFreeSchedules;
    }

    /**
     * Returns  FacilityFreeSchedule list
     * which contains facility_id time of each empty_spot and
     * capacity corresponding to it.
     * Empty spot should be in working hours range
     *
     * @param facility
     * @return
     */
    private static List<FacilityFreeSchedule> generateFacilityFreeSchedules(Facility facility) {
        List<FacilityFreeSchedule> facilityFreeSchedules=new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        Map<Integer,List<LocalDateTime>> workingHoursForMonth=workingHoursForFacilityPerWeek(facility, now);
             workingHoursForMonth.keySet().stream().forEach(weekIndex -> {
                List<LocalDateTime> workingHoursPerWeek = workingHoursForMonth.get(weekIndex);
                facilityFreeSchedules.addAll(workingHoursPerWeek.stream()
                            .map(dateTime -> generateFacilityFreeSchedule(facility, dateTime, weekIndex))
                            .collect(Collectors.toList()));
            });
             return  facilityFreeSchedules;
    }

    /**
     * Returns working hours per week for specified facility
     *
     * @param facility
     * @param now
;     * @return
     */
    private static Map<Integer,List<LocalDateTime>> workingHoursForFacilityPerWeek(Facility facility, LocalDateTime now) {
        Map<Integer,List<LocalDateTime>> workingHoursForWeek=new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.WORKING_HOURS_FORMAT);
        LocalTime startTime = LocalTime.parse(facility.getWorkingHours().split(Constants.HOUR_SEPARATOR)[0], formatter);
        LocalTime endTime = LocalTime.parse(facility.getWorkingHours().split(Constants.HOUR_SEPARATOR)[1], formatter);
        int emptySpot= facility.getEmptySpot();
        IntStream.range(0,Constants.WEEK_COUNT).forEach(weekIndex -> {
                now.plusWeeks(1);
                workingHoursForWeek.put(weekIndex, TimeUtil.getWorkingHoursForWeek(startTime, endTime, now).subList(0,emptySpot));

        });
        return workingHoursForWeek;

    }

    /**
     * Generates  FacilityFreeSchedule object
     *
     * @param facility
     * @param empty_spot_time
     * @return
     */
    private static FacilityFreeSchedule generateFacilityFreeSchedule(Facility facility, LocalDateTime empty_spot_time, int weekIndex){
        long emptySpotTimestamp = empty_spot_time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        FacilityFreeSchedule facilityFreeSchedule=new FacilityFreeSchedule();
        facilityFreeSchedule.setFacilityId(facility.getId());
        facilityFreeSchedule.setCapacity(facility.getPerHour());
        facilityFreeSchedule.setEmptySpotTimestamp(emptySpotTimestamp);
        facilityFreeSchedule.setWeekIndex(weekIndex);
        return facilityFreeSchedule;
    }


}
