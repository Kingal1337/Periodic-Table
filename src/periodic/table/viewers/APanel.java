/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package periodic.table.viewers;

import java.awt.Dimension;
import javax.swing.JPanel;

/**
 *
 * @author Alan Tsui
 */
public class APanel extends JPanel{
    public APanel(){
        
    }
    
    public void resizePanel(Dimension previousSize, Dimension dimensions){
        setSize(dimensions.width, dimensions.height);
        resizeComponents(previousSize, dimensions);
    }
    
    public void resizeComponents(Dimension previousSize, Dimension dimensions){
        
    }
}
