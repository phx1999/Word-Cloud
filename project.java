import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.List;

class Scribbling {
    String what;
    int xpos;
    int ypos;
    double angle;
    Font font;
    Color col;
    Color bgCol;
}

class WritePanel extends JPanel {
    //private Graphics2D g;
    private ArrayList<Scribbling> text = new ArrayList<Scribbling>();

    private void doWriting(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Font font = getFont();
        Color col = g.getColor();
        double angle = 0;
        AffineTransform orig = g2d.getTransform();

        for (Scribbling s : text) {
            if (s.angle != angle) {
                g2d.setTransform(orig);
                g2d.rotate(s.angle * Math.PI / 180.0);
                angle = s.angle;
            }
            if (s.font != null && !s.font.equals(font)) {
                g2d.setFont(s.font);
                font = g2d.getFont();
            }
            if (!s.col.equals(col)) {
                col = new Color(s.col.getRGB());
                g2d.setColor(col);
            }
            TextLayout layout = new TextLayout(
                    s.what, font, g2d.getFontRenderContext()
            );
            if (s.bgCol != null) {
                Rectangle2D bounds = layout.getBounds();
                bounds.setRect(
                        bounds.getX() + s.xpos,
                        bounds.getY() + s.ypos,
                        bounds.getWidth(),
                        bounds.getHeight()
                );
                Color preColor = g2d.getColor();
                g2d.setColor(s.bgCol);
                g2d.fill(bounds);
                g2d.setColor(preColor);
            }
            layout.draw(g2d, s.xpos, s.ypos);

            // g2d.drawString(s.what, s.xpos, s.ypos);
        }
    }

    public void addText(
            String what, int xpos, int ypos, double angle,
            Font font, Color col, Color bgCol
    ) {
        Scribbling s = new Scribbling();
        s.what = what;
        s.xpos = xpos;
        s.ypos = ypos;
        s.angle = angle;
        s.font = font;
        s.col = col;
        s.bgCol = bgCol;
        text.add(s);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doWriting(g);
    }
}

/**
 * Textboard is in fact a full graphical application that displays
 * text in a graphical Window
 */

public class project  extends JFrame implements ActionListener  {
    JTextField t1, t2, t3,t4;
    JLabel b1, b2, b3,b4;
    JButton jb;
    JPanel jp;
    static Color bgcol;
    static Color chcol;
    private WritePanel board;
    private double currentAngle;
    private Color currentColor;
    private Font currentFont;

    public project(int width, int height, Color col) {
        super("词云");
        jp = new JPanel();
        b1 = new JLabel("背景  R");
        b2 = new JLabel("G");
        b3 = new JLabel("B");
        b4 = new JLabel("text");
        t1 = new JTextField(3);
        t2 = new JTextField(3);
        t3 = new JTextField(3);
        t4 = new JTextField(20);
        jb = new JButton("生成");
        jb.addActionListener(this::actionPerformed);
        board = new WritePanel();
        jp.add(b1);
        jp.add(t1);
        jp.add(b2);
        jp.add(t2);
        jp.add(b3);
        jp.add(t3);
        jp.add(b4);
        jp.add(t4);
        jp.add(jb);
        board.add(jp);
        board.setLayout(new FlowLayout());
        add(board, BorderLayout.CENTER);

        board.setOpaque(true);
        board.setBackground(col);
        // Initial settings
        currentAngle = 0;
        currentColor = Color.BLACK;
        currentFont = getFont();
        // Geometry of the main window
        setSize(width, height);
        setResizable(false);
        setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static void setBgcol(Color bgcol) {
        project.bgcol = bgcol;
    }

    /**
     * Get a Rectangle bounds for a String s when write in font at (x,y)
     *
     * @param s    The text to write
     * @param font Font to use to write the text
     * @param x    Horizontal position in pixels, from the left hand side
     * @param y    Vertical position in pixels, from the top
     */
    public Rectangle2D getBounds(String s, Font font, int x, int y) {
        setVisible(true);
        Graphics2D g2d = ((Graphics2D) getGraphics());
        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout layout = new TextLayout(s, font, frc);
        Rectangle2D bounds = layout.getBounds();
        bounds.setRect(
                bounds.getX() + x,
                bounds.getY() + y,
                bounds.getWidth(),
                bounds.getHeight()
        );
        setVisible(false);
        return bounds;
    }

    // Overloaded methods to write text

    /**
     * Write text to the screen
     * <p>
     * The text can be written at any position, it can be
     * rotated, the font and color can be changed. Every piece
     * of text is added to a list, and only written when the
     * window is drawn.
     *
     * @param what  The text to write
     * @param xpos  Horizontal position in pixels, from the left hand side
     * @param ypos  Vertical position in pixels, from the top
     * @param angle Rotation angle in degrees
     * @param font  Font to use to write the text
     * @param col   Text color
     * @param bgCol Background color
     */

    public void write(
            String what, int xpos, int ypos, double angle,
            Font font, Color col, Color bgCol
    ) {
        if (currentAngle != angle) {
            currentAngle = angle;
        }
        if (currentFont != null && !currentFont.equals(font)) {
            currentFont = font;
        }
        if (!currentColor.equals(col)) {
            currentColor = new Color(col.getRGB());
        }
        board.addText(what, xpos, ypos, angle, font, col, bgCol);
    }

    public void write(
            String what, int xpos, int ypos, double angle, Font f, Color c
    ) {
        write(what, xpos, ypos, angle, f, c, null);
    }

    public void write(
            String what, int xpos, int ypos, Font f, Color c, Color bg
    ) {
        write(what, xpos, ypos, 0.0, f, c, bg);
    }

    /**
     * Write text to the screen
     * <p>
     * Use the last set angle (0 by default), font and color
     * (black by default).
     *
     * @param what The text to write
     * @param xpos Horizontal position in pixels, from the left hand side
     * @param ypos Vertical position in pixels, from the top
     */
    public void write(String what, int xpos, int ypos) {
        write(what, xpos, ypos, currentAngle, currentFont, currentColor);
    }

    /**
     * Write text to the screen
     * <p>
     * Use the last set font and color (black by default), set the angle.
     *
     * @param what  The text to write
     * @param xpos  Horizontal position in pixels, from the left hand side
     * @param ypos  Vertical position in pixels, from the top
     * @param angle Rotation angle in degrees
     */
    public void write(String what, int xpos, int ypos, double angle) {
        write(what, xpos, ypos, angle, currentFont, currentColor);
    }

    /**
     * Write text to the screen
     * <p>
     * Use the last set font, set angle and color.
     *
     * @param what  The text to write
     * @param xpos  Horizontal position in pixels, from the left hand side
     * @param ypos  Vertical position in pixels, from the top
     * @param angle Rotation angle in degrees
     * @param col   Text color
     */
    public void write(
            String what, int xpos, int ypos, double angle, Color c
    ) {
        write(what, xpos, ypos, angle, currentFont, c);
    }

    /**
     * Write text to the screen
     * <p>
     * Use the last set angle(0 by default) and font, set the color.
     *
     * @param what The text to write
     * @param xpos Horizontal position in pixels, from the left hand side
     * @param ypos Vertical position in pixels, from the top
     * @param col  Text color
     */
    public void write(String what, int xpos, int ypos, Color c) {
        write(what, xpos, ypos, currentAngle, currentFont, c);
    }

    /**
     * Write text to the screen
     * <p>
     * Use the last set angle (0 by default), set font and color.
     *
     * @param what The text to write
     * @param xpos Horizontal position in pixels, from the left hand side
     * @param ypos Vertical position in pixels, from the top
     * @param font Font to use to write the text
     * @param col  Text color
     */
    public void write(
            String what, int xpos, int ypos, Font f, Color c
    ) {
        write(what, xpos, ypos, currentAngle, f, c);
    }

    /**
     * Show the window
     */
    public void display() {
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        //String file= t4.getText().trim();
        File file= new File(t4.getText().trim());
        String text=method.readFile(file);
        java.util.List<String> key = method.getkeywords(text, 140);
        Collections.sort(key);
        java.util.List<String> ac = method.controlsize(key);
        System.out.println(ac.size());
        java.util.List<String> abc = method.fenci(text);
        java.util.List<String> bcd = method.getcharacter(abc);
        Collections.sort(bcd);
        List<String> c = method.controlsize(bcd);
        List<Integer> number= method.wordsnumber(ac,c);
        List [] ci=method.collection(ac,number);
        ciyu[] ciyu = new ciyu[ac.size()];
        List<String> words = ci[0];
        List<Integer> frequence = ci[1];
        System.out.println(words);
        System.out.println(frequence);
        int max =frequence.get(0);
        int min=frequence.get(frequence.size()-1);
        for (int i = 0; i < words.size(); i++) {
            if (frequence.get(i)> 0.8 * (max-min)) {
                Font font = new Font("华文行楷", Font.PLAIN, 70);
                int x = new Random().nextInt(255 - 0) + 0;
                int y = new Random().nextInt(255 - 0) + 0;
                int z = new Random().nextInt(255 - 0) + 0;
                Color color = new Color(x,y,z);
                Object wor = words.get(i);
                String word = String.valueOf(wor);
                ciyu ciyu1 = new ciyu(word, font, color);
                ciyu[i] = ciyu1;
            }
            if (frequence.get(i) <= 0.8 * (max - min) && frequence.get(i) > 0.5 * (max - min)) {
                Font font = new Font("华文行楷", Font.PLAIN, 50);
                int x = new Random().nextInt(255 - 0) + 0;
                int y = new Random().nextInt(255 - 0) + 0;
                int z = new Random().nextInt(255 - 0) + 0;
                Color color = new Color(x,y,z);
                Object wor = words.get(i);
                String word = String.valueOf(wor);
                ciyu ciyu1 = new ciyu(word, font, color);
                ciyu[i] = ciyu1;
            }
            if (frequence.get(i) <= 0.5 * (max - min) && frequence.get(i) > 0.2 * (max - min)) {
                Font font = new Font("华文行楷", Font.PLAIN, 30);
                int x = new Random().nextInt(255 - 0) + 0;
                int y = new Random().nextInt(255 - 0) + 0;
                int z = new Random().nextInt(255 - 0) + 0;
                Color color = new Color(x,y,z);
                Object wor = words.get(i);
                String word = String.valueOf(wor);
                ciyu ciyu1 = new ciyu(word, font, color);
                ciyu[i] = ciyu1;
            }
            if (frequence.get(i) <=0.2 * (max - min)) {
                Font font = new Font("华文行楷", Font.PLAIN, 25);
                int x = new Random().nextInt(255 - 0) + 0;
                int y = new Random().nextInt(255 - 0) + 0;
                int z = new Random().nextInt(255 - 0) + 0;
                Color color = new Color(x,y,z);
                Object wor = words.get(i);
                String word = String.valueOf(wor);
                ciyu ciyu1 = new ciyu(word, font, color);
                ciyu[i] = ciyu1;
            }
        }
        if (e.getActionCommand().equals("生成")) {
            int r = Integer.parseInt(t1.getText());
            int g = Integer.parseInt(t2.getText());
            int b = Integer.parseInt(t3.getText());
            if (r >= 0 && r <= 255 && g >= 0 && g <= 255 && b >= 0 && b <= 255) {
                bgcol = new Color(r, g, b);
                jp.setBackground(bgcol);
                project tb = new project(800, 800, bgcol);
                Rectangle2D fanwei[] = new Rectangle2D[ciyu.length];
                int num=0;
                label:
                for (int i = 0; i < ciyu.length; i++) {
                    int x = new Random().nextInt(700 - 100) + 100;
                    int y = new Random().nextInt(700 - 100) + 100;
                    if (num>30){
                        Font font = new Font("华文行楷", Font.PLAIN, 20);
                        ciyu[i].setFont(font);
                    }
                    if (num>40){
                        Font font = new Font("华文行楷", Font.PLAIN, 15);
                        ciyu[i].setFont(font);
                    }

                    if (num>50){
                        Font font = new Font("华文行楷", Font.PLAIN, 10);
                        ciyu[i].setFont(font);
                    }
                    fanwei[i] = tb.getBounds(ciyu[i].getWord(), ciyu[i].getFont(), x, y);

                    double x1=(x-400)*(x-400)+(y-400)*(y-400);
                    double x2=(x-400)*(x-400)+(y+fanwei[i].getHeight()-400)*(y+fanwei[i].getHeight()-400);
                    double x3=(x+fanwei[i].getHeight() - 400) * (x + fanwei[i].getHeight() - 400) + (y + fanwei[i].getHeight() - 400) * (y + fanwei[i].getHeight() - 400);
                    double x4=(x+fanwei[i].getWidth() - 400) * (x + fanwei[i].getWidth() - 400) + (y- 400) * (y- 400);
                    for (int j = 0; j < i; j++) {
                        if (fanwei[i].intersects(fanwei[j])||x1>100000||x2>100000||x3>100000||x4>100000) {
                            i = i - 1;
                            num=num+1;
                            continue label;
                        } else continue;
                    }
                    tb.write(ciyu[i].getWord(), x, y, 0, ciyu[i].getFont(), ciyu[i].getColor());
                    num=0;
                }
                tb.display();
                method.creatjpg(tb);
            }
        }

    }


    public static void main(String[] args) {
        new  project(800, 800, bgcol);
    }
}

