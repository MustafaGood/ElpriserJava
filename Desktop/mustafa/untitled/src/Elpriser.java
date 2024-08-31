import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Elpriser {
    private static final int[] priser = new int[24];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char val;

        do {
            visaMeny();
            val = scanner.next().charAt(0);
            switch (val) {
                case '1':
                    mataInPriser(scanner);
                    break;
                case '2':
                    visaMinMaxMedel();
                    break;
                case '3':
                    sorteraPriser();
                    break;
                case '4':
                    bästaLaddningstid();
                    break;
                case '5':
                    läsFrånFil();
                    break;
                case 'e':
                case 'E':
                    System.out.println("Programmet avslutas.");
                    break;
                default:
                    System.out.println("Ogiltigt val, försök igen.");
            }
        } while (val != 'e' && val != 'E');
    }

    private static void visaMeny() {
        System.out.println("\nElpriser");
        System.out.println("=========");
        System.out.println("1. Mata in priser");
        System.out.println("2. Min, Max och Medel");
        System.out.println("3. Sortera priser");
        System.out.println("4. Bästa laddningstid (4 timmar)");
        System.out.println("5. Läs in priser från CSV-fil");
        System.out.println("e. Avsluta");
        System.out.print("Välj ett alternativ: ");
    }

    private static void mataInPriser(Scanner scanner) {
        System.out.println("Ange elpriser för varje timme (öre/kWh):");
        for (int i = 0; i < priser.length; i++) {
            while (true) {
                try {
                    System.out.printf("Timme %02d-%02d: ", i, i + 1);
                    priser[i] = Integer.parseInt(scanner.next());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Ogiltig inmatning. Ange ett heltal.");
                }
            }
        }
    }

    private static void visaMinMaxMedel() {
        int minPris = Arrays.stream(priser).min().orElse(0);
        int maxPris = Arrays.stream(priser).max().orElse(0);
        double medelPris = Arrays.stream(priser).average().orElse(0.0);

        int minTimme = indexOf(priser, minPris);
        int maxTimme = indexOf(priser, maxPris);

        System.out.println("Lägsta pris: " + minPris + " öre vid timme " + minTimme);
        System.out.println("Högsta pris: " + maxPris + " öre vid timme " + maxTimme);
        System.out.println("Medelpris: " + medelPris + " öre/kWh");
    }

    private static void sorteraPriser() {
        int[] sorteradePriser = priser.clone();
        Arrays.sort(sorteradePriser);

        System.out.println("Priser sorterade från lägst till högst:");
        for (int i = 0; i < 24; i++) {
            System.out.printf("Timme %02d: %d öre%n", i, sorteradePriser[i]);
        }
    }

    private static void bästaLaddningstid() {
        int bästaStartTimme = 0;
        int lägstaSumma = Integer.MAX_VALUE;

        for (int i = 0; i <= priser.length - 4; i++) {
            int summa = 0;
            for (int j = 0; j < 4; j++) {
                summa += priser[i + j];
            }
            if (summa < lägstaSumma) {
                lägstaSumma = summa;
                bästaStartTimme = i;
            }
        }

        double medelPris = lägstaSumma / 4.0;
        System.out.println("Bästa tid att starta laddningen är " + bästaStartTimme + ":00 med ett medelpris på " + medelPris + " öre/kWh");
    }

    private static void läsFrånFil() {
        String filnamn = "elpriser.csv";
        System.out.println("Försöker läsa filen från: " + new java.io.File(filnamn).getAbsolutePath());

        try (BufferedReader br = new BufferedReader(new FileReader(filnamn))) {
            for (int i = 0; i < priser.length; i++) {
                String rad = br.readLine();
                if (rad != null) {
                    priser[i] = Integer.parseInt(rad.trim());
                } else {
                    System.out.println("Filen innehåller inte tillräckligt många rader.");
                    return;
                }
            }
            System.out.println("Priser har lästs in från filen.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Fel vid läsning av filen: " + e.getMessage());
        }
    }

    private static int indexOf(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }
}
