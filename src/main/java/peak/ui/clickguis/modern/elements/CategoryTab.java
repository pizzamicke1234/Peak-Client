package peak.ui.clickguis.modern.elements;

import peak.managers.font.FontUtil;
import peak.managers.render.RenderManager;
import peak.modules.Module.Category;

import java.awt.*;

public class CategoryTab  {

    public Category category;
    public int x, y, width, height;

    public CategoryTab(Category category, int x, int y, int width, int height) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(int mouseX, int mouseY) {
        final Color c = new Color(70,70, 70, 230);
        final Color cHover = new Color(80,80, 80, 230);

        Color color = isHovered(mouseX, mouseY) ? cHover : c;

        RenderManager.drawRoundedRect(x, y, width, height, 2, color);

        String n = category.name().toLowerCase();
        String name = n.substring(0, 1).toUpperCase() + n.substring(1);

        FontUtil.normal.drawCenteredString(name, x + (float) width / 2, y + (float) height / 2 - ((float) FontUtil.normal.getHeight() / 2), -1);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (mouseX > x && mouseX < (x + width) && mouseY > y && mouseY < (y + height));
    }

}
