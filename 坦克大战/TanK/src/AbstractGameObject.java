import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Graphics;

public abstract class AbstractGameObject {
    public Image image;
    int x;
    int y;
    Tankpanel tankpanel;

    public AbstractGameObject(String img,int x,int y,Tankpanel tankpanel){
        this.image=Toolkit.getDefaultToolkit().getImage(img);
        this.x=x;
        this.y=y;
        this.tankpanel=tankpanel;
    }

    public abstract void paintSelf(Graphics g);

    public abstract Rectangle getRec();
}
