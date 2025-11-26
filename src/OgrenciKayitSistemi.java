import java.util.*;

public class OgrenciKayitSistemi {
    private HashTablo hashTablo;
    public boolean gelismisVeriTipi;
    private HashTablo digerModTablo;

    public OgrenciKayitSistemi(boolean gelismisVeriTipi) {
        this.gelismisVeriTipi = gelismisVeriTipi;
        this.hashTablo = new HashTablo(gelismisVeriTipi);
        this.digerModTablo = new HashTablo(!gelismisVeriTipi);
        verileriYukle();
    }

    public void setGelismisVeriTipi(boolean gelismisVeriTipi) {
        this.gelismisVeriTipi = gelismisVeriTipi;
    }

    private void verileriYukle() {
        List<Ogrenci> ogrenciler = DosyaIslemleri.ogrencileriDosyadanOku();
        hashTablo.tumOgrencileriYukle(ogrenciler);
        digerModTablo.tumOgrencileriYukle(ogrenciler);
    }

    public boolean ogrenciEkle(Ogrenci ogr) {
        if (ogr == null) return false;


        boolean mevcutSonuc = hashTablo.ogrenciEkle(ogr);
        boolean digerSonuc = digerModTablo.ogrenciEkle(ogr);

        if (mevcutSonuc && digerSonuc) {

            DosyaIslemleri.ogrencileriDosyayaYaz(hashTablo.tumOgrencileriGetir());
            return true;
        } else {

            if (mevcutSonuc) hashTablo.ogrenciSil(ogr.getOgrNo());
            if (digerSonuc) digerModTablo.ogrenciSil(ogr.getOgrNo());
            return false;
        }
    }


    public void ogrenciGuncelle(Ogrenci ogr) {
        if (ogr == null) return;


        hashTablo.ogrenciGuncelle(ogr);
        digerModTablo.ogrenciGuncelle(ogr);


        DosyaIslemleri.ogrencileriDosyayaYaz(hashTablo.tumOgrencileriGetir());
    }


    public Ogrenci ogrenciNoIleAra(int ogrNo) {
        if (String.valueOf(ogrNo).length() != 9) {
            System.out.println("Hata: Geçersiz öğrenci numarası formatı! 9 haneli numara giriniz: " + ogrNo);
            return null;
        }
        if(gelismisVeriTipi){

            return hashTablo.ogrenciNoIleBul(ogrNo);

        }else{
            return digerModTablo.ogrenciNoIleBul(ogrNo);
        }

    }

    public List<Ogrenci> adIleAra(String isim) {
        List<Ogrenci> sonuclar = new ArrayList<>();
        if(gelismisVeriTipi){
            for (Ogrenci ogr : hashTablo.tumOgrencileriGetir()) {
                if (ogr.getIsim().equalsIgnoreCase(isim)) {
                    sonuclar.add(ogr);
                }
            }

        }else{
            for (Ogrenci ogr : digerModTablo.tumOgrencileriGetir()) {
                if (ogr.getIsim().equalsIgnoreCase(isim)) {
                    sonuclar.add(ogr);
                }
            }

        }



        sonuclar.sort((o1, o2) -> Float.compare(o2.getGano(), o1.getGano()));

        return sonuclar;
    }


    public boolean ogrenciSil(int ogrNo) {
        if (String.valueOf(ogrNo).length() != 9) {
            System.out.println("Hata: Geçersiz öğrenci numarası formatı! 9 haneli numara giriniz: " + ogrNo);
            return false;
        }

        boolean mevcutSonuc = hashTablo.ogrenciSil(ogrNo);
        boolean digerSonuc = digerModTablo.ogrenciSil(ogrNo);

        if (mevcutSonuc && digerSonuc) {
            DosyaIslemleri.ogrencileriDosyayaYaz(hashTablo.tumOgrencileriGetir());
            return true;
        } else {

            if (mevcutSonuc) {
                // Mevcut modda silinmişse diğer moddan alıp geri ekle
                Ogrenci yedek = digerModTablo.ogrenciNoIleBul(ogrNo);
                if (yedek != null) hashTablo.ogrenciEkle(yedek);
            }
            if (digerSonuc) {

                Ogrenci yedek = hashTablo.ogrenciNoIleBul(ogrNo);
                if (yedek != null) digerModTablo.ogrenciEkle(yedek);
            }
            return false;
        }
    }


    public List<Ogrenci> tumOgrencileriGetir() {
        return hashTablo.tumOgrencileriGetir();
    }


    public List<Ogrenci> ganoSiralıGetir() {
        List<Ogrenci> sonuclar = new ArrayList<>();
        if(gelismisVeriTipi){
            sonuclar=hashTablo.ganoSiralıGetir();
        }else{
            sonuclar=digerModTablo.ganoSiralıGetir();
        }
        return sonuclar;
    }

    public List<Ogrenci> isimSiralıGetir() {
        List<Ogrenci> sonuclar = new ArrayList<>();
        if(gelismisVeriTipi){
            sonuclar=hashTablo.isimSiralıGetir();
        }else{
            sonuclar=digerModTablo.isimSiralıGetir();
        }
        return sonuclar;
    }

    public List<Ogrenci> sinifSiralıGetir() {
        List<Ogrenci> sonuclar = new ArrayList<>();
        if(gelismisVeriTipi){
            sonuclar=hashTablo.sinifSiralıGetir();
        }else{
            sonuclar=digerModTablo.sinifSiralıGetir();
        }
        return sonuclar;
    }

    public List<Ogrenci> cinsiyetSiralıGetir() {
        List<Ogrenci> sonuclar = new ArrayList<>();
        if(gelismisVeriTipi){
            sonuclar=hashTablo.cinsiyetSiralıGetir();
        }else{
            sonuclar=digerModTablo.cinsiyetSiralıGetir();
        }
        return sonuclar;
    }

