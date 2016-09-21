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
