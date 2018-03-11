package org.app.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by lilit on 3/9/18.
 */
@Entity
@NamedQueries({ @NamedQuery(name = "@HQL_GET_ALL_FACILITY_FREE_SCHEDULE",
        query = "from  FacilityFreeSchedule as f WHERE f.facilityId=:id") })

@Table(name = "facility_free_schedule", schema = "public", catalog = "postgres")
public class FacilityFreeSchedule {
    private int id;
    private int facilityId;
    private int capacity;
    private long emptySpotTimestamp;
    private int weekIndex;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "facility_id", nullable = false)
    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    @Basic
    @Column(name = "capacity", nullable = false)
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Basic
    @Column(name = "empty_spot_timestamp", nullable = false)
    public long getEmptySpotTimestamp() {
        return emptySpotTimestamp;
    }

    public void setEmptySpotTimestamp(long emptySpotTimestamp) {
        this.emptySpotTimestamp = emptySpotTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FacilityFreeSchedule that = (FacilityFreeSchedule) o;

        if (id != that.id) return false;
        if (facilityId != that.facilityId) return false;
        if (capacity != that.capacity) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + facilityId;
        result = 31 * result + capacity;
        return result;
    }

    @Basic
    @Column(name = "week_index", nullable = false)
    public int getWeekIndex() {
        return weekIndex;
    }

    public void setWeekIndex(int weekIndex) {
        this.weekIndex = weekIndex;
    }
}
