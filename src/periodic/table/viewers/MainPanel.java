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

import alanutilites.timer.Timer;
import alanutilites.util.text.Text;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import periodic.table.Element;
import periodic.table.ElementTile;
import periodic.table.ElementViewer;
import periodic.table.Group;
import periodic.table.config.Config;

/**
 *
 * @author Alan Tsui
 */
public class MainPanel extends APanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener, MouseWheelListener{
    private Timer timer;
    private ElementTile displayElement;
    private ArrayList<ArrayList<ElementTile>> elementHitboxes;
    private ArrayList<ArrayList<Element>> allElements;
    private ArrayList<Group> groups;
    
    private Element elementInstructions;
    private Element lanthanoidLabel;
    private Element actinoidLabel;
    
    private Rectangle2D.Double creatorPosition;
    
    private double currentTemperature;
    private final double maxTemperature = 9999;
    
    private double temperatureSliderFontSize;
    private double temperatureSliderFontOffset;
    private Rectangle2D.Double temperatureSlider;
    private Rectangle2D.Double temperatureThumb;
    private Rectangle2D.Double temperatureNumber;
    
    private boolean temperatureNumberSelected;
    
    private double temperatureStrokeSize;
    private BasicStroke temperatureStroke;
    
    private Font temperatureFont;
    private ImageIcon temperatureImage;
    private boolean temperatureClicked;
    
    private final BasicStroke DEFAULT_STROKE = new BasicStroke(1);
    
    private int gcCounter;
    
    private CheckBox showSymbol;
    private CheckBox showName;
    private CheckBox showAtomNumber;
    private CheckBox showWeight;
    private CheckBox lockMovement;
    
    private double previousTemperature;
    private String currentText;//for temperature when user double clicks ontemperature
        
    private Point mousePoint;
    
    private double maxScrollAmount;
    private double currentScrollAmount;
    
    private Point2D.Double defaultPoint;
    private double defaultOffsetX;
    private double defaultOffsetY;
    
    public MainPanel(ArrayList<ArrayList<Element>> allElements){
        this.allElements = allElements;
        elementHitboxes = new ArrayList<>();
        groups = new ArrayList<>();
        
        creatorPosition = new Rectangle2D.Double(98, 226, 428, 34);
        
        elementInstructions = new Element("Atomic #","Symbol", "Name", "Weight", null, null, null, null, null, null, null, 2, 1);
        lanthanoidLabel = new Element("","57-71", "", "", "lanthanoid", "10000", "10000", null, null, null, null, 3, 6);
        actinoidLabel = new Element("","89-103", "", "", "actinoid", "10000", "10000", null, null, null, null, 3, 7);
        
        this.allElements.get(this.allElements.size()-1).add(lanthanoidLabel);
        this.allElements.get(this.allElements.size()-1).add(actinoidLabel);
                
        displayElement = new ElementTile(null, new Rectangle2D.Double(70,15,70,70), Color.DARK_GRAY, Color.BLACK, Color.GRAY);
        displayElement.setOtherTextFontNumber(10);
        displayElement.setAtomicNumberFontSize(24);
        initElements();
        initSizeAndPosition(0,0);
        initGroups();
        initCheckBoxes();
        
        for(int i=0;i<elementHitboxes.size();i++){
            for(int j=0;j<elementHitboxes.get(i).size();j++){
                ElementTile tile = elementHitboxes.get(i).get(j);
                if(tile != null && tile.getElement().equals(elementInstructions)){
                    Color transparent = new Color(0,0,0,0);
                    tile.setBackgroundColor(transparent);
                    tile.setForegroundColor(Color.BLACK);
                    tile.setDisabledBackgroundColor(transparent);
                    tile.setDisabledForegroundColor(Color.GRAY);
                    tile.setSelectable(false);
                }
                if(tile != null && (tile.getElement() == lanthanoidLabel || tile.getElement() == actinoidLabel)){
                    tile.setSelectable(false);
                }
            }
        }
        
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addKeyListener(this);
        setFocusable(true);
        
        currentTemperature = 0;
        currentText = "";
        
        defaultPoint = new Point2D.Double(1,1);
        defaultOffsetX = 2;
        defaultOffsetY = 2;
        
        timer = new Timer(16, this);
        timer.start();
        initTemperatureSlider();
        adjustTemperature("273");
        adjustSlider(273);
        adjustComponentSize(1, 1);
        adjustComponentPostitons(0,0);
        
    }
    
