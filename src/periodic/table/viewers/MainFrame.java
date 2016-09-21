/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package periodic.table.viewers;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
        previousDimensions = Config.MINIMUM_FRAME_SIZE;
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
            previousDimensions = new Dimension(dimensions);
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