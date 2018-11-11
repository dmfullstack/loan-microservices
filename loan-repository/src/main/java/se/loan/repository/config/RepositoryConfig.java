package se.loan.repository.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import se.loan.repository.domain.blacklist.Blacklist;
import se.loan.repository.domain.loanapplication.LoanApplication;
import se.loan.repository.domain.schedule.LoanScheduleItem;

@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(LoanApplication.class);
        config.exposeIdsFor(Blacklist.class);
        config.exposeIdsFor(LoanScheduleItem.class);
    }
}