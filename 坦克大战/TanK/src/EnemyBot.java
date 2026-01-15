import java.awt.Rectangle;
import java.util.Random;
import java.awt.Graphics;
import java.awt.Point;

public class EnemyBot extends Tank{
    int moveTime=0;

    public EnemyBot(String img, int x, int y, Tankpanel tankpanel, 
        String upPic, String downPic, String rightPic,String leftPic) {
        super(img, x, y, tankpanel, upPic, downPic, rightPic, leftPic);
    }
    
    @Override
    public Rectangle getRec() {
        return super.getRec();
    }

    @Override
    public void paintSelf(Graphics g) {
        super.paintSelf(g);
        move();
        attack();
    }

    public DirectionEnum randomEnemyTankDirection(){
        Random r=new Random();
        int rnum=r.nextInt(4);
        if(rnum==0){
            return DirectionEnum.UP;
        }else if(rnum==1){
            return DirectionEnum.RIGHT;
        }else if(rnum==2){
            return DirectionEnum.LEFT;
        }else{
            return DirectionEnum.DOWN;
        }
    }

    public void move(){
        if(moveTime>=20){
            directionEnum=randomEnemyTankDirection();
            moveTime=0;
        }else{
            moveTime+=2;
        }
        
        if(directionEnum==DirectionEnum.UP){
            upWard();
        }else if(directionEnum==DirectionEnum.DOWN){
            downWard();
        }else if(directionEnum==DirectionEnum.RIGHT){
            rightWard();
        }else if(directionEnum==DirectionEnum.LEFT){
            leftWard();
        }
    }

    public void attack(){
        Point p=getHeadPoint();
        EnemyBullet bullet=new EnemyBullet("TanK\\image\\bullet\\bulletYellow.gif", 
            p.x-10, p.y-10, directionEnum, tankpanel);
        Random random=new Random();
        int i=random.nextInt(400);
        if(i<5)
            this.tankpanel.bulletList.add(bullet);
    }
}
