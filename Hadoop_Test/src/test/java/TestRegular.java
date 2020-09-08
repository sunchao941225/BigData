import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegular {

    public static void main(String[] args) throws ParseException {

        final String regex = "^(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}).*\\[(.*)\\]\\s\"(\\w*)\\s(.*)\\sHTTP\\/1\\.1\"\\s(\\d{3}).*$";
        final String string = "192.168.11.1 - - [28/Sep/2019:13:06:29 +0800] \"GET /favicon.ico HTTP/1.1\" 404 571 \"http://hadoopnode00/\" \"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36\"\n";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            System.out.println("Full match: " + matcher.group(0));
//            for (int i = 1; i <= matcher.groupCount(); i++) {
//                System.out.println("Group " + i + ": " + matcher.group(i));
//            }

            String strDate = matcher.group(2);
            System.out.println(strDate);
            // 28/Sep/2019:13:06:29 +0800
            // 2019-10-08 17:45:00

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
            Date date = sdf.parse(strDate);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            System.out.println(sdf2.format(date));
        }
    }
}
