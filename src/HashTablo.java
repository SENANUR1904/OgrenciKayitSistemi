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
        this.tumOgrenciler = new ArrayList<>();
        if (gelismisVeriTipi) {
            gelismisTablo = new HashMap<>(TABLO_BOYUTU, 0.75f);
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
            // GELİŞMİŞ MOD - HashMap ile (O(1))
            if (gelismisTablo.containsKey(ogr.getOgrNo())) return false;
            gelismisTablo.put(ogr.getOgrNo(), ogr);
            tumOgrenciler.add(ogr);
            return true;
        } else {
            // TEMEL MOD - Dizi ile Linear Probing (O(n))
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
            // GELİŞMİŞ MOD - HashMap ile (O(1))
            return gelismisTablo.get(ogrNo);
        } else {
            // TEMEL MOD - Dizi ile Linear Probing (O(n))
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
            // GELİŞMİŞ MOD - HashMap ile (O(1))
            Ogrenci sil = gelismisTablo.remove(ogrNo);
            if (sil != null) {
                tumOgrenciler.removeIf(ogrenci -> ogrenci.getOgrNo() == ogrNo);
                return true;
            }
            return false;
        } else {
            // TEMEL MOD - Dizi ile Linear Probing (O(n))
            int index = hashHesapla(ogrNo);
            for (int i = 0; i < TABLO_BOYUTU; i++) {
                int idx = (index + i) % TABLO_BOYUTU;
                if (anaTablo[idx] != null && anaTablo[idx].getOgrNo() == ogrNo) {
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

    // NORMAL LİSTELEME - Öğrencileri eklenme sırasına göre getir
    public List<Ogrenci> tumOgrencileriGetir() {
        return new ArrayList<>(tumOgrenciler);
    }

    // GANO SIRALI LİSTELEME - İKİ FARKLI ALGORİTMA
    public List<Ogrenci> ganoSiralıGetir() {
        long baslangic = System.nanoTime();
        List<Ogrenci> siraliListe;

        if (gelismisVeriTipi) {
            // GELİŞMİŞ MOD - QuickSort benzeri hızlı sıralama
            siraliListe = new ArrayList<>(tumOgrenciler);
            Collections.sort(siraliListe, (o1, o2) -> Float.compare(o2.getGano(), o1.getGano()));
        } else {
            // BASIC MOD - Önce null olmayanları filtrele, sonra sırala
            int count = 0;
// Önce null olmayan öğrenci sayısını bul
            for (Ogrenci ogr : anaTablo) {
                if (ogr != null) {
                    count++;
                }
            }

// Sadece null olmayan öğrenciler için dizi oluştur
            Ogrenci[] doluOgrenciler = new Ogrenci[count];
            int index = 0;
            for (Ogrenci ogr : anaTablo) {
                if (ogr != null) {
                    doluOgrenciler[index++] = ogr;
                }
            }

// Şimdi sadece dolu öğrencilerle sıralama yap
            for (int i = 0; i < doluOgrenciler.length - 1; i++) {
                for (int j = 0; j < doluOgrenciler.length - i - 1; j++) {
                    if (doluOgrenciler[j].getGano() < doluOgrenciler[j + 1].getGano()) {
                        Ogrenci temp = doluOgrenciler[j];
                        doluOgrenciler[j] = doluOgrenciler[j + 1];
                        doluOgrenciler[j + 1] = temp;
                    }
                }
            }
            siraliListe = Arrays.asList(doluOgrenciler);
        }

        long sure = System.nanoTime() - baslangic;
        String mod = gelismisVeriTipi ? "Gelişmiş" : "Temel";
        RaporUretici.manuelIslemKaydet("GANO Sıralı Listeleme", sure / 1000, mod,
                "Listelenen: " + siraliListe.size() + " öğrenci");

        return siraliListe;
    }

    // İSİM SIRALI LİSTELEME - İKİ FARKLI ALGORİTMA
    public List<Ogrenci> isimSiralıGetir() {
        long baslangic = System.nanoTime();
        List<Ogrenci> siraliListe;

        if (gelismisVeriTipi) {
            // GELİŞMİŞ MOD - Collections.sort (hızlı)
            siraliListe = new ArrayList<>(tumOgrenciler);
            Collections.sort(siraliListe, (o1, o2) -> {
                int isimKarsilastirma = o1.getIsim().compareToIgnoreCase(o2.getIsim());
                if (isimKarsilastirma != 0) return isimKarsilastirma;
                return o1.getSoyad().compareToIgnoreCase(o2.getSoyad());
            });
        } else {
            // TEMEL MOD - Selection Sort (yavaş)
            // BASIC MOD - Önce null olmayanları filtrele, sonra Selection Sort yap
            int count = 0;
// Önce null olmayan öğrenci sayısını bul
            for (Ogrenci ogr : anaTablo) {
                if (ogr != null) {
                    count++;
                }
            }

// Sadece null olmayan öğrenciler için dizi oluştur
            Ogrenci[] doluOgrenciler = new Ogrenci[count];
            int index = 0;
            for (Ogrenci ogr : anaTablo) {
                if (ogr != null) {
                    doluOgrenciler[index++] = ogr;
                }
            }

// Şimdi sadece dolu öğrencilerle SELECTION SORT yap
            for (int i = 0; i < doluOgrenciler.length - 1; i++) {
                int minIndex = i;
                for (int j = i + 1; j < doluOgrenciler.length; j++) {
                    Ogrenci ogr1 = doluOgrenciler[j];
                    Ogrenci ogr2 = doluOgrenciler[minIndex];
                    int karsilastirma = ogr1.getIsim().compareToIgnoreCase(ogr2.getIsim());
                    if (karsilastirma < 0 ||
                            (karsilastirma == 0 && ogr1.getSoyad().compareToIgnoreCase(ogr2.getSoyad()) < 0)) {
                        minIndex = j;
                    }
                }
                if (minIndex != i) {
                    Ogrenci temp = doluOgrenciler[i];
                    doluOgrenciler[i] = doluOgrenciler[minIndex];
                    doluOgrenciler[minIndex] = temp;
                }
            }
            siraliListe = Arrays.asList(doluOgrenciler);
        }

        long sure = System.nanoTime() - baslangic;
        String mod = gelismisVeriTipi ? "Gelişmiş" : "Temel";
        RaporUretici.manuelIslemKaydet("İsim Sıralı Listeleme", sure / 1000, mod,
                "Listelenen: " + siraliListe.size() + " öğrenci");

        return siraliListe;
    }

    // SINIF SIRALI LİSTELEME - İKİ FARKLI ALGORİTMA
    public List<Ogrenci> sinifSiralıGetir() {
        long baslangic = System.nanoTime();
        List<Ogrenci> siraliListe;

        if (gelismisVeriTipi) {
            // GELİŞMİŞ MOD - Collections.sort (hızlı)
            siraliListe = new ArrayList<>(tumOgrenciler);
            Collections.sort(siraliListe, Comparator.comparingInt(Ogrenci::getSinif));
        } else {
            // BASIC MOD - Önce null olmayanları filtrele, sonra Insertion Sort yap
            int count = 0;
// Önce null olmayan öğrenci sayısını bul
            for (Ogrenci ogr : anaTablo) {
                if (ogr != null) {
                    count++;
                }
            }

// Sadece null olmayan öğrenciler için dizi oluştur
            Ogrenci[] doluOgrenciler = new Ogrenci[count];
            int index = 0;
            for (Ogrenci ogr : anaTablo) {
                if (ogr != null) {
                    doluOgrenciler[index++] = ogr;
                }
            }

// Şimdi sadece dolu öğrencilerle INSERTION SORT yap
            for (int i = 1; i < doluOgrenciler.length; i++) {
                Ogrenci key = doluOgrenciler[i];
                int j = i - 1;
                while (j >= 0 && doluOgrenciler[j].getSinif() > key.getSinif()) {
                    doluOgrenciler[j + 1] = doluOgrenciler[j];
                    j = j - 1;
                }
                doluOgrenciler[j + 1] = key;
            }
            siraliListe = Arrays.asList(doluOgrenciler);
        }

        long sure = System.nanoTime() - baslangic;
        String mod = gelismisVeriTipi ? "Gelişmiş" : "Temel";
        RaporUretici.manuelIslemKaydet("Sınıf Sıralı Listeleme", sure / 1000, mod,
                "Listelenen: " + siraliListe.size() + " öğrenci");

        return siraliListe;
    }

    // CİNSİYET SIRALI LİSTELEME - İKİ FARKLI ALGORİTMA
    public List<Ogrenci> cinsiyetSiralıGetir() {
        long baslangic = System.nanoTime();
        List<Ogrenci> siraliListe;

        if (gelismisVeriTipi) {
            // GELİŞMİŞ MOD - Collections.sort (hızlı)
            siraliListe = new ArrayList<>(tumOgrenciler);
            Collections.sort(siraliListe, (o1, o2) -> {
                int cinsiyetKarsilastirma = Character.compare(o1.getCinsiyet(), o2.getCinsiyet());
                if (cinsiyetKarsilastirma != 0) return cinsiyetKarsilastirma;
                return Float.compare(o2.getGano(), o1.getGano());
            });
        } else {
            // BASIC MOD - Önce null olmayanları filtrele, sonra Bubble Sort yap
            int count = 0;
// Önce null olmayan öğrenci sayısını bul
            for (Ogrenci ogr : anaTablo) {
                if (ogr != null) {
                    count++;
                }
            }

// Sadece null olmayan öğrenciler için dizi oluştur
            Ogrenci[] doluOgrenciler = new Ogrenci[count];
            int index = 0;
            for (Ogrenci ogr : anaTablo) {
                if (ogr != null) {
                    doluOgrenciler[index++] = ogr;
                }
            }

// Şimdi sadece dolu öğrencilerle BUBBLE SORT yap
            for (int i = 0; i < doluOgrenciler.length - 1; i++) {
                for (int j = 0; j < doluOgrenciler.length - i - 1; j++) {
                    Ogrenci o1 = doluOgrenciler[j];
                    Ogrenci o2 = doluOgrenciler[j + 1];
                    boolean swap = false;

                    if (o1.getCinsiyet() > o2.getCinsiyet()) {
                        swap = true;
                    } else if (o1.getCinsiyet() == o2.getCinsiyet() && o1.getGano() < o2.getGano()) {
                        swap = true;
                    }

                    if (swap) {
                        Ogrenci temp = doluOgrenciler[j];
                        doluOgrenciler[j] = doluOgrenciler[j + 1];
                        doluOgrenciler[j + 1] = temp;
                    }
                }
            }
            siraliListe = Arrays.asList(doluOgrenciler);
        }

        long sure = System.nanoTime() - baslangic;
        String mod = gelismisVeriTipi ? "Gelişmiş" : "Temel";
        RaporUretici.manuelIslemKaydet("Cinsiyet Sıralı Listeleme", sure / 1000, mod,
                "Listelenen: " + siraliListe.size() + " öğrenci");

        return siraliListe;
    }

    // ÖĞRENCİ NO SIRALI LİSTELEME - İKİ FARKLI ALGORİTMA
    public List<Ogrenci> ogrenciNoSiralıGetir() {
        long baslangic = System.nanoTime();
        List<Ogrenci> siraliListe;

        if (gelismisVeriTipi) {
            // GELİŞMİŞ MOD - Collections.sort (hızlı)
            siraliListe = new ArrayList<>(tumOgrenciler);
            Collections.sort(siraliListe, Comparator.comparingInt(Ogrenci::getOgrNo));
        } else {
            // TEMEL MOD - Merge Sort (orta hız)
            // BASIC MOD - Merge Sort (sadece dolu öğrencilerle)
            int count = 0;
            for (Ogrenci ogr : anaTablo) {
                if (ogr != null) count++;
            }

            Ogrenci[] doluOgrenciler = new Ogrenci[count];
            int index = 0;
            for (Ogrenci ogr : anaTablo) {
                if (ogr != null) doluOgrenciler[index++] = ogr;
            }

// Merge Sort uygula
            Ogrenci[] siralidizi = mergeSort(doluOgrenciler);
            siraliListe = Arrays.asList(siralidizi);
        }

        long sure = System.nanoTime() - baslangic;
        String mod = gelismisVeriTipi ? "Gelişmiş" : "Temel";
        RaporUretici.manuelIslemKaydet("Öğrenci No Sıralı Listeleme", sure / 1000, mod,
                "Listelenen: " + siraliListe.size() + " öğrenci");

        return siraliListe;
    }

    // TEMEL MOD İÇİN MERGE SORT ALGORİTMASI
    private Ogrenci[] mergeSort(Ogrenci[] dizi) {
        if (dizi.length <= 1) {
            return dizi;
        }

        int orta = dizi.length / 2;
        Ogrenci[] sol = new Ogrenci[orta];
        Ogrenci[] sag = new Ogrenci[dizi.length - orta];

        // Sol diziyi kopyala
        System.arraycopy(dizi, 0, sol, 0, orta);
        // Sağ diziyi kopyala
        System.arraycopy(dizi, orta, sag, 0, dizi.length - orta);

        sol = mergeSort(sol);
        sag = mergeSort(sag);

        return merge(sol, sag);
    }

    private Ogrenci[] merge(Ogrenci[] sol, Ogrenci[] sag) {
        Ogrenci[] sonuc = new Ogrenci[sol.length + sag.length];
        int i = 0, j = 0, k = 0;

        while (i < sol.length && j < sag.length) {
            if (sol[i].getOgrNo() <= sag[j].getOgrNo()) {
                sonuc[k] = sol[i];
                i++;
            } else {
                sonuc[k] = sag[j];
                j++;
            }
            k++;
        }

        while (i < sol.length) {
            sonuc[k] = sol[i];
            i++;
            k++;
        }

        while (j < sag.length) {
            sonuc[k] = sag[j];
            j++;
            k++;
        }

        return sonuc;
    }

    public String hashTablosunuGoster() {
        StringBuilder sb = new StringBuilder();
        if (gelismisVeriTipi) {
            sb.append("=== GELİŞMİŞ MOD (HashMap) ===\n");
            sb.append("Toplam Öğrenci: ").append(tumOgrenciler.size()).append("\n");
            sb.append("Tablo Boyutu: ").append(TABLO_BOYUTU).append("\n");
            sb.append("Doluluk Oranı: ").append(String.format("%.2f%%", (tumOgrenciler.size() * 100.0 / TABLO_BOYUTU))).append("\n");
            sb.append("Çalışma Prensibi: HashMap - Anahtar-Değer çiftleri\n");
            sb.append("Ortalama İşlem Süresi: O(1) - Çok Hızlı\n\n");

            // --- Tüm kayıtları göster ---
            for (Map.Entry<Integer, Ogrenci> e : gelismisTablo.entrySet()) {
                sb.append("Anahtar: ").append(e.getKey())
                        .append(" -> ").append(e.getValue()).append("\n");
            }

        } else {
            sb.append("=== TEMEL MOD (Linear Probing) ===\n");
            sb.append("Toplam Öğrenci: ").append(tumOgrenciler.size()).append("\n");
            sb.append("Tablo Boyutu: ").append(TABLO_BOYUTU).append("\n");
            sb.append("Doluluk Oranı: ").append(String.format("%.2f%%", (tumOgrenciler.size() * 100.0 / TABLO_BOYUTU))).append("\n");
            sb.append("Toplam Çakışma: ").append(cakismaSayisi).append("\n");
            sb.append("Çalışma Prensibi: Dizi - Linear Probing ile çakışma çözümü\n");
            sb.append("Ortalama İşlem Süresi: O(n) - Yavaş\n\n");

            // --- Tüm dolu hücreleri göster ---
            for (int i = 0; i < TABLO_BOYUTU; i++) {
                if (anaTablo[i] != null) {
                    int orj = hashHesapla(anaTablo[i].getOgrNo());
                    sb.append("Orijinal: ").append(orj)
                            .append(" | Bulundu: ").append(i);
                    if (orj != i) sb.append(" | Çakışma");
                    sb.append(" -> ").append(anaTablo[i]).append("\n");
                }
            }
        }
        return sb.toString();
    }


    public int getToplamOgrenciSayisi() {
        return tumOgrenciler.size();
    }

    public int getCakismaSayisi() {
        return cakismaSayisi;
    }

    public void tumOgrencileriTemizle() {
        tumOgrenciler.clear();
        if (gelismisVeriTipi) {
            gelismisTablo.clear();
        } else {
            anaTablo = new Ogrenci[TABLO_BOYUTU];
        }
        cakismaSayisi = 0;
    }

    public void tumOgrencileriYukle(List<Ogrenci> ogrenciler) {
        tumOgrencileriTemizle();
        for (Ogrenci ogr : ogrenciler) {
            ogrenciEkle(ogr);
        }
    }
}