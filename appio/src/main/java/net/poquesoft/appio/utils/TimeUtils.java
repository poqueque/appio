package net.poquesoft.appio.utils;

import android.util.Log;

import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtils {

	public final static long ONE_SECOND = 1000;
	public final static long SECONDS = 60;

	public final static long ONE_MINUTE = ONE_SECOND * 60;
	public final static long MINUTES = 60;

	public final static long ONE_HOUR = ONE_MINUTE * 60;
	public final static long HOURS = 24;

	public final static long ONE_DAY = ONE_HOUR * 24;
	public static final String TODAY = "Hoy";
	public static final String YESTERDAY = "Ayer";
	public static final String TOMORROW = "Mañana";
	private static final String[] WEEK = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
	private static final String TAG = "TimeUtils";
	private static final String[] SHORT_MONTH = {"Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"};

	private TimeUtils() {
	}

	/**
	 * converts time (in milliseconds) to human-readable format
	 *  "<w> days, <x> hours, <y> minutes and (z) seconds"
	 */
	public static String millisToLongDHMS(long duration) {
		StringBuffer res = new StringBuffer();
		long temp = 0;
		if (duration >= ONE_SECOND) {
			temp = duration / ONE_DAY;
			if (temp > 0) {
				duration -= temp * ONE_DAY;
				res.append(temp).append(" day").append(temp > 1 ? "s" : "")
				.append(duration >= ONE_MINUTE ? ", " : "");
			}

			temp = duration / ONE_HOUR;
			if (temp > 0) {
				duration -= temp * ONE_HOUR;
				res.append(temp).append(" hour").append(temp > 1 ? "s" : "")
				.append(duration >= ONE_MINUTE ? ", " : "");
			}

			temp = duration / ONE_MINUTE;
			if (temp > 0) {
				duration -= temp * ONE_MINUTE;
				res.append(temp).append(" minute").append(temp > 1 ? "s" : "");
			}

			if (!res.toString().equals("") && duration >= ONE_SECOND) {
				res.append(" and ");
			}

			temp = duration / ONE_SECOND;
			if (temp > 0) {
				res.append(temp).append(" second").append(temp > 1 ? "s" : "");
			}
			return res.toString();
		} else {
			return "0 second";
		}
	}

	/**
	 * converts time (in milliseconds) to human-readable format
	 *  "<w> days, <x> hours, <y> minutes"
	 */
	public static String millisToLongDHM(long duration) {
		StringBuffer res = new StringBuffer();
		long temp = 0;
		if (duration >= ONE_MINUTE) {
			temp = duration / ONE_DAY;
			if (temp > 0) {
				duration -= temp * ONE_DAY;
				res.append(temp).append(" day").append(temp > 1 ? "s" : "")
				.append(duration >= ONE_MINUTE ? ", " : "");
			}

			temp = duration / ONE_HOUR;
			if (temp > 0) {
				duration -= temp * ONE_HOUR;
				res.append(temp).append(" hour").append(temp > 1 ? "s" : "")
				.append(duration >= ONE_MINUTE ? ", " : "");
			}

			temp = duration / ONE_MINUTE;
			if (temp > 0) {
				duration -= temp * ONE_MINUTE;
				res.append(temp).append(" minute").append(temp > 1 ? "s" : "");
			}
			return res.toString();
		} else {
			return "0 minutes";
		}
	}


    public static String getDiaRelativoHumanReadable(long timestamp) {
        return getDiaRelativoHumanReadable(new Date(timestamp));
    }

	public static String getDiaRelativoHumanReadable(Date date) {
		return getDiaRelativoHumanReadable(date, 0);
	}

	public static String getDiaRelativoHumanReadable(Date date, int horaDeCambioDeDia) {
		if (date == null) return "Null date";
		
		if (horaDeCambioDeDia != 0){
			Calendar c = new GregorianCalendar();
			c.setTime(date);
			c.add(Calendar.HOUR,-horaDeCambioDeDia);
			date = c.getTime();
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date currentDate = new Date();
		try {
			currentDate = sdf.parse(sdf.format(currentDate));
			date = sdf.parse(sdf.format(date));
		} catch (ParseException e) {
		}
		
		Calendar today = new GregorianCalendar();
			today.setTime(currentDate);

		if (date.equals(today.getTime())) return TODAY;
			
		Calendar yesterday = (Calendar) today.clone();
			yesterday.add(Calendar.DAY_OF_MONTH, -1);

		if (date.equals(yesterday.getTime())) return YESTERDAY;
		
		Calendar tomorrow = (Calendar) today.clone();
			tomorrow.add(Calendar.DAY_OF_MONTH, 1);

		if (date.equals(tomorrow.getTime())) return TOMORROW;
	
		Calendar ref = new GregorianCalendar();
			ref.setTime(date);

		int w = ref.get(Calendar.DAY_OF_WEEK)-1;
		return WEEK[w];
	}

    /*
     * Devuelve "Hace X minutos", "Hace X horas", etc... para mensajes. En castellano.
     */

    public static String getElapsedTimeHumanReadable(long when) {
        long now = System.currentTimeMillis();
        //Comprobar si when viene sin millis
//        if (now/when > 100) when *=1000;

        if (now < when) {
            //A futuro
            long diffInSeconds = (when - now)/1000;
            if (diffInSeconds < 60) return "ahora";

            long diffInMinutes = (diffInSeconds)/60;
            if (diffInMinutes == 1) return "en 1 minuto";
            if (diffInMinutes < 60) return "en "+diffInMinutes+" minutos";

            long diffInHours = (diffInMinutes)/60;
            if (diffInHours == 1) return "en 1 hora";
            if (diffInHours < 24) return "en "+diffInHours+" horas";

            long diffInDays = (diffInHours)/24;
            if (diffInDays == 1) return "en 1 día";
            if (diffInDays < 30) return "en "+diffInDays+" días";

            long diffInMonths = (diffInDays)/30;
            if (diffInMonths == 1) return "en 1 mes";
            if (diffInMonths < 12) return "en "+diffInMonths+" meses";

            long diffInYears = (diffInMonths)/12;
            if (diffInYears == 1) return "en 1 año";
            return "en "+diffInYears+" años";
        }

        long diffInSeconds = (now - when)/1000;
        if (diffInSeconds < 60) return "ahora";

        long diffInMinutes = (diffInSeconds)/60;
        if (diffInMinutes == 1) return "hace 1 minuto";
        if (diffInMinutes < 60) return "hace "+diffInMinutes+" minutos";

        long diffInHours = (diffInMinutes)/60;
        if (diffInHours == 1) return "hace 1 hora";
        if (diffInHours < 24) return "hace "+diffInHours+" horas";

        long diffInDays = (diffInHours)/24;
        if (diffInDays == 1) return "hace 1 día";
        if (diffInDays < 30) return "hace "+diffInDays+" días";

        long diffInMonths = (diffInDays)/30;
        if (diffInMonths == 1) return "hace 1 mes";
        if (diffInMonths < 12) return "hace "+diffInMonths+" meses";

        long diffInYears = (diffInMonths)/12;
        if (diffInYears == 1) return "hace 1 año";
        return "hace "+diffInYears+" años";
    }


	public static int Hora() {
		Date currentDate = new Date();
		return currentDate.getHours();
	}
	
	public static int Minuto() {
		Date currentDate = new Date();
		return currentDate.getMinutes();
	}

	public static float HM() {
		Date currentDate = new Date();
		float hm = 0F + currentDate.getHours() + (currentDate.getMinutes()/60F);
		return hm;
	}
	
	public static float HM(String hora) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date d;
		try {
			d = sdf.parse(hora);
		} catch (ParseException e) {
			return 0;
		}
		float hm = 0F + d.getHours() + (d.getMinutes()/60F);
		return hm;
	}

	public static String HoraMinuto() {
		return HoraMinuto(new Date());
	}

    public static String HoraMinuto(long currentDate) {
        return HoraMinuto(new Date(currentDate));
    }

	public static String HoraMinuto(Date currentDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(currentDate);
	}

	public static String HoraMinutoSegundo() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date currentDate = new Date();
		return sdf.format(currentDate);
	}

	public static String HoraMinutoSegundo(Date currentDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(currentDate);
	}

	public static String DiaHoraMinuto(long l) {
		return DiaHoraMinuto(new Date(l));
	}

    public static String DiaHoraMinuto(Date d) {
		if (d==null) return "No Date";
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return getDiaRelativoHumanReadable(d) + " " + sdf.format(d);
	}

    public static String DiaHoraMinutoSegundo(long l) {
        return DiaHoraMinutoSegundo(new Date(l));
    }

    public static String DiaHoraMinutoSegundo(Date d) {
        if (d==null) return "No Date";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return getDiaRelativoHumanReadable(d) + " " + sdf.format(d);
    }

	public static boolean isBetween(String f, String t) {
		float now = HM();
		float t1 = HM(f);
		float t2 = HM(t);
		Log.i(TAG,"[SRV] now:"+now);
		Log.i(TAG,"[SRV] t1:"+t1);
		Log.i(TAG,"[SRV] t2:"+t2);
		if (t1 == t2) return false;
		if (t1 < t2) {
			if (t1 < now && now < t2) return true;
			return false;
		};
		if (t1 > t2) {
			if (t1 < now) return true;
			if (now < t2) return true;
			return false;
		}
		return false;
	}

    public static long hoursElapsed(long appStarted) {
        if (appStarted < 100) return 0;
        long time = System.currentTimeMillis()-appStarted;
        long secs = time/1000;
        long mins = secs/60;
        long hours = mins/60;
        return hours;
    }

    public static long minutesElapsed(long appStarted) {
        if (appStarted < 100) return 0;
        long time = System.currentTimeMillis()-appStarted;
        long secs = time/1000;
        long mins = secs/60;
        return mins;
    }

	public static long secondsElapsed(long appStarted) {
		if (appStarted < 100) return 0;
		long time = System.currentTimeMillis()-appStarted;
		long secs = time/1000;
		return secs;
	}

	public static String DiaMesAnyo(Date d) {
		if (d==null) return "No Date";
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		return day + " " + SHORT_MONTH[month] + " " + year;
	}

    public static String MesAnyo(Date d) {
        if (d==null) return "No Date";
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        return SHORT_MONTH[month] + " " + year;
    }
    public static Date getDate(int theYear, int theMonth, int theDay) {
        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, theYear);
        myCal.set(Calendar.MONTH, theMonth-1); //Month is zero-based
        myCal.set(Calendar.DAY_OF_MONTH, theDay);
        return myCal.getTime();
    }

    public static boolean esMayorDeEdad(int anyo, int mes, int dia, int edad) {
        if (anyo*mes*dia == 0) return false;
        LocalDate birthdate = new LocalDate(anyo,mes,dia);
        LocalDate now = new LocalDate();
        Years age = Years.yearsBetween(birthdate, now);
        return (age.getYears()>=edad);
    }

    public static long getExpireTS(long time) {
        return System.currentTimeMillis()+time;
    }

    public static boolean hasExpired(long time) {
        return (System.currentTimeMillis()>time);
    }
}