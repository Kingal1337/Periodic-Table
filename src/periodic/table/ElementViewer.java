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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import periodic.table.config.BrowserOpener;
import periodic.table.config.Config;
import periodic.table.viewers.APanel;

/**
 *
 * @author Alan Tsui
 */
public class ElementViewer extends APanel implements MouseWheelListener,MouseListener,MouseMotionListener{  
    private ElementTile element;
    private ElementTile elementView;
    
    private double nameFontSize;
    private Color backgroundColor;
    
    private ArrayList<Information> information;
    
    private Color currentButtonColor;
    private Rectangle2D.Double moreInfoButton;
    
    private double lineWidth;
    private double fontSize;
    
    private boolean browserInUse;
    
    public ElementViewer(ElementTile element){
        this.element = element;
        elementView = new ElementTile(element.getElement(), new Rectangle2D.Double(), element.getBackgroundColor(), element.getForegroundColor(), element.getSelectedColor());     
        elementView.setOtherTextFontNumber(12);
        elementView.setAtomicNumberFontSize(28);
        backgroundColor = element.getColor(element.getElement().getType());
        fontSize = 12;
        nameFontSize = 24;
        information = convertInformationToStrings(element.getElement());
        moreInfoButton = new Rectangle2D.Double(getSize().width/2+5, ((((double)getSize().height)*(1.0/4.0)))/2, 100, 30);
        currentButtonColor = Color.GRAY;
        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    private ArrayList<Information> convertInformationToStrings(Element element){
        ArrayList<Information> info = new ArrayList<>();
        info.add(new Information("Atomic Number", element.getAtomicNumber()));
        info.add(new Information("Symbol", element.getSymbol()));
        info.add(new Information("Name", element.getName()));
        info.add(new Information("Weight", (element.getWeight().isEmpty() ? "--" : element.getWeight())+" u"));
        info.add(new Information("Type", element.getType()));
        info.add(new Information("Density", (element.getDensity().isEmpty() ? "--" : element.getDensity())+" g/cm³"));
        info.add(new Information("Bonding Type", element.getBondingType()));
        info.add(new Information("Boiling Point", (element.getBoilingPoint().isEmpty() ? "--" : element.getBoilingPoint())+" °K"));
        info.add(new Information("Melting Point", (element.getMeltingPoint().isEmpty() ? "--" : element.getMeltingPoint())+" °K"));
        info.add(new Information("Electron Configuration", element.getElections()));
        info.add(new Information("Year Discovered", element.getYearDiscovered()));
        int[] pen = element.getPEN(0);
        info.add(new Information("# of Protons", pen[0]+""));
        info.add(new Information("# of Electrons", pen[1]+""));
        info.add(new Information("# of Neutrons", pen[2]+""));
        return info;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gd = (Graphics2D)g;
        
        gd.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        gd.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        double height = ((double)getSize().height-(((double)getSize().height*(1.0/4.0))))/(double)(information.size());
        double x = 0;
        double y = ((((double)getSize().height)*(1.0/4.0)));
        double width = (double)getSize().width/2.0;
        Color currentColor = Color.WHITE;
        
        gd.setColor(backgroundColor);
        gd.fillRect(0, 0, getSize().width, (int)y);
                
        gd.setColor(Color.BLACK);
        gd.setFont(new Font(Font.SANS_SERIF, 0, (int)nameFontSize));
        Text.drawCenteredString(element.getElement().getName(), 0, 0, getSize().width/2, (int)y, gd);
        
        gd.setFont(new Font(Font.SANS_SERIF, 0, (int)fontSize));
        
        gd.setColor(currentButtonColor);
        gd.fill(moreInfoButton);
        gd.setColor(Color.BLACK);
        Text.drawCenteredString("More Info", (int)moreInfoButton.x, (int)moreInfoButton.y, (int)moreInfoButton.width, (int)moreInfoButton.height, gd);
        
        for(Information info : information){
            gd.setColor(currentColor);
            gd.fillRect((int)x, (int)y, (int)width, (int)height);
            gd.fillRect((int)(x+width), (int)y, (int)width, (int)height);
            gd.setColor(Color.BLACK);
            gd.drawLine((int)(x+width), (int)y, (int)(x+width), (int)(y+height));
            Text.drawCenteredLeftString(Text.getLengthOfString(info.getTitle(), gd.getFont())>width ? Text.ellipsisText(info.getTitle(), (int)width, gd.getFont()) : info.getTitle(), (int)(x+3), (int)y, (int)width, (int)height, gd);
            Text.drawCenteredLeftString(info.getValue(), (int)(x+3+width), (int)y, (int)width, (int)height, gd);
            if(currentColor == Color.WHITE){
                currentColor = Color.LIGHT_GRAY;
            }
            else{
                currentColor = Color.WHITE;
            }
            y+=height;
        }
    }

    @Override
    public void resizeComponents(Dimension previousSize, Dimension dimensions) {
        super.resizeComponents(previousSize, dimensions);
        double widthToResizeBy = (double)dimensions.width/(double)previousSize.width;
        double heightToResizeBy = (double)dimensions.height/(double)previousSize.height;
        
        lineWidth = lineWidth*widthToResizeBy;
        fontSize = fontSize*heightToResizeBy;
        nameFontSize = nameFontSize*widthToResizeBy;
        
        elementView.resize(widthToResizeBy, heightToResizeBy);   
        elementView.getHitbox().setRect((((double)getSize().width/2)-(((((double)getSize().height)*(1.0/4.0)))-10))/2, 5, ((((double)getSize().height)*(1.0/4.0)))-10, ((((double)getSize().height)*(1.0/4.0)))-10);
        moreInfoButton.setRect(((getSize().width+(getSize().width/2))-(moreInfoButton.width*widthToResizeBy))/2, (((((double)getSize().height)*(1.0/4.0))))/2, moreInfoButton.width*widthToResizeBy, moreInfoButton.height*heightToResizeBy);
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        repaint();
    }    
    
    private void viewMoreInfo(boolean useSystemBrowser){
//        if(!browserInUse){
//            browserInUse = true;
//            if(!useSystemBrowser){
//                JFrame frame = new JFrame();
//                Wiki panel = new Wiki(Config.WIKI_LINK+"/"+element.getElement().getName());
//
//                frame.add(panel);
//
//                frame.addWindowListener(new WindowAdapter() {
//                    @Override
//                    public void windowClosed(WindowEvent e) {
//                        super.windowClosed(e);
//                        browserInUse = false;
//                    }
//                });
//
//                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//                frame.setResizable(true);
//                frame.setMinimumSize(Config.MINIMUM_FRAME_SIZE);
//                frame.setPreferredSize(Config.MINIMUM_FRAME_SIZE);
//
//                frame.pack();
//                frame.setLocationRelativeTo(null);
//
//                frame.setVisible(true);   
//            }
//        }
//        if(useSystemBrowser){
            BrowserOpener.open(Config.WIKI_LINK+"/"+element.getElement().getName());
//        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        
    }

    @Override
    public void mousePressed(MouseEvent me) {
        int x = me.getX();
        int y = me.getY();
        int button = me.getButton();
        if(moreInfoButton.contains(x,y)){
//            if(button == MouseEvent.BUTTON1){
//                viewMoreInfo(false);
//            }
//            else if(button == MouseEvent.BUTTON3){
                viewMoreInfo(true);
//            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        
    }

    @Override
    public void mouseExited(MouseEvent me) {
        
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        int x = me.getX();
        int y = me.getY();
        if(moreInfoButton.contains(x,y)){
            currentButtonColor = Color.LIGHT_GRAY;
        }
        else{
            currentButtonColor = Color.GRAY;
        }
        repaint();
    }
    
    private class Information{
        private String title;
        private String value;
        public Information(String title, String value){
            this.title = title;
            this.value = value.isEmpty() ? "N/A" : value;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}

