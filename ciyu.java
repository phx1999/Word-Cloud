import java.awt.*;
import java.util.List;

public class ciyu {
    String word;
    Font font;
    Color color;

    public ciyu(String a, Font b, Color c) {
        this.word = a;
        this.font = b;
        this.color = c;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Color getColor() {
        return color;
    }

    public Font getFont() {
        return font;
    }

    public String getWord() {
        return word;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
