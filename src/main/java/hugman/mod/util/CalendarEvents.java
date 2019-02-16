package hugman.mod.util;

import java.util.Calendar;

@SuppressWarnings("unused")
public class CalendarEvents
{
	public boolean isNewYear;
	public boolean isTodayTemp;
	public boolean isAprilFools;
	public boolean isCochuBirthday;
	public boolean isMubbleBirthday;
	public boolean isHugmanBirthday;
	public boolean isChristmas;
    
    public CalendarEvents()
    {
        Calendar cal = Calendar.getInstance();
        
        int month 	= cal.get(Calendar.MONTH);
        int day 	= cal.get(Calendar.DAY_OF_MONTH);
        
        int january 	= Calendar.JANUARY;
        int february 	= Calendar.FEBRUARY;
        int april 		= Calendar.APRIL;
        int june 		= Calendar.JUNE;
        int july 		= Calendar.JULY;
        int september 	= Calendar.SEPTEMBER;
        int october 	= Calendar.OCTOBER;
        int november 	= Calendar.NOVEMBER;
        int december 	= Calendar.DECEMBER;

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
