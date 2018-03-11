package org.app.time;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by lilit on 3/4/18.
 */
public class DateTimeRange implements Iterable<LocalDateTime> {

    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    public DateTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        //check that range is valid (null, start < end)
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    @Override
    public Iterator<LocalDateTime> iterator() {
        return dateStream().iterator();
    }

    public Stream<LocalDateTime> dateStream() {
        return Stream.iterate(startDateTime, d -> d.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDateTime, endDateTime) + 1);
    }
    public Stream<LocalDateTime> timeStream() {
        return Stream.iterate(startDateTime, d -> d.plusDays(1))
                .limit(ChronoUnit.HOURS.between(startDateTime, endDateTime) + 1);
    }
    public List<LocalDateTime> toList() { //could also be built from the dateStream() method
        List<LocalDateTime> dates = new ArrayList<>();
        for (LocalDateTime d = startDateTime; !d.isAfter(endDateTime); d = d.plusDays(1)) {
            dates.add(d);
        }
        return dates;
    }
}