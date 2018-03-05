package org.app.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by lilit on 3/4/18.
 */
@Entity
public class Facility {
    private int id;
    private String email;
    private String address;
    private String workingHours;
    private int emptySpot;
    private int perHour;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "email", nullable = false, length = 200)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "address", nullable = false, length = 200)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "working_hours", nullable = false, length = 200)
    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    @Basic
    @Column(name = "empty_spot", nullable = false)
    public int getEmptySpot() {
        return emptySpot;
    }

    public void setEmptySpot(int emptySpot) {
        this.emptySpot = emptySpot;
    }

    @Basic
    @Column(name = "per_hour", nullable = false)
    public int getPerHour() {
        return perHour;
    }

    public void setPerHour(int perHour) {
        this.perHour = perHour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Facility facility = (Facility) o;

        if (id != facility.id) return false;
        if (emptySpot != facility.emptySpot) return false;
        if (perHour != facility.perHour) return false;
        if (email != null ? !email.equals(facility.email) : facility.email != null) return false;
        if (address != null ? !address.equals(facility.address) : facility.address != null) return false;
        if (workingHours != null ? !workingHours.equals(facility.workingHours) : facility.workingHours != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (workingHours != null ? workingHours.hashCode() : 0);
        result = 31 * result + emptySpot;
        result = 31 * result + perHour;
        return result;
    }
}
