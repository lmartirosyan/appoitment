package org.app.controller;



import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.app.util.AppointmentUtil;
import org.app.util.WrapperUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * Controller class which serves requests
 * from the client
 * Created by lilit on 3/3/18.
 */
@RestController
public class Controller {

    /**
     * Responds client list of customers
     * and list of facilities
     * @param customers
     * @param facilities
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/list",method = RequestMethod.POST)
    public String customers(@RequestParam("customers") MultipartFile customers,@RequestParam("facilities") MultipartFile facilities) throws IOException {
       return WrapperUtil.generateResponse(customers,facilities);
    }

    /**
     * Handles the distribution of appointments
     * into facilities based on user entry
     * @param facilityId
     * @param customerCount
     * @param emptySpotPerWeek
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/appointments",method = RequestMethod.POST)
    public void distribute(@RequestParam("facility_id") int   facilityId,@RequestParam("count") int customerCount,@RequestParam("empty_spot") int emptySpotPerWeek,HttpServletResponse response ) throws IOException {
        System.out.println(facilityId+ " "+ customerCount+" "+emptySpotPerWeek);

        InputStream is=AppointmentUtil.getAppointmentsIS(facilityId, customerCount, emptySpotPerWeek);
        response.addHeader("Content-disposition", "attachment;filename=list-of-appointments.csv");
        response.setContentType("txt/plain");

        // Copy the stream to the response's output stream.
        IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();

    }


}
