import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Bullet extends AbstractGameObject{
    private int width=10;
    private int height=10;
    private int speed=7;

    DirectionEnum direction;

    public Bullet(String img, int x, int y,DirectionEnum direction, Tankpanel tankpanel) {
        super(img, x, y, tankpanel);
        this.direction=direction;
    }

    public void upWard(){
        y-=speed;
    }

    public void downWard(){
        y+=speed;
    }

    public void leftWard(){
        x-=speed;
    }

    public void rightWard(){
        x+=speed;
    }

    public void go(){
        if(direction==DirectionEnum.UP){
            upWard();
        }else if(direction==DirectionEnum.DOWN){
            downWard();
        }else if(direction==DirectionEnum.LEFT){
            leftWard();
        }else if(direction==DirectionEnum.RIGHT){
            rightWard();
        }
    }

    public void hitEnemyBot(){
        Rectangle bulletr=this.getRec();
        ArrayList<EnemyBot> enemyBotList=this.tankpanel.enemyBotsList;
        
        for (EnemyBot enemyBot : enemyBotList) {
            if(enemyBot.getRec().intersects(bulletr)){
                for(int i=0;i<7;i++){
                    Boom boom=new Boom("TanK\\image\\boom\\"+(i+1)+".gif",x, y, this.tankpanel);
                    this.tankpanel.boomlist.add(boom);
                }

                Music.boomPlay();

                this.tankpanel.enemyBotsList.remove(enemyBot);
                this.tankpanel.bulletsRemoveList.add(this);
                break;
            }
        }
    }

    public void hitWall(){
        Rectangle next=this.getRec();
        ArrayList<Wall> walls=this.tankpanel.wallList;
        for (Wall wall : walls) {
            if(wall.getRec().intersects(next)){
                Music.wallPlay();

                this.tankpanel.wallList.remove(wall);
                this.tankpanel.bulletsRemoveList.add(this);
                break;
            }
        }
    }

    public void moveOutOfBorder(){
        if(x<0||x>1195){
            this.tankpanel.bulletsRemoveList.add(this);
        }
        if(y<0||y>715){
            this.tankpanel.bulletsRemoveList.add(this);
        }
    }

    public void hitFeWall(){
        Rectangle next=this.getRec();
        ArrayList<FeWall> fes=this.tankpanel.fewallList;

        for (FeWall feWall : fes) {
            if(feWall.getRec().intersects(next)){
                Music.wallPlay();
                
                this.tankpanel.bulletsRemoveList.add(this);
                break;
            }
        }
    }

    public void hitBase(){
        Rectangle next=this.getRec();
        for (Base base : tankpanel.baseList) {
            if(base.getRec().intersects(next)){
                for(int i=0;i<7;i++){
                    Boom boom=new Boom("TanK\\image\\boom\\"+(i+1)+".gif", x, y, this.tankpanel);
                    this.tankpanel.boomlist.add(boom);
                }

                this.tankpanel.baseList.remove(base);
                this.tankpanel.bulletsRemoveList.add(this);

                new Thread(){
                    public void run(){
                        try {
                            Thread.sleep(2000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tankpanel.state=3;
                    }
                }.start();
                break;
            }
        }
    }

    @Override
    public void paintSelf(Graphics g) {
        g.drawImage(image, x, y, null);
        go();
        hitEnemyBot();
        hitWall();
        moveOutOfBorder();
        hitFeWall();
        hitBase();
    }

    @Override
    public Rectangle getRec() {
        return new Rectangle(x,y,width,height);    
    }
    
}
