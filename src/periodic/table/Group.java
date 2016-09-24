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
package periodic.table;

import alanutilites.util.text.Text;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 *
 * @author Alan Tsui
 */
public class Group extends Hitbox{
    private String name;
    private ArrayList<ElementTile> tiles;
    
    private Font font;
    private double stringHeight;
    private double fontSize;
    
    private boolean disabled;
    
    private Color backgroundColor;
    private Color foregroundColor;
    private Color selectedColor;
    private double strokeSize;
    
    private BasicStroke stroke;
    
    private boolean selected;
    public Group(String name, Rectangle2D.Double hitbox, Color backgroundColor, Color foregroundColor, Color selectedColor){
        super(hitbox);
        this.name = name;
        this.tiles = new ArrayList<>();
        strokeSize = 2;
        
        this.backgroundColor = backgroundColor != null ? backgroundColor : Color.DARK_GRAY;
        this.foregroundColor = foregroundColor != null ? foregroundColor : Color.BLACK;
        this.selectedColor = selectedColor != null ? selectedColor : Color.GRAY;
        
        fontSize = 8;
        font = new Font(Font.SANS_SERIF, 0, (int)fontSize);
        stringHeight = Text.getHeightOfString(font);
    }
    
    public void addElement(ElementTile tile){
        tiles.add(tile);
    }
    
    public void removeElement(ElementTile tile){
        tiles.remove(tile);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public void resize(double widthToResizeBy, double heightToResizeBy){
        super.resize(widthToResizeBy,heightToResizeBy);
        strokeSize = strokeSize*widthToResizeBy;
        fontSize = fontSize*widthToResizeBy;
        stringHeight = stringHeight*heightToResizeBy;
        font = new Font(Font.SANS_SERIF, 0, (int)fontSize);
        stroke = new BasicStroke((float)strokeSize);
    }

    @Override
    public void render(Graphics2D gd) {
        gd.setFont(font);
        gd.setColor(backgroundColor);
        gd.fill(getHitbox());
        gd.setColor(foregroundColor);
        char c[] = name.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        String newGroupName = new String(c);
        String[] groupName = Text.prettyStringWrap(newGroupName, (int)getHitbox().getWidth(), font);
        int x = (int)getHitbox().getX();
        int y = (int)getHitbox().getY();
        for(String string : groupName){
            if(!string.isEmpty()){
                Text.drawCenteredString(string, x, y, (int)(getHitbox().getWidth()), (int)getHitbox().getHeight()/groupName.length, gd);
                y+=(int)getHitbox().getHeight()/groupName.length;
            }
        }
        if(selected){
            gd.setColor(selectedColor);
            gd.setStroke(stroke);
            gd.drawRect((int)getHitbox().x, (int)getHitbox().y, (int)getHitbox().width, (int)getHitbox().height);
        }
    }

    public ArrayList<ElementTile> getTiles() {
        return tiles;
    }

    public void setTiles(ArrayList<ElementTile> tiles) {
        this.tiles = tiles;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public double getFontSize() {
        return fontSize;
    }

    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    public double getStrokeSize() {
        return strokeSize;
    }

    public void setStrokeSize(double strokeSize) {
        this.strokeSize = strokeSize;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
