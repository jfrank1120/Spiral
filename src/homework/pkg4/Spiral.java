package homework.pkg4;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import javax.swing.JComponent;

/**
 * Draws a SPIRAL.
 * 
 * @author Jared Frank
 */
public class Spiral extends JComponent {

    private static Color background_Color = Color.BLACK;
    private static Color SPIRAL_Color = Color.MAGENTA;
    private boolean nautilusShell = false;

    // The radius of a curve at a given point is defined to be the radius of the circular arc that 
    // best approximates the curve at that point. In the case of a SPIRAL, the radius describes how 
    // tightly it winds around its center. The smaller the radius, the tighter the winding.    
    private int radius;
    private double lineLength = 1.0;
    
    // Initializes the Sprial with the given radius
    public Spiral(int radius, Color spiralColor, Color backgroundColor, double lineLength) {
        this.radius = radius;
    }   
    
    // Sets the color of the background of the spiral
    public void setBackgroundColor(Color inputColor){
        this.background_Color = inputColor;
        repaint();
    }
    
    public Color getBackgroundColor() {
        return this.background_Color;
    }
    
    // Sets the color of the spiral
    public void setSpiralColor(Color inputColor){
        this.SPIRAL_Color = inputColor;
        repaint();
    }
    
    public Color getSpiralColor() {
        return this.SPIRAL_Color;
    }
    
    // Sets the line length
    public void setLineLength(double inputLength){
        this.lineLength = inputLength;
        repaint();
    }
    
    public double getLineLength() {
        return this.lineLength;
    }
    
    // Sets the radius of the spiral
    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }
    
    public int getRadius() {
        return this.radius;
    }
    
    // Turns on/off the Nautilus Shell feature
    void setNautilusShell(boolean nautilusShell) {
        this.nautilusShell = nautilusShell;
        repaint();
    }

    // Draws the spiral
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));

        int w = getWidth();
        int h = getHeight();
        final int LINE_THICKNESS = 2;
        g2.setStroke(new BasicStroke(LINE_THICKNESS));

        // Paint the background.
        g2.setColor(background_Color);
        g2.fill(new Rectangle(w, h));
        g2.setColor(SPIRAL_Color);
        g2.translate(w / 2, h / 2);

        // We move a point around the center of a circle, steadily increasing its distance from the
        // origin. At each step, a line is drawn to the current point from the previous one. These
        // lines will be small enough that the drawing will appear as a smooth curve rather than a 
        // patchwork of line segments.
        double x1 = 0.0;
        double y1 = 0.0;
        final double STEP = lineLength / 5.0; // proportional to line segment length
        for (double a = 0.0; a < 6 * Math.PI; a += STEP) { // wrap around center 3 times

            // calculate position of next point
            double x2 = radius * a * Math.cos(a);
            double y2 = radius * a * Math.sin(a);

            // draw line from (x1, y1) to (x2, y2) and update the current point to the next one
            g2.draw(new Line2D.Double(x1, y1, x2, y2));
            x1 = x2;
            y1 = y2;

            if (nautilusShell) {
                g2.draw(new Line2D.Double(0, 0, x2, y2));
            }
        }
    }
}