    public void adjustSlider(int kelvin){
        temperatureThumb.x = (((double)kelvin)*temperatureSlider.width)/maxTemperature+temperatureSlider.x;
        currentTemperature = kelvin;
    }
    
    public void adjustTemperature(String kelvin){
        for(int i=0;i<elementHitboxes.size();i++){
            for(int j=0;j<elementHitboxes.get(i).size();j++){
                ElementTile element = elementHitboxes.get(i).get(j);
                if(element != null){
                    int state = element.getElement().getStateAtCurrentTemp(kelvin);
                    switch (state) {
                        case 0:
                            elementHitboxes.get(i).get(j).setForegroundColor(Config.SOLID_COLOR);
                            break;
                        case 1:
                            elementHitboxes.get(i).get(j).setForegroundColor(Config.LIQUID_COLOR);
                            break;
                        case 2:
                            elementHitboxes.get(i).get(j).setForegroundColor(Config.GAS_COLOR);
                            break;
                        default:
                            elementHitboxes.get(i).get(j).setForegroundColor(Config.UNKNOWN_COLOR);
                            break;
                    }
                }
            }
        }
    }
    
    private void initCheckBoxes(){
        showSymbol = new CheckBox("Symbol", new Rectangle2D.Double(390, 5, 10, 5));
        showSymbol.setSelected(true);
        
        showName = new CheckBox("Name", new Rectangle2D.Double(430, 5, 10, 5));
        showName.setSelected(true);
        
        showAtomNumber = new CheckBox("Number", new Rectangle2D.Double(465, 5, 10, 5));
        showAtomNumber.setSelected(true);
        
        showWeight = new CheckBox("Weight", new Rectangle2D.Double(505, 5, 10, 5));
        showWeight.setSelected(true);
        
        lockMovement = new CheckBox("Lock", new Rectangle2D.Double(390,25, 10, 5));
        lockMovement.setSelected(true);
    }
    
    private void initTemperatureSlider(){
        temperatureStrokeSize = 1;
        temperatureSliderFontSize = 8;
        temperatureSliderFontOffset = 5;
        temperatureSlider = new Rectangle2D.Double(416, 18, 98, 3);
        temperatureThumb = new Rectangle2D.Double(416, 15.5, 4, 9);
        temperatureImage = new ImageIcon(MainPanel.class.getResource("/periodic/table/config/resources/temperature.png"));
        
        temperatureNumber = new Rectangle2D.Double(0, 0, 25, 8);
        temperatureNumberSelected = false;
    }
    
    private void initGroups(){
        int offset = 2;
        int ogX = 185;
        int ogY = 5;
        int x = ogX;
        int y = ogY;
        int width = 50;
        int height = 20;
        int rows = 4;
        int col = 3;
        int rCounter = 0;
        int cCounter = 0;
        for(int i=0;i<elementHitboxes.size();i++){
            for(int j=0;j<elementHitboxes.get(i).size();j++){
                ElementTile element = elementHitboxes.get(i).get(j);
                if(element != null){
                    int groupIndex = -1;
                    String groupString = element.getElement().getType();
                    if(groupString != null && !groupString.isEmpty()){
                        for (int g=0;g<groups.size();g++) {
                            if(groups.get(g).getName().equals(groupString)){
                                groupIndex = g;
                                break;
                            }
                        }
                        if(groupIndex == -1){
                            groups.add(new Group(groupString, new Rectangle2D.Double(x, y, width, height), element.getColor(groupString), Color.BLACK, Color.GRAY));
                            cCounter++;
                            x += width+offset;
                            if(cCounter >= col){
                                cCounter = 0;
                                y += height+offset;
                                rCounter++;
                                x = ogX;
                            }
                            if(rCounter >= rows){
                                rCounter = 0;
                            }
                        }
                        else{
                            groups.get(groupIndex).addElement(element);
                        }
                    }
                }
            }
        }
    }
    
