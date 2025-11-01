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
        if (ogrenciler.isEmpty()) ogrenciler = DosyaIslemleri.baslangicVerisiOlustur();
        for (Ogrenci o : ogrenciler) hashTablo.ogrenciEkle(o);
    }

    public boolean ogrenciEkle(Ogrenci o) {
        boolean ok = hashTablo.ogrenciEkle(o);
        if (ok) DosyaIslemleri.ogrencileriDosyayaYaz(hashTablo.tumOgrencileriGetir());
        return ok;
    }

    public Ogrenci ogrenciNoIleAra(int no) {
        return hashTablo.ogrenciNoIleBul(no);
    }

    public List<Ogrenci> adIleAra(String ad) {
        List<Ogrenci> l = new ArrayList<>();
        for (Ogrenci o : hashTablo.tumOgrencileriGetir()) if (o.getIsim().equalsIgnoreCase(ad)) l.add(o);
        l.sort((a, b) -> Float.compare(b.getGano(), a.getGano()));
        return l;
    }

    public boolean ogrenciSil(int no) {
        boolean ok = hashTablo.ogrenciSil(no);
        if (ok) DosyaIslemleri.ogrencileriDosyayaYaz(hashTablo.tumOgrencileriGetir());
        return ok;
    }

    public void ogrenciGuncelle(Ogrenci o) {
        hashTablo.ogrenciGuncelle(o);
        DosyaIslemleri.ogrencileriDosyayaYaz(hashTablo.tumOgrencileriGetir());
    }

    public List<Ogrenci> tumOgrencileriGetir() {
        List<Ogrenci> l = hashTablo.tumOgrencileriGetir();
        l.sort((a, b) -> Float.compare(b.getGano(), a.getGano()));
        return l;
    }

    public List<Ogrenci> sinifaGoreGetir(int s) {
        List<Ogrenci> l = new ArrayList<>();
        for (Ogrenci o : hashTablo.tumOgrencileriGetir()) if (o.getSinif() == s) l.add(o);
        l.sort((a, b) -> Float.compare(b.getGano(), a.getGano()));
        return l;
    }

    public List<Ogrenci> cinsiyeteGoreGetir(char c) {
        List<Ogrenci> l = new ArrayList<>();
        for (Ogrenci o : hashTablo.tumOgrencileriGetir()) if (o.getCinsiyet() == c) l.add(o);
        l.sort((a, b) -> Float.compare(b.getGano(), a.getGano()));
        return l;
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
