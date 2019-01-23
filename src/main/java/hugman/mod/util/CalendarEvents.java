package hugman.mod.util;

import java.util.Calendar;

public class CalendarEvents
{
    private boolean isNewYear;
    private boolean isTodayTemp;
    private boolean isAprilFools;
    private boolean isCochuBirthday;
    private boolean isMubbleBirthday;
    private boolean isHugmanBirthday;
    private boolean isChristmas;
    
    public CalendarEvents()
    {
        Calendar cal = Calendar.getInstance();
        
        int month 	= cal.get(Calendar.MONTH);
        int day 	= cal.get(Calendar.DAY_OF_MONTH);
        
        int january 	= cal.JANUARY;
        int february 	= cal.FEBRUARY;
        int april 		= cal.APRIL;
        int june 		= cal.JUNE;
        int july 		= cal.JULY;
        int september 	= cal.SEPTEMBER;
        int october 	= cal.OCTOBER;
        int november 	= cal.NOVEMBER;
        int december 	= cal.DECEMBER;

        if (month == january 	&& day == 1) this.isNewYear = true;
        if (month == january 	&& day == 7) this.isTodayTemp = true;
        if (month == april 		&& day == 1) this.isAprilFools = true;
        if (month == june 		&& day == 18) this.isCochuBirthday = true;
        if (month == july 		&& day == 14) this.isMubbleBirthday = true;
        if (month == october 	&& day == 3) this.isHugmanBirthday = true;
        if (month == december 	&& day >= 24 && day <= 26) this.isChristmas = true;
        if (month == december	&& day == 31) this.isNewYear = true;
    }
}
