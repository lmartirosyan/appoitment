package org.app.processor;

import com.opencsv.CSVWriter;
import org.app.model.Appointment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lilit on 3/4/18.
 */
public class EntryProcessor {
    /**
     * Parses Appointment object list to csv
     *
     * @param appointments
     * @throws IOException
     */
    public static StringWriter parseToCsv(List<Appointment> appointments) throws IOException {
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer,';');
        List<String[]> data  = toStringArray(appointments);
        csvWriter.writeAll(data);
        csvWriter.close();
        return writer;

    }

    /**
     *  Generates content of csv file
     *
     * @param appointments
     * @return
     */
    private static  List<String[]> toStringArray(List<Appointment> appointments) {
        List<String[]> records = new ArrayList<>();
        //add header record
        records.add(new String[]{"id","user_name","user_email","facility_address", "start_time", "end_time"});
        Iterator<Appointment> it = appointments.iterator();
        while(it.hasNext()){
            Appointment appointment = it.next();
            records.add(new String[]{String.valueOf(appointment.getId()),appointment.getUserName(),
                    appointment.getUserEmail(), appointment.getFacilityAddress(),
                    appointment.getStartDateTime(),appointment.getEndDateTime()});
        }
        return records;
    }

    /**
     * Returns scheduled appointment input dateStream
     *
     * @param facilityId
     * @param customerCount
     * @param emptySpotPerWeek
     * @return
     */
    public static InputStream getAppointmentsIS(int facilityId, int customerCount, int emptySpotPerWeek) {
        InputStream inputStream=null;
       try{
            List<Appointment> appiontments = DistributionProcessor.getAppointments(facilityId, customerCount, emptySpotPerWeek);
            StringWriter writer = parseToCsv(appiontments);
           inputStream = new ByteArrayInputStream(writer.toString().getBytes(StandardCharsets.UTF_8));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;

    }
}
