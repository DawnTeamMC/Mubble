package hugman.mod.util;

import java.util.Calendar;

public class CalendarEvents
{
	public boolean isNewYear;
	public boolean isTodayTemp;
	public boolean isAprilFools;
	public boolean isJoaquinBirthday;
	public boolean isJulesBirthday;
	public boolean isMarkBirthday;
	public boolean isMiaBirthday;
	public boolean isMubbleBirthday;
	public boolean isHugmanBirthday;
	public boolean isChristmas;
    
    public CalendarEvents()
    {
        Calendar cal = Calendar.getInstance();
        
        int month 	= cal.get(Calendar.MONTH);
        int day 	= cal.get(Calendar.DAY_OF_MONTH);
        
        int january 	= Calendar.JANUARY;
        int march		= Calendar.MARCH;
        int april 		= Calendar.APRIL;
        int june 		= Calendar.JUNE;
        int july 		= Calendar.JULY;
        int october 	= Calendar.OCTOBER;
        int december 	= Calendar.DECEMBER;

        if (month == january 	&& day == 1) this.isNewYear = true;
        if (month == january 	&& day == 12) this.isMiaBirthday = true;
        if (month == march		&& day == 9) this.isJulesBirthday = true;
        if (month == march		&& day == 14) this.isTodayTemp = true;
        if (month == april 		&& day == 1)
        {
        	this.isAprilFools = true;
        	this.isMarkBirthday = true;
        }
        if (month == june 		&& day == 18) this.isJoaquinBirthday = true;
        if (month == july 		&& day == 14) this.isMubbleBirthday = true;
        if (month == october 	&& day == 3) this.isHugmanBirthday = true;
        if (month == december 	&& day >= 24 && day <= 26) this.isChristmas = true;
        if (month == december	&& day == 31) this.isNewYear = true;
    }
}