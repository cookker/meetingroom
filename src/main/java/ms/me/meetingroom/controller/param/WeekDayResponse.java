package ms.me.meetingroom.controller.param;

public class WeekDayResponse {
    private String previousWeekDay;
    private String today;
    private String nextWeekDay;
    private String monday;
    private String friday;

    public WeekDayResponse(String previousWeekDay, String today, String nextWeekDay, String monday, String friday) {
        this.previousWeekDay = previousWeekDay;
        this.today = today;
        this.nextWeekDay = nextWeekDay;
        this.monday = monday;
        this.friday = friday;
    }

    public String getPreviousWeekDay() {
        return previousWeekDay;
    }

    public void setPreviousWeekDay(String previousWeekDay) {
        this.previousWeekDay = previousWeekDay;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getNextWeekDay() {
        return nextWeekDay;
    }

    public void setNextWeekDay(String nextWeekDay) {
        this.nextWeekDay = nextWeekDay;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    @Override
    public String toString() {
        return "WeekDayResponse{" +
                "previousWeekDay='" + previousWeekDay + '\'' +
                ", today='" + today + '\'' +
                ", nextWeekDay='" + nextWeekDay + '\'' +
                ", monday='" + monday + '\'' +
                ", friday='" + friday + '\'' +
                '}';
    }
}
