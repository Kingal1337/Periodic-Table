/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
