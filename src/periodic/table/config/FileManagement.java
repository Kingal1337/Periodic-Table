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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import periodic.table.Element;

/**
 *
 * @author Alan Tsui
 */
public class FileManagement {
    public static void main(String[] args){
        ArrayList<ArrayList<Element>> array = open();
        for(int i=0;i<array.size();i++){
            for(int j=0;j<array.get(i).size();j++){
                if(!array.get(i).get(j).equals(Element.EMPTY_ELEMENT)){
                    System.out.print("-");
                }
                else{
                    System.out.print(" ");
                }
            }
            System.out.println("");
        }
    }
    
    private static final String ROWS = "rows";
    private static final String COLUMNS = "columns";
    
    private static final String ELEMENTS_ARRAY = "Elements";
    private static final String ATOMICNUMBER = "atomicNumber";
    private static final String SYMBOL = "symbol";
    private static final String NAME = "name";
    private static final String ROW = "row";
    private static final String GROUP_COLUMN = "group-column";
    private static final String ATOMICMASS = "atomicMass";
    private static final String CPKHEXCOLOR = "cpkHexColor";
    private static final String ELECTRONICCONFIGURATION = "electronicConfiguration";
    private static final String ELECTRONEGATIVITY = "electronegativity";
    private static final String ATOMICRADIUS = "atomicRadius";
    private static final String IONRADIUS = "ionRadius";
    private static final String VANDELWAALSRADIUS = "vanDelWaalsRadius";
    private static final String IONIZATIONENERGY = "ionizationEnergy";
    private static final String ELECTRONAFFINITY = "electronAffinity";
    private static final String OXIDATIONSTATES = "oxidationStates";
    private static final String STANDARDSTATE = "standardState";
    private static final String BONDINGTYPE = "bondingType";
    private static final String MELTINGPOINT = "meltingPoint";
    private static final String BOILINGPOINT = "boilingPoint";
    private static final String DENSITY = "density";
    private static final String GROUPBLOCK = "groupBlock";
    private static final String YEARDISCOVERED = "yearDiscovered";
    
    private FileManagement(){}
    
    public static ArrayList<ArrayList<Element>> open(){
        JsonObject myJsonObject = null;
        try {
            myJsonObject = loadObject(FileManagement.class.getResourceAsStream("/periodic/table/config/resources/Periodic Table.json"));
        } catch (IOException ex) {
            
        }
        return read(myJsonObject);
    }
    
    private static ArrayList<ArrayList<Element>> sortElements(HashMap<String, Element> elements, int row, int col){
        ArrayList<ArrayList<Element>> elementTable = new ArrayList<>();
        for(int i=0;i<row;i++){
            ArrayList<Element> currentArrayList = new ArrayList<>();
            for(int j=0;j<col;j++){
                Element element = elements.get((i+1)+""+(j+1));
                if(element != null){
                    currentArrayList.add(element);
                }
                else{
                    currentArrayList.add(Element.EMPTY_ELEMENT);
                }
            }
            elementTable.add(currentArrayList);
        }
        return elementTable;
    }
    
    private static ArrayList<ArrayList<Element>> read(JsonObject jsonObject){
        HashMap<String, Element> elements = new HashMap<>();
        JsonArray allElements = jsonObject.getJsonArray(ELEMENTS_ARRAY);
        System.out.println(allElements.getJsonObject(0));
        int rows = allElements.getJsonObject(0).getInt(ROWS);
        int columns = allElements.getJsonObject(0).getInt(COLUMNS);
        for(int i=1;i<allElements.size();i++){
            JsonObject jsonElement = allElements.getJsonObject(i);
            String atomicNumber = jsonElement.getInt(ATOMICNUMBER)+"";
            String symbol = jsonElement.getString(SYMBOL);
            String name = jsonElement.getString(NAME);
            String weight = jsonElement.getString(ATOMICMASS);
            String type = jsonElement.getString(GROUPBLOCK);
            String boilingPoint = jsonElement.getString(BOILINGPOINT);
            String meltingPoint = jsonElement.getString(MELTINGPOINT);
            String elections = jsonElement.getString(ELECTRONICCONFIGURATION);
            String yearDiscovered = jsonElement.getString(YEARDISCOVERED);
            int row = jsonElement.getInt(ROW);
            int column = jsonElement.getInt(GROUP_COLUMN);
            Element element = new Element(atomicNumber, symbol, name, weight, type, boilingPoint, meltingPoint, elections, yearDiscovered, column, row);
            elements.put(row+""+column, element);
        }
        return sortElements(elements, rows, columns);
    }
    
    private static JsonObject loadObject(InputStream path) throws IOException{
        InputStream is = path;
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;
    }
}
