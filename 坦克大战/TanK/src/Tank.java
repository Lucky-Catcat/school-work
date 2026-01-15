import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;



public class Tank extends AbstractGameObject{
    private String upPic;
    private String downPic;
    private String rightPic;
    private String leftPic;

    int width=50;
    int height=50;

    DirectionEnum directionEnum=DirectionEnum.UP;
    private int speed=3;

    private boolean attackCooling=true;
    private int attackIntervalTime=1000;
    
    public Tank(String img, int x, int y, Tankpanel tankpanel,
        String upPic, String downPic, String rightPic, String leftPic) {
        super(img, x, y, tankpanel);
        this.upPic=upPic;
        this.downPic=downPic;
        this.rightPic=rightPic;
        this.leftPic=leftPic;
    }

    @Override
    public void paintSelf(Graphics g) {
        g.drawImage(image, x, y, null);    
    }

    @Override
    public Rectangle getRec() {
        return new Rectangle(x,y,width,height);    
    }
    
    public void setImg(String img){
        this.image=Toolkit.getDefaultToolkit().getImage(img);
    }

    public boolean tankHitWall(int x,int y){
        ArrayList<Wall> walllList=this.tankpanel.wallList;
        ArrayList<FeWall> feWalls=this.tankpanel.fewallList;
        Rectangle next=new Rectangle(x, y, this.width, this.height);
        
        for (Wall wall : walllList) {
            if(wall.getRec().intersects(next))
                return true;
        }

        for (FeWall feWall : feWalls) {
            if(feWall.getRec().intersects(next))
                return true;
        }
        return false;
    }

    public void leftWard(){
        if(!tankHitWall(x-speed, y)&&x-speed>0){
            x-=speed;
            directionEnum=DirectionEnum.LEFT;
            setImg(leftPic);
        }
    }

    public void rightWard(){
        if(!tankHitWall(x+speed, y)&&x+speed<1195){
            x+=speed;
            directionEnum=DirectionEnum.RIGHT;
            setImg(rightPic);
        }
    }

    public void upWard(){
        if(!tankHitWall(x, y-speed)&&y-speed>0){
            y-=speed;
            directionEnum=DirectionEnum.UP;
            setImg(upPic);
        }
    }

    public void downWard(){
        if(!tankHitWall(x, y+speed)&&y+speed<715){
            y+=speed;
            directionEnum=DirectionEnum.DOWN;
            setImg(downPic);
        }
    }

    public Point getHeadPoint(){
        if(directionEnum==DirectionEnum.UP){
            return new Point(x+width/2,y);
        }else if(directionEnum==DirectionEnum.LEFT){
            return new Point(x, y+height/2);
        }else if(directionEnum==DirectionEnum.DOWN){
            return new Point(x+width/2, y+height);
        }else if(directionEnum==DirectionEnum.RIGHT){
            return new Point(x+width, y+height/2);
        }else return null;
    }

    public void attack(){
        Point p=getHeadPoint();
        Bullet bullet=new Bullet("TanK\\image\\bullet\\bulletGreen.gif", 
            p.x-10, p.y-10, directionEnum, tankpanel);
        this.tankpanel.bulletList.add(bullet);
        new Thread(new AttackCD()).start();
    }
    
    class  AttackCD implements Runnable{

        @Override
        public void run() {
            attackCooling=false;
            try{
                Thread.sleep(attackIntervalTime);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            attackCooling=true;
        }
    }
}
