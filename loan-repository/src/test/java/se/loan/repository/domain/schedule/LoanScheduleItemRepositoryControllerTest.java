package se.loan.repository.domain.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import se.loan.repository.domain.loanapplication.LoanApplication;
import se.loan.repository.domain.loanapplication.LoanApplicationRepository;
import se.loan.repository.domain.loanapplication.LoanApplicationStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.loan.repository.LoanRepositoryApplication.BASE_PATH;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoanScheduleItemRepositoryControllerTest {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private LoanScheduleItemRepository loanScheduleItemRepository;

    @Autowired
    private MockMvc mvc;

    private ObjectMapper om = new ObjectMapper();

    @Test
    public void save() throws Exception {
        LoanApplication storedLA = loanApplicationRepository.save(loanApplication("save", new Date()));
        LoanScheduleItem item1 = item(1, "1.00", "2.00", new Date());
        LoanScheduleItem item2 = item(2, "2.00", "3.00", new Date());

        mvc.perform(post(BASE_PATH + "/LoanScheduleItemsCustom/" + storedLA.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(Arrays.asList(item1, item2))))
                .andExpect(status().is(200));

        List<LoanScheduleItem> stored = loanScheduleItemRepository.byLoanApplication(storedLA.getId());
        assertEquals(2, stored.size());
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

    private LoanScheduleItem item(int no, String commission, String principal, Date termDate) {
        LoanScheduleItem item = new LoanScheduleItem();
        item.setNo(no);
        item.setCommission(new BigDecimal(commission));
        item.setTermDate(termDate);
        item.setPrincipal(new BigDecimal(principal));
        return item;
    }
}