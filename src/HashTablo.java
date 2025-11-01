import java.util.*;

public class HashTablo {
    private static final int TABLO_BOYUTU = 13003;
    private boolean gelismisVeriTipi;
    private Ogrenci[] anaTablo;
    private Map<Integer, Ogrenci> gelismisTablo;
    private List<Ogrenci> tumOgrenciler;
    private int cakismaSayisi;

    public HashTablo(boolean gelismisVeriTipi) {
        this.gelismisVeriTipi = gelismisVeriTipi;
        this.cakismaSayisi = 0;
        if (gelismisVeriTipi) {
            gelismisTablo = new HashMap<>();
            tumOgrenciler = new ArrayList<>();
        } else {
            anaTablo = new Ogrenci[TABLO_BOYUTU];
            tumOgrenciler = new ArrayList<>();
        }
    }

    private int hashHesapla(int ogrNo) {
        return ogrNo % TABLO_BOYUTU;
    }

    public boolean ogrenciEkle(Ogrenci ogr) {
        if (gelismisVeriTipi) {
            if (gelismisTablo.containsKey(ogr.getOgrNo())) return false;
            gelismisTablo.put(ogr.getOgrNo(), ogr);
            tumOgrenciler.add(ogr);
            return true;
        } else {
            int index = hashHesapla(ogr.getOgrNo());
            int deneme = 0;
            while (anaTablo[index] != null) {
                deneme++;
                index = (index + 1) % TABLO_BOYUTU;
                cakismaSayisi++;
            }
            anaTablo[index] = ogr;
            tumOgrenciler.add(ogr);
            return true;
        }
    }

    public Ogrenci ogrenciNoIleBul(int ogrNo) {
        if (gelismisVeriTipi) return gelismisTablo.get(ogrNo);
        int index = hashHesapla(ogrNo);
        for (int i = 0; i < TABLO_BOYUTU; i++) {
            int yeni = (index + i) % TABLO_BOYUTU;
            if (anaTablo[yeni] != null && anaTablo[yeni].getOgrNo() == ogrNo) return anaTablo[yeni];
        }
        return null;
    }

    public boolean ogrenciSil(int ogrNo) {
        if (gelismisVeriTipi) {
            Ogrenci sil = gelismisTablo.remove(ogrNo);
            if (sil != null) tumOgrenciler.remove(sil);
            return sil != null;
        } else {
            int index = hashHesapla(ogrNo);
            for (int i = 0; i < TABLO_BOYUTU; i++) {
                int yeni = (index + i) % TABLO_BOYUTU;
                if (anaTablo[yeni] != null && anaTablo[yeni].getOgrNo() == ogrNo) {
                    tumOgrenciler.remove(anaTablo[yeni]);
                    anaTablo[yeni] = null;
                    return true;
                }
            }
            return false;
        }
    }

    public void ogrenciGuncelle(Ogrenci ogr) {
        ogrenciSil(ogr.getOgrNo());
        ogrenciEkle(ogr);
    }

    public List<Ogrenci> tumOgrencileriGetir() {
        return new ArrayList<>(tumOgrenciler);
    }

    public String hashTablosunuGoster() {
        StringBuilder sb = new StringBuilder();
        if (gelismisVeriTipi) {
            sb.append("=== GELİŞMİŞ MOD (HashMap) ===\n");
            for (Map.Entry<Integer, Ogrenci> e : gelismisTablo.entrySet()) {
                sb.append("Anahtar: ").append(e.getKey())
                  .append(" -> ").append(e.getValue()).append("\n");
            }
        } else {
            sb.append("=== TEMEL MOD (Linear Probing) ===\n");
            for (int i = 0; i < TABLO_BOYUTU; i++) {
                if (anaTablo[i] != null) {
                    int orj = hashHesapla(anaTablo[i].getOgrNo());
                    sb.append("Orijinal Index: ").append(orj)
                      .append(" | Yerleştiği Index: ").append(i);
                    if (orj != i) sb.append(" | Çakışma");
                    sb.append(" -> ").append(anaTablo[i]).append("\n");
                }
            }
            sb.append("\nToplam Çakışma Sayısı: ").append(cakismaSayisi);
        }
        return sb.toString();
    }

    public int getToplamOgrenciSayisi() {
        return tumOgrenciler.size();
    }
}
