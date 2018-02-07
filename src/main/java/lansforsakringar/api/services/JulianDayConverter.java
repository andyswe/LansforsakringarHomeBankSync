package lansforsakringar.api.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static java.time.temporal.ChronoUnit.DAYS;

public class JulianDayConverter {
    private static final Long base = 730120L;
    private static final ZoneId z = ZoneId.systemDefault();
    private static final ZonedDateTime start = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, z).truncatedTo(ChronoUnit.DAYS);

    public Long convert(Date date) {
        Instant dateInstant = date.toInstant();
        Temporal stop = ZonedDateTime.ofInstant(dateInstant, z).truncatedTo(ChronoUnit.DAYS);
        long between = ChronoUnit.DAYS.between(start, stop);
        return between + base;

    }
}
