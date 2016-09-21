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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import periodic.table.config.Config;

/**
 *
 * @author Alan Tsui
 */
public class ElementTile extends Hitbox{    
    private Element element;
    private double atomicNumberFontSize;
    private double otherTextFontNumber;
    private Font symbolFont;
    private Font otherTextFont;
    
    private boolean disabled;
    
    private double textOffset;
    private double textSpacing;
    
    private boolean groupColor;
    
    private Color disabledForegroundColor;
    private Color disabledBackgroundColor;
    private Color backgroundColor;
    private Color foregroundColor;
    private Color selectedColor;
    private double strokeSize;
    private boolean selected;
    
    private boolean selectable;

    public ElementTile(Element element, Rectangle2D.Double hitbox, Color backgroundColor, Color foregroundColor, Color selectedColor) {
        super(hitbox);
        this.element = element;
        this.backgroundColor = backgroundColor != null ? backgroundColor : Color.DARK_GRAY;
        this.foregroundColor = foregroundColor != null ? foregroundColor : Color.BLACK;
        this.selectedColor = selectedColor != null ? selectedColor : Color.GRAY;
        disabledForegroundColor = Color.GRAY;
        disabledBackgroundColor = Color.LIGHT_GRAY;
        
        strokeSize = 2;
        textOffset = 1;
        textSpacing = 5;
        
        groupColor = true;
        
        atomicNumberFontSize = 8;
        otherTextFontNumber = 5;
        
        symbolFont = new Font(Font.SANS_SERIF, 1, (int)atomicNumberFontSize);
        otherTextFont = new Font(Font.SANS_SERIF, 0, (int)otherTextFontNumber);
        
        selectable = true;
    }
    
    @Override
    public void render(Graphics2D gd){
        Color previousColor = gd.getColor();
        if(groupColor){
            gd.setColor(getColor(element.getType()));
        }
        else{            
            gd.setColor(backgroundColor);
        }
        if(disabled){
            gd.setColor(disabledBackgroundColor);
        }
        gd.fill(getHitbox());
        
        gd.setColor(foregroundColor);
        if(disabled){
            gd.setColor(disabledForegroundColor);
        }
        
        int otherTextFontSize = Text.getHeightOfString(otherTextFont);
        int symbolFontSize = Text.getHeightOfString(symbolFont);
        
        gd.setFont(otherTextFont);
        gd.drawString(element.getAtomicNumber()+"", (int)(getHitbox().x+textOffset), (int)(getHitbox().y+textOffset+otherTextFontSize));
        
        int lengthOfSymbol = Text.getLengthOfString(element.getSymbol(), symbolFont);
        String symbol = lengthOfSymbol >= (getHitbox().width-textOffset) ? Text.ellipsisText(element.getSymbol(), (int)getHitbox().width, symbolFont) : element.getSymbol();
        if(symbol.equals("Symbol")){
            System.out.println(getHitbox().width+" "+lengthOfSymbol);
        }
        gd.setFont(symbolFont);
        gd.drawString(symbol, (int)(getHitbox().x+textOffset), (int)(getHitbox().y+textOffset+otherTextFontSize+symbolFontSize));
                
        int lengthOfElementName = Text.getLengthOfString(element.getName(), otherTextFont);
        String elementName = lengthOfElementName >= (getHitbox().width-textOffset) ? Text.ellipsisText(element.getName(), (int)getHitbox().width, otherTextFont) : element.getName();
        gd.setFont(otherTextFont);
        gd.drawString(elementName, (int)(getHitbox().x+textOffset), (int)(getHitbox().y+textOffset+textOffset+otherTextFontSize+symbolFontSize+otherTextFontSize));
        
        int lengthOfMass = Text.getLengthOfString(element.getWeight(), otherTextFont);
        String massName = lengthOfMass >= (getHitbox().width-textOffset) ? Text.ellipsisText(element.getWeight(), (int)getHitbox().width, otherTextFont) : element.getWeight();
        gd.setFont(otherTextFont);
        gd.drawString(massName, (int)(getHitbox().x+textOffset), (int)(getHitbox().y+textOffset+textOffset+textOffset+otherTextFontSize+symbolFontSize+otherTextFontSize+otherTextFontSize));
        
        
        if(selected){
            gd.setColor(selectedColor);
            gd.setStroke(new BasicStroke((float)strokeSize));
            gd.drawRect((int)getHitbox().x, (int)getHitbox().y, (int)getHitbox().width, (int)getHitbox().height);
        }
        
        gd.setColor(previousColor);
    }
    
