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
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import periodic.table.Element;
import periodic.table.ElementTile;
import periodic.table.Group;
import periodic.table.config.Config;

/**
 *
 * @author Alan Tsui
 */
public class MainPanel extends APanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener{
    private Timer timer;
    private ElementTile displayElement;
    private ArrayList<ArrayList<ElementTile>> elementHitboxes;
    private ArrayList<ArrayList<Element>> allElements;
    private ArrayList<Group> groups;
    
    private Element elementInstructions;
    
    private Rectangle2D.Double creatorPosition;
    
    private double currentTemperature;
    private final double maxTemperature = 6000;
    
    private double temperatureSliderFontSize;
    private double temperatureSliderFontOffset;
    private Rectangle2D.Double temperatureSlider;
    private Rectangle2D.Double temperatureThumb;
    private double temperatureStrokeSize;
    private ImageIcon temperatureImage;
    private boolean temperatureClicked;
    
    public MainPanel(ArrayList<ArrayList<Element>> allElements){
        this.allElements = allElements;
        elementHitboxes = new ArrayList<>();
        groups = new ArrayList<>();
        
        creatorPosition = new Rectangle2D.Double(98, 226, 428, 34);
        
        elementInstructions = new Element("Atomic #","Symbol", "Name", "Weight", null, null, null, null, null, 2, 1);
        
        this.allElements.get(this.allElements.size()-1).add(elementInstructions);
                
        displayElement = new ElementTile(null, new Rectangle2D.Double(70,15,70,70), Color.DARK_GRAY, Color.BLACK, Color.GRAY);
        displayElement.setOtherTextFontNumber(10);
        displayElement.setAtomicNumberFontSize(24);
        initElements();
        initSizeAndPosition(0,0);
        initGroups();
        
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
            }
        }
        
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        
        currentTemperature = 0;
        
        timer = new Timer(16, this);
        timer.start();
        adjustTemperature("273");
        initTemperatureSlider();
        
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
    
    private void initTemperatureSlider(){
        temperatureStrokeSize = 1;
        temperatureSliderFontSize = 8;
        temperatureSliderFontOffset = 5;
        temperatureSlider = new Rectangle2D.Double(416, 18, 98, 3);
        temperatureThumb = new Rectangle2D.Double(416, 15.5, 4, 9);
        temperatureImage = new ImageIcon(MainPanel.class.getResource("/periodic/table/config/resources/temperature.png"));
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
    //                        System.out.println(groupString);
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
                    if(tile.getElement().getAtomicNumber().equals("Atomic #")){
                        System.out.println(((tile.getElement().getGroup()-1)*(width+offset))+x+""+ ((tile.getElement().getRow()-1)*(height+offset))+y+offset);
                    }
                    tile.getHitbox().setRect(((tile.getElement().getGroup()-1)*(width+offset))+x, ((tile.getElement().getRow()-1)*(height+offset))+y+offset, width, height);
                }
            }
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
        gd.setStroke(new BasicStroke((float)temperatureStrokeSize));
        gd.drawRect((int)temperatureSlider.x, (int)temperatureSlider.y, (int)temperatureSlider.width, (int)temperatureSlider.height);
        gd.setStroke(new BasicStroke((float)1));
        gd.setColor(Color.WHITE);
        gd.fill(temperatureThumb);
        gd.setColor(Color.BLACK);
        gd.setStroke(new BasicStroke((float)temperatureStrokeSize));
        gd.drawRect((int)temperatureThumb.x, (int)temperatureThumb.y, (int)temperatureThumb.width, (int)temperatureThumb.height);
        gd.setStroke(new BasicStroke((float)1));
        
        gd.setFont(new Font(Font.SANS_SERIF, 0, (int)temperatureSliderFontSize));
        gd.drawString((currentTemperature+"").substring(0, (currentTemperature+"").indexOf("."))+" K", (int)(temperatureSlider.x+temperatureSlider.width+temperatureSliderFontOffset), (int)(temperatureSlider.y+temperatureSlider.height));
        
        Text.drawCenteredString("Copyright © "+Config.CREATOR+" 2016", (int)creatorPosition.x, (int)creatorPosition.y, (int)creatorPosition.width, (int)creatorPosition.height, gd);
        
        if(displayElement.getElement() != null){
            displayElement.render(gd);
        }
            
        for(int i=0;i<elementHitboxes.size();i++){
            for(int j=0;j<elementHitboxes.get(i).size();j++){
                if(elementHitboxes.get(i).get(j) != null){
                    elementHitboxes.get(i).get(j).render(gd);
                }
            }
        }
            
        for(int i=0;i<groups.size();i++){
            if(groups.get(i) != null){
                groups.get(i).render(gd);
            }
        }
    }

    @Override
    public void resizeComponents(Dimension previousSize, Dimension dimensions) {
        super.resizeComponents(previousSize, dimensions);
        double widthToResizeBy = (double)dimensions.width/(double)previousSize.width;
        double heightToResizeBy = (double)dimensions.height/(double)previousSize.height;
        
        creatorPosition.setRect(creatorPosition.x*widthToResizeBy, creatorPosition.y*heightToResizeBy, creatorPosition.width*widthToResizeBy, creatorPosition.height*heightToResizeBy);
        
        temperatureSliderFontSize = temperatureSliderFontSize*widthToResizeBy;
        temperatureSliderFontOffset = temperatureSliderFontOffset*widthToResizeBy;
        
        temperatureStrokeSize = temperatureStrokeSize*widthToResizeBy;
        
        temperatureSlider.setRect(temperatureSlider.x*widthToResizeBy, temperatureSlider.y*heightToResizeBy, temperatureSlider.width*widthToResizeBy, temperatureSlider.height*heightToResizeBy);
        temperatureThumb.setRect(temperatureThumb.x*widthToResizeBy, temperatureThumb.y*heightToResizeBy, temperatureThumb.width*widthToResizeBy, temperatureThumb.height*heightToResizeBy);
        
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
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(temperatureThumb.contains(x, y)){
            temperatureClicked = true;
        }
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
            currentTemperature = Math.round((6000*(temperatureThumb.x-temperatureSlider.x))/temperatureSlider.width);
            adjustTemperature(currentTemperature+"");
        }
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
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}
