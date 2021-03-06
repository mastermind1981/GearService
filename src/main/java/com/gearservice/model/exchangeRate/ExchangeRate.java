package com.gearservice.model.exchangeRate;

import org.jsoup.Jsoup;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Class ExchangeRate is model Entity, that store in database and consists exchange rate data.
 * Used for currency conversion.
 *
 * @version 1.1
 * @author Dmitry
 * @since 21.01.2016
 */

@Entity
public class ExchangeRate {

    @Id
    private String addDate;

    private BigDecimal usd;
    private BigDecimal uah;
    private BigDecimal eur;
    private BigDecimal rub;

    public ExchangeRate() {
        this.addDate = LocalDate.now().toString();
        this.usd = new BigDecimal("1");
        this.uah = new BigDecimal("1");
        this.eur = new BigDecimal("1");
        this.rub = new BigDecimal("1");
    }

    /**
     * Method getFromServer handle current ExchangeRate entity filling it with exchange rates of USD, EUR, UAH
     * and return this edited object
     * @param link to website, which publishes exchange rates
     * @param tag which contains exchange rate data
     * @return this ExchangeRate object after editing
     */
    public ExchangeRate getFromServer(String link, String tag) throws IOException {
        String[] elements = parseWebSite(link, tag);

        for(int i = 0; i < elements.length;) {
            switch (elements[i++]) {
                case "USD":
                    this.setUsd(average(new BigDecimal(elements[i++]), new BigDecimal(elements[++i]))); break;
                case "UAH":
                    this.setUah(average(new BigDecimal(elements[i++]), new BigDecimal(elements[++i]))); break;
                case "EUR":
                    this.setEur(average(new BigDecimal(elements[i++]), new BigDecimal(elements[++i]))); break;
            }
            i++;
        }
        return this;
    }

    /**
     * Method parseWebSite parse website with JSoup
     * @param link to site, which publishes exchange rates
     * @param tag which contains exchange rate data
     * @return array of strings, which contains currency rates
     */
    private static String[] parseWebSite(String link, String tag) throws IOException {
            return Jsoup.connect(link)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36")
                    .cookie("realauth", "SvBD85dINu3")
                    .get()
                    .select(tag)
                    .text()
                    .split(" ");
    }

    /**
     * Method average find average of two BigDecimals
     * @param first BigDecimal
     * @param second BigDecimal
     * @return BigDecimal, which contains an average of input values
     */
    private static BigDecimal average(BigDecimal first, BigDecimal second) {
        BigDecimal two = new BigDecimal("2");
        return first.add(second).divide(two, 2, BigDecimal.ROUND_HALF_UP);
    }

    public String getAddDate() {return addDate;}
    public void setAddDate(String addDate) {this.addDate = addDate;}
    public BigDecimal getUsd() {return usd;}
    public void setUsd(BigDecimal usd) {this.usd = usd;}
    public BigDecimal getUah() {return uah;}
    public void setUah(BigDecimal uah) {this.uah = uah;}
    public BigDecimal getEur() {return eur;}
    public void setEur(BigDecimal eur) {this.eur = eur;}
    public BigDecimal getRub() {return rub;}
    public void setRub(BigDecimal rub) {this.rub = rub;}
}
