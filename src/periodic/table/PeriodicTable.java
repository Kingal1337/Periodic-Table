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
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JFrame;
import periodic.table.config.Config;
import periodic.table.config.FileManagement;
import periodic.table.viewers.MainFrame;
import periodic.table.viewers.MainPanel;

/**
 *
 * @author Alan Tsui
 */
public class PeriodicTable {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        ArrayList<ArrayList<Element>> array = FileManagement.open();
//        ArrayList<String> strings = new ArrayList<>();
//        for(int i=0;i<array.size();i++){
//            for(int j=0;j<array.get(i).size();j++){
//                if(array.get(i).get(j) != null){
//                    strings.add(array.get(i).get(j).getName());
//                }
//            }
//        }
//        System.out.println(Text.getLargestString(strings.toArray(new String[0])));
        MainFrame frame = new MainFrame();
        MainPanel panel = new MainPanel(FileManagement.open());
        
        frame.setTitle(Config.TITLE);
        
        frame.changePanel(panel);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setMinimumSize(Config.MINIMUM_FRAME_SIZE);
        frame.setPreferredSize(Config.MINIMUM_FRAME_SIZE);

        frame.pack();
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}
