/*
 * The MIT License
 *
 * Copyright 2016 Alan Tsui.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package periodic.table.config;

/**
 *
 * @author Alan Tsui
 * @version 1.0
 * @since 1.2
 */
public class Version {
    /**
     * Major Version Number
     * Big changes
     * Major enhancements
     * graphic change 
     */
    private static final int MAJOR = 1;
    
    /**
     * Minor Version number
     * medium changes
     * minor enhancements
     * new features
     */
    private static final int MINOR = 0;
    
    /**
     * Minor Version number
     * small changes
     * small enhancements
     * bug fixes
     * typos
     */
    private static final int FIXES = 0;
    
    /**
     * don't let anyone instantiate this class
     */
    private Version() {}

    /**
     * Returns the version as a string.
     * @return String
     */
    public static final String getVersion() {
        return MAJOR + "." + MINOR + "." +FIXES;
    }

    /**
     * Returns the version numbers in an array of ints.
     * <p>
     * The array is of length 3 and has the major, minor, and
     * revision numbers in that order.
     * @return int[] the major, minor, and revision numbers
     */
    public static final int[] getVersionNumbers(){
        return new int[] {MAJOR, MINOR, FIXES};
    }

    /**
     * Returns the major version number.
     * @return int
     */
    public static final int getMajorNumber(){
        return MAJOR;
    }

    /**
     * Returns the minor version number.
     * @return int
     */
    public static final int getMinorNumber(){
        return MINOR;
    }

    /**
     * Returns the fixes version number.
     * @return int
     */
    public static final int getFixesNumber(){
        return FIXES;
    }
}
