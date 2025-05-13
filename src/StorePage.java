import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StorePage extends BackgroundPanel2 {
    public StorePage(AppFrame frame) {
        setLayout(new BorderLayout());

        // --- MARGIN / BORDER ---
        int mx = (int)(Toolkit.getDefaultToolkit().getScreenSize().width * 0.04);
        int my = (int)(Toolkit.getDefaultToolkit().getScreenSize().height * 0.05);
        setBorder(new EmptyBorder(my, mx, my, mx));

        // --- TOP BAR ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        // Sol üst: Başlık
        JLabel title = new JLabel("BILKENT HELPER | STORE");
        title.setFont(new Font("Avenir Next", Font.PLAIN, 20));
        title.setForeground(Color.WHITE);
        JPanel tl = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tl.setOpaque(false);
        tl.add(title);
        topBar.add(tl, BorderLayout.WEST);

        // Sağ üst: Geri ve Profil ikonları
        JPanel tr = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        tr.setOpaque(false);
        tr.add(iconButton("logos/go-back-logo.png", () -> frame.showPage("main")));
        tr.add(iconButton("logos/profile-icon.png", () -> frame.showPage("profile")));
        topBar.add(tr, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);
    }

    // 40x40 ikon butonu oluşturan yardımcı metod
    private JButton iconButton(String path, Runnable onClick) {
        ImageIcon raw = new ImageIcon(path);
        Image img = raw.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton btn = new JButton(new ImageIcon(img));
        btn.setPreferredSize(new Dimension(40, 40));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.addActionListener(e -> onClick.run());
        return btn;
    }
}
