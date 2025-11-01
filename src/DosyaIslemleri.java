import java.io.*;
import java.util.*;

public class DosyaIslemleri {
    private static final String DOSYA_ADI = "ogrenciler.txt";

    public static List<Ogrenci> ogrencileriDosyadanOku() {
        List<Ogrenci> ogrenciler = new ArrayList<>();
        File f = new File(DOSYA_ADI);
        if (!f.exists()) return ogrenciler;

        try (BufferedReader br = new BufferedReader(new FileReader(DOSYA_ADI))) {
            String s;
            while ((s = br.readLine()) != null) {
                Ogrenci o = Ogrenci.dosyadanOku(s);
                if (o != null) ogrenciler.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ogrenciler;
    }

    public static void ogrencileriDosyayaYaz(List<Ogrenci> ogrenciler) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DOSYA_ADI))) {
            for (Ogrenci o : ogrenciler) pw.println(o.dosyaFormatinda());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Ogrenci> baslangicVerisiOlustur() {
        List<Ogrenci> ogrenciler = new ArrayList<>();
        ogrenciler.add(new Ogrenci("Ali", "Kaya", 236001, 3.5f, 1, 'E'));
        ogrenciler.add(new Ogrenci("Ayşe", "Demir", 236002, 3.8f, 2, 'K'));
        ogrencileriDosyayaYaz(ogrenciler);
        return ogrenciler;
    }
}
