package peak.managers;

import java.awt.*;

public class ColorManager {

    public static int getColorWave(long offset) {
        Color color1 = new Color(255, 0, 255);
        Color color2 = new Color(0, 255, 200);

        float speed = 1.5f;

        double time = (System.currentTimeMillis() + offset) / (speed * 1000.0);
        float t = (float) (Math.sin(time) * 0.5 + 0.5);

        int r = (int) (color1.getRed() + t * (color2.getRed() - color1.getRed()));
        int g = (int) (color1.getGreen() + t * (color2.getGreen() - color1.getGreen()));
        int b = (int) (color1.getBlue() + t * (color2.getBlue() - color1.getBlue()));

        return (255 << 24) | (r << 16) | (g << 8) | b;
    }

    public static int getRainbowWave(float seconds, long offset) {
        float hue = ((System.currentTimeMillis() + offset) % (int)(seconds * 1000)) / (seconds * 1000f);
        return Color.HSBtoRGB(hue, 1f, 1f);
    }

}
