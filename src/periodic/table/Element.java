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

/**
 *
 * @author Alan Tsui 
*/
public class Element {    
    public static final Element EMPTY_ELEMENT = new Element("EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", "EMPTY", -1, -1);
    private String atomicNumber;
    private String symbol;
    private String name;
    private String weight;
    private String type;
    private String boilingPoint;
    private String meltingPoint;
    private String elections;
    private String yearDiscovered;
    private String bondingType;
    private String density;
    
    private int group;
    private int row;

    public Element(String atomicNumber, String symbol, String name, String weight, String type, String boilingPoint, String meltingPoint, String elections, String yearDiscovered, String bondingType, String density, int group, int row) {
        this.atomicNumber = atomicNumber;
        this.symbol = symbol;
        this.name = name;
        this.weight = weight;
        this.type = type;
        this.boilingPoint = boilingPoint;
        this.meltingPoint = meltingPoint;
        this.elections = elections;
        this.yearDiscovered = yearDiscovered;
        this.bondingType = bondingType;
        this.density = density;
        this.group = group;
        this.row = row;
    }
    
    /**
     * P - Protons
     * E - Electrons
     * N - Neutrons
     * @param ions  number of ions atom has
     * @return  returns an array of Protons, Elections, and Neutrons
     */
    public int[] getPEN(int ions){
        int[] pen = new int[3];
        pen[0] = alanutilites.math.Number.isInteger(atomicNumber) ? Integer.parseInt(atomicNumber) : -1;
        pen[1] = pen[0] == -1 ? -1 : ions == 0 ? pen[0] : ions > 0 ? pen[0]-ions : pen[0]+Math.abs(ions);
        String newWeight = weight.replaceAll("[()\\[\\]]", "");
        pen[2] = alanutilites.math.Number.isDouble(newWeight) ? ((int)Math.round(Double.parseDouble(newWeight)))-Integer.parseInt(atomicNumber) : -1;
        return pen;
    }
    
    /**
     * 
     * @param kelvin  temperature
     * @return  
     * -1 - unknown
     * 0 - solid
     * 1 - liquid
     * 2 - gas
     */
    public int getStateAtCurrentTemp(String kelvin){
        if(kelvin != null && !kelvin.isEmpty()){
            double temperature = Double.parseDouble(kelvin);
            if(boilingPoint != null && !boilingPoint.isEmpty()){
                double boilingTemp = Double.parseDouble(boilingPoint);
                if(temperature >= boilingTemp){
                    return 2;
                }
            }
            if(meltingPoint != null && !meltingPoint.isEmpty()){
                double meltingTemp = Double.parseDouble(meltingPoint);
                if(temperature >= meltingTemp){
                    return 1;
                }
            }
            if((meltingPoint != null && !meltingPoint.isEmpty()) && (boilingPoint != null && !boilingPoint.isEmpty())){
                double boilingTemp = Double.parseDouble(boilingPoint);
                double meltingTemp = Double.parseDouble(meltingPoint);
                if(temperature <= meltingTemp && temperature <= boilingTemp){
                    return 0;
                }
            }
        }
        return -1;
    }

    public String getAtomicNumber() {
        return atomicNumber;
    }

    public void setAtomicNumber(String atomicNumber) {
        this.atomicNumber = atomicNumber;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBoilingPoint() {
        return boilingPoint;
    }

    public void setBoilingPoint(String boilingPoint) {
        this.boilingPoint = boilingPoint;
    }

    public String getMeltingPoint() {
        return meltingPoint;
    }

    public void setMeltingPoint(String meltingPoint) {
        this.meltingPoint = meltingPoint;
    }

    public String getElections() {
        return elections;
    }

    public void setElections(String elections) {
        this.elections = elections;
    }

    public String getYearDiscovered() {
        return yearDiscovered;
    }

    public void setYearDiscovered(String yearDiscovered) {
        this.yearDiscovered = yearDiscovered;
    }

    public String getBondingType() {
        return bondingType;
    }

    public void setBondingType(String bondingType) {
        this.bondingType = bondingType;
    }

    public String getDensity() {
        return density;
    }

    public void setDensity(String density) {
        this.density = density;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
