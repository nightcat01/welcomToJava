package IF;


import java.util.Scanner;

public class Alarm {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int h = scanner.nextInt();
        int m = scanner.nextInt();

//        60m -> 1h
//        언제나 60m - 45m

        m = m - 45;

        if(m < 0) {
            h -=1;
            m += 60;
        }

        if(h < 0) {
            h += 24;
        }

        System.out.println(h + " " + m);
    }

}
