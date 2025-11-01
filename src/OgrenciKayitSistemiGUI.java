import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OgrenciKayitSistemiGUI extends JFrame {
    private OgrenciKayitSistemi sistem;
    private JTabbedPane tabbedPane;
    private JTable ogrenciTablosu;
    private DefaultTableModel tableModel;
    private JTextArea sonucAlani;
    private JComboBox<String> modComboBox;
    
    private JTextField txtIsim, txtSoyad, txtOgrNo, txtGANO, txtSinif, txtCinsiyet;
    private JTextField txtAramaOgrNo, txtAramaIsim;
    
    public OgrenciKayitSistemiGUI() {
        sistem = new OgrenciKayitSistemi(true);
        initializeGUI();
        showBildirim("Hoş Geldiniz", 
            "Öğrenci Kayıt Sistemine hoş geldiniz!\n" +
            "Toplam " + sistem.getToplamOgrenciSayisi() + " öğrenci yüklendi.", 
            "info");
    }
    
    private void initializeGUI() {
        setTitle("BMÜ3311 Veri Yönetimi - Öğrenci Kayıt Sistemi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        createTopPanel();
        createTabbedPane();
        createBottomPanel();
        
        setVisible(true);
    }
    
    private void showBildirim(String baslik, String mesaj, String tip) {
        // Özel bildirim penceresi oluştur
        JDialog bildirim = new JDialog(this, baslik, true);
        bildirim.setSize(400, 250);
        bildirim.setLocationRelativeTo(this);
        bildirim.setLayout(new BorderLayout());
        bildirim.setResizable(false);
        
        // İkon ve renk ayarı
        String icon = "";
        Color color = Color.BLUE;
        if (tip.equals("success")) {
            icon = "✅";
            color = new Color(34, 139, 34); // Yeşil
        } else if (tip.equals("error")) {
            icon = "❌";
            color = new Color(178, 34, 34); // Kırmızı
        } else if (tip.equals("warning")) {
            icon = "⚠️";
            color = new Color(255, 140, 0); // Turuncu
        } else {
            icon = "ℹ️";
            color = new Color(70, 130, 180); // Mavi
        }
        
        // Başlık paneli
        JPanel baslikPanel = new JPanel(new FlowLayout());
        baslikPanel.setBackground(color);
        baslikPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel baslikLabel = new JLabel(icon + " " + baslik);
        baslikLabel.setFont(new Font("Arial", Font.BOLD, 16));
        baslikLabel.setForeground(Color.WHITE);
        baslikPanel.add(baslikLabel);
        
        // Mesaj paneli
        JPanel mesajPanel = new JPanel(new BorderLayout());
        mesajPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JTextArea mesajArea = new JTextArea(mesaj);
        mesajArea.setEditable(false);
        mesajArea.setBackground(new Color(245, 245, 245));
        mesajArea.setFont(new Font("Arial", Font.PLAIN, 14));
        mesajArea.setLineWrap(true);
        mesajArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(mesajArea);
        mesajPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Buton paneli
        JPanel butonPanel = new JPanel(new FlowLayout());
        JButton tamamBtn = new JButton("Tamam");
        tamamBtn.setBackground(color);
        tamamBtn.setForeground(Color.WHITE);
        tamamBtn.addActionListener(e -> bildirim.dispose());
        butonPanel.add(tamamBtn);
        
        bildirim.add(baslikPanel, BorderLayout.NORTH);
        bildirim.add(mesajPanel, BorderLayout.CENTER);
        bildirim.add(butonPanel, BorderLayout.SOUTH);
        
        bildirim.setVisible(true);
    }
    
    private void createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(70, 130, 180));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel baslik = new JLabel("ÖĞRENCİ KAYIT SİSTEMİ", JLabel.CENTER);
        baslik.setFont(new Font("Arial", Font.BOLD, 20));
        baslik.setForeground(Color.WHITE);
        topPanel.add(baslik, BorderLayout.CENTER);
        
        JPanel modPanel = new JPanel(new FlowLayout());
        modPanel.setOpaque(false);
        modPanel.add(new JLabel("Çalışma Modu:"));
        
        modComboBox = new JComboBox<>(new String[]{"Gelişmiş Veri Tipi", "Temel Veri Tipi"});
        modComboBox.setBackground(Color.WHITE);
        modPanel.add(modComboBox);
        
        JButton modDegistirBtn = new JButton("Modu Değiştir");
        modDegistirBtn.setBackground(new Color(255, 140, 0));
        modDegistirBtn.setForeground(Color.WHITE);
        modDegistirBtn.addActionListener(e -> modDegistir());
        modPanel.add(modDegistirBtn);
        
        topPanel.add(modPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
    }
    
    private void createTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(240, 248, 255));
        
        tabbedPane.addTab("Öğrenci İşlemleri", createOgrenciIslemleriPanel());
        tabbedPane.addTab("Arama İşlemleri", createAramaPanel());
        tabbedPane.addTab("Listeleme", createListelemePanel());
        tabbedPane.addTab("Hash Tablosu", createHashPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        JLabel infoLabel = new JLabel(" Toplam Öğrenci: " + sistem.getToplamOgrenciSayisi() + " | BMÜ3311 Veri Yönetimi Ödevi");
        infoLabel.setBackground(new Color(47, 79, 79));
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setOpaque(true);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        sonucAlani = new JTextArea(6, 100);
        sonucAlani.setEditable(false);
        sonucAlani.setBackground(new Color(253, 245, 230));
        sonucAlani.setForeground(Color.DARK_GRAY);
        sonucAlani.setFont(new Font("Monospaced", Font.PLAIN, 12));
        sonucAlani.setBorder(BorderFactory.createTitledBorder("İşlem Sonuçları"));
        
        JScrollPane scrollPane = new JScrollPane(sonucAlani);
        
        bottomPanel.add(infoLabel, BorderLayout.NORTH);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createOgrenciIslemleriPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Öğrenci İşlemleri"));
        panel.setBackground(new Color(245, 245, 245));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("İsim:"), gbc);
        gbc.gridx = 1;
        txtIsim = new JTextField();
        panel.add(txtIsim, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Soyad:"), gbc);
        gbc.gridx = 1;
        txtSoyad = new JTextField();
        panel.add(txtSoyad, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Öğrenci No:"), gbc);
        gbc.gridx = 1;
        txtOgrNo = new JTextField();
        panel.add(txtOgrNo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("GANO:"), gbc);
        gbc.gridx = 1;
        txtGANO = new JTextField();
        panel.add(txtGANO, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Sınıf (1-4):"), gbc);
        gbc.gridx = 1;
        txtSinif = new JTextField();
        panel.add(txtSinif, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Cinsiyet (E/K):"), gbc);
        gbc.gridx = 1;
        txtCinsiyet = new JTextField();
        panel.add(txtCinsiyet, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel butonPanel = new JPanel(new FlowLayout());
        butonPanel.setOpaque(false);
        
        JButton btnEkle = new JButton("Yeni Öğrenci Ekle");
        btnEkle.setBackground(new Color(34, 139, 34));
        btnEkle.setForeground(Color.WHITE);
        btnEkle.addActionListener(e -> ogrenciEkle());
        butonPanel.add(btnEkle);
        
        JButton btnGuncelle = new JButton("Öğrenci Güncelle");
        btnGuncelle.setBackground(new Color(255, 140, 0));
        btnGuncelle.setForeground(Color.WHITE);
        btnGuncelle.addActionListener(e -> ogrenciGuncelle());
        butonPanel.add(btnGuncelle);
        
        JButton btnSil = new JButton("Öğrenci Sil");
        btnSil.setBackground(new Color(178, 34, 34));
        btnSil.setForeground(Color.WHITE);
        btnSil.addActionListener(e -> ogrenciSil());
        butonPanel.add(btnSil);
        
        panel.add(butonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createAramaPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Arama İşlemleri"));
        panel.setBackground(new Color(245, 245, 245));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Öğrenci No ile Ara:"), gbc);
        gbc.gridx = 1;
        txtAramaOgrNo = new JTextField();
        panel.add(txtAramaOgrNo, gbc);
        gbc.gridx = 2;
        JButton btnAraNo = new JButton("Numaraya Göre Ara");
        btnAraNo.setBackground(new Color(70, 130, 180));
        btnAraNo.setForeground(Color.WHITE);
        btnAraNo.addActionListener(e -> ogrenciNoIleAra());
        panel.add(btnAraNo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("İsim ile Ara:"), gbc);
        gbc.gridx = 1;
        txtAramaIsim = new JTextField();
        panel.add(txtAramaIsim, gbc);
        gbc.gridx = 2;
        JButton btnAraIsim = new JButton("İsme Göre Ara");
        btnAraIsim.setBackground(new Color(70, 130, 180));
        btnAraIsim.setForeground(Color.WHITE);
        btnAraIsim.addActionListener(e -> adIleAra());
        panel.add(btnAraIsim, gbc);
        
        return panel;
    }
    
    private JPanel createListelemePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Listeleme İşlemleri"));
        
        JPanel butonPanel = new JPanel(new FlowLayout());
        butonPanel.setBackground(new Color(240, 248, 255));
        
        JButton btnTumListele = new JButton("Tüm Öğrencileri Listele (GANO)");
        btnTumListele.setBackground(new Color(70, 130, 180));
        btnTumListele.setForeground(Color.WHITE);
        btnTumListele.addActionListener(e -> tumOgrencileriListele());
        butonPanel.add(btnTumListele);
        
        JButton btnSinifListele = new JButton("Sınıfa Göre Listele");
        btnSinifListele.setBackground(new Color(34, 139, 34));
        btnSinifListele.setForeground(Color.WHITE);
        btnSinifListele.addActionListener(e -> sinifaGoreListele());
        butonPanel.add(btnSinifListele);
        
        JButton btnCinsiyetListele = new JButton("Cinsiyete Göre Listele");
        btnCinsiyetListele.setBackground(new Color(148, 0, 211));
        btnCinsiyetListele.setForeground(Color.WHITE);
        btnCinsiyetListele.addActionListener(e -> cinsiyeteGoreListele());
        butonPanel.add(btnCinsiyetListele);
        
        JButton btnRaporla = new JButton("Raporları Oluştur");
        btnRaporla.setBackground(new Color(139, 0, 0));
        btnRaporla.setForeground(Color.WHITE);
        btnRaporla.addActionListener(e -> raporlariOlustur());
        butonPanel.add(btnRaporla);
        
        panel.add(butonPanel, BorderLayout.NORTH);
        
        String[] kolonlar = {"İsim", "Soyad", "Öğrenci No", "GANO", "Sınıf", "Cinsiyet"};
        tableModel = new DefaultTableModel(kolonlar, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ogrenciTablosu = new JTable(tableModel);
        ogrenciTablosu.setBackground(new Color(253, 245, 230));
        ogrenciTablosu.setSelectionBackground(new Color(70, 130, 180));
        ogrenciTablosu.setSelectionForeground(Color.WHITE);
        ogrenciTablosu.setRowHeight(25);
        ogrenciTablosu.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(ogrenciTablosu);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Öğrenci Listesi"));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createHashPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Hash Tablosu Görüntüleme"));
        
        JButton btnHashGoster = new JButton("Hash Tablosunu Göster");
        btnHashGoster.setBackground(new Color(47, 79, 79));
        btnHashGoster.setForeground(Color.WHITE);
        btnHashGoster.addActionListener(e -> hashTablosunuGoster());
        
        JPanel butonPanel = new JPanel();
        butonPanel.add(btnHashGoster);
        panel.add(butonPanel, BorderLayout.NORTH);
        
        JTextArea hashTextArea = new JTextArea();
        hashTextArea.setEditable(false);
        hashTextArea.setBackground(new Color(240, 248, 255));
        hashTextArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollPane = new JScrollPane(hashTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void modDegistir() {
        boolean yeniMod = modComboBox.getSelectedIndex() == 0;
        sistem = new OgrenciKayitSistemi(yeniMod);
        sonucAlani.append("Mod değiştirildi: " + (yeniMod ? "Gelişmiş Veri Tipi" : "Temel Veri Tipi") + "\n");
        updateOgrenciSayisi();
        showBildirim("Mod Değiştirildi", 
            "Yeni mod: " + (yeniMod ? "Gelişmiş Veri Tipi" : "Temel Veri Tipi") +
            "\n\nToplam öğrenci: " + sistem.getToplamOgrenciSayisi(), 
            "info");
    }
    
    private void ogrenciEkle() {
        try {
            String isim = txtIsim.getText().trim();
            String soyad = txtSoyad.getText().trim();
            int ogrNo = Integer.parseInt(txtOgrNo.getText().trim());
            float gano = Float.parseFloat(txtGANO.getText().trim());
            int sinif = Integer.parseInt(txtSinif.getText().trim());
            char cinsiyet = txtCinsiyet.getText().trim().toUpperCase().charAt(0);
            
            if (isim.isEmpty() || soyad.isEmpty()) {
                showBildirim("Hata", "İsim ve soyad boş olamaz!", "error");
                return;
            }
            
            if (sinif < 1 || sinif > 4) {
                showBildirim("Hata", "Sınıf 1-4 arasında olmalıdır!", "error");
                return;
            }
            
            if (cinsiyet != 'E' && cinsiyet != 'K') {
                showBildirim("Hata", "Cinsiyet E veya K olmalıdır!", "error");
                return;
            }
            
            Ogrenci ogr = new Ogrenci(isim, soyad, ogrNo, gano, sinif, cinsiyet);
            
            if (sistem.ogrenciEkle(ogr)) {
                sonucAlani.append("Öğrenci başarıyla eklendi: " + ogrNo + " - " + isim + " " + soyad + "\n");
                formTemizle();
                updateOgrenciSayisi();
                tumOgrencileriListele();
                
                // ÖZEL BİLDİRİM - EKLEME
                showBildirim("Öğrenci Eklendi", 
                    "Öğrenci başarıyla listeye eklendi!\n\n" +
                    "📋 ÖĞRENCİ BİLGİLERİ:\n" +
                    "─────────────────────\n" +
                    "• İsim: " + isim + "\n" +
                    "• Soyad: " + soyad + "\n" +
                    "• No: " + ogrNo + "\n" +
                    "• GANO: " + gano + "\n" +
                    "• Cinsiyet: " + (cinsiyet == 'E' ? "Erkek" : "Kız") + "\n" +
                    "• Sınıf: " + sinif + "\n\n" +
                    "✅ Öğrenci başarıyla eklendi!", 
                    "success");
            } else {
                showBildirim("Hata", 
                    ogrNo + " numarası zaten mevcut!\n\n" +
                    "Lütfen farklı bir öğrenci numarası giriniz.", 
                    "error");
            }
        } catch (Exception ex) {
            showBildirim("Hata", "Geçerli veri giriniz!\n\nHata: " + ex.getMessage(), "error");
        }
    }
    
    private void ogrenciGuncelle() {
        try {
            int ogrNo = Integer.parseInt(txtOgrNo.getText().trim());
            Ogrenci ogr = sistem.ogrenciNoIleAra(ogrNo);
            
            if (ogr != null) {
                String isim = txtIsim.getText().trim();
                String soyad = txtSoyad.getText().trim();
                float gano = Float.parseFloat(txtGANO.getText().trim());
                int sinif = Integer.parseInt(txtSinif.getText().trim());
                char cinsiyet = txtCinsiyet.getText().trim().toUpperCase().charAt(0);
                
                // Eski bilgileri sakla
                String eskiIsim = ogr.getIsim();
                String eskiSoyad = ogr.getSoyad();
                float eskiGano = ogr.getGano();
                int eskiSinif = ogr.getSinif();
                char eskiCinsiyet = ogr.getCinsiyet();
                
                ogr.setIsim(isim);
                ogr.setSoyad(soyad);
                ogr.setGano(gano);
                ogr.setSinif(sinif);
                ogr.setCinsiyet(cinsiyet);
                
                sistem.ogrenciGuncelle(ogr);
                sonucAlani.append("Öğrenci güncellendi: " + ogrNo + " - " + isim + " " + soyad + "\n");
                formTemizle();
                updateOgrenciSayisi();
                tumOgrencileriListele();
                
                // ÖZEL BİLDİRİM - GÜNCELLEME
                showBildirim("Öğrenci Güncellendi", 
                    "Öğrenci bilgileri başarıyla güncellendi!\n\n" +
                    "📋 GÜNCELLENEN BİLGİLER:\n" +
                    "───────────────────────\n" +
                    "• Öğrenci No: " + ogrNo + "\n\n" +
                    "📝 ESKİ BİLGİLER:\n" +
                    "• İsim: " + eskiIsim + "\n" +
                    "• Soyad: " + eskiSoyad + "\n" +
                    "• GANO: " + eskiGano + "\n" +
                    "• Cinsiyet: " + (eskiCinsiyet == 'E' ? "Erkek" : "Kız") + "\n" +
                    "• Sınıf: " + eskiSinif + "\n\n" +
                    "📝 YENİ BİLGİLER:\n" +
                    "• İsim: " + isim + "\n" +
                    "• Soyad: " + soyad + "\n" +
                    "• GANO: " + gano + "\n" +
                    "• Cinsiyet: " + (cinsiyet == 'E' ? "Erkek" : "Kız") + "\n" +
                    "• Sınıf: " + sinif + "\n\n" +
                    "✅ Öğrenci başarıyla güncellendi!", 
                    "success");
            } else {
                showBildirim("Hata", 
                    ogrNo + " numaralı öğrenci bulunamadı!\n\n" +
                    "Lütfen geçerli bir öğrenci numarası giriniz.", 
                    "error");
            }
        } catch (Exception ex) {
            showBildirim("Hata", "Geçerli veri giriniz!\n\nHata: " + ex.getMessage(), "error");
        }
    }
    
    private void ogrenciSil() {
        try {
            int ogrNo = Integer.parseInt(txtOgrNo.getText().trim());
            Ogrenci ogr = sistem.ogrenciNoIleAra(ogrNo);
            
            if (ogr != null) {
                // Silinmeden önce bilgileri sakla
                String isim = ogr.getIsim();
                String soyad = ogr.getSoyad();
                float gano = ogr.getGano();
                int sinif = ogr.getSinif();
                char cinsiyet = ogr.getCinsiyet();
                
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Aşağıdaki öğrenciyi silmek istiyor musunuz?\n\n" +
                    "İsim: " + isim + "\n" +
                    "Soyad: " + soyad + "\n" +
                    "No: " + ogrNo + "\n" +
                    "GANO: " + gano + "\n" +
                    "Sınıf: " + sinif,
                    "Öğrenci Silme Onayı", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    if (sistem.ogrenciSil(ogrNo)) {
                        sonucAlani.append("Öğrenci silindi: " + ogrNo + " - " + isim + " " + soyad + "\n");
                        formTemizle();
                        updateOgrenciSayisi();
                        tumOgrencileriListele();
                        
                        // ÖZEL BİLDİRİM - SİLME
                        showBildirim("Öğrenci Silindi", 
                            "Öğrenci başarıyla listeden silindi!\n\n" +
                            "📋 SİLİNEN ÖĞRENCİ BİLGİLERİ:\n" +
                            "──────────────────────────\n" +
                            "• İsim: " + isim + "\n" +
                            "• Soyad: " + soyad + "\n" +
                            "• No: " + ogrNo + "\n" +
                            "• GANO: " + gano + "\n" +
                            "• Cinsiyet: " + (cinsiyet == 'E' ? "Erkek" : "Kız") + "\n" +
                            "• Sınıf: " + sinif + "\n\n" +
                            "✅ Öğrenci başarıyla silindi!", 
                            "success");
                    }
                }
            } else {
                showBildirim("Hata", 
                    ogrNo + " numaralı öğrenci bulunamadı!\n\n" +
                    "Lütfen geçerli bir öğrenci numarası giriniz.", 
                    "error");
            }
        } catch (Exception ex) {
            showBildirim("Hata", "Geçerli öğrenci numarası giriniz!\n\nHata: " + ex.getMessage(), "error");
        }
    }
    
    private void ogrenciNoIleAra() {
        try {
            int ogrNo = Integer.parseInt(txtAramaOgrNo.getText().trim());
            Ogrenci ogr = sistem.ogrenciNoIleAra(ogrNo);
            
            if (ogr != null) {
                sonucAlani.append("Bulunan Öğrenci: " + ogr + "\n");
                txtAramaOgrNo.setText("");
                
                List<Ogrenci> tekOgrenci = java.util.Arrays.asList(ogr);
                tabloyuGuncelle(tekOgrenci);
                
                // ÖZEL BİLDİRİM - BULUNDU
                showBildirim("Öğrenci Bulundu", 
                    "Arama sonucu: ÖĞRENCİ BULUNDU!\n\n" +
                    "📋 BULUNAN ÖĞRENCİ BİLGİLERİ:\n" +
                    "──────────────────────────\n" +
                    "• İsim: " + ogr.getIsim() + "\n" +
                    "• Soyad: " + ogr.getSoyad() + "\n" +
                    "• No: " + ogr.getOgrNo() + "\n" +
                    "• GANO: " + ogr.getGano() + "\n" +
                    "• Cinsiyet: " + (ogr.getCinsiyet() == 'E' ? "Erkek" : "Kız") + "\n" +
                    "• Sınıf: " + ogr.getSinif() + "\n\n" +
                    "✅ Öğrenci başarıyla bulundu!", 
                    "success");
            } else {
                showBildirim("Arama Sonucu", 
                    "Arama sonucu: ÖĞRENCİ BULUNAMADI!\n\n" +
                    "Aranan Öğrenci No: " + ogrNo + "\n\n" +
                    "⚠️ Bu numaraya kayıtlı öğrenci bulunamadı.\n" +
                    "Lütfen öğrenci numarasını kontrol ediniz.", 
                    "warning");
            }
        } catch (Exception ex) {
            showBildirim("Hata", "Geçerli öğrenci numarası giriniz!\n\nHata: " + ex.getMessage(), "error");
        }
    }
    
    private void adIleAra() {
        String isim = txtAramaIsim.getText().trim();
        if (!isim.isEmpty()) {
            List<Ogrenci> sonuclar = sistem.adIleAra(isim);
            sonucAlani.append("İsim ile arama sonuçları (" + isim + "):\n");
            for (Ogrenci ogr : sonuclar) {
                sonucAlani.append("   " + ogr + "\n");
            }
            if (sonuclar.isEmpty()) {
                sonucAlani.append("   Öğrenci bulunamadı!\n");
                showBildirim("Arama Sonucu", 
                    "Arama sonucu: ÖĞRENCİ BULUNAMADI!\n\n" +
                    "Aranan İsim: '" + isim + "'\n\n" +
                    "⚠️ Bu isme kayıtlı öğrenci bulunamadı.\n" +
                    "Lütfen ismi kontrol ediniz.", 
                    "warning");
            } else {
                sonucAlani.append("   Toplam " + sonuclar.size() + " öğrenci bulundu.\n");
                tabloyuGuncelle(sonuclar);
                showBildirim("Arama Sonucu", 
                    "Arama sonucu: " + sonuclar.size() + " ÖĞRENCİ BULUNDU!\n\n" +
                    "Aranan İsim: '" + isim + "'\n\n" +
                    "📊 BULUNAN ÖĞRENCİLER:\n" +
                    "─────────────────────\n" +
                    getOgrenciListesi(sonuclar) + "\n" +
                    "✅ Toplam " + sonuclar.size() + " öğrenci bulundu!", 
                    "success");
            }
            txtAramaIsim.setText("");
        } else {
            showBildirim("Hata", "Arama için isim giriniz!", "error");
        }
    }
    
    private String getOgrenciListesi(List<Ogrenci> ogrenciler) {
        StringBuilder sb = new StringBuilder();
        int sayac = 0;
        for (Ogrenci ogr : ogrenciler) {
            if (sayac < 5) { // İlk 5 öğrenciyi göster
                sb.append("• ").append(ogr.getIsim()).append(" ").append(ogr.getSoyad())
                  .append(" (").append(ogr.getOgrNo()).append(") - GANO: ").append(ogr.getGano()).append("\n");
                sayac++;
            }
        }
        if (ogrenciler.size() > 5) {
            sb.append("• ... ve ").append(ogrenciler.size() - 5).append(" öğrenci daha\n");
        }
        return sb.toString();
    }
    
    private void tumOgrencileriListele() {
        List<Ogrenci> ogrenciler = sistem.tumOgrencileriGetir();
        tabloyuGuncelle(ogrenciler);
        sonucAlani.append("Tüm öğrenciler listelendi (GANO sıralı). Toplam: " + ogrenciler.size() + " öğrenci\n");
        showBildirim("Listeleme Tamamlandı", 
            "TÜM ÖĞRENCİLER LİSTELENDİ!\n\n" +
            "📊 LİSTELEME BİLGİLERİ:\n" +
            "─────────────────────\n" +
            "• Sıralama: GANO'ya göre (büyükten küçüğe)\n" +
            "• Toplam Öğrenci: " + ogrenciler.size() + "\n" +
            "• Listeleme Tarihi: " + new java.util.Date() + "\n\n" +
            "✅ Tüm öğrenciler başarıyla listelendi!", 
            "info");
    }
    
    private void sinifaGoreListele() {
        String sinifStr = JOptionPane.showInputDialog(this, "Sınıf giriniz (1-4):", "Sınıf Seçimi", JOptionPane.QUESTION_MESSAGE);
        if (sinifStr != null && !sinifStr.trim().isEmpty()) {
            try {
                int sinif = Integer.parseInt(sinifStr.trim());
                List<Ogrenci> ogrenciler = sistem.sinifaGoreGetir(sinif);
                tabloyuGuncelle(ogrenciler);
                sonucAlani.append(sinif + ". sınıf öğrencileri listelendi. Sayı: " + ogrenciler.size() + " öğrenci\n");
                showBildirim("Listeleme Tamamlandı", 
                    "SINIF LİSTESİ HAZIR!\n\n" +
                    "📊 LİSTELEME BİLGİLERİ:\n" +
                    "─────────────────────\n" +
                    "• Sınıf: " + sinif + "\n" +
                    "• Toplam Öğrenci: " + ogrenciler.size() + "\n" +
                    "• Sıralama: GANO'ya göre (büyükten küçüğe)\n\n" +
                    "✅ " + sinif + ". sınıf öğrencileri başarıyla listelendi!", 
                    "info");
            } catch (NumberFormatException ex) {
                showBildirim("Hata", "Geçerli bir sınıf numarası giriniz!", "error");
            }
        }
    }
    
    private void cinsiyeteGoreListele() {
        String cinsiyetStr = JOptionPane.showInputDialog(this, "Cinsiyet giriniz (E/K):", "Cinsiyet Seçimi", JOptionPane.QUESTION_MESSAGE);
        if (cinsiyetStr != null && !cinsiyetStr.trim().isEmpty()) {
            char cinsiyet = cinsiyetStr.trim().toUpperCase().charAt(0);
            List<Ogrenci> ogrenciler = sistem.cinsiyeteGoreGetir(cinsiyet);
            tabloyuGuncelle(ogrenciler);
            String cinsiyetAdi = (cinsiyet == 'E') ? "Erkek" : "Kız";
            sonucAlani.append(cinsiyetAdi + " öğrenciler listelendi. Sayı: " + ogrenciler.size() + " öğrenci\n");
            showBildirim("Listeleme Tamamlandı", 
                "CİNSİYET LİSTESİ HAZIR!\n\n" +
                "📊 LİSTELEME BİLGİLERİ:\n" +
                "─────────────────────\n" +
                "• Cinsiyet: " + cinsiyetAdi + "\n" +
                "• Toplam Öğrenci: " + ogrenciler.size() + "\n" +
                "• Sıralama: GANO'ya göre (büyükten küçüğe)\n\n" +
                "✅ " + cinsiyetAdi + " öğrenciler başarıyla listelendi!", 
                "info");
        }
    }
    
    private void hashTablosunuGoster() {
        String hashBilgisi = sistem.hashTablosunuGoster();
        sonucAlani.append("Hash Tablosu İçeriği:\n" + hashBilgisi + "\n");
        
        JTextArea hashTextArea = new JTextArea(hashBilgisi);
        hashTextArea.setEditable(false);
        hashTextArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollPane = new JScrollPane(hashTextArea);
        scrollPane.setPreferredSize(new Dimension(700, 500));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Hash Tablosu Detayı", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void raporlariOlustur() {
        sistem.raporlariOlustur();
        sonucAlani.append("Raporlama tamamlandı! 6 rapor dosyası oluşturuldu.\n");
        
        showBildirim("Raporlama Tamamlandı", 
            "📈 RAPORLAMA İŞLEMİ TAMAMLANDI!\n\n" +
            "✅ 6 adet rapor dosyası başarıyla oluşturuldu:\n\n" +
            "📄 OLUŞTURULAN RAPORLAR:\n" +
            "───────────────────────\n" +
            "1. ogrenciler.txt - GANO sıralı tüm öğrenciler\n" +
            "2. sinif_sirasi.txt - Sınıf sıralı liste\n" +
            "3. ogrenci_no_sirali.txt - Öğrenci no sıralı liste\n" +
            "4. bolum_sirasi.txt - Bölüm sıralı liste\n" +
            "5. hash_index.txt - Hash tablosu içeriği\n" +
            "6. performans.txt - Performans istatistikleri\n\n" +
            "🗂️ Raporlar proje klasöründe oluşturuldu.", 
            "success");
    }
    
    private void tabloyuGuncelle(List<Ogrenci> ogrenciler) {
        tableModel.setRowCount(0);
        for (Ogrenci ogr : ogrenciler) {
            tableModel.addRow(new Object[]{
                ogr.getIsim(), 
                ogr.getSoyad(), 
                ogr.getOgrNo(), 
                String.format("%.2f", ogr.getGano()), 
                ogr.getSinif(), 
                (ogr.getCinsiyet() == 'E') ? "Erkek" : "Kız"
            });
        }
    }
    
    private void formTemizle() {
        txtIsim.setText("");
        txtSoyad.setText("");
        txtOgrNo.setText("");
        txtGANO.setText("");
        txtSinif.setText("");
        txtCinsiyet.setText("");
    }
    
    private void updateOgrenciSayisi() {
        Container contentPane = getContentPane();
        for (Component comp : contentPane.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                if (panel.getComponentCount() > 0) {
                    Component child = panel.getComponent(0);
                    if (child instanceof JLabel) {
                        JLabel label = (JLabel) child;
                        if (label.getText().contains("Toplam Öğrenci")) {
                            label.setText(" Toplam Öğrenci: " + sistem.getToplamOgrenciSayisi() + " | BMÜ3311 Veri Yönetimi Ödevi");
                            break;
                        }
                    }
                }
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new OgrenciKayitSistemiGUI();
        });
    }
}