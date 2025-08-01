package java_project;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedImagePanel extends JPanel {
    private final Image image;
    private final int cornerRadius = 30;
    private final int shadowOffset = 6;
    private final Color shadowColor = new Color(0, 0, 0, 60);

    public RoundedImagePanel(Image image) {
        this.image = image;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(shadowColor);
        g2.fillRoundRect(shadowOffset, shadowOffset, width - shadowOffset, height - shadowOffset, cornerRadius, cornerRadius);

        Shape clip = new RoundRectangle2D.Float(0, 0, width - shadowOffset, height - shadowOffset, cornerRadius, cornerRadius);
        g2.setClip(clip);
        g2.drawImage(image, 0, 0, width - shadowOffset, height - shadowOffset, this);

        g2.dispose();
    }
}