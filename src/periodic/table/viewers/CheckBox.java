/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Alan Tsui
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package periodic.table.viewers;

import alanutilites.util.text.Text;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import periodic.table.Hitbox;

/**
 *
 * @author Alan Tsui
 */
public class CheckBox extends Hitbox{
    private boolean selected;
    private String text;
    private double offset;
    private double stroke;
    private BasicStroke currentStroke;
    private final BasicStroke DEFAULT_STROKE;
    private Font font; 
    private double fontSize;
    public CheckBox(String text, Rectangle2D.Double hitbox) {
        super(hitbox);
        this.text = text;
        fontSize = 8;
        offset = 2;
        stroke = 1;
        font = new Font(Font.SANS_SERIF, 0, (int)fontSize);
        getHitbox().setRect(getHitbox().x, getHitbox().y, getHitbox().height+Text.getLengthOfString(text, font)+offset, getHitbox().height);
        currentStroke = new BasicStroke((float)stroke);
        DEFAULT_STROKE = new BasicStroke(1);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void render(Graphics2D gd) {
        gd.setColor(Color.BLACK);
        gd.setFont(font);
        gd.setStroke(currentStroke);
        gd.drawRect((int)getHitbox().x, (int)getHitbox().y, (int)getHitbox().height, (int)getHitbox().height);
        gd.setStroke(DEFAULT_STROKE);
        if(selected){
            Text.drawCenteredString("✔", (int)getHitbox().x, (int)getHitbox().y, (int)getHitbox().height, (int)getHitbox().height/2, gd);
        }
        Text.drawCenteredLeftString(text, (int)(getHitbox().x+getHitbox().height+offset), (int)getHitbox().y, (int)(getHitbox().width-getHitbox().height), (int)getHitbox().height, gd);
    }

    @Override
    public void resize(double widthToResizeBy, double heightToResizeBy) {
        super.resize(widthToResizeBy, heightToResizeBy);
        offset = offset*widthToResizeBy;
        currentStroke = new BasicStroke((float)(stroke*=widthToResizeBy));
        fontSize = fontSize*widthToResizeBy;
        font = new Font(Font.SANS_SERIF, 0, (int)fontSize);
    }
    
    public void toggle(){
        selected = !selected;
    }
}
