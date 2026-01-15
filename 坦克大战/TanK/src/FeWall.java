import java.awt.Rectangle;
import java.awt.Graphics;

public class FeWall extends AbstractGameObject{
    public int width=60;
    public int height=60;

    public FeWall(String img, int x, int y, Tankpanel tankpanel) {
        super(img, x, y, tankpanel);
    }

    @Override
    public void paintSelf(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    @Override
    public Rectangle getRec() {
        return new Rectangle(x,y,width,height);
    }
    
}
