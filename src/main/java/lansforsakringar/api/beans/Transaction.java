package lansforsakringar.api.beans;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.util.Date;

import lansforsakringar.api.services.JulianDayConverter;
import org.apache.commons.codec.digest.DigestUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.cglib.core.Local;

import static java.time.temporal.ChronoUnit.DAYS;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {
    private Double ammount;
    private String text;
    private Date transactiondate;
    private Long transactiondataAsLong;
    private static Temporal zeroDate = LocalDate.parse("0000-01-01");

    public Transaction() {
        // ...
    }

    public Double getAmmount() {
        return ammount;
    }

    public void setAmmount(Double ammount) {
        this.ammount = ammount;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTransactiondate() {
        return transactiondate;
    }

    public Long gettransactiondataAsLong() {
        return transactiondataAsLong;
    }

    public Long getHomeBankDateFormat() {
        return new JulianDayConverter().convert(this.transactiondate);
    }

    public void setTransactiondate(Long transactiondate) {
        // It's not cool to have business logic here, not sure how to fix this differently.
        this.transactiondate = new Date(transactiondate);
        this.transactiondataAsLong = transactiondate;
    }

    @Override
    public String toString() {
        return
                transactiondate + ", " + text + ", " + ammount;


    }

    public String sha1hash() {
        return DigestUtils.sha1Hex(
                transactiondataAsLong + ", " + text + ", " + ammount);


    }
}
