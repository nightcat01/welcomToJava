package IO;

import java.util.Scanner;

public class Remainder {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int num1;
        int num2;
        int num3;

        num1 = scanner.nextInt();
        num2 = scanner.nextInt();
        num3 = scanner.nextInt();

        System.out.println((num1+num2)%num3);
        System.out.println((num1%num3 + num2%num3)%num3);
        System.out.println((num1*num2)%num3);
        System.out.println((num1%num3 * num2%num3)%num3);
    }
}
