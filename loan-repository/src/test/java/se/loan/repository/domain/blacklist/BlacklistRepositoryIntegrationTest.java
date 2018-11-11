package se.loan.repository.domain.blacklist;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.loan.repository.LoanRepositoryApplication.BASE_PATH;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BlacklistRepositoryIntegrationTest {

    @Autowired
    private MockMvc mvc;
    private ObjectMapper om = new ObjectMapper();

    @Autowired
    private BlacklistRepository repository;

    @Before
    public void setup() {
        repository.deleteAll();
    }

    @Test
    public void persistOrMerge() throws Exception {
        String regNo = "persistOrMerge";

        mvc.perform(put(BASE_PATH + "/blacklists/" + regNo)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().is(201));
        assertEquals(1, repository.findAll().size());

        mvc.perform(put(BASE_PATH + "/blacklists/" + regNo)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().is(204));
        assertEquals(1, repository.findAll().size());
    }

    @Test
    public void exists_true() throws Exception {
        String expectedRegNo = "getByIdRegNo";
        Blacklist b = new Blacklist();
        b.setRegistrationNumber(expectedRegNo);
        repository.save(b);

        mvc.perform(get(BASE_PATH + "/blacklists/search/exists?regNo=" + expectedRegNo))
                .andExpect(status().is(200))
                .andDo(result -> {
                    Boolean exists = om.readValue(result.getResponse().getContentAsString(), Boolean.class);
                    assertTrue(exists);
                });
    }

    @Test
    public void exists_false() throws Exception {
        String expectedRegNo = "getByIdRegNo";
        Blacklist b = new Blacklist();
        b.setRegistrationNumber(expectedRegNo);
        repository.save(b);

        mvc.perform(get(BASE_PATH + "/blacklists/search/exists?regNo=notExisting"))
                .andExpect(status().is(200))
                .andDo(result -> {
                    Boolean exists = om.readValue(result.getResponse().getContentAsString(), Boolean.class);
                    assertFalse(exists);
                });
    }

}