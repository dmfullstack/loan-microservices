package se.loan.service.config;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@Component
public class RepositoryISODateFormatter implements Formatter<Date> { //TODO unit test

    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        OffsetDateTime odt = OffsetDateTime.parse(text);
        return Date.from(odt.toInstant());
    }

    @Override
    public String print(Date date, Locale locale) {
        OffsetDateTime odt = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toOffsetDateTime();
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(odt);
    }
}