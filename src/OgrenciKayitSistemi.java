import java.util.*;

public class OgrenciKayitSistemi {
    private HashTablo hashTablo;
    private boolean gelismisVeriTipi;
    
    public OgrenciKayitSistemi(boolean gelismisVeriTipi) {
        this.gelismisVeriTipi = gelismisVeriTipi;
        this.hashTablo = new HashTablo(gelismisVeriTipi);
        verileriYukle();
    }
    
    private void verileriYukle() {
        List<Ogrenci> ogrenciler = DosyaIslemleri.ogrencileriDosyadanOku();
        if (ogrenciler.isEmpty()) {
            ogrenciler = DosyaIslemleri.baslangicVerisiOlustur();
        }
        for (Ogrenci ogr : ogrenciler) {
            hashTablo.ogrenciEkle(ogr);
        }
    }
    
    public boolean ogrenciEkle(Ogrenci ogr) {
        boolean sonuc = hashTablo.ogrenciEkle(ogr);
        if (sonuc) {
            DosyaIslemleri.ogrencileriDosyayaYaz(hashTablo.tumOgrencileriGetir());
        }
        return sonuc;
    }
    
    public Ogrenci ogrenciNoIleAra(int ogrNo) {
        return hashTablo.ogrenciNoIleBul(ogrNo);
    }
    
    public List<Ogrenci> adIleAra(String isim) {
        List<Ogrenci> sonuclar = new ArrayList<>();
        for (Ogrenci ogr : hashTablo.tumOgrencileriGetir()) {
            if (ogr.getIsim().equalsIgnoreCase(isim)) {
                sonuclar.add(ogr);
            }
        }
        sonuclar.sort((o1, o2) -> Float.compare(o2.getGano(), o1.getGano()));
        return sonuclar;
    }
    
    public boolean ogrenciSil(int ogrNo) {
        boolean sonuc = hashTablo.ogrenciSil(ogrNo);
        if (sonuc) {
            DosyaIslemleri.ogrencileriDosyayaYaz(hashTablo.tumOgrencileriGetir());
        }
        return sonuc;
    }
    
    public void ogrenciGuncelle(Ogrenci ogr) {
        hashTablo.ogrenciGuncelle(ogr);
        DosyaIslemleri.ogrencileriDosyayaYaz(hashTablo.tumOgrencileriGetir());
    }
    
    public List<Ogrenci> tumOgrencileriGetir() {
        List<Ogrenci> liste = hashTablo.tumOgrencileriGetir();
        liste.sort((o1, o2) -> Float.compare(o2.getGano(), o1.getGano()));
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
}
