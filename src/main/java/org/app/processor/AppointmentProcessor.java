package org.app.processor;

import org.app.dao.DBUtil;
import org.app.model.Appointment;
import org.app.model.Customer;
import org.app.model.Facility;
import org.app.model.FacilityFreeSchedule;
import org.app.time.TimeUtil;
import org.app.util.Constants;


import java.util.*;
import java.util.stream.Collectors;

/**
 * AppointmentProcessor class, distributes
 * customers on specified facility
 *
 * Created by lilit on 3/4/18.
 */
public class AppointmentProcessor {



    /**
     * 1.Returns appointment of custmoners
     * for specified facility
     *
     * 2.After appointment generation
     * updates facility in db
     *
     * 3.Deletes scheduled customers
     * from customer table
     *
     * @param facilityId
     * @param customerCount
     * @param emptySpotPerWeek
     * @return
     */
    public static List<Appointment> getAppointments(int facilityId, int customerCount, int emptySpotPerWeek){
        List<Appointment> appointments ;
        List<FacilityFreeSchedule> facilityFreeSchedules=DBUtil.load(facilityId);

        Map<Integer, List<FacilityFreeSchedule>> map = facilityFreeSchedules.stream()
                .collect(Collectors.groupingBy(d -> d.getWeekIndex()));
        appointments = getAppointmentsPerWeek(map, customerCount, emptySpotPerWeek, facilityId);
        DBUtil.saveOrUpdate(appointments);
        return  appointments;

    }

    /**
     * Method Returns appointments per week
     * after scheduling  appointments customers deleted from db
     * so next time they will not be scheduled
     *
     * @param facilityFreeSchedulePerWeek
     * @param customerCount
     * @param reqEmptySpotPerWeek
     * @param facilityId
     * @return
     */
    private static List<Appointment>  getAppointmentsPerWeek(Map<Integer, List<FacilityFreeSchedule>> facilityFreeSchedulePerWeek, int customerCount, int reqEmptySpotPerWeek, int facilityId) {
        List<Appointment>  appointments=new ArrayList<>();
        List<Customer> customers = DBUtil.loadCustomers(customerCount);
        Facility facility=DBUtil.get(Facility.class,facilityId);

        facilityFreeSchedulePerWeek.keySet().stream().forEach(p->{
        List<FacilityFreeSchedule> scheduleForSpecificWeek = facilityFreeSchedulePerWeek.get(p);
        int totalCapacityForWeek = scheduleForSpecificWeek.stream().mapToInt(FacilityFreeSchedule::getCapacity).sum();

        if(totalCapacityForWeek>reqEmptySpotPerWeek){
            int limitForCurrentWeek = totalCapacityForWeek - reqEmptySpotPerWeek;
            List<Customer> customersForCurrentWeek=limitForCurrentWeek<customers.size()
                    ?customers.subList(0, limitForCurrentWeek):customers;
            appointments.addAll(distribute(scheduleForSpecificWeek, facility,customersForCurrentWeek ));
            DBUtil.delete(customersForCurrentWeek);
            customers.removeAll(customersForCurrentWeek);

        }
    });
        return appointments;
    }

    /**
     * Distributes customers
     * upon facility empty spots
     * and after appointment generation
     * updatesfacily
     * @param customers
     * @param facility
     * @return
     */
    private static List<Appointment> distribute( List<FacilityFreeSchedule> scheduleForSpecificWeek,Facility facility, List<Customer> customers) {

           return customers.stream().map(customer-> {
                     Appointment appointment=  generateAppointment(customer, facility,scheduleForSpecificWeek.get(0));
                     updateFacilityFreeSchedule(scheduleForSpecificWeek);
                     return appointment;
                   }
            ).collect(Collectors.toList());
    }

    /**
     * Updates capacity of Facilities free schedule
     * if capacity 0 then row should be deleted
     * @param scheduleForSpecificWeek
     */
    private static void updateFacilityFreeSchedule(List<FacilityFreeSchedule> scheduleForSpecificWeek) {
        FacilityFreeSchedule emptySpot=scheduleForSpecificWeek.get(0);
        int capacity = emptySpot.getCapacity();
        if(--capacity>0) {
            emptySpot.setCapacity(capacity);
            scheduleForSpecificWeek.set(0,emptySpot);
            DBUtil.saveOrUpdate(emptySpot);
        }else {
            scheduleForSpecificWeek.remove(emptySpot);
            DBUtil.delete(emptySpot);
        }

    }

    /**
     * generates appointment
     * for customer
     * @param customer
     * @param facility
     * @return
     */
    private static Appointment generateAppointment(Customer customer, Facility facility, FacilityFreeSchedule emptyPoint) {
        Appointment appointment=new Appointment();
        String[] appTimeRange= TimeUtil.getAppointmentTimeRange(emptyPoint.getEmptySpotTimestamp(), Constants.APPOINTMENT_DATE_TIME_FORMAT);
        appointment.setUserName(customer.getName());
        appointment.setUserEmail(customer.getEmail());
        appointment.setFacilityAddress(facility.getAddress());
        appointment.setStartDateTime(appTimeRange[0]);
        appointment.setEndDateTime(appTimeRange[1]);

        return appointment;
    }
}