    public Color getColor(String group){
        if(group == null || group.isEmpty()){
            return backgroundColor;
        }
        String newGroup = group.replaceAll("-", "_").replaceAll(" ","_").toUpperCase();
        switch(newGroup){
            case "METALLOID":
                return Config.METALLOID;
            case "NONMETAL":
                return Config.NONMETAL;
            case "HALOGEN":
                return Config.HALOGEN;
            case "NOBLE_GAS":
                return Config.NOBLE_GAS;
            case "ALKALI_METAL":
                return Config.ALKALI_METAL;
            case "ALKALINE_EARTH_METAL":
                return Config.ALKALINE_EARTH_METAL;
            case "LANTHANOID":
                return Config.LANTHANOID;
            case "ACTINOID":
                return Config.ACTINOID;
            case "TRANSITION_METAL":
                return Config.TRANSITION_METAL;
            case "POST_TRANSITION_METAL":
                return Config.POST_TRANSITION_METAL;
            default:
                return backgroundColor;
        }
    }
    
    @Override
    public void resize(double widthToResizeBy, double heightToResizeBy){
        super.resize(widthToResizeBy,heightToResizeBy);
        strokeSize = strokeSize*widthToResizeBy;
        textOffset = textOffset*widthToResizeBy;
        
        atomicNumberFontSize = atomicNumberFontSize*widthToResizeBy;
        otherTextFontNumber = otherTextFontNumber*widthToResizeBy;
        
        textSpacing = textSpacing*heightToResizeBy;
        
        symbolFont = new Font(Font.SANS_SERIF, 1, (int)atomicNumberFontSize);
        otherTextFont = new Font(Font.SANS_SERIF, 0, (int)otherTextFontNumber);
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public double getTextOffset() {
        return textOffset;
    }

    public void setTextOffset(double textOffset) {
        this.textOffset = textOffset;
    }

    public double getStrokeSize() {
        return strokeSize;
    }

    public void setStrokeSize(double strokeSize) {
        this.strokeSize = strokeSize;
    }

    public double getAtomicNumberFontSize() {
        return atomicNumberFontSize;
    }

    public void setAtomicNumberFontSize(double atomicNumberFontSize) {
        this.atomicNumberFontSize = atomicNumberFontSize;
    }

    public double getOtherTextFontNumber() {
        return otherTextFontNumber;
    }

    public void setOtherTextFontNumber(double otherTextFontNumber) {
        this.otherTextFontNumber = otherTextFontNumber;
    }

    public Font getSymbolFont() {
        return symbolFont;
    }

    public void setSymbolFont(Font symbolFont) {
        this.symbolFont = symbolFont;
    }

    public Font getOtherTextFont() {
        return otherTextFont;
    }

    public void setOtherTextFont(Font otherTextFont) {
        this.otherTextFont = otherTextFont;
    }

    public boolean showGroupColor() {
        return groupColor;
    }

    public void setGroupColor(boolean groupColor) {
        this.groupColor = groupColor;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public double getTextSpacing() {
        return textSpacing;
    }

    public void setTextSpacing(double textSpacing) {
        this.textSpacing = textSpacing;
    }

    public Color getDisabledForegroundColor() {
        return disabledForegroundColor;
    }

    public void setDisabledForegroundColor(Color disabledForegroundColor) {
        this.disabledForegroundColor = disabledForegroundColor;
    }

    public Color getDisabledBackgroundColor() {
        return disabledBackgroundColor;
    }

    public void setDisabledBackgroundColor(Color disabledBackgroundColor) {
        this.disabledBackgroundColor = disabledBackgroundColor;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }
}
