package entity;

import java.util.Calendar;
import java.util.Date;

public class Membership {

    private Date init;
    private Date end;
    private boolean valid;
    private double cost;

    public Membership(Date init, Date end, double cost) {
        this.init = init;
        this.end = end;
        this.cost = cost;
        valid = Calendar.getInstance().getTime().compareTo(end) > 0;
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