    private void initElements(){
        for(int i=0;i<allElements.size();i++){
            ArrayList<ElementTile> currentArrayList = new ArrayList<>();
            for(int j=0;j<allElements.get(i).size();j++){
                if(!allElements.get(i).get(j).equals(Element.EMPTY_ELEMENT)){
                    ElementTile tile = new ElementTile(
                            allElements.get(i).get(j),
                            new Rectangle2D.Double(), Color.DARK_GRAY,
                            Color.BLACK, Color.GRAY);
                    currentArrayList.add(tile);
                }
                else{
                    currentArrayList.add(null);
                }
            }
            elementHitboxes.add(currentArrayList);
        }
    }
    
    private void initSizeAndPosition(double initX, double initY){
        double width = 30;
        double height = 30;
        double offset = 2;
        double x = initX+offset;
        double y = initY;
        
        for(int i=0;i<elementHitboxes.size();i++){
            for(int j=0;j<elementHitboxes.get(i).size();j++){
                if(elementHitboxes.get(i).get(j) != null){
                    ElementTile tile = elementHitboxes.get(i).get(j);
                    tile.getHitbox().setRect(((tile.getElement().getGroup()-1)*(width+offset))+x, ((tile.getElement().getRow()-1)*(height+offset))+y+offset, width, height);
                }
            }
        }
    }
    
