import java.util.*;

public class OgrenciKayitSistemi {
    private HashTablo hashTablo;
    private boolean gelismisVeriTipi;
    private HashTablo digerModTablo;
    
    public OgrenciKayitSistemi(boolean gelismisVeriTipi) {
        this.gelismisVeriTipi = gelismisVeriTipi;
        this.hashTablo = new HashTablo(gelismisVeriTipi);
        this.digerModTablo = new HashTablo(!gelismisVeriTipi);
        verileriYukle();
    }
    
    private void verileriYukle() {
        List<Ogrenci> ogrenciler = DosyaIslemleri.ogrencileriDosyadanOku();
        hashTablo.tumOgrencileriYukle(ogrenciler);
        digerModTablo.tumOgrencileriYukle(ogrenciler);
    }
    
    // SENKRONİZE ÖĞRENCİ EKLEME
    public boolean ogrenciEkle(Ogrenci ogr) {
        long baslangic = System.nanoTime();
        boolean mevcutSonuc = hashTablo.ogrenciEkle(ogr);
        long mevcutSure = System.nanoTime() - baslangic;
        
        long digerBaslangic = System.nanoTime();
        boolean digerSonuc = digerModTablo.ogrenciEkle(ogr);
        long digerSure = System.nanoTime() - digerBaslangic;
        
        if (mevcutSonuc && digerSonuc) {
            DosyaIslemleri.ogrencileriDosyayaYaz(hashTablo.tumOgrencileriGetir());
            
            // Performans kayıtları
            RaporUretici.manuelIslemKaydet("Öğrenci Ekleme", mevcutSure / 1000, 
                (gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
                ogr.getIsim() + " " + ogr.getSoyad() + " (" + ogr.getOgrNo() + ")");
                
            RaporUretici.manuelIslemKaydet("Öğrenci Ekleme", digerSure / 1000, 
                (!gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
                ogr.getIsim() + " " + ogr.getSoyad() + " (" + ogr.getOgrNo() + ")");
                
            return true;
        } else {
            // Senkronizasyon hatası - geri al
            if (mevcutSonuc) hashTablo.ogrenciSil(ogr.getOgrNo());
            if (digerSonuc) digerModTablo.ogrenciSil(ogr.getOgrNo());
            return false;
        }
    }
    
    public Ogrenci ogrenciNoIleAra(int ogrNo) {
        if (String.valueOf(ogrNo).length() != 9) {
            System.out.println("Hata: Geçersiz öğrenci numarası formatı! 9 haneli numara giriniz: " + ogrNo);
            return null;
        }
        
        long baslangic = System.nanoTime();
        Ogrenci mevcutSonuc = hashTablo.ogrenciNoIleBul(ogrNo);
        long mevcutSure = System.nanoTime() - baslangic;
        
        long digerBaslangic = System.nanoTime();
        Ogrenci digerSonuc = digerModTablo.ogrenciNoIleBul(ogrNo);
        long digerSure = System.nanoTime() - digerBaslangic;
        
        // Performans kayıtları
        RaporUretici.manuelIslemKaydet("Öğrenci Arama (No)", mevcutSure / 1000, 
            (gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
            "Aranan: " + ogrNo + " - " + (mevcutSonuc != null ? "Bulundu" : "Bulunamadı"));
        
        RaporUretici.manuelIslemKaydet("Öğrenci Arama (No)", digerSure / 1000, 
            (!gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
            "Aranan: " + ogrNo + " - " + (digerSonuc != null ? "Bulundu" : "Bulunamadı"));
        
        return mevcutSonuc;
    }
    
    public List<Ogrenci> adIleAra(String isim) {
        long baslangic = System.nanoTime();
        List<Ogrenci> sonuclar = new ArrayList<>();
        for (Ogrenci ogr : hashTablo.tumOgrencileriGetir()) {
            if (ogr.getIsim().equalsIgnoreCase(isim)) {
                sonuclar.add(ogr);
            }
        }
        sonuclar.sort((o1, o2) -> Float.compare(o2.getGano(), o1.getGano()));
        long sure = System.nanoTime() - baslangic;
        
        RaporUretici.manuelIslemKaydet("Öğrenci Arama (İsim)", sure / 1000, 
            (gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
            "Aranan: '" + isim + "' - Bulunan: " + sonuclar.size() + " öğrenci");
        
        return sonuclar;
    }
    
    // SENKRONİZE ÖĞRENCİ SİLME
    public boolean ogrenciSil(int ogrNo) {
        if (String.valueOf(ogrNo).length() != 9) {
            System.out.println("Hata: Geçersiz öğrenci numarası formatı! 9 haneli numara giriniz: " + ogrNo);
            return false;
        }
        
        long baslangic = System.nanoTime();
        boolean mevcutSonuc = hashTablo.ogrenciSil(ogrNo);
        long mevcutSure = System.nanoTime() - baslangic;
        
        long digerBaslangic = System.nanoTime();
        boolean digerSonuc = digerModTablo.ogrenciSil(ogrNo);
        long digerSure = System.nanoTime() - digerBaslangic;
        
        if (mevcutSonuc && digerSonuc) {
            DosyaIslemleri.ogrencileriDosyayaYaz(hashTablo.tumOgrencileriGetir());
            
            RaporUretici.manuelIslemKaydet("Öğrenci Silme", mevcutSure / 1000, 
                (gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
                "Silinen No: " + ogrNo);
                
            RaporUretici.manuelIslemKaydet("Öğrenci Silme", digerSure / 1000, 
                (!gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
                "Silinen No: " + ogrNo);
                
            return true;
        } else {
            // Senkronizasyon hatası - geri al
            Ogrenci ogr = (mevcutSonuc ? null : hashTablo.ogrenciNoIleBul(ogrNo));
            if (ogr != null) {
                hashTablo.ogrenciEkle(ogr);
                digerModTablo.ogrenciEkle(ogr);
            }
            return false;
        }
    }
    
    // SENKRONİZE ÖĞRENCİ GÜNCELLEME
    public void ogrenciGuncelle(Ogrenci ogr) {
        long baslangic = System.nanoTime();
        hashTablo.ogrenciGuncelle(ogr);
        long mevcutSure = System.nanoTime() - baslangic;
        
        long digerBaslangic = System.nanoTime();
        digerModTablo.ogrenciGuncelle(ogr);
        long digerSure = System.nanoTime() - digerBaslangic;
        
        DosyaIslemleri.ogrencileriDosyayaYaz(hashTablo.tumOgrencileriGetir());
        
        RaporUretici.manuelIslemKaydet("Öğrenci Güncelleme", mevcutSure / 1000, 
            (gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
            ogr.getIsim() + " " + ogr.getSoyad() + " (" + ogr.getOgrNo() + ")");
            
        RaporUretici.manuelIslemKaydet("Öğrenci Güncelleme", digerSure / 1000, 
            (!gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
            ogr.getIsim() + " " + ogr.getSoyad() + " (" + ogr.getOgrNo() + ")");
    }
    
    public List<Ogrenci> tumOgrencileriGetir() {
        long baslangic = System.nanoTime();
        List<Ogrenci> liste = hashTablo.tumOgrencileriGetir();
        liste.sort((o1, o2) -> Float.compare(o2.getGano(), o1.getGano()));
        long mevcutSure = System.nanoTime() - baslangic;
        
        long digerBaslangic = System.nanoTime();
        List<Ogrenci> digerListe = digerModTablo.tumOgrencileriGetir();
        digerListe.sort((o1, o2) -> Float.compare(o2.getGano(), o1.getGano()));
        long digerSure = System.nanoTime() - digerBaslangic;
        
        RaporUretici.manuelIslemKaydet("Tüm Öğrencileri Listeleme", mevcutSure / 1000, 
            (gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
            "Listelenen: " + liste.size() + " öğrenci");
        
        RaporUretici.manuelIslemKaydet("Tüm Öğrencileri Listeleme", digerSure / 1000, 
            (!gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
            "Listelenen: " + digerListe.size() + " öğrenci");
        
        return liste;
    }
    
    public List<Ogrenci> sinifaGoreGetir(int sinif) {
        List<Ogrenci> sonuc = new ArrayList<>();
        for (Ogrenci ogr : hashTablo.tumOgrencileriGetir()) {
            if (ogr.getSinif() == sinif) {
                sonuc.add(ogr);
            }
        }
        sonuc.sort((o1, o2) -> Float.compare(o2.getGano(), o1.getGano()));
        return sonuc;
    }
    
    public List<Ogrenci> cinsiyeteGoreGetir(char cinsiyet) {
        List<Ogrenci> sonuc = new ArrayList<>();
        for (Ogrenci ogr : hashTablo.tumOgrencileriGetir()) {
            if (ogr.getCinsiyet() == cinsiyet) {
                sonuc.add(ogr);
            }
        }
        sonuc.sort((o1, o2) -> Float.compare(o2.getGano(), o1.getGano()));
        return sonuc;
    }
    
    public String hashTablosunuGoster() {
        return hashTablo.hashTablosunuGoster();
    }
    
    public void raporlariOlustur() {
        RaporUretici.raporlariOlustur(hashTablo.tumOgrencileriGetir());
        RaporUretici.hashRaporuOlustur(hashTablo.hashTablosunuGoster());
    }
    
    public int getToplamOgrenciSayisi() {
        return hashTablo.getToplamOgrenciSayisi();
    }
    
    // SENKRONİZASYON KONTROLÜ
    public boolean listelerSenkronizeMi() {
        List<Ogrenci> mevcutListe = hashTablo.tumOgrencileriGetir();
        List<Ogrenci> digerListe = digerModTablo.tumOgrencileriGetir();
        
        if (mevcutListe.size() != digerListe.size()) return false;
        
        Set<Integer> mevcutNumaralar = new HashSet<>();
        for (Ogrenci ogr : mevcutListe) mevcutNumaralar.add(ogr.getOgrNo());
        
        for (Ogrenci ogr : digerListe) {
            if (!mevcutNumaralar.contains(ogr.getOgrNo())) return false;
        }
        
        return true;
    }
    
    public String senkronizasyonRaporu() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== LİSTE SENKRONİZASYON RAPORU ===\n");
        sb.append("Mevcut Mod (").append(gelismisVeriTipi ? "Gelişmiş" : "Temel").append("): ").append(hashTablo.getToplamOgrenciSayisi()).append(" öğrenci\n");
        sb.append("Diğer Mod (").append(!gelismisVeriTipi ? "Gelişmiş" : "Temel").append("): ").append(digerModTablo.getToplamOgrenciSayisi()).append(" öğrenci\n");
        sb.append("Senkronizasyon: ").append(listelerSenkronizeMi() ? "✓ BAŞARILI" : "✗ HATALI").append("\n");
        
        if (!listelerSenkronizeMi()) {
            sb.append("\n⚠️ SENKRONİZASYON UYARISI:\n");
            sb.append("İki mod arasında veri tutarsızlığı tespit edildi!\n");
            sb.append("Lütfen sistem yöneticisi ile iletişime geçin.\n");
        }
        
        return sb.toString();
    }
    
    // BAŞLANGIÇ VERİLERİNE DÖN
    public void baslangicVerilerineDon() {
        DosyaIslemleri.baslangicVerilerineDon();
        verileriYukle();
    }
    
    // EKSİK PERFORMANS TEST METOTLARI - EKLENDİ
    
    public String performansTestiEkleme() {
        StringBuilder sonuc = new StringBuilder();
        sonuc.append("=== EKLEME PERFORMANS TESTİ ===\n\n");
        
        Random r = new Random();
        int testNo = 999000000 + r.nextInt(10000);
        while (String.valueOf(testNo).length() != 9) {
            testNo = 236000000 + r.nextInt(1_000_000);
        }
        
        Ogrenci testOgr = new Ogrenci("Test", "Öğrenci", testNo, 3.5f, 1, 'E');
        
        long baslangic = System.nanoTime();
        boolean eklemeSonuc = ogrenciEkle(testOgr);
        long sure = System.nanoTime() - baslangic;
        
        sonuc.append("SENKRONİZE EKLEME TESTİ:\n");
        sonuc.append("Toplam süre: ").append(sure / 1000).append(" mikrosaniye\n");
        sonuc.append("Başarı: ").append(eklemeSonuc ? "EVET" : "HAYIR").append("\n");
        sonuc.append("Toplam öğrenci: ").append(getToplamOgrenciSayisi()).append("\n");
        sonuc.append("Senkronizasyon: ").append(listelerSenkronizeMi() ? "✓ BAŞARILI" : "✗ HATALI").append("\n\n");
        
        return sonuc.toString();
    }
    
    public String performansTestiArama() {
        StringBuilder sonuc = new StringBuilder();
        sonuc.append("=== ARAMA PERFORMANS TESTİ ===\n\n");
        
        List<Ogrenci> ogrenciler = hashTablo.tumOgrencileriGetir();
        if (ogrenciler.isEmpty()) {
            return "Test için yeterli öğrenci yok!\n";
        }
        
        Random r = new Random();
        Ogrenci rastgeleOgr = ogrenciler.get(r.nextInt(ogrenciler.size()));
        int arananNo = rastgeleOgr.getOgrNo();
        
        long baslangic = System.nanoTime();
        Ogrenci bulunan = ogrenciNoIleAra(arananNo);
        long sure = System.nanoTime() - baslangic;
        
        sonuc.append("ÇİFT MOD ARAMA TESTİ:\n");
        sonuc.append("Toplam süre: ").append(sure / 1000).append(" mikrosaniye\n");
        sonuc.append("Bulunan: ").append(bulunan != null ? bulunan.getIsim() + " " + bulunan.getSoyad() : "YOK").append("\n");
        sonuc.append("Aranan numara: ").append(arananNo).append("\n");
        sonuc.append("Senkronizasyon: ").append(listelerSenkronizeMi() ? "✓ BAŞARILI" : "✗ HATALI").append("\n\n");
        
        return sonuc.toString();
    }
    
    // EKSİK METOT: performansTestiSilme
    public String performansTestiSilme() {
        StringBuilder sonuc = new StringBuilder();
        sonuc.append("=== SİLME PERFORMANS TESTİ ===\n\n");
        
        List<Ogrenci> ogrenciler = hashTablo.tumOgrencileriGetir();
        if (ogrenciler.size() < 2) {
            return "Test için yeterli öğrenci yok!\n";
        }
        
        Random r = new Random();
        int index = r.nextInt(ogrenciler.size() - 1);
        Ogrenci silinecekOgr = ogrenciler.get(index);
        int silinecekNo = silinecekOgr.getOgrNo();
        
        long baslangic = System.nanoTime();
        boolean silmeSonuc = ogrenciSil(silinecekNo);
        long sure = System.nanoTime() - baslangic;
        
        sonuc.append("SENKRONİZE SİLME TESTİ:\n");
        sonuc.append("Toplam süre: ").append(sure / 1000).append(" mikrosaniye\n");
        sonuc.append("Başarı: ").append(silmeSonuc ? "EVET" : "HAYIR").append("\n");
        sonuc.append("Silinen öğrenci: ").append(silinecekOgr.getIsim()).append(" ").append(silinecekOgr.getSoyad()).append("\n");
        sonuc.append("Kalan öğrenci: ").append(getToplamOgrenciSayisi()).append("\n");
        sonuc.append("Senkronizasyon: ").append(listelerSenkronizeMi() ? "✓ BAŞARILI" : "✗ HATALI").append("\n\n");
        
        // Test öğrencisini geri ekleyelim ki sistem bozulmasın
        if (silmeSonuc) {
            ogrenciEkle(silinecekOgr);
        }
        
        return sonuc.toString();
    }
    
    // EKSİK METOT: performansTestiListeleme
    public String performansTestiListeleme() {
        StringBuilder sonuc = new StringBuilder();
        sonuc.append("=== LİSTELEME PERFORMANS TESTİ ===\n\n");
        
        long baslangic = System.nanoTime();
        List<Ogrenci> liste = tumOgrencileriGetir();
        long sure = System.nanoTime() - baslangic;
        
        sonuc.append("SENKRONİZE LİSTELEME TESTİ:\n");
        sonuc.append("Toplam süre: ").append(sure / 1000).append(" mikrosaniye\n");
        sonuc.append("Listelenen öğrenci sayısı: ").append(liste.size()).append("\n");
        sonuc.append("Senkronizasyon: ").append(listelerSenkronizeMi() ? "✓ BAŞARILI" : "✗ HATALI").append("\n\n");
        
        return sonuc.toString();
    }
    
    public String tumPerformansTestleriniCalistir() {
        StringBuilder sonuc = new StringBuilder();
        sonuc.append("=== TÜM PERFORMANS TESTLERİ ===\n\n");
        
        sonuc.append(performansTestiEkleme());
        sonuc.append(performansTestiArama());
        sonuc.append(performansTestiSilme());
        sonuc.append(performansTestiListeleme());
        
        sonuc.append("=== PERFORMANS KARŞILAŞTIRMASI ===\n");
        sonuc.append("Gelişmiş Mod (HashMap): \n");
        sonuc.append("  - Ortalama O(1) işlem süresi\n");
        sonuc.append("  - Daha hızlı arama ve ekleme\n");
        sonuc.append("  - Daha fazla bellek kullanımı\n\n");
        
        sonuc.append("Temel Mod (Linear Probing): \n");
        sonuc.append("  - Ortalama O(n) işlem süresi\n");
        sonuc.append("  - Çakışmalar olabilir\n");
        sonuc.append("  - Daha az bellek kullanımı\n\n");
        
        sonuc.append("Öneri: Büyük veri setleri için Gelişmiş Mod tercih edilmelidir.\n");
        
        return sonuc.toString();
    }
}