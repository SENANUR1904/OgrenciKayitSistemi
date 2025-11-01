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
            noSirali.sort((o1, o2) -> Integer.compare(o1.getOgrNo(), o2.getOgrNo()));
            dosyayaYaz("ogrenci_no_sirali.txt", noSirali, "ÖĞRENCİ NO SIRASI");
            
            dosyayaYaz("bolum_sirasi.txt", ganoSirali, "BÖLÜM SIRASI (GANO'ya göre)");
            
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
                    ogr.getIsim(), ogr.getSoyad(), ogr.getOgrNo(), 
                    ogr.getGano(), ogr.getSinif(), 
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
            
            writer.println("=== SINIF DAĞILIMI ===");
            for (int i = 1; i <= 4; i++) {
                final int sinif = i;
                long sayi = ogrenciler.stream().filter(o -> o.getSinif() == sinif).count();
                writer.println(sinif + ". Sınıf: " + sayi + " öğrenci");
            }
            
            writer.println("\n=== CİNSİYET DAĞILIMI ===");
            long erkek = ogrenciler.stream().filter(o -> o.getCinsiyet() == 'E').count();
            long kiz = ogrenciler.stream().filter(o -> o.getCinsiyet() == 'K').count();
            writer.println("Erkek: " + erkek + " öğrenci");
            writer.println("Kız: " + kiz + " öğrenci");
            
            writer.println("\n=== GANO İSTATİSTİKLERİ ===");
            double ortalama = ogrenciler.stream().mapToDouble(Ogrenci::getGano).average().orElse(0);
            double max = ogrenciler.stream().mapToDouble(Ogrenci::getGano).max().orElse(0);
            double min = ogrenciler.stream().mapToDouble(Ogrenci::getGano).min().orElse(0);
            writer.printf("Ortalama GANO: %.2f\n", ortalama);
            writer.printf("En Yüksek GANO: %.2f\n", max);
            writer.printf("En Düşük GANO: %.2f\n", min);
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