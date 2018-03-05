package org.app.util;

import org.app.dao.DBUtil;
import org.app.model.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by lilit on 3/4/18.
 */
public class CustomerUtil {


    private static final int CUSTOMER_CSV_SIZE=3;
    /**
     * Parses customer.csv to json
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static List<Customer> parseCustomerCSV(MultipartFile file) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            Pattern pattern = Pattern.compile(WrapperUtil.CSV_SEPARATOR);
            List<Customer> customers = in.lines().skip(1).map(line -> {
                String[] x = pattern.split(line);
                return generateCustomer(x);
            }).collect(Collectors.toList());
            return customers;
        }
    }

    /**
     * Inits Customer object
     * @param params
     * @return
     */
    public static Customer generateCustomer(String[] params) {
        Customer customer=null;
        if (params.length == CUSTOMER_CSV_SIZE) {
             customer = new Customer();
            customer.setId(Integer.parseInt(params[0]));
            customer.setName(params[1]);
            customer.setEmail(params[2]);
        }
        return customer;
    }


}