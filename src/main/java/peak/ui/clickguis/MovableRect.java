package peak.ui.clickguis;

import net.minecraft.client.gui.Gui;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.List;

public class MovableRect {

    public int left;
    public int top;
    public int right;
    public int bottom;
    public int color;

    public int left1;
    public int top1;
    public int right1;
    public int bottom1;
    public int firstclickposX;
    public int firstclickposY;

    public MovableRect(int left, int top, int right, int bottom, int color) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.color = color;

        this.left1 = left;
        this.top1 = top;
        this.right1 = right;
        this.bottom1 = bottom;
    }

    public boolean isClicked(int x, int y) {
        if(this.left <= x && x <= this.right && this.top <= y && y <= this.bottom) {
            //Gets the first click offset to correctly move the rect later on
            if(firstclickposX == 0 && firstclickposY == 0) {
                firstclickposX = x;
                firstclickposY = y;
            }

            return true;
        }
        return  false;
    }

    /*
    *   Calculates the offset of the mouse position to the rect position
    *   Returns the offsets for each rect side
    */
    public List<Integer> getClickOffset(int x, int y) {
        List<Integer> ints = new ArrayList<Integer>();

        ints.add(this.left1 - x);
        ints.add(this.top1 - y);
        ints.add(this.right1 - x);
        ints.add(this.bottom1 - y);

        return ints;
    }

    public void move(int x, int y) {
        List<Integer> offsets = getClickOffset(firstclickposX, firstclickposY);
        this.left = x + offsets.get(0);
        this.top = y + offsets.get(1);
        this.right = x + offsets.get(2);
        this.bottom = y + offsets.get(3);
    }

    public void draw() {
        Gui.drawRect(this.left, this.top, this.right, this.bottom, this.color);
    }

}
