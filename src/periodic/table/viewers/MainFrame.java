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

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import periodic.table.config.Config;

/**
 *
 * @author Alan Tsui
 */
public class MainFrame extends JFrame implements ComponentListener{
    private Dimension previousDimensions;
    private APanel currentPanel;
    public MainFrame(){
        currentPanel = null;
        addComponentListener(this);
        init();
    }
    
    public MainFrame(APanel panel){
        currentPanel = panel;
        init();
    }
    
    private void init(){
        addComponentListener(this);
        previousDimensions = new Dimension(Config.MINIMUM_FRAME_SIZE);
        
        ArrayList<Image> images = new ArrayList<>();
        images.add(new ImageIcon(MainFrame.class.getResource("/periodic/table/config/resources/icon16x16.png")).getImage());
        images.add(new ImageIcon(MainFrame.class.getResource("/periodic/table/config/resources/icon24x24.png")).getImage());
        images.add(new ImageIcon(MainFrame.class.getResource("/periodic/table/config/resources/icon32x32.png")).getImage());
        images.add(new ImageIcon(MainFrame.class.getResource("/periodic/table/config/resources/icon64x64.png")).getImage());
        setIconImages(images);
    }
    
    public void changePanel(APanel panel){
        if(currentPanel != null){
            remove(currentPanel);
            revalidate();
            repaint();
        }
        currentPanel = panel;
        add(currentPanel);
            currentPanel.requestFocusInWindow();
        revalidate();
        repaint();
    }
    
    @Override
    public void componentResized(ComponentEvent e) {
        if(currentPanel != null){
            Dimension dimensions = getContentPane().getSize();
            currentPanel.resizePanel(previousDimensions, dimensions);
            previousDimensions.setSize(dimensions);
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        
    }

    @Override
    public void componentShown(ComponentEvent e) {
        
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        
    }

    public Dimension getPreviousDimensions() {
        return previousDimensions;
    }

    public void setPreviousDimensions(Dimension previousDimensions) {
        this.previousDimensions = previousDimensions;
    }

    public APanel getCurrentPanel() {
        return currentPanel;
    }
}