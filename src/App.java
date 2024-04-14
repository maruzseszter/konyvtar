public class App {
    public static void main(String[] args) {
        Feladat feladat = new Feladat();
        try {
            feladat.importKolcsonzok("kolcsonzok.csv");
            feladat.importKolcsonzesek("kolcsonzesek.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

