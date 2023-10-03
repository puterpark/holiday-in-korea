import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CreateIcsFile {

    private final static String LINE_SEPARATOR = System.getProperty("line.separator");

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

    private static String content(Map<Integer, String> map) {
        StringBuilder sb = new StringBuilder();
        final int[] cnt = {0};

        map.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(
                entry -> {
                    cnt[0]++;
                    sb.append("BEGIN:VEVENT").append(LINE_SEPARATOR);
                    sb.append("DTSTART;VALUE=DATE:").append(entry.getKey()).append(LINE_SEPARATOR);
                    sb.append("DTSTAMP:").append(entry.getKey() + "T000000Z").append(LINE_SEPARATOR);
                    sb.append("UID:").append(entry.getKey()).append(LINE_SEPARATOR);
                    sb.append("SUMMARY:").append(entry.getValue()).append(LINE_SEPARATOR);
                    sb.append("CLASS:PUBLIC").append(LINE_SEPARATOR);
                    sb.append("TRANSP:TRANSPARENT").append(LINE_SEPARATOR);
                    sb.append("END:VEVENT").append(map.size() == cnt[0] ? "" : LINE_SEPARATOR);
                }
        );

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

    private static LocalDate toLocalDate(Integer date) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(String.valueOf(date), format);
    }

    private static Integer toInteger(LocalDate localDate) {
        return Integer.parseInt(localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    }

    /**
     * 양력 공휴일 및 대체공휴일 확인
     * @param year
     * @param map
     */
    private static void commonHoliday(Integer year, Map<Integer, String> map) {
        // 양력 휴일
        Integer int0101 = Integer.parseInt(year + "0101");
        Integer int0301 = Integer.parseInt(year + "0301");
        Integer int0505 = Integer.parseInt(year + "0505");
        Integer int0606 = Integer.parseInt(year + "0606");
        Integer int0815 = Integer.parseInt(year + "0815");
        Integer int1003 = Integer.parseInt(year + "1003");
        Integer int1009 = Integer.parseInt(year + "1009");
        Integer int1225 = Integer.parseInt(year + "1225");

        map.put(int0101, "새해 첫날");
        map.put(int0301, "삼일절");
        map.put(int0505, "어린이날");
        map.put(int0606, "현충일");
        map.put(int0815, "광복절");
        map.put(int1003, "개천절");
        map.put(int1009, "한글날");
        map.put(int1225, "크리스마스");

        // 삼일절, 어린이날, 개천절, 한글날 대체공휴일
        LocalDate holiday0301 = substituteHoliday(int0301);
        if (holiday0301 != null) {
            map.put(toInteger(holiday0301), "대체공휴일(삼일절)");
        }
        LocalDate holiday0505 = substituteHoliday(int0505);
        if (holiday0505 != null) {
            map.put(toInteger(holiday0505), "대체공휴일(어린이날)");
        }
        LocalDate holiday1003 = substituteHoliday(int1003);
        if (holiday1003 != null) {
            map.put(toInteger(holiday1003), "대체공휴일(개천절)");
        }
        LocalDate holiday1009 = substituteHoliday(int1009);
        if (holiday1009 != null) {
            map.put(toInteger(holiday1009), "대체공휴일(한글날)");
        }

        if (year >= 2023) {
            // 2023년부터 크리스마스 대체공휴일 지정
            LocalDate holiday1225 = substituteHoliday(int1225);
            if (holiday1225 != null) {
                map.put(toInteger(holiday1225), "대체공휴일(크리스마스)");
            }
        }
    }

    private static Map<Integer, String> setHoliday() {
        Map<Integer, String> map = new HashMap<>();

        // 2022년
        commonHoliday(2022, map);
        map.put(20220131, "설날 전날");
        map.put(20220201, "설날");
        map.put(20220202, "설날 다음 날");
        map.put(20220309, "제20대 대통령 선거");
        map.put(20220508, "부처님오신날");
        map.put(20220601, "제8회 전국동시지방 선거");
        map.put(20220909, "추석 전날");
        map.put(20220910, "추석");
        map.put(20220911, "추석 다음 날");
        map.put(20220912, "대체공휴일(추석)");

        // 2023년
        commonHoliday(2023, map);
        map.put(20230121, "설날 전날");
        map.put(20230122, "설날");
        map.put(20230123, "설날 다음 날");
        map.put(20230124, "대체공휴일(설날)");
        map.put(20230527, "부처님오신날");
        map.put(20230529, "대체공휴일(부처님오신날)");
        map.put(20230928, "추석 전날");
        map.put(20230929, "추석");
        map.put(20230930, "추석 다음 날");
        map.put(20231002, "임시공휴일");

        // 2024년
        commonHoliday(2024, map);
        map.put(20240209, "설날 전날");
        map.put(20240210, "설날");
        map.put(20240211, "설날 다음 날");
        map.put(20240212, "대체공휴일(설날)");
        map.put(20240410, "제22대 국회의원 선거");
        map.put(20240515, "부처님오신날");
        map.put(20240916, "추석 전날");
        map.put(20240917, "추석");
        map.put(20240918, "추석 다음 날");

        return map;
    }
}
