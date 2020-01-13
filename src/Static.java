import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Static {

    public static List<String> commandList = new ArrayList<>();

    public static void setCommandList(){

        commandList.add("exit");
        commandList.add("new");
        commandList.add("apply");
        commandList.add("");
    }

    public static String inS(){
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        if (s.equalsIgnoreCase("exit")){
            System.exit(0);
        }
        return s;
    }

    public static int inI(){
        boolean isInt = false;
        int i = 0;
        while (!isInt) {
            try {
                Scanner scanner = new Scanner(System.in);
                i = scanner.nextInt();
                isInt = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid character");
            }
        }
        return i;
    }

    public static BigDecimal inB(){
        boolean isBigDecimal = false;
        BigDecimal b = null;
        while (!isBigDecimal) {
            try {
                Scanner scanner = new Scanner(System.in);
                b = scanner.nextBigDecimal();
                isBigDecimal = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid character");
            }
        }
        return b;
    }

    public static LocalDate date(){
        boolean isDate = false;
        LocalDate date = null;
        while (!isDate) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            try {
                date = LocalDate.parse(inS(), formatter);
                isDate = true;
            } catch (DateTimeException e) {
                System.out.println("Date format is wrong");
            }
        }
        return date;
    }
}
