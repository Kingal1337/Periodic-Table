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
package periodic.table.config;

import java.awt.Color;
import java.awt.Dimension;

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
    
    public static final String WIKI_LINK = "https://en.wikipedia.org/wiki";
    
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
    
}
