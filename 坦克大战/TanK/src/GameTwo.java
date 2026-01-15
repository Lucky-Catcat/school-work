import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class GameTwo{
    public GamerOne play1;
    public GamerOne play2;

    public GameTwo(String img, int x, int y, Tankpanel tankpanel, String upPic, 
        String downPic, String rightPic,String leftPic) {
        play1=new GamerOne(img, x, y, tankpanel, upPic, downPic, rightPic, leftPic,1);
        play2=new GamerOne(img, x+60, y, tankpanel, upPic, downPic, rightPic, leftPic,6);
    }
    

    public void keyPressed(KeyEvent e){
        play1.keyPressed(e);
        play2.keyPressed(e);
    }

    public void keyReleased(KeyEvent e){
        play1.keyReleased(e);
        play2.keyReleased(e);
    }

    public void paintSelf(Graphics g) {
        play1.paintSelf(g);
        play2.paintSelf(g);
    }

}
