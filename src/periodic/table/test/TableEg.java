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
package periodic.table.test;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TableEg {

    public static void main(String[] args) {
//        String html = "http://publib.boulder.ibm.com/infocenter/iadthelp/v7r1/topic/"
//                + "com.ibm.etools.iseries.toolbox.doc/htmtblex.htm";
//String html = "http://www.lenntech.com/periodic/number/atomic-number.htm";
String html = "http://www.lenntech.com/periodic/elements/h.htm";
        try {
            Document doc = Jsoup.connect(html).get();
            Elements tableElements = doc.select("table");

            Elements tableHeaderEles = tableElements.select("thead tr th");
            System.out.println("headers");
            for (int i = 0; i < tableHeaderEles.size(); i++) {
                System.out.println(tableHeaderEles.get(i).text());
            }
            System.out.println();

            Elements tableRowElements = tableElements.select(":not(thead) tr");

            for (int i = 0; i < tableRowElements.size(); i++) {
                Element row = tableRowElements.get(i);
//                System.out.println("row");
                Elements rowItems = row.select("td");
                for (int j = 0; j < rowItems.size(); j++) {
                    System.out.println(rowItems.get(j).text());
                }
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
