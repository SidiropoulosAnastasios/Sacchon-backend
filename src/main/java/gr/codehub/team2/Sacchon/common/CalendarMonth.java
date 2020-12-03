package gr.codehub.team2.Sacchon.common;

public enum CalendarMonth {
    /**
     * The calendar month of the measurements in days duration.
     */
    CALENDAR_MONTH(30);

    private long duration;

    private CalendarMonth(long duration) {
        this.duration = duration;
    }

    public long getDuration() { return duration; }

}
