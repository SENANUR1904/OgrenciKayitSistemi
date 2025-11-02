import java.io.*;
import java.util.*;

public class RaporUretici {
    
    public static void raporlariOlustur(List<Ogrenci> ogrenciler) {
        try {
            long baslangicZamani = System.currentTimeMillis();
            
            List<Ogrenci> ganoSirali = new ArrayList<>(ogrenciler);
            ganoSirali.sort((o1, o2) -> Float.compare(o2.getGano(), o1.getGano()));
            dosyayaYaz("ogrenciler.txt", ganoSirali, "TÜM ÖĞRENCİLER (GANO'ya göre sıralı)");
            
            List<Ogrenci> sinifSirali = new ArrayList<>(ogrenciler);
            sinifSirali.sort((o1, o2) -> {
                if (o1.getSinif() != o2.getSinif()) return Integer.compare(o1.getSinif(), o2.getSinif());
                return Float.compare(o2.getGano(), o1.getGano());
            });
            dosyayaYaz("sinif_sirasi.txt", sinifSirali, "SINIF SIRASI");
            
            List<Ogrenci> noSirali = new ArrayList<>(ogrenciler);
            noSirali.sort(Comparator.comparingInt(Ogrenci::getOgrNo));
            dosyayaYaz("ogrenci_no_sirali.txt", noSirali, "ÖĞRENCİ NO SIRASI");
            
            performansRaporuOlustur(ogrenciler, System.currentTimeMillis() - baslangicZamani);
            
        } catch (IOException e) {
            System.out.println("Rapor oluşturma hatası: " + e.getMessage());
        }
    }
    
    private static void dosyayaYaz(String dosyaAdi, List<Ogrenci> ogrenciler, String baslik) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(dosyaAdi))) {
            writer.println("=== " + baslik + " ===");
            writer.println("Oluşturulma Tarihi: " + new Date());
            writer.println("Toplam Kayıt: " + ogrenciler.size());
            writer.printf("%-12s %-12s %-12s %-8s %-6s %s\n", "İsim", "Soyad", "Öğrenci No", "GANO", "Sınıf", "Cinsiyet");
            writer.println("----------------------------------------------------------------");
            for (Ogrenci ogr : ogrenciler) {
                writer.printf("%-12s %-12s %-12d %-8.2f %-6d %s\n", 
                    ogr.getIsim(), ogr.getSoyad(), ogr.getOgrNo(), ogr.getGano(), ogr.getSinif(), 
                    ogr.getCinsiyet() == 'E' ? "Erkek" : "Kız");
            }
        }
    }
    
    private static void performansRaporuOlustur(List<Ogrenci> ogrenciler, long sure) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter("performans.txt"))) {
            writer.println("=== PERFORMANS RAPORU ===");
            writer.println("Oluşturulma Tarihi: " + new Date());
            writer.println("Toplam Öğrenci Sayısı: " + ogrenciler.size());
            writer.println("Rapor Oluşturma Süresi: " + sure + " ms\n");
        }
    }
    
    public static void hashRaporuOlustur(String hashIcerik) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("hash_index.txt"))) {
            writer.println("=== HASH TABLOSU İÇERİĞİ ===");
            writer.println("Oluşturulma Tarihi: " + new Date());
            writer.println("\n" + hashIcerik);
        } catch (IOException e) {
            System.out.println("Hash raporu oluşturma hatası: " + e.getMessage());
        }
    }
}
