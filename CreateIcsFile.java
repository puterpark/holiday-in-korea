import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.LinkedHashMap;
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
        int cnt = 0;

        for (Integer date : map.keySet()) {
            cnt++;
            sb.append("BEGIN:VEVENT").append(LINE_SEPARATOR);
            sb.append("DTSTART;VALUE=DATE:").append(date).append(LINE_SEPARATOR);
            sb.append("DTSTAMP:").append(date + "T000000Z" ).append(LINE_SEPARATOR);
            sb.append("UID:").append(date).append(LINE_SEPARATOR);
            sb.append("SUMMARY:").append(map.get(date)).append(LINE_SEPARATOR);
            sb.append("CLASS:PUBLIC").append(LINE_SEPARATOR);
            sb.append("TRANSP:TRANSPARENT").append(LINE_SEPARATOR);
            sb.append("END:VEVENT").append(map.size() == cnt ? "" : LINE_SEPARATOR);
        }

        return sb.toString();
    }

    private static Map<Integer, String> setHoliday() {
        Map<Integer, String> map = new LinkedHashMap<>();

        // 2022년
        map.put(20220101, "새해 첫날");
        map.put(20220131, "설날 전날");
        map.put(20220201, "설날");
        map.put(20220202, "설날 다음 날");
        map.put(20220301, "삼일절");
        map.put(20220309, "제20대 대통령선거");
        map.put(20220505, "어린이날");
        map.put(20220508, "부처님오신날");
        map.put(20220601, "제8회 전국동시지방선거");
        map.put(20220606, "현충일");
        map.put(20220815, "광복절");
        map.put(20220909, "추석 전날");
        map.put(20220910, "추석");
        map.put(20220911, "추석 다음 날");
        map.put(20220912, "대체공휴일(추석)");
        map.put(20221003, "개천절");
        map.put(20221009, "한글날");
        map.put(20221010, "대체공휴일(한글날)");
        map.put(20221225, "크리스마스");

        // 2023년
        map.put(20230101, "새해 첫날");
        map.put(20230121, "설날 전날");
        map.put(20230122, "설날");
        map.put(20230123, "설날 다음 날");
        map.put(20230124, "대체공휴일(설날)");
        map.put(20230301, "삼일절");
        map.put(20230505, "어린이날");
        map.put(20230527, "부처님오신날");
        map.put(20230529, "대체공휴일(부처님오신날)");
        map.put(20230606, "현충일");
        map.put(20230815, "광복절");
        map.put(20230928, "추석 전날");
        map.put(20230929, "추석");
        map.put(20230930, "추석 다음 날");
        map.put(20231002, "임시공휴일");
        map.put(20231003, "개천절");
        map.put(20231009, "한글날");
        map.put(20231225, "크리스마스");

        // 2024년
        map.put(20240101, "새해 첫날");
        map.put(20240209, "설날 전날");
        map.put(20240210, "설날");
        map.put(20240211, "설날 다음 날");
        map.put(20240212, "대체공휴일(설날)");
        map.put(20240301, "삼일절");
        map.put(20240410, "제22대 국회의원 선거");
        map.put(20240505, "어린이날");
        map.put(20240506, "대체공휴일(어린이날)");
        map.put(20240515, "부처님오신날");
        map.put(20240606, "현충일");
        map.put(20240815, "광복절");
        map.put(20240916, "추석 전날");
        map.put(20240917, "추석");
        map.put(20240918, "추석 다음 날");
        map.put(20241003, "개천절");
        map.put(20241009, "한글날");
        map.put(20241225, "크리스마스");

        return map;
    }
}
