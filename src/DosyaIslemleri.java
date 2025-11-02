import java.io.*;
import java.util.*;

public class DosyaIslemleri {
    private static final String DOSYA_ADI = "ogrenciler.txt";

    // Dosyadan öğrenci listesini okur
    public static List<Ogrenci> ogrencileriDosyadanOku() {
        List<Ogrenci> ogrenciler = new ArrayList<>();
        File dosya = new File(DOSYA_ADI);
        if (!dosya.exists()) {
            return ogrenciler;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DOSYA_ADI))) {
            String satir;
            while ((satir = reader.readLine()) != null) {
                Ogrenci ogr = Ogrenci.dosyadanOku(satir);
                if (ogr != null) ogrenciler.add(ogr);
            }
        } catch (IOException e) {
            System.out.println("Dosya okuma hatası: " + e.getMessage());
        }
        return ogrenciler;
    }

    // Öğrencileri dosyaya yazar
    public static void ogrencileriDosyayaYaz(List<Ogrenci> ogrenciler) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(DOSYA_ADI)))) {
            for (Ogrenci ogr : ogrenciler) {
                writer.println(ogr.dosyaFormatinda());
            }
        } catch (IOException e) {
            System.out.println("Dosya yazma hatası: " + e.getMessage());
        }
    }

    // Dosyayı sıfırdan oluşturur ve 10.000 öğrenci yazar
    public static List<Ogrenci> baslangicVerisiOlustur() {
        // Önce eski dosyayı sil
        File dosya = new File(DOSYA_ADI);
        if (dosya.exists()) {
            dosya.delete();
            System.out.println("Eski dosya silindi, yeni veri oluşturuluyor...");
        }

        String[] erkekIsimler = {"Ali","Mehmet","Mustafa","Ahmet","Hasan","Yusuf","Emre","Burak","Kerem","Mert"};
        String[] kizIsimler = {"Ayşe","Fatma","Zeynep","Elif","Meryem","Ebru","Ceren","Seda","İrem","Deniz"};
        String[] soyadlar = {"Yılmaz","Kaya","Demir","Çelik","Şahin","Koç","Acar","Öztürk","Aslan","Doğan"};

        List<Ogrenci> ogrenciler = new ArrayList<>(10000);
        Random rastgele = new Random();
        Set<Integer> kullanilanNumaralar = new HashSet<>(10000);

        for (int i = 0; i < 10000; i++) {
            char cinsiyet = rastgele.nextBoolean() ? 'E' : 'K';
            String isim = (cinsiyet == 'E')
                    ? erkekIsimler[rastgele.nextInt(erkekIsimler.length)]
                    : kizIsimler[rastgele.nextInt(kizIsimler.length)];
            String soyad = soyadlar[rastgele.nextInt(soyadlar.length)];

            int ogrNo;
            do {
                ogrNo = 236000000 + rastgele.nextInt(1_000_000);
            } while (kullanilanNumaralar.contains(ogrNo));
            kullanilanNumaralar.add(ogrNo);

            float gano = 1.0f + rastgele.nextFloat() * 3.0f;
            int sinif = rastgele.nextInt(4) + 1;

            ogrenciler.add(new Ogrenci(isim, soyad, ogrNo, gano, sinif, cinsiyet));
        }

        ogrencileriDosyayaYaz(ogrenciler);
        System.out.println("✅ 10.000 öğrenci başarıyla oluşturuldu ve dosyaya kaydedildi!");
        return ogrenciler;
    }
}
