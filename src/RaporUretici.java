import java.io.*;
import java.util.*;

public class RaporUretici {
    
    public static void raporlariOlustur(List<Ogrenci> ogrenciler) {
        try {
            long baslangic = System.currentTimeMillis();
            
            // 1. GANO sıralı rapor
            List<Ogrenci> ganoSirali = new ArrayList<>(ogrenciler);
            ganoSirali.sort((o1, o2) -> Float.compare(o2.getGano(), o1.getGano()));
            dosyayaYaz("ogrenciler_gano_sirali.txt", ganoSirali, "GANO'ya göre sıralı tüm öğrenciler");
            
            // 2. Sınıf sıralı rapor
            List<Ogrenci> sinifSirali = new ArrayList<>(ogrenciler);
            sinifSirali.sort(Comparator.comparingInt(Ogrenci::getSinif));
            dosyayaYaz("sinif_sirali.txt", sinifSirali, "Sınıfa göre sıralı öğrenciler");
            
            // 3. Öğrenci no sıralı rapor
            List<Ogrenci> noSirali = new ArrayList<>(ogrenciler);
            noSirali.sort(Comparator.comparingInt(Ogrenci::getOgrNo));
            dosyayaYaz("ogrenci_no_sirali.txt", noSirali, "Öğrenci numarasına göre sıralı öğrenciler");
            
            // 4. Bölüm sıralı rapor
            List<Ogrenci> bolumSirali = new ArrayList<>(ogrenciler);
            bolumSirali.sort((o1, o2) -> {
                int bolumKarsilastirma = Integer.compare(o1.getOgrNo() / 1000000, o2.getOgrNo() / 1000000);
                if (bolumKarsilastirma != 0) return bolumKarsilastirma;
                return Float.compare(o2.getGano(), o1.getGano());
            });
            dosyayaYaz("bolum_sirali.txt", bolumSirali, "Bölüme göre sıralı öğrenciler");
            
            // 5. Cinsiyet sıralı rapor
            List<Ogrenci> cinsiyetSirali = new ArrayList<>(ogrenciler);
            cinsiyetSirali.sort((o1, o2) -> {
                int cinsiyetKarsilastirma = Character.compare(o1.getCinsiyet(), o2.getCinsiyet());
                if (cinsiyetKarsilastirma != 0) return cinsiyetKarsilastirma;
                return Float.compare(o2.getGano(), o1.getGano());
            });
            dosyayaYaz("cinsiyet_sirali.txt", cinsiyetSirali, "Cinsiyete göre sıralı öğrenciler");
            
            // 6. İsim sıralı rapor
            List<Ogrenci> isimSirali = new ArrayList<>(ogrenciler);
            isimSirali.sort((o1, o2) -> {
                int isimKarsilastirma = o1.getIsim().compareTo(o2.getIsim());
                if (isimKarsilastirma != 0) return isimKarsilastirma;
                return o1.getSoyad().compareTo(o2.getSoyad());
            });
            dosyayaYaz("isim_sirali.txt", isimSirali, "İsime göre sıralı öğrenciler");

            long sure = System.currentTimeMillis() - baslangic;
            performansKaydetMs("6 rapor dosyası oluşturma", sure);
            
            System.out.println("✅ 6 rapor dosyası başarıyla oluşturuldu!");
            
        } catch (IOException e) {
            System.out.println("Rapor oluşturma hatası: " + e.getMessage());
        }
    }

    public static void hashRaporuOlustur(String hashBilgisi) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("hash_tablosu.txt"))) {
            writer.println("=== HASH TABLOSU RAPORU ===");
            writer.println("Tarih: " + new Date());
            writer.println(hashBilgisi);
            System.out.println("✅ Hash tablosu raporu oluşturuldu!");
        } catch (IOException e) {
            System.out.println("Hash raporu oluşturma hatası: " + e.getMessage());
        }
    }

    private static void dosyayaYaz(String dosyaAdi, List<Ogrenci> ogrenciler, String baslik) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(dosyaAdi))) {
            writer.println("=== " + baslik + " ===");
            writer.println("Tarih: " + new Date());
            writer.println("Toplam Öğrenci: " + ogrenciler.size());
            writer.printf("%-12s %-12s %-12s %-8s %-6s %s\n", "İsim", "Soyad", "No", "GANO", "Sınıf", "Cinsiyet");
            writer.println("----------------------------------------------------------------");
            for (Ogrenci o : ogrenciler) {
                writer.printf("%-12s %-12s %-12d %-8.2f %-6d %c\n", 
                    o.getIsim(), o.getSoyad(), o.getOgrNo(), o.getGano(), o.getSinif(), o.getCinsiyet());
            }
            writer.println("\nToplam: " + ogrenciler.size() + " öğrenci");
        }
    }

    // Milisaniye cinsinden kayıt
    public static void performansKaydetMs(String islem, long sureMs) {
        try (FileWriter fw = new FileWriter("performans.txt", true)) {
            fw.write(String.format("[%s] %s süresi: %d ms%n", 
                new Date(), islem, sureMs));
        } catch (IOException e) {
            System.out.println("Performans yazma hatası: " + e.getMessage());
        }
    }
    
    // Mikrosaniye cinsinden kayıt
    public static void performansKaydetMikro(String islem, long mikrosaniye) {
        try (FileWriter fw = new FileWriter("performans_detayli.txt", true)) {
            fw.write(String.format("[%s] %s: %d μs%n", 
                new Date(), islem, mikrosaniye));
        } catch (IOException e) {
            System.out.println("Detaylı performans yazma hatası: " + e.getMessage());
        }
    }
    
    // Manuel işlemler için performans kaydı
    public static void manuelIslemKaydet(String islem, long mikrosaniye, String mod, String detay) {
        try (FileWriter fw = new FileWriter("manuel_performans.txt", true)) {
            fw.write(String.format("[%s] %s - %s Mod: %d μs | %s%n", 
                new Date(), islem, mod, mikrosaniye, detay));
        } catch (IOException e) {
            System.out.println("Manuel performans yazma hatası: " + e.getMessage());
        }
    }
}