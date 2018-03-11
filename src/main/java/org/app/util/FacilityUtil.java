package org.app.util;

import org.app.model.Facility;
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
public class FacilityUtil {
    private static final int CUSTOMER_CSV_SIZE=5;
    /**
     * Parses facility.csv to json
     * @param file
     * @return
     * @throws IOException
     */
    public static List<Facility> parseFacilityCSV(MultipartFile file) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            Pattern pattern = Pattern.compile(ResponseWrapperUtil.CSV_SEPARATOR);
            List<Facility> facilities = in.lines().skip(1).map(line -> {
                String[] x = pattern.split(line);
                Facility facility = generateFacility(x);
                return facility;
            }).collect(Collectors.toList());
            return facilities;
        }
    }
    /**
     * Inits Facility object
     * @param params
     * @return
     */
    public static Facility generateFacility(String[] params) {
        Facility facility=null;
        if (params.length ==CUSTOMER_CSV_SIZE) {
            facility = new Facility();
            facility.setAddress(params[0]);
            facility.setEmail(params[1]);
            facility.setWorkingHours(params[2]);
            facility.setEmptySpot(Integer.parseInt(params[3]));
            facility.setPerHour(Integer.parseInt(params[4]));
        }
        return facility;

    }


}
