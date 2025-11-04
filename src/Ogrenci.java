public class Ogrenci {
    private String isim;
    private String soyad;
    private int ogrNo;
    private float gano;
    private int sinif;
    private char cinsiyet;

    public Ogrenci(String isim, String soyad, int ogrNo, float gano, int sinif, char cinsiyet) {
        // Öğrenci numarası format kontrolü - 9 haneli olmalı
        if (String.valueOf(ogrNo).length() != 9) {
            throw new IllegalArgumentException("Geçersiz öğrenci numarası! 9 haneli numara giriniz: " + ogrNo);
        }

        this.isim = isim;
        this.soyad = soyad;
        this.ogrNo = ogrNo;
        this.gano = gano;
        this.sinif = sinif;
        this.cinsiyet = cinsiyet;
    }

    public String getIsim() { return isim; }
    public void setIsim(String isim) { this.isim = isim; }

    public String getSoyad() { return soyad; }
    public void setSoyad(String soyad) { this.soyad = soyad; }

    public int getOgrNo() { return ogrNo; }
    public void setOgrNo(int ogrNo) {
        // Setter'da da format kontrolü
        if (String.valueOf(ogrNo).length() != 9) {
            throw new IllegalArgumentException("Geçersiz öğrenci numarası! 9 haneli numara giriniz: " + ogrNo);
        }
        this.ogrNo = ogrNo;
    }

    public float getGano() { return gano; }
    public void setGano(float gano) { this.gano = gano; }

    public int getSinif() { return sinif; }
    public void setSinif(int sinif) { this.sinif = sinif; }

    public char getCinsiyet() { return cinsiyet; }
    public void setCinsiyet(char cinsiyet) { this.cinsiyet = cinsiyet; }

    @Override
    public String toString() {
        return String.format("%-10s %-10s %-10d %-6.2f %-4d %c", isim, soyad, ogrNo, gano, sinif, cinsiyet);
    }

    public String dosyaFormatinda() {
        return isim + "," + soyad + "," + ogrNo + "," + gano + "," + sinif + "," + cinsiyet;
    }

    public static Ogrenci dosyadanOku(String satir) {
        String[] parcalar = satir.split(",");
        if (parcalar.length == 6) {
            return new Ogrenci(parcalar[0], parcalar[1], Integer.parseInt(parcalar[2]),
                    Float.parseFloat(parcalar[3]), Integer.parseInt(parcalar[4]),
                    parcalar[5].charAt(0));
        }
        return null;
    }
}