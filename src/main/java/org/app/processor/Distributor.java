package org.app.processor;

import org.app.dao.DBUtil;
import org.app.model.Appointment;
import org.app.model.Customer;
import org.app.model.Facility;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Distributor class, distributes
 * customers on specified facility
 *
 * Created by lilit on 3/4/18.
 */
public class Distributor {

    private static final int DAY_PER_MONTH=30;

    private static final int WEEKS_PER_MONTH=4;

    private static final int WORKING_DAYS_PER_WEEK=4;

    private static final String HOUR_SEPARATOR="-";

    private static final String WORKING_HOURS_FORMAT="hh:mm a";

    private static final int APPOINTMENT_INTERVAL=1;//hour

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
        List<Appointment> result = new ArrayList<>();
        Facility facility=DBUtil.get(Facility.class,facilityId);
          boolean isAvailable= isAvailable(facility,emptySpotPerWeek, customerCount);
          if (isAvailable) {
              List<Customer> customers = DBUtil.loadCustomers(customerCount);
              result=distribute(customers,facility);
            DBUtil.saveOrUpdate(facility); //updating facility info after appointment creation
//            DBUtil.saveOrUpdate(result);// saving appointments to db //todo:uncomment
            DBUtil.delete(customers);// appointed customers should be removed from db
          }
        return  result;

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
    private static List<Appointment> distribute(List<Customer> customers, Facility facility) {

           return customers.stream().map(customer-> {
                     Appointment appointment=  generateAppointment(customer, facility);
                     updateFacility(facility, appointment.getStartDateTime());
                     return appointment;
                   }
            ).collect(Collectors.toList());
    }

    /**
     * generates appointment
     * for customer
     * @param customer
     * @param facility
     * @return
     */
    private static Appointment generateAppointment(Customer customer, Facility facility) {
        Appointment appointment=new Appointment();
        String[] appTimeRange=getAppointmentTimeRange(facility);
        appointment.setUserName(customer.getName());
        appointment.setUserEmail(customer.getEmail());
        appointment.setFacilityAddress(facility.getAddress());
        appointment.setStartDateTime(appTimeRange[0]);
        appointment.setEndDateTime(appTimeRange[1]);

        return appointment;
    }

    /**
     * Returns appointment start & end time
     * for customer
     *
     * @param facility
     * @return
     */
    private static String[] getAppointmentTimeRange(Facility facility) {
        String[] timeRange=new String[2];
        DateFormat dateFormatter = new SimpleDateFormat(WORKING_HOURS_FORMAT);
        Date date=null;
        try {
            date = dateFormatter.parse(facility.getWorkingHours().split(HOUR_SEPARATOR)[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if (facility.getPerHour()>0) {
                timeRange[0]= dateFormatter.format(cal.getTime()).toString();
            }else{
                cal.add(Calendar.HOUR_OF_DAY, APPOINTMENT_INTERVAL);
                timeRange[0]=dateFormatter.format(cal.getTime()).toString();
            }
            cal.add(Calendar.HOUR_OF_DAY, APPOINTMENT_INTERVAL);
            timeRange[1] =dateFormatter.format(cal.getTime()).toString();
            System.out.println(timeRange);

        return timeRange;
    }

    /**
     * After each new appointment
     * should update capacity and empty spots
     * of Facility object
     *
     * @param facility
     * @param startTime
     */
    private static Facility updateFacility(Facility facility, String startTime) {
        int capacity = facility.getPerHour();
        int emptySpot =facility.getEmptySpot();
        String endTime=facility.getWorkingHours().split(HOUR_SEPARATOR)[1];
        facility.setWorkingHours(startTime +HOUR_SEPARATOR+endTime);
        facility.setPerHour(--capacity);
        facility.setEmptySpot(--emptySpot);// todo:recheck logic multiply empty spots with capacity
        return facility;
    }

    /**
     * Method makes sure that
     * for all specified customers
     * facility has empty spots
     *
     * @param facility
     * @param emptySpotPerWeek
     * @param customerCount
     * @return
     */
    private static boolean isAvailable(Facility facility, int emptySpotPerWeek, int customerCount) {
        int availableSpotsPerMonth = facility.getEmptySpot() * DAY_PER_MONTH;
        int shouldBeAvailablePerWeek = emptySpotPerWeek * WEEKS_PER_MONTH;
        int availableCount = (availableSpotsPerMonth - shouldBeAvailablePerWeek) * facility.getPerHour();
            return availableCount>= customerCount;
    }

}
