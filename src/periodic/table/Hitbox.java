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

import java.awt.geom.Rectangle2D;

/**
 *
 * @author Alan Tsui
 */
public abstract class Hitbox implements Renderer{
    private Rectangle2D.Double hitbox;
    public Hitbox(Rectangle2D.Double hitbox){
        this.hitbox = hitbox;
    }
    
    public void resize(double widthToResizeBy, double heightToResizeBy){
        hitbox.x = hitbox.x*widthToResizeBy;
        hitbox.y = hitbox.y*heightToResizeBy;
        hitbox.width = hitbox.width*widthToResizeBy;
        hitbox.height = hitbox.height*heightToResizeBy;
    }

    public Rectangle2D.Double getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle2D.Double hitbox) {
        this.hitbox = hitbox;
    }

}
