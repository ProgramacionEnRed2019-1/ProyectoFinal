package model;

import java.util.Calendar;
import java.util.Date;

public class Membership {

    private Date init;
    private Date end;
    private boolean valid;
    private double value;

    public Membership(Date init, Date end, double value) {
        this.init = init;
        this.end = end;
        this.value = value;
        valid = Calendar.getInstance().getTime().compareTo(end) > 0;
    }

    public Membership() {

    }

    public Date getInit() {
        return init;
    }

    public void setInit(Date init) {
        this.init = init;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
        valid = Calendar.getInstance().getTime().compareTo(end) > 0;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
