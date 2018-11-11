package se.loan.service.config;

import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;

@Configuration
public class ConverterConfig implements FeignFormatterRegistrar {

    @Override
    public void registerFormatters(FormatterRegistry registry) {
        registry.addFormatter(new RepositoryISODateFormatter());
    }
}