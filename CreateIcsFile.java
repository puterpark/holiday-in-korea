import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CreateIcsFile {

    private final static String LINE_SEPARATOR = System.lineSeparator();

    public record Holiday(Integer date, String desc) {
    }

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("holiday.ics");
        FileOutputStream fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        System.setOut(ps);

        System.out.print(BeginVcalendar());
        System.out.println(content(setHoliday()));
        System.out.print(endVcalendar());
    }

    private static String BeginVcalendar() {
        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN:VCALENDAR").append(LINE_SEPARATOR);
        sb.append("VERSION:2.0").append(LINE_SEPARATOR);
        sb.append("PRODID:-//GitHub@puterpark//holiday-in-korea").append(LINE_SEPARATOR);
        sb.append("X-WR-CALNAME:대한민국의 공휴일").append(LINE_SEPARATOR);
        sb.append("X-WR-TIMEZONE:Asia/Seoul").append(LINE_SEPARATOR);
        sb.append("X-WR-CALDESC:https://github.com/puterpark/holiday-in-korea").append(LINE_SEPARATOR);
        return sb.toString();
    }

    private static String endVcalendar() {
        return "END:VCALENDAR";
    }

    private static String content(List<Holiday> list) {
        StringBuilder sb = new StringBuilder();
        final int[] cnt = {0};

        list.sort(Comparator.comparing(Holiday::date));
        list.forEach(holiday -> {
            cnt[0]++;
            Integer date = holiday.date();
            sb.append("BEGIN:VEVENT").append(LINE_SEPARATOR);
            sb.append("DTSTART;VALUE=DATE:").append(date).append(LINE_SEPARATOR);
            sb.append("DTSTAMP:").append(date).append("T000000Z").append(LINE_SEPARATOR);
            sb.append("UID:").append(date).append(LINE_SEPARATOR);
            sb.append("SUMMARY:").append(holiday.desc()).append(LINE_SEPARATOR);
            sb.append("CLASS:PUBLIC").append(LINE_SEPARATOR);
            sb.append("TRANSP:TRANSPARENT").append(LINE_SEPARATOR);
            sb.append("END:VEVENT").append(list.size() == cnt[0] ? "" : LINE_SEPARATOR);
        });

        return sb.toString();
    }

    private static LocalDate substituteHoliday(Integer date) {
        LocalDate localDate = toLocalDate(date);

        if (DayOfWeek.SUNDAY.equals(localDate.getDayOfWeek())) {
            return localDate.plusDays(1);
        }
        if (DayOfWeek.SATURDAY.equals(localDate.getDayOfWeek())) {
            return localDate.plusDays(2);
        }

        return null;
    }

    /**
     * Integer -> LocalDate 변환
     * @param date
     * @return
     */
    private static LocalDate toLocalDate(Integer date) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(String.valueOf(date), format);
    }

    /**
     * LocalDate -> Integer
     * @param localDate
     * @return
     */
    private static Integer toInteger(LocalDate localDate) {
        return Integer.parseInt(localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    }

    private static void addHoliday(List<Holiday> list, Integer date, String desc) {
        list.add(new Holiday(date, desc));
    }

    /**
     * 양력 공휴일 및 대체 공휴일 확인
     * @param year
     * @param list
     */
    private static void commonHoliday(Integer year, List<Holiday> list) {
        // 양력 휴일
        Integer int0101 = Integer.parseInt(year + "0101");
        Integer int0301 = Integer.parseInt(year + "0301");
        Integer int0505 = Integer.parseInt(year + "0505");
        Integer int0606 = Integer.parseInt(year + "0606");
        Integer int0815 = Integer.parseInt(year + "0815");
        Integer int1003 = Integer.parseInt(year + "1003");
        Integer int1009 = Integer.parseInt(year + "1009");
        Integer int1225 = Integer.parseInt(year + "1225");

        addHoliday(list, int0101, "새해 첫날");
        addHoliday(list, int0301, "삼일절");
        addHoliday(list, int0505, "어린이날");
        addHoliday(list, int0606, "현충일");
        addHoliday(list, int0815, "광복절");
        addHoliday(list, int1003, "개천절");
        addHoliday(list, int1009, "한글날");
        addHoliday(list, int1225, "크리스마스");

        // 삼일절, 어린이날, 개천절, 한글날 대체공휴일
        LocalDate holiday0301 = substituteHoliday(int0301);
        if (holiday0301 != null) {
            addHoliday(list, toInteger(holiday0301), "대체공휴일(삼일절)");
        }
        LocalDate holiday0505 = substituteHoliday(int0505);
        if (holiday0505 != null) {
            addHoliday(list, toInteger(holiday0505), "대체공휴일(어린이날)");
        }
        LocalDate holiday1003 = substituteHoliday(int1003);
        if (holiday1003 != null) {
            addHoliday(list, toInteger(holiday1003), "대체공휴일(개천절)");
        }
        LocalDate holiday1009 = substituteHoliday(int1009);
        if (holiday1009 != null) {
            addHoliday(list, toInteger(holiday1009), "대체공휴일(한글날)");
        }

        if (year >= 2023) {
            // 2023년부터 크리스마스 대체공휴일 지정
            LocalDate holiday1225 = substituteHoliday(int1225);
            if (holiday1225 != null) {
                addHoliday(list, toInteger(holiday1225), "대체공휴일(크리스마스)");
            }
        }
    }

    /**
     * 공휴일 설정
     * @return
     */
    private static List<Holiday> setHoliday() {
        List<Holiday> list = new ArrayList<>();

        // 2022년
        commonHoliday(2022, list);
        addHoliday(list, 20220131, "설날 전날");
        addHoliday(list, 20220201, "설날");
        addHoliday(list, 20220202, "설날 다음 날");
        addHoliday(list, 20220309, "제20대 대통령 선거");
        addHoliday(list, 20220508, "부처님오신날");
        addHoliday(list, 20220601, "제8회 전국동시지방 선거");
        addHoliday(list, 20220909, "추석 전날");
        addHoliday(list, 20220910, "추석");
        addHoliday(list, 20220911, "추석 다음 날");
        addHoliday(list, 20220912, "대체공휴일(추석)");

        // 2023년
        commonHoliday(2023, list);
        addHoliday(list, 20230121, "설날 전날");
        addHoliday(list, 20230122, "설날");
        addHoliday(list, 20230123, "설날 다음 날");
        addHoliday(list, 20230124, "대체공휴일(설날)");
        addHoliday(list, 20230527, "부처님오신날");
        addHoliday(list, 20230529, "대체공휴일(부처님오신날)");
        addHoliday(list, 20230928, "추석 전날");
        addHoliday(list, 20230929, "추석");
        addHoliday(list, 20230930, "추석 다음 날");
        addHoliday(list, 20231002, "임시공휴일");

        // 2024년
        commonHoliday(2024, list);
        addHoliday(list, 20240209, "설날 전날");
        addHoliday(list, 20240210, "설날");
        addHoliday(list, 20240211, "설날 다음 날");
        addHoliday(list, 20240212, "대체공휴일(설날)");
        addHoliday(list, 20240410, "제22대 국회의원 선거");
        addHoliday(list, 20240515, "부처님오신날");
        addHoliday(list, 20240916, "추석 전날");
        addHoliday(list, 20240917, "추석");
        addHoliday(list, 20240918, "추석 다음 날");
        addHoliday(list, 20241001, "임시공휴일(국군의 날)");

        // 2025년
        commonHoliday(2025, list);
        addHoliday(list, 20250128, "설날 전날");
        addHoliday(list, 20250129, "설날");
        addHoliday(list, 20250130, "설날 다음 날");
        addHoliday(list, 20250505, "부처님오신날");
        addHoliday(list, 20250506, "대체공휴일(부처님오신날)");
        addHoliday(list, 20251005, "추석 전날");
        addHoliday(list, 20251006, "추석");
        addHoliday(list, 20251007, "추석 다음 날");
        addHoliday(list, 20251008, "대체공휴일(추석)");

        return list;
    }
}
