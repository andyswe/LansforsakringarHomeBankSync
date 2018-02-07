package lansforsakringar.api.services.internal;

import lansforsakringar.api.services.JulianDayConverter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class JulianDayConverterTest {

    @Test
    public void test_Valid_conversion() {
        // 730120 2000-01-01
        // 2018-02-06 736731
        // 2018-05-05 736819
        Calendar instance = Calendar.getInstance();
        instance.setTimeZone(TimeZone.getDefault());
        instance.set(2018,4,5);
        Date time = instance.getTime();
System.out.println(time);
        JulianDayConverter converter = new JulianDayConverter();
        long convert = converter.convert(time);
        Assert.assertEquals(736819L,convert);
    }
}
