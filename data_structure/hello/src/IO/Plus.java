package IO;

import java.util.Scanner;

public class Plus {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int num = 0;
        for(int i = 0; i < 2; i++) {
            num += scanner.nextInt();
        }
        System.out.println(num);
    }
}