    private void reset(){
        for(int i=0;i<10;i++){
            double elementX = elementHitboxes.get(0).get(0).getHitbox().x;
            double elementY = elementHitboxes.get(0).get(0).getHitbox().y;
            double diffX = defaultOffsetX-elementX;
            double diffY = defaultOffsetY-elementY;

            adjustComponentPostitons(diffX, diffY);
            double width = ((elementHitboxes.get(0).size())*elementHitboxes.get(0).get(0).getHitbox().width)+((elementHitboxes.get(0).size()+1)*defaultOffsetX);
            double height = ((elementHitboxes.size())*elementHitboxes.get(0).get(0).getHitbox().height)+((elementHitboxes.size()+1)*defaultOffsetY);
            double resizeW = (double)getSize().width/width;
            double resizeH = (double)getSize().height/height;
            resizeComponents(resizeW, resizeH);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gd  = (Graphics2D)g;
        
        gd.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        gd.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        gd.drawImage(temperatureImage.getImage(), (int)temperatureSlider.x, (int)temperatureSlider.y, (int)temperatureSlider.width, (int)temperatureSlider.height, null);
        gd.setColor(Color.BLACK);
        gd.setStroke(temperatureStroke);
        gd.drawRect((int)temperatureSlider.x, (int)temperatureSlider.y, (int)temperatureSlider.width, (int)temperatureSlider.height);
        gd.setStroke(DEFAULT_STROKE);
        gd.setColor(Color.WHITE);
        gd.fill(temperatureThumb);
        gd.setColor(Color.BLACK);
        gd.setStroke(temperatureStroke);
        gd.drawRect((int)temperatureThumb.x, (int)temperatureThumb.y, (int)temperatureThumb.width, (int)temperatureThumb.height);
        gd.setStroke(DEFAULT_STROKE);
        
        if(temperatureNumberSelected){
            gd.setColor(Color.WHITE);
            gd.fill(temperatureNumber);
        }
        gd.setFont(temperatureFont);
        gd.setColor(Color.BLACK);
        gd.drawString((currentTemperature+"").substring(0, (currentTemperature+"").indexOf("."))+" K", (int)(temperatureSlider.x+temperatureSlider.width+temperatureSliderFontOffset), (int)(temperatureSlider.y+temperatureSlider.height));
        
        
        showSymbol.render(gd);
        showName.render(gd);
        showAtomNumber.render(gd);
        showWeight.render(gd);
        lockMovement.render(gd);
        
        Text.drawCenteredString("Copyright © "+Config.CREATOR+" 2016", (int)creatorPosition.x, (int)creatorPosition.y, (int)creatorPosition.width, (int)creatorPosition.height, gd);
        
        if(displayElement.getElement() != null){
            displayElement.render(gd);
        }
            
        for(int i=0;i<elementHitboxes.size();i++){
            for(int j=0;j<elementHitboxes.get(i).size();j++){
                if(elementHitboxes.get(i).get(j) != null){
                    if(elementHitboxes.get(i).get(j).getHitbox().intersects(0, 0, getSize().width, getSize().height)){
                        elementHitboxes.get(i).get(j).render(gd);
                    }
                }
            }
        }
            
        for(int i=0;i<groups.size();i++){
            if(groups.get(i) != null){
                if(groups.get(i).getHitbox().intersects(0, 0, getSize().width, getSize().height)){
                    groups.get(i).render(gd);
                }
            }
        }
    }
    
    private void adjustComponentSize(double widthResize, double heightResize){
        resizeComponents(widthResize, heightResize);
    }
    
    private void adjustComponentPostitons(double diffX, double diffY){
        creatorPosition.setRect(creatorPosition.x+diffX, creatorPosition.y+diffY, creatorPosition.width, creatorPosition.height);
        temperatureSlider.setRect(temperatureSlider.x+diffX, temperatureSlider.y+diffY, temperatureSlider.width, temperatureSlider.height);
        temperatureThumb.setRect(temperatureThumb.x+diffX, temperatureThumb.y+diffY, temperatureThumb.width, temperatureThumb.height);
        temperatureNumber.setRect(temperatureNumber.x+diffX, temperatureNumber.y+diffY, temperatureNumber.width, temperatureNumber.height);
        showSymbol.getHitbox().setRect(showSymbol.getHitbox().x+diffX, showSymbol.getHitbox().y+diffY, showSymbol.getHitbox().width, showSymbol.getHitbox().height);
        showName.getHitbox().setRect(showName.getHitbox().x+diffX, showName.getHitbox().y+diffY, showName.getHitbox().width, showName.getHitbox().height);
        showAtomNumber.getHitbox().setRect(showAtomNumber.getHitbox().x+diffX, showAtomNumber.getHitbox().y+diffY, showAtomNumber.getHitbox().width, showAtomNumber.getHitbox().height);
        showWeight.getHitbox().setRect(showWeight.getHitbox().x+diffX, showWeight.getHitbox().y+diffY, showWeight.getHitbox().width, showWeight.getHitbox().height);
        lockMovement.getHitbox().setRect(lockMovement.getHitbox().x+diffX, lockMovement.getHitbox().y+diffY, lockMovement.getHitbox().width, lockMovement.getHitbox().height);
        displayElement.getHitbox().setRect(displayElement.getHitbox().x+diffX, displayElement.getHitbox().y+diffY, displayElement.getHitbox().width, displayElement.getHitbox().height);
        for(int i=0;i<elementHitboxes.size();i++){
            for(int j=0;j<elementHitboxes.get(i).size();j++){
                if(elementHitboxes.get(i).get(j) != null){
                    elementHitboxes.get(i).get(j).getHitbox().setRect(elementHitboxes.get(i).get(j).getHitbox().x+diffX, elementHitboxes.get(i).get(j).getHitbox().y+diffY, elementHitboxes.get(i).get(j).getHitbox().width, elementHitboxes.get(i).get(j).getHitbox().height);
                }
            }
        }
            
        for(int i=0;i<groups.size();i++){
            if(groups.get(i) != null){
                groups.get(i).getHitbox().setRect(groups.get(i).getHitbox().x+diffX, groups.get(i).getHitbox().y+diffY, groups.get(i).getHitbox().width, groups.get(i).getHitbox().height);
            }
        }
    }

    @Override
    public void resizeComponents(Dimension previousSize, Dimension dimensions) {
        super.resizeComponents(previousSize, dimensions);
        double widthToResizeBy = (double)dimensions.width/(double)previousSize.width;
        double heightToResizeBy = (double)dimensions.height/(double)previousSize.height;
        resizeComponents(widthToResizeBy, heightToResizeBy);
        defaultOffsetX = defaultOffsetX*widthToResizeBy;
        defaultOffsetY = defaultOffsetY*heightToResizeBy;
    }
    
    private void resizeComponents(double widthToResizeBy, double heightToResizeBy){
        creatorPosition.setRect(creatorPosition.x*widthToResizeBy, creatorPosition.y*heightToResizeBy, creatorPosition.width*widthToResizeBy, creatorPosition.height*heightToResizeBy);
        temperatureFont = new Font(Font.SANS_SERIF, 0, (int)temperatureSliderFontSize);
        
        temperatureSliderFontSize = temperatureSliderFontSize*widthToResizeBy;
        temperatureSliderFontOffset = temperatureSliderFontOffset*widthToResizeBy;
        
        temperatureStrokeSize = temperatureStrokeSize*widthToResizeBy;
        temperatureStroke = new BasicStroke((float)temperatureStrokeSize);
        
        temperatureSlider.setRect(temperatureSlider.x*widthToResizeBy, temperatureSlider.y*heightToResizeBy, temperatureSlider.width*widthToResizeBy, temperatureSlider.height*heightToResizeBy);
        temperatureThumb.setRect(temperatureThumb.x*widthToResizeBy, temperatureThumb.y*heightToResizeBy, temperatureThumb.width*widthToResizeBy, temperatureThumb.height*heightToResizeBy);
        
        temperatureNumber.setRect((int)(temperatureSlider.x+temperatureSlider.width+temperatureSliderFontOffset), (int)(temperatureSlider.y-temperatureSlider.height-(temperatureSlider.height/2)), temperatureNumber.width*widthToResizeBy, temperatureNumber.height*heightToResizeBy);
        
        showSymbol.resize(widthToResizeBy, heightToResizeBy);
        showName.resize(widthToResizeBy, heightToResizeBy);
        showAtomNumber.resize(widthToResizeBy, heightToResizeBy);
        showWeight.resize(widthToResizeBy, heightToResizeBy);
        lockMovement.resize(widthToResizeBy, heightToResizeBy);
        
        displayElement.resize(widthToResizeBy, heightToResizeBy);
        for(int i=0;i<elementHitboxes.size();i++){
            for(int j=0;j<elementHitboxes.get(i).size();j++){
                if(elementHitboxes.get(i).get(j) != null){
                    elementHitboxes.get(i).get(j).resize(widthToResizeBy, heightToResizeBy);
                }
            }
        }
            
        for(int i=0;i<groups.size();i++){
            if(groups.get(i) != null){
                groups.get(i).resize(widthToResizeBy, heightToResizeBy);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gcCounter++;
        if(gcCounter >= 1000/16){
            gcCounter = 0;
            System.gc();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int button = e.getButton();
        if(button == MouseEvent.BUTTON1){
            if(temperatureThumb.contains(x, y)){
                temperatureClicked = true;
            }

            if(temperatureNumber.contains(x, y)){
                temperatureNumberSelected = true;
                previousTemperature = currentTemperature;
                currentText = "";
            }
            else{
                temperatureNumberSelected = false;            
            }

            if(showSymbol.getHitbox().contains(x, y)){
                showSymbol.toggle();
            }
            if(showAtomNumber.getHitbox().contains(x, y)){
                showAtomNumber.toggle();
            }
            if(showName.getHitbox().contains(x, y)){
                showName.toggle();
            }
            if(showWeight.getHitbox().contains(x, y)){
                showWeight.toggle();
            }
            if(lockMovement.getHitbox().contains(x, y)){
                lockMovement.toggle();
            }
            if(showSymbol.getHitbox().contains(x, y) || showAtomNumber.getHitbox().contains(x, y) || showName.getHitbox().contains(x, y) || showWeight.getHitbox().contains(x, y)){
                for(int i=0;i<elementHitboxes.size();i++){
                    for(int j=0;j<elementHitboxes.get(i).size();j++){
                        ElementTile tile = elementHitboxes.get(i).get(j);
                        if(tile != null && tile.isSelectable()){
                            tile.setShowSymbol(showSymbol.isSelected());
                            tile.setShowName(showName.isSelected());
                            tile.setShowAtomNumber(showAtomNumber.isSelected());
                            tile.setShowWeight(showWeight.isSelected());
                        }
                    }
                }
            }
            for(int i=0;i<elementHitboxes.size();i++){
                for(int j=0;j<elementHitboxes.get(i).size();j++){
                    ElementTile tile = elementHitboxes.get(i).get(j);
                    if(tile != null){
                        if(tile.getHitbox().contains(x, y)){
                            if(e.getClickCount() == 2){
                                if(tile.isSelectable()){
                                    MainFrame frame = new MainFrame();
                                    ElementViewer panel = new ElementViewer(tile);

                                    frame.setTitle(tile.getElement().getName());

                                    frame.changePanel(panel);

                                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                    frame.setResizable(true);
                                    frame.setMinimumSize(new Dimension(400,400));
                                    frame.setPreferredSize(frame.getMinimumSize());

                                    frame.pack();
                                    frame.setLocationRelativeTo(null);

                                    frame.setVisible(true);
                                }
                            }
                        }
                    }
                }
            }
        }
        else if(button == MouseEvent.BUTTON3){
            if(!lockMovement.isSelected()){
                mousePoint = e.getPoint();
            }
        }
        else if(button == MouseEvent.BUTTON2){
            reset();
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        temperatureClicked = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(SwingUtilities.isLeftMouseButton(e)){
            if(temperatureClicked){
                if(x > temperatureThumb.getCenterX()){
                    temperatureThumb.x += x-temperatureThumb.x;
                }
                else if(x < temperatureThumb.getCenterX()){
                    temperatureThumb.x -= temperatureThumb.x-x;
                }
                if(temperatureThumb.x <= temperatureSlider.x){
                    temperatureThumb.x = temperatureSlider.x;
                }
                else if(temperatureThumb.x >= temperatureSlider.x+temperatureSlider.width){
                    temperatureThumb.x = temperatureSlider.x+temperatureSlider.width;
                }
                currentTemperature = Math.round((maxTemperature*(temperatureThumb.x-temperatureSlider.x))/temperatureSlider.width);
                adjustTemperature(currentTemperature+"");
            }
        }
        else if(SwingUtilities.isRightMouseButton(e)){
            if(!lockMovement.isSelected()){
                int dx = e.getX() - mousePoint.x;
                int dy = e.getY() - mousePoint.y;
                mousePoint = e.getPoint();
                adjustComponentPostitons(dx, dy);
            }
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        ElementTile elementToSetSelected = null;
        for(int i=0;i<elementHitboxes.size();i++){
            for(int j=0;j<elementHitboxes.get(i).size();j++){
                if(elementHitboxes.get(i).get(j) != null){
                    if(elementHitboxes.get(i).get(j).getHitbox().contains(x, y)){
                        if(elementHitboxes.get(i).get(j).isSelectable()){
                            elementToSetSelected = elementHitboxes.get(i).get(j);
                        }
                        else{
                            elementHitboxes.get(i).get(j).setSelected(false);
                            elementHitboxes.get(i).get(j).setDisabled(false);
                            displayElement.setElement(null);
                        }
                    }
                    else{
                        elementHitboxes.get(i).get(j).setSelected(false);
                        elementHitboxes.get(i).get(j).setDisabled(false);
                        displayElement.setElement(null);
                    }
                }
            }
        }
        if(elementToSetSelected != null){
            elementToSetSelected.setSelected(true);
            displayElement.setElement(elementToSetSelected.getElement());
            for(int i=0;i<groups.size();i++){
                groups.get(i).setSelected(false);
            }
            for(int i=0;i<groups.size();i++){
                String type = elementToSetSelected.getElement().getType();
                if(type != null && !type.isEmpty() && type.equals(groups.get(i).getName())){
                    groups.get(i).setSelected(true);
                }
            }
        }
        else{
            Group groupSelected = null;
            for(int i=0;i<groups.size();i++){
                if(groups.get(i) != null){
                    if(groups.get(i).getHitbox().contains(x, y)){
                        groupSelected = groups.get(i);
                    }
                    else{
                        groups.get(i).setSelected(false);
                    }
                }
            }
            if(groupSelected != null){
                groupSelected.setSelected(true);
                ArrayList<ElementTile> selectedElements = new ArrayList<>();
                for(int i=0;i<elementHitboxes.size();i++){
                    for(int j=0;j<elementHitboxes.get(i).size();j++){
                        if(elementHitboxes.get(i).get(j) != null){
                            String type = elementHitboxes.get(i).get(j).getElement().getType();
                            if(type != null && !type.isEmpty() && type.equals(groupSelected.getName())){
                                selectedElements.add(elementHitboxes.get(i).get(j));
                            }
                            else{
                                elementHitboxes.get(i).get(j).setDisabled(true);
                            }
                        }
                    }
                }
                if(!selectedElements.isEmpty()){
                    for(int i=0;i<selectedElements.size();i++){
                        selectedElements.get(i).setDisabled(false);
                    }
                }
            }
        }
        repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double amount = e.getPreciseWheelRotation();
        if(!lockMovement.isSelected()){
            if(amount < 0){
                adjustComponentSize(1.01,1.01);
            }
            else{
                adjustComponentSize(.99, .99);
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        char keyChar = (char)key;
        if(key == KeyEvent.VK_R){
            reset();
        }
        if(e.isControlDown() && key == KeyEvent.VK_L){
            lockMovement.toggle();
        }
        if(e.isControlDown() && key == KeyEvent.VK_S){
            showSymbol.toggle();
        }
        if(e.isControlDown() && !e.isShiftDown() && key == KeyEvent.VK_N){
            showName.toggle();
        }
        if(e.isControlDown() && e.isShiftDown() && key == KeyEvent.VK_N){
            showAtomNumber.toggle();
        }
        if(e.isControlDown() && key == KeyEvent.VK_W){
            showWeight.toggle();
        }
        
        if(e.isControlDown() && (key == KeyEvent.VK_W || key == KeyEvent.VK_N || key == KeyEvent.VK_S || key == KeyEvent.VK_L)){
            for(int i=0;i<elementHitboxes.size();i++){
                for(int j=0;j<elementHitboxes.get(i).size();j++){
                    ElementTile tile = elementHitboxes.get(i).get(j);
                    if(tile != null && tile.isSelectable()){
                        tile.setShowSymbol(showSymbol.isSelected());
                        tile.setShowName(showName.isSelected());
                        tile.setShowAtomNumber(showAtomNumber.isSelected());
                        tile.setShowWeight(showWeight.isSelected());
                    }
                }
            }            
        }
        
        if(e.isControlDown() && key == KeyEvent.VK_UP){
            currentTemperature+=25;
            if(currentTemperature > maxTemperature){
                currentTemperature = maxTemperature;
            }
            adjustTemperature((currentTemperature)+"");
            adjustSlider((int)currentTemperature);
        }
        if(e.isControlDown() && key == KeyEvent.VK_DOWN){
            currentTemperature-=25;
            if(currentTemperature < 0){
                currentTemperature = 0;
            }
            adjustTemperature((currentTemperature)+"");
            adjustSlider((int)currentTemperature);
        }
        if(key == KeyEvent.VK_ENTER){
            temperatureNumberSelected = false;
            currentText = "";
        }
        if(key == KeyEvent.VK_ESCAPE){
            temperatureNumberSelected = false;
            currentTemperature = previousTemperature;
            currentText = "";
        }
        if(key == KeyEvent.VK_BACK_SPACE){
            if(!currentText.isEmpty()){
                currentText = currentText.substring(0, currentText.length()-1);
                currentTemperature = Integer.parseInt(currentText.isEmpty() ? "0" : currentText);
                adjustTemperature(currentTemperature+"");
                adjustSlider((int)currentTemperature);
            }
        }
        if(temperatureNumberSelected){
            if(Character.isDigit(keyChar) && (currentText.length()+1 <= 4 && currentTemperature < maxTemperature)){
                currentText+=keyChar;
                currentTemperature = Integer.parseInt(currentText);
                adjustTemperature(currentTemperature+"");
                adjustSlider((int)currentTemperature);
            }
        }
        if(!temperatureNumberSelected){
            if(!currentText.isEmpty()){
                currentTemperature = Integer.parseInt(currentText);
                currentText = "";
            }
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}
