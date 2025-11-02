import java.util.*;

public class HashTablo {
    private static final int TABLO_BOYUTU = 13003; // Asal sayı (iyi dağılım)
    private boolean gelismisVeriTipi;
    private Ogrenci[] anaTablo;
    private Map<Integer, Ogrenci> gelismisTablo;
    private List<Ogrenci> tumOgrenciler;
    private int cakismaSayisi;

    public HashTablo(boolean gelismisVeriTipi) {
        this.gelismisVeriTipi = gelismisVeriTipi;
        this.cakismaSayisi = 0;
        this.tumOgrenciler = new ArrayList<>();
        if (gelismisVeriTipi) {
            gelismisTablo = new HashMap<>();
        } else {
            anaTablo = new Ogrenci[TABLO_BOYUTU];
        }
    }

    private int hashHesapla(int ogrNo) {
        return Math.abs(ogrNo) % TABLO_BOYUTU;
    }

    public boolean ogrenciEkle(Ogrenci ogr) {
        if (ogr == null) return false;

        if (gelismisVeriTipi) {
            if (gelismisTablo.containsKey(ogr.getOgrNo())) return false;
            gelismisTablo.put(ogr.getOgrNo(), ogr);
            tumOgrenciler.add(ogr);
            return true;
        } else {
            int index = hashHesapla(ogr.getOgrNo());
            int probe = 0;
            while (anaTablo[index] != null) {
                probe++;
                index = (index + 1) % TABLO_BOYUTU;
                cakismaSayisi++;
                if (probe >= TABLO_BOYUTU) return false;
            }
            anaTablo[index] = ogr;
            tumOgrenciler.add(ogr);
            return true;
        }
    }

    public Ogrenci ogrenciNoIleBul(int ogrNo) {
        if (gelismisVeriTipi) {
            return gelismisTablo.get(ogrNo);
        } else {
            int index = hashHesapla(ogrNo);
            for (int i = 0; i < TABLO_BOYUTU; i++) {
                int idx = (index + i) % TABLO_BOYUTU;
                if (anaTablo[idx] != null && anaTablo[idx].getOgrNo() == ogrNo) {
                    return anaTablo[idx];
                }
            }
            return null;
        }
    }

    public boolean ogrenciSil(int ogrNo) {
        if (gelismisVeriTipi) {
            Ogrenci sil = gelismisTablo.remove(ogrNo);
            if (sil != null) {
                // DÜZELTME: tumOgrenciler listesinden de kaldır
                boolean removed = tumOgrenciler.removeIf(ogrenci -> ogrenci.getOgrNo() == ogrNo);
                return removed;
            }
            return false;
        } else {
            int index = hashHesapla(ogrNo);
            for (int i = 0; i < TABLO_BOYUTU; i++) {
                int idx = (index + i) % TABLO_BOYUTU;
                if (anaTablo[idx] != null && anaTablo[idx].getOgrNo() == ogrNo) {
                    // DÜZELTME: tumOgrenciler listesinden de kaldır
                    tumOgrenciler.removeIf(ogrenci -> ogrenci.getOgrNo() == ogrNo);
                    anaTablo[idx] = null;
                    return true;
                }
            }
            return false;
        }
    }

    public void ogrenciGuncelle(Ogrenci ogr) {
        if (ogr == null) return;
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
                sb.append("Anahtar: ").append(e.getKey()).append(" -> ").append(e.getValue()).append("\n");
            }
        } else {
            sb.append("=== TEMEL MOD (Linear Probing) ===\n");
            for (int i = 0; i < TABLO_BOYUTU; i++) {
                if (anaTablo[i] != null) {
                    int orj = hashHesapla(anaTablo[i].getOgrNo());
                    sb.append("Orijinal: ").append(orj).append(" | Bulundu: ").append(i);
                    if (orj != i) sb.append(" | Çakışma");
                    sb.append(" -> ").append(anaTablo[i]).append("\n");
                }
            }
            sb.append("\nToplam Çakışma Sayısı: ").append(cakismaSayisi).append("\n");
        }
        return sb.toString();
    }

    public int getToplamOgrenciSayisi() {
        return tumOgrenciler.size();
    }

    public int getCakismaSayisi() {
        return cakismaSayisi;
    }
}