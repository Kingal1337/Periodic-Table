/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package periodic.table.config;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 *
 * @author Alan Tsui
 */
public class Config {
    public static final String VERSION = Version.getVersion();
    
    public static final Dimension MINIMUM_FRAME_SIZE = new Dimension(578,323);
    
    public static final String CREATOR = "Alan Tsui";
    
    public static final String COMPANY = "Bearian Inc.";
    
    public static final String YEAR = "2016";
    
    public static final String TITLE = "Periodic Table"; 
    
    public static final Color UNKNOWN_COLOR = Color.GRAY;
    
    public static final Color LIQUID_COLOR = Color.BLUE;
    
    public static final Color SOLID_COLOR = Color.BLACK;
    
    public static final Color GAS_COLOR = Color.RED;
    
    public static final Color METALLOID = Color.decode("#77DD88");
    
    public static final Color NONMETAL = Color.decode("#22FF22");
    
    public static final Color HALOGEN = Color.decode("#22EECC");
    
    public static final Color NOBLE_GAS = Color.decode("#77CCFF");
    
    public static final Color ALKALI_METAL = Color.decode("#FFCC33");
    
    public static final Color ALKALINE_EARTH_METAL = Color.decode("#FFFF44");
    
    public static final Color LANTHANOID = Color.decode("#FFBB99");
    
    public static final Color ACTINOID = Color.decode("#EEBBDD");
    
    public static final Color TRANSITION_METAL = Color.decode("#DDBBBB");
    
    public static final Color POST_TRANSITION_METAL = Color.decode("#99DDCC");
    
//    public static final Font DEFAULT_ELEMENT_FONT = Font.createFont(0, fontStream);
    
}
