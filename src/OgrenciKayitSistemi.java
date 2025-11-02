import java.util.*;

public class OgrenciKayitSistemi {
    private HashTablo hashTablo;
    private boolean gelismisVeriTipi;
    // Çift liste desteği için diğer modun tablosu
    private HashTablo digerModTablo;
    
    public OgrenciKayitSistemi(boolean gelismisVeriTipi) {
        this.gelismisVeriTipi = gelismisVeriTipi;
        this.hashTablo = new HashTablo(gelismisVeriTipi);
        // Diğer modun tablosunu da oluştur
        this.digerModTablo = new HashTablo(!gelismisVeriTipi);
        verileriYukle();
    }
    
    private void verileriYukle() {
        List<Ogrenci> ogrenciler = DosyaIslemleri.ogrencileriDosyadanOku();
        if (ogrenciler.isEmpty()) {
            ogrenciler = DosyaIslemleri.baslangicVerisiOlustur();
        }
        for (Ogrenci ogr : ogrenciler) {
            hashTablo.ogrenciEkle(ogr);
            digerModTablo.ogrenciEkle(ogr); // Diğer moda da ekle
        }
    }
    
    // Manuel işlemler için performans takipli metodlar
    public boolean ogrenciEkle(Ogrenci ogr) {
        long baslangic = System.nanoTime();
        boolean sonuc = hashTablo.ogrenciEkle(ogr);
        long sure = System.nanoTime() - baslangic;
        
        if (sonuc) {
            // DİĞER MODA DA EKLE
            digerModTablo.ogrenciEkle(ogr);
            
            DosyaIslemleri.ogrencileriDosyayaYaz(hashTablo.tumOgrencileriGetir());
            
            // Manuel işlem performansını kaydet - HER İKİ MOD İÇİN
            RaporUretici.manuelIslemKaydet("Öğrenci Ekleme", sure / 1000, 
                (gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
                ogr.getIsim() + " " + ogr.getSoyad() + " (" + ogr.getOgrNo() + ")");
                
            // Diğer modun performansını da test et
            long digerBaslangic = System.nanoTime();
            digerModTablo.ogrenciEkle(ogr);
            long digerSure = System.nanoTime() - digerBaslangic;
            
            RaporUretici.manuelIslemKaydet("Öğrenci Ekleme", digerSure / 1000, 
                (!gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
                ogr.getIsim() + " " + ogr.getSoyad() + " (" + ogr.getOgrNo() + ")");
        }
        return sonuc;
    }
    
    public Ogrenci ogrenciNoIleAra(int ogrNo) {
        // Öğrenci numarası format kontrolü
        if (String.valueOf(ogrNo).length() != 9) {
            System.out.println("Hata: Geçersiz öğrenci numarası formatı! 9 haneli numara giriniz: " + ogrNo);
            return null;
        }
        
        long baslangic = System.nanoTime();
        Ogrenci sonuc = hashTablo.ogrenciNoIleBul(ogrNo);
        long sure = System.nanoTime() - baslangic;
        
        // Manuel işlem performansını kaydet - MEVCUT MOD
        RaporUretici.manuelIslemKaydet("Öğrenci Arama (No)", sure / 1000, 
            (gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
            "Aranan: " + ogrNo + " - " + (sonuc != null ? "Bulundu" : "Bulunamadı"));
        
        // DİĞER MODDA DA ARA VE PERFORMANS KAYDET
        long digerBaslangic = System.nanoTime();
        Ogrenci digerSonuc = digerModTablo.ogrenciNoIleBul(ogrNo);
        long digerSure = System.nanoTime() - digerBaslangic;
        
        RaporUretici.manuelIslemKaydet("Öğrenci Arama (No)", digerSure / 1000, 
            (!gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
            "Aranan: " + ogrNo + " - " + (digerSonuc != null ? "Bulundu" : "Bulunamadı"));
        
        return sonuc;
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
        
        // Manuel işlem performansını kaydet
        RaporUretici.manuelIslemKaydet("Öğrenci Arama (İsim)", sure / 1000, 
            (gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
            "Aranan: '" + isim + "' - Bulunan: " + sonuclar.size() + " öğrenci");
        
        return sonuclar;
    }
    
    public boolean ogrenciSil(int ogrNo) {
        // Öğrenci numarası format kontrolü
        if (String.valueOf(ogrNo).length() != 9) {
            System.out.println("Hata: Geçersiz öğrenci numarası formatı! 9 haneli numara giriniz: " + ogrNo);
            return false;
        }
        
        long baslangic = System.nanoTime();
        boolean sonuc = hashTablo.ogrenciSil(ogrNo);
        long sure = System.nanoTime() - baslangic;
        
        if (sonuc) {
            // DİĞER MODDAN DA SİL
            digerModTablo.ogrenciSil(ogrNo);
            
            DosyaIslemleri.ogrencileriDosyayaYaz(hashTablo.tumOgrencileriGetir());
            
            // Manuel işlem performansını kaydet - HER İKİ MOD İÇİN
            RaporUretici.manuelIslemKaydet("Öğrenci Silme", sure / 1000, 
                (gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
                "Silinen No: " + ogrNo);
                
            // Diğer modun performansını da test et
            long digerBaslangic = System.nanoTime();
            digerModTablo.ogrenciSil(ogrNo);
            long digerSure = System.nanoTime() - digerBaslangic;
            
            RaporUretici.manuelIslemKaydet("Öğrenci Silme", digerSure / 1000, 
                (!gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
                "Silinen No: " + ogrNo);
        }
        return sonuc;
    }
    
    public void ogrenciGuncelle(Ogrenci ogr) {
        long baslangic = System.nanoTime();
        hashTablo.ogrenciGuncelle(ogr);
        long sure = System.nanoTime() - baslangic;
        
        // DİĞER MODDA DA GÜNCELLE
        digerModTablo.ogrenciGuncelle(ogr);
        
        DosyaIslemleri.ogrencileriDosyayaYaz(hashTablo.tumOgrencileriGetir());
        
        // Manuel işlem performansını kaydet - HER İKİ MOD İÇİN
        RaporUretici.manuelIslemKaydet("Öğrenci Güncelleme", sure / 1000, 
            (gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
            ogr.getIsim() + " " + ogr.getSoyad() + " (" + ogr.getOgrNo() + ")");
            
        // Diğer modun performansını da test et
        long digerBaslangic = System.nanoTime();
        digerModTablo.ogrenciGuncelle(ogr);
        long digerSure = System.nanoTime() - digerBaslangic;
        
        RaporUretici.manuelIslemKaydet("Öğrenci Güncelleme", digerSure / 1000, 
            (!gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
            ogr.getIsim() + " " + ogr.getSoyad() + " (" + ogr.getOgrNo() + ")");
    }
    
    public List<Ogrenci> tumOgrencileriGetir() {
        long baslangic = System.nanoTime();
        List<Ogrenci> liste = hashTablo.tumOgrencileriGetir();
        liste.sort((o1, o2) -> Float.compare(o2.getGano(), o1.getGano()));
        long sure = System.nanoTime() - baslangic;
        
        // Manuel işlem performansını kaydet - MEVCUT MOD
        RaporUretici.manuelIslemKaydet("Tüm Öğrencileri Listeleme", sure / 1000, 
            (gelismisVeriTipi ? "Gelişmiş" : "Temel"), 
            "Listelenen: " + liste.size() + " öğrenci");
        
        // DİĞER MODUN PERFORMANSINI DA KAYDET
        long digerBaslangic = System.nanoTime();
        List<Ogrenci> digerListe = digerModTablo.tumOgrencileriGetir();
        digerListe.sort((o1, o2) -> Float.compare(o2.getGano(), o1.getGano()));
        long digerSure = System.nanoTime() - digerBaslangic;
        
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
    
    // Senkronizasyon kontrolü
    public boolean listelerSenkronizeMi() {
        return hashTablo.getToplamOgrenciSayisi() == digerModTablo.getToplamOgrenciSayisi();
    }
    
    public String senkronizasyonRaporu() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== LİSTE SENKRONİZASYON RAPORU ===\n");
        sb.append("Mevcut Mod (").append(gelismisVeriTipi ? "Gelişmiş" : "Temel").append("): ").append(hashTablo.getToplamOgrenciSayisi()).append(" öğrenci\n");
        sb.append("Diğer Mod (").append(!gelismisVeriTipi ? "Gelişmiş" : "Temel").append("): ").append(digerModTablo.getToplamOgrenciSayisi()).append(" öğrenci\n");
        sb.append("Senkronizasyon: ").append(listelerSenkronizeMi() ? "✓ BAŞARILI" : "✗ HATALI").append("\n");
        return sb.toString();
    }
    
    // EKSİK PERFORMANS TEST METOTLARI - BUNLARI EKLEYELİM
    
    public String performansTestiEkleme() {
        StringBuilder sonuc = new StringBuilder();
        sonuc.append("=== EKLEME PERFORMANS TESTİ ===\n\n");
        
        Random r = new Random();
        int testNo = 999000000 + r.nextInt(10000);
        // Öğrenci numarası format kontrolü
        while (String.valueOf(testNo).length() != 9) {
            testNo = 236000000 + r.nextInt(1_000_000);
        }
        
        Ogrenci testOgr = new Ogrenci("Test", "Öğrenci", testNo, 3.5f, 1, 'E');
        
        long baslangic = System.nanoTime();
        boolean eklemeSonuc = hashTablo.ogrenciEkle(testOgr);
        long sureMevcut = System.nanoTime() - baslangic;
        
        sonuc.append("MEVCUT MOD (" + (gelismisVeriTipi ? "Gelişmiş" : "Temel") + "):\n");
        sonuc.append("Ekleme süresi: " + (sureMevcut / 1000) + " mikrosaniye\n");
        sonuc.append("Başarı: " + (eklemeSonuc ? "EVET" : "HAYIR") + "\n");
        sonuc.append("Toplam öğrenci: " + getToplamOgrenciSayisi() + "\n\n");
        
        // Diğer mod için test (zaten digerModTablo'da mevcut)
        int digerTestNo = 999100000 + r.nextInt(10000);
        // Öğrenci numarası format kontrolü
        while (String.valueOf(digerTestNo).length() != 9) {
            digerTestNo = 236000000 + r.nextInt(1_000_000);
        }
        
        Ogrenci digerTestOgr = new Ogrenci("Test", "Öğrenci2", digerTestNo, 3.5f, 1, 'E');
        
        baslangic = System.nanoTime();
        boolean digerEklemeSonuc = digerModTablo.ogrenciEkle(digerTestOgr);
        long sureDiger = System.nanoTime() - baslangic;
        
        sonuc.append("DİĞER MOD (" + (!gelismisVeriTipi ? "Gelişmiş" : "Temel") + "):\n");
        sonuc.append("Ekleme süresi: " + (sureDiger / 1000) + " mikrosaniye\n");
        sonuc.append("Başarı: " + (digerEklemeSonuc ? "EVET" : "HAYIR") + "\n");
        sonuc.append("Toplam öğrenci: " + digerModTablo.getToplamOgrenciSayisi() + "\n\n");
        
        RaporUretici.performansKaydetMikro("Ekleme Testi - " + (gelismisVeriTipi ? "Gelişmiş" : "Temel") + " Mod", sureMevcut / 1000);
        RaporUretici.performansKaydetMikro("Ekleme Testi - " + (!gelismisVeriTipi ? "Gelişmiş" : "Temel") + " Mod", sureDiger / 1000);
        
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
        Ogrenci bulunan = hashTablo.ogrenciNoIleBul(arananNo);
        long sureMevcut = System.nanoTime() - baslangic;
        
        sonuc.append("MEVCUT MOD (" + (gelismisVeriTipi ? "Gelişmiş" : "Temel") + "):\n");
        sonuc.append("Arama süresi: " + (sureMevcut / 1000) + " mikrosaniye\n");
        sonuc.append("Bulunan: " + (bulunan != null ? bulunan.getIsim() + " " + bulunan.getSoyad() : "YOK") + "\n");
        sonuc.append("Aranan numara: " + arananNo + "\n\n");
        
        HashTablo digerMod = new HashTablo(!gelismisVeriTipi);
        for (Ogrenci ogr : ogrenciler) {
            digerMod.ogrenciEkle(ogr);
        }
        
        baslangic = System.nanoTime();
        Ogrenci digerBulunan = digerMod.ogrenciNoIleBul(arananNo);
        long sureDiger = System.nanoTime() - baslangic;
        
        sonuc.append("DİĞER MOD (" + (!gelismisVeriTipi ? "Gelişmiş" : "Temel") + "):\n");
        sonuc.append("Arama süresi: " + (sureDiger / 1000) + " mikrosaniye\n");
        sonuc.append("Bulunan: " + (digerBulunan != null ? digerBulunan.getIsim() + " " + digerBulunan.getSoyad() : "YOK") + "\n");
        sonuc.append("Aranan numara: " + arananNo + "\n\n");
        
        RaporUretici.performansKaydetMikro("Arama Testi - " + (gelismisVeriTipi ? "Gelişmiş" : "Temel") + " Mod", sureMevcut / 1000);
        RaporUretici.performansKaydetMikro("Arama Testi - " + (!gelismisVeriTipi ? "Gelişmiş" : "Temel") + " Mod", sureDiger / 1000);
        
        return sonuc.toString();
    }
    
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
        boolean silmeSonuc = hashTablo.ogrenciSil(silinecekNo);
        long sureMevcut = System.nanoTime() - baslangic;
        
        sonuc.append("MEVCUT MOD (" + (gelismisVeriTipi ? "Gelişmiş" : "Temel") + "):\n");
        sonuc.append("Silme süresi: " + (sureMevcut / 1000) + " mikrosaniye\n");
        sonuc.append("Başarı: " + (silmeSonuc ? "EVET" : "HAYIR") + "\n");
        sonuc.append("Silinen öğrenci: " + silinecekOgr.getIsim() + " " + silinecekOgr.getSoyad() + "\n");
        sonuc.append("Kalan öğrenci: " + getToplamOgrenciSayisi() + "\n\n");
        
        if (silmeSonuc) {
            hashTablo.ogrenciEkle(silinecekOgr);
        }
        
        HashTablo digerMod = new HashTablo(!gelismisVeriTipi);
        for (Ogrenci ogr : hashTablo.tumOgrencileriGetir()) {
            digerMod.ogrenciEkle(ogr);
        }
        
        baslangic = System.nanoTime();
        boolean digerSilmeSonuc = digerMod.ogrenciSil(silinecekNo);
        long sureDiger = System.nanoTime() - baslangic;
        
        sonuc.append("DİĞER MOD (" + (!gelismisVeriTipi ? "Gelişmiş" : "Temel") + "):\n");
        sonuc.append("Silme süresi: " + (sureDiger / 1000) + " mikrosaniye\n");
        sonuc.append("Başarı: " + (digerSilmeSonuc ? "EVET" : "HAYIR") + "\n");
        sonuc.append("Silinen öğrenci: " + silinecekOgr.getIsim() + " " + silinecekOgr.getSoyad() + "\n");
        sonuc.append("Kalan öğrenci: " + digerMod.getToplamOgrenciSayisi() + "\n\n");
        
        RaporUretici.performansKaydetMikro("Silme Testi - " + (gelismisVeriTipi ? "Gelişmiş" : "Temel") + " Mod", sureMevcut / 1000);
        RaporUretici.performansKaydetMikro("Silme Testi - " + (!gelismisVeriTipi ? "Gelişmiş" : "Temel") + " Mod", sureDiger / 1000);
        
        return sonuc.toString();
    }
    
    public String performansTestiListeleme() {
        StringBuilder sonuc = new StringBuilder();
        sonuc.append("=== LİSTELEME PERFORMANS TESTİ ===\n\n");
        
        long baslangic = System.nanoTime();
        List<Ogrenci> liste = hashTablo.tumOgrencileriGetir();
        liste.sort((o1, o2) -> Float.compare(o2.getGano(), o1.getGano()));
        long sureMevcut = System.nanoTime() - baslangic;
        
        sonuc.append("MEVCUT MOD (" + (gelismisVeriTipi ? "Gelişmiş" : "Temel") + "):\n");
        sonuc.append("Listeleme süresi: " + (sureMevcut / 1000) + " mikrosaniye\n");
        sonuc.append("Listelenen öğrenci sayısı: " + liste.size() + "\n\n");
        
        HashTablo digerMod = new HashTablo(!gelismisVeriTipi);
        List<Ogrenci> ogrenciler = hashTablo.tumOgrencileriGetir();
        for (Ogrenci ogr : ogrenciler) {
            digerMod.ogrenciEkle(ogr);
        }
        
        baslangic = System.nanoTime();
        List<Ogrenci> digerListe = digerMod.tumOgrencileriGetir();
        digerListe.sort((o1, o2) -> Float.compare(o2.getGano(), o1.getGano()));
        long sureDiger = System.nanoTime() - baslangic;
        
        sonuc.append("DİĞER MOD (" + (!gelismisVeriTipi ? "Gelişmiş" : "Temel") + "):\n");
        sonuc.append("Listeleme süresi: " + (sureDiger / 1000) + " mikrosaniye\n");
        sonuc.append("Listelenen öğrenci sayısı: " + digerListe.size() + "\n\n");
        
        RaporUretici.performansKaydetMikro("Listeleme Testi - " + (gelismisVeriTipi ? "Gelişmiş" : "Temel") + " Mod", sureMevcut / 1000);
        RaporUretici.performansKaydetMikro("Listeleme Testi - " + (!gelismisVeriTipi ? "Gelişmiş" : "Temel") + " Mod", sureDiger / 1000);
        
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
        sonuc.append("  - Daha hızlı arama (O(1) ortalama)\n");
        sonuc.append("  - Daha hızlı ekleme (O(1) ortalama)\n");
        sonuc.append("  - Daha hızlı silme (O(1) ortalama)\n");
        sonuc.append("  - Daha fazla bellek kullanımı\n\n");
        
        sonuc.append("Temel Mod (Linear Probing): \n");
        sonuc.append("  - Daha yavaş arama (O(n) en kötü durum)\n");
        sonuc.append("  - Daha yavaş ekleme (O(n) en kötü durum)\n");
        sonuc.append("  - Çakışmalar olabilir\n");
        sonuc.append("  - Daha az bellek kullanımı\n\n");
        
        sonuc.append("Öneri: Büyük veri setleri için Gelişmiş Mod kullanılmalıdır.\n");
        
        return sonuc.toString();
    }
}