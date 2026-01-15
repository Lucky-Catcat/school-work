import java.awt.Graphics;
import java.awt.Rectangle;

public class Boom extends AbstractGameObject{

    public Boom(String img, int x, int y, Tankpanel tankpanel) {
        super(img, x, y, tankpanel);
    }

    @Override
    public void paintSelf(Graphics g) {
        g.drawImage(image, x, y, null); 
    }

    @Override
    public Rectangle getRec() {
        return null;
    }
    
}
