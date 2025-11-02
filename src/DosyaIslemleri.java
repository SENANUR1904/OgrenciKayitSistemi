import java.io.*;
import java.util.*;

public class DosyaIslemleri {
    private static final String DOSYA_ADI = "ogrenciler.txt";

    public static List<Ogrenci> ogrencileriDosyadanOku() {
    List<Ogrenci> ogrenciler = new ArrayList<>();
    File dosya = new File(DOSYA_ADI);
    if (!dosya.exists()) return ogrenciler;

    try (BufferedReader reader = new BufferedReader(new FileReader(dosya))) {
        String satir;
        int gecersizKayit = 0;
        while ((satir = reader.readLine()) != null) {
            try {
                Ogrenci ogr = Ogrenci.dosyadanOku(satir);
                if (ogr != null) {
                    // Dosyadan okunan öğrenci numarasını kontrol et
                    if (String.valueOf(ogr.getOgrNo()).length() == 9) {
                        ogrenciler.add(ogr);
                    } else {
                        gecersizKayit++;
                        System.out.println("Geçersiz öğrenci numarası atlandı: " + ogr.getOgrNo());
                    }
                }
            } catch (Exception e) {
                gecersizKayit++;
            }
        }
        if (gecersizKayit > 0) {
            System.out.println(gecersizKayit + " geçersiz kayıt atlandı.");
        }
    } catch (IOException e) {
        System.out.println("Dosya okuma hatası: " + e.getMessage());
    }
    return ogrenciler;
}

    public static void ogrencileriDosyayaYaz(List<Ogrenci> ogrenciler) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(DOSYA_ADI)))) {
            for (Ogrenci ogr : ogrenciler) {
                writer.println(ogr.dosyaFormatinda());
            }
        } catch (IOException e) {
            System.out.println("Dosya yazma hatası: " + e.getMessage());
        }
    }

    public static List<Ogrenci> baslangicVerisiOlustur() {
        File dosya = new File(DOSYA_ADI);
        if (dosya.exists()) dosya.delete();

        String[] erkekIsimler = {"Ali","Mehmet","Mustafa","Ahmet","Hasan","Yusuf","Emre","Burak","Kerem","Mert"};
        String[] kizIsimler = {"Ayşe","Fatma","Zeynep","Elif","Meryem","Ebru","Ceren","Seda","İrem","Deniz"};
        String[] soyadlar = {"Yılmaz","Kaya","Demir","Çelik","Şahin","Koç","Acar","Öztürk","Aslan","Doğan"};

        List<Ogrenci> ogrenciler = new ArrayList<>(10000);
        Random r = new Random();
        Set<Integer> kullanilanNumaralar = new HashSet<>(10000);

        for (int i = 0; i < 10000; i++) {
            char cinsiyet = r.nextBoolean() ? 'E' : 'K';
            String isim = (cinsiyet == 'E') ? erkekIsimler[r.nextInt(erkekIsimler.length)] : kizIsimler[r.nextInt(kizIsimler.length)];
            String soyad = soyadlar[r.nextInt(soyadlar.length)];

            int ogrNo;
            do {
                ogrNo = 236000000 + r.nextInt(1_000_000);
            } while (kullanilanNumaralar.contains(ogrNo));
            kullanilanNumaralar.add(ogrNo);

            float gano = 1.0f + r.nextFloat() * 3.0f;
            int sinif = 1 + r.nextInt(4);

            ogrenciler.add(new Ogrenci(isim, soyad, ogrNo, gano, sinif, cinsiyet));
        }

        ogrencileriDosyayaYaz(ogrenciler);
        System.out.println("✅ 10.000 öğrenci başarıyla oluşturuldu ve dosyaya kaydedildi!");
        return ogrenciler;
    }
}