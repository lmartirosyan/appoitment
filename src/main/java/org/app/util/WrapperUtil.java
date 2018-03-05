package org.app.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.app.dao.DBUtil;
import org.app.model.Customer;
import org.app.model.Facility;
import org.app.model.ResponseWrapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by lilit on 3/4/18.
 */
public class WrapperUtil {
    public static final String CSV_SEPARATOR=";";

    /**
     * Returns to client  customers
     * and  facilities list
     * @param customers
     * @param facilities
     * @return
     * @throws IOException
     */
    public static String generateResponse(MultipartFile customers, MultipartFile facilities) throws IOException {
        List<Customer> customersObj = CustomerUtil.parseCustomerCSV(customers);
        List<Facility> facilityObj = FacilityUtil.parseFacilityCSV(facilities);
        DBUtil.saveOrUpdate(customersObj);
        DBUtil.saveOrUpdate(facilityObj);
        ResponseWrapper responseWrapper = new ResponseWrapper.Builder()
                .customers(customersObj)
                .facilities(facilityObj)
                .build();
        return objectToJson(responseWrapper);
    }

    /**
     * Parses java object to
     * json String
     *
     * @param obj
     * @param <T>
     * @return
     * @throws IOException
     */
    public static  <T> String objectToJson(T obj) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return  mapper.writeValueAsString(obj);
    }




}