    public List<Ogrenci> ogrenciNoSiralıGetir() {
        List<Ogrenci> sonuclar = new ArrayList<>();
        if(gelismisVeriTipi){
            sonuclar=hashTablo.ogrenciNoSiralıGetir();
        }else{
            sonuclar=digerModTablo.ogrenciNoSiralıGetir();
        }
        return sonuclar;
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
        String goster;
        if(gelismisVeriTipi){
            goster=hashTablo.hashTablosunuGoster();
        }else{
            goster=digerModTablo.hashTablosunuGoster();
        }
        return goster;
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

        if (mevcutListe.size() != digerListe.size()) {
            System.out.println("Boyut farkı: Mevcut=" + mevcutListe.size() + ", Diğer=" + digerListe.size());
            return false;
        }

        // Tüm öğrenci numaralarını kontrol et
        Set<Integer> mevcutNumaralar = new HashSet<>();
        for (Ogrenci ogr : mevcutListe) {
            mevcutNumaralar.add(ogr.getOgrNo());
        }

        for (Ogrenci ogr : digerListe) {
            if (!mevcutNumaralar.contains(ogr.getOgrNo())) {
                System.out.println("Eksik öğrenci: " + ogr.getOgrNo());
                return false;
            }
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
            sb.append("Lütfen 'Başlangıç Verilerine Dön' butonunu kullanın.\n");
        }

        return sb.toString();
    }


    public void baslangicVerilerineDon() {
        DosyaIslemleri.baslangicVerilerineDon();
        List<Ogrenci> ogrenciler = DosyaIslemleri.ogrencileriDosyadanOku();
        hashTablo.tumOgrencileriYukle(ogrenciler);
        digerModTablo.tumOgrencileriYukle(ogrenciler);
    }


    public String performansTestiEkleme() {
        StringBuilder sonuc = new StringBuilder();
        sonuc.append("=== EKLEME PERFORMANS TESTİ ===\n\n");

        Random r = new Random();
        int testNo = 900000000 + r.nextInt(100000);
        Ogrenci testOgr = new Ogrenci("Test", "Ogrenci", testNo, 3.5f, 1, 'E');


        HashTablo tempGelismis = new HashTablo(true);
        List<Ogrenci> base = DosyaIslemleri.ogrencileriDosyadanOku();
        tempGelismis.tumOgrencileriYukle(base);

        long gBas = System.nanoTime();
        boolean gSonuc = tempGelismis.ogrenciEkle(testOgr);
        long gSure = System.nanoTime() - gBas;


        HashTablo tempTemel = new HashTablo(false);
        tempTemel.tumOgrencileriYukle(base);

        long tBas = System.nanoTime();
        boolean tSonuc = tempTemel.ogrenciEkle(testOgr);
        long tSure = System.nanoTime() - tBas;

        sonuc.append("Gelişmiş Mod: ").append(gSure / 1000).append(" μs | Başarı: ").append(gSonuc ? "EVET" : "HAYIR").append("\n");
        sonuc.append("Temel Mod: ").append(tSure / 1000).append(" μs | Başarı: ").append(tSonuc ? "EVET" : "HAYIR").append("\n");
        sonuc.append("\nFark: ").append((tSure - gSure) / 1000).append(" μs | Gelişmiş mod ").append(gSure < tSure ? "daha hızlı" : "daha yavaş").append("\n");

        return sonuc.toString();
    }

    public String performansTestiArama() {
        StringBuilder sonuc = new StringBuilder();
        sonuc.append("=== ARAMA PERFORMANS TESTİ ===\n\n");

        List<Ogrenci> ogrenciler = hashTablo.tumOgrencileriGetir();
        if (ogrenciler.isEmpty()) return "Test için yeterli öğrenci yok!\n";

        Random r = new Random();
        Ogrenci rast = ogrenciler.get(r.nextInt(ogrenciler.size()));
        int arananNo = rast.getOgrNo();


        HashTablo tempG = new HashTablo(true);
        List<Ogrenci> base = DosyaIslemleri.ogrencileriDosyadanOku();
        tempG.tumOgrencileriYukle(base);

        long gBas = System.nanoTime();
        Ogrenci gBul = tempG.ogrenciNoIleBul(arananNo);
        long gSure = System.nanoTime() - gBas;


        HashTablo tempT = new HashTablo(false);
        tempT.tumOgrencileriYukle(base);

        long tBas = System.nanoTime();
        Ogrenci tBul = tempT.ogrenciNoIleBul(arananNo);
        long tSure = System.nanoTime() - tBas;

        sonuc.append("Aranan No: ").append(arananNo).append("\n");
        sonuc.append("Gelişmiş Mod: ").append(gSure / 1000).append(" μs | Bulundu: ").append(gBul != null ? "EVET" : "HAYIR").append("\n");
        sonuc.append("Temel Mod: ").append(tSure / 1000).append(" μs | Bulundu: ").append(tBul != null ? "EVET" : "HAYIR").append("\n");
        sonuc.append("\nFark: ").append((tSure - gSure) / 1000).append(" μs | Gelişmiş mod ").append(gSure < tSure ? "daha hızlı" : "daha yavaş").append("\n");

        return sonuc.toString();
    }

    public String tumPerformansTestleriniCalistir() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== TÜM PERFORMANS TESTLERİ ===\n\n");
        sb.append(performansTestiEkleme()).append("\n");
        sb.append(performansTestiArama()).append("\n");
        sb.append("\n💡 SONUÇ: Gelişmiş Mod (HashMap) genellikle daha hızlıdır!\n");
        return sb.toString();
    }
}