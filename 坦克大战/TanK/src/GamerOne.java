import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class GamerOne extends Tank{
    private DirectionPlayer player=new DirectionPlayer();
    public int state;

    public GamerOne(String img, int x, int y, Tankpanel tankpanel,
        String upPic, String downPic, String rightPic,String leftPic,int state) {
        super(img, x, y, tankpanel, upPic, downPic, rightPic, leftPic);
        this.state=state;
    }
    
    private void keychoose(int key,boolean k){
        if(state==1){
            if(key==KeyEvent.VK_A){
                player.left=k;
            }else if(key==KeyEvent.VK_D){
                player.right=k;
            }else if(key==KeyEvent.VK_W){
                player.up=k;
            }else if(key==KeyEvent.VK_S){
                player.down=k;
            }else if(key==KeyEvent.VK_F){
                if(k){
                    Music.attackPlay();
                    this.attack();
                }
            }
        }else if(state==6){
            if(key==KeyEvent.VK_LEFT){
                player.left=k;
            }else if(key==KeyEvent.VK_RIGHT){
                player.right=k;
            }else if(key==KeyEvent.VK_UP){
                player.up=k;
            }else if(key==KeyEvent.VK_DOWN){
                player.down=k;
            }else if(key==KeyEvent.VK_SPACE){
                if(k){
                    Music.attackPlay();
                    this.attack();
                }
            }
        }
        
    }

    public void keyPressed(KeyEvent e){
        int key=e.getKeyCode();
        keychoose(key,true);
    }

    public void keyReleased(KeyEvent e){
        int key=e.getKeyCode();
        keychoose(key, false);
    }

    public void move(){
        if(player.left)
            leftWard();
        else if(player.right)
            rightWard();
        else if(player.up)
            upWard();
        else if(player.down)
            downWard();
    }

    @Override
    public void paintSelf(Graphics g) {
        super.paintSelf(g);
        move(); 
    }

    @Override
    public Rectangle getRec() {
        return super.getRec();    
    }
}
