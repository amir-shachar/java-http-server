public class UserSiteVisitDetails
{
    private long firstVisit;
    private int visitsInWindow;
    public static final int VISIT_LIMIT = 5;
    public static final long WINDOW_IN_MILIS =5000;

    public UserSiteVisitDetails()
    {
        startWindow();
    }

    public boolean hasTimeWindowExpired()
    {
        return System.currentTimeMillis() - firstVisit > WINDOW_IN_MILIS ;
    }

    public void addVisit()
    {
        visitsInWindow++;
    }

    public void startWindow()
    {
        firstVisit = System.currentTimeMillis();
        visitsInWindow = 1;
    }

    public boolean hasExceededVisits()
    {
        return visitsInWindow >= VISIT_LIMIT;
    }
}
