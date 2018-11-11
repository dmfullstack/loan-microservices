package se.loan.repository.domain.loanapplication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.loan.repository.LoanRepositoryApplication.BASE_PATH;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoanApplicationRepositoryIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private LoanApplicationRepository repository;

    private ObjectMapper om = new ObjectMapper();

    @Before
    public void setup() {
        repository.deleteAll();
    }

    @Test
    public void persist() throws Exception {
        LoanApplication toPersist = new LoanApplication();
        toPersist.setCompanyType(CompanyType.CORPORATION);
        toPersist.setAmount(new BigDecimal("123.45"));
        toPersist.setCompanyName("asdf");
        toPersist.setCompanyType(CompanyType.CORPORATION);
        toPersist.setRegistrationNumber("reg no");
        toPersist.setEmail("email@asdf.com");
        toPersist.setPhoneNo("+371 123 234");
        toPersist.setYearlyTurnover(new BigDecimal("1000.23"));
        toPersist.setTerm(5);
        toPersist.setCreatedDate(new Date());
        toPersist.setStatus(LoanApplicationStatus.NEW);

        mvc.perform(post(BASE_PATH + "/loanApplications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(toPersist)))
                .andExpect(status().is(201));

        List<LoanApplication> persistedLoans = repository.findAll();
        assertEquals(1, persistedLoans.size());
        LoanApplication persisted = persistedLoans.get(0);
        assertEquals(toPersist.getAmount(), persisted.getAmount());
        assertEquals(toPersist.getCompanyName(), persisted.getCompanyName());
        assertNotNull(persisted.getId());
    }

    @Test
    public void count() throws Exception {
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.SECOND, -30);
        Date sec30Ago = cal.getTime();
        cal.add(Calendar.SECOND, -90);
        Date min2Ago = cal.getTime();
        repository.save(loanApplication("missed", now));
        repository.save(loanApplication("count", now));
        repository.save(loanApplication("count", sec30Ago));
        repository.save(loanApplication("count", min2Ago));

        assertEquals(1L, repository.count(sec30Ago, "count").longValue());
        assertEquals(2L, repository.count(min2Ago, "count").longValue());

        mvc.perform(get(String.format("%s/loanApplications/search/count?afterDate=%s&regNo=%s", BASE_PATH, toISOWithOffset(min2Ago), "count")))
                .andExpect(status().is(200))
                .andDo(result -> assertEquals("2", result.getResponse().getContentAsString()));
    }

    private String toISOWithOffset(Date date) {
        OffsetDateTime odt = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toOffsetDateTime();
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(odt);
    }

    private LoanApplication loanApplication(String regNo, Date createdDate) {
        LoanApplication la = new LoanApplication();
        la.setAmount(BigDecimal.ONE);
        la.setRegistrationNumber(regNo);
        la.setEmail("A@a.com");
        la.setPhoneNo("123");
        la.setTerm(2);
        la.setCreatedDate(createdDate);
        la.setStatus(LoanApplicationStatus.NEW);

        return la;
    }

}
