package org.app.controller;


import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;

import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ControllerTest {

    private String entryURL="/api/schedule/entry";

    private String distributeURL="/api/schedule/distribute";

    private static  final String customersCSVPath="csv/list-of-customers.csv";

    private static  final String facilitiesCSVPath="csv/list-of-facilities.csv";

    private static  final JSONArray expectedEntry=new JSONArray()
            .put("customers")
            .put("facilities");
    private static final  String expectedCSVHeader="\"id\";\"user_name\";\"user_email\";\"facility_address\";\"start_time\";\"end_time\"";

   // facilityId field should be changes and be specified based on your entry response.
    private String facilityId="38";

    private String customerCount="2";

    private String emptySpot="1";

    private JSONObject data;

    private MockMvc mockMvc;

    private  Controller controller;

    /**
     *
     */
    @Before
    public void setup(){
        controller=new Controller();
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /**
     * Test method which checks if entry response is ok and
     * response contains expected json objects
     *
     * @throws Exception
     */

    @Test
    public void getEntry_test() throws Exception {
        FileInputStream customersFile = new FileInputStream(customersCSVPath);
        FileInputStream facilitiesFile = new FileInputStream(facilitiesCSVPath);

        MockMultipartFile customers = new MockMultipartFile("customers", "list-of-customers.csv", MediaType.TEXT_PLAIN_VALUE, customersFile);
        MockMultipartFile facilities = new MockMultipartFile("facilities", "list-of-facilities.csv", MediaType.TEXT_PLAIN_VALUE, facilitiesFile);
        String result = mockMvc.perform(fileUpload(entryURL)
                .file(customers)
                .file(facilities))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        data = new JSONObject(result);
        JSONAssert.assertEquals(expectedEntry, data.names(),false );
    }

    /**
     * Test method which checks if distribution response is ok and
     * response contains required headers
     *
     * IMPORTANT!!!!!
     * facilityId field should be specified based on entry response.
     *
     * @throws Exception
     */
    @Test
    public void distribute_test() throws Exception {

         String result =  mockMvc.perform(post(distributeURL)
                        .param("facility_id",facilityId)
                        .param("count",customerCount)
                        .param("empty_spot",emptySpot))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        Assert.hasText(expectedCSVHeader,result);



    }
}
