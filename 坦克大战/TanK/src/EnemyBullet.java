import java.awt.Rectangle;
import java.util.ArrayList;
import java.awt.Graphics;

public class EnemyBullet extends Bullet{

    public EnemyBullet(String img, int x, int y, DirectionEnum direction, Tankpanel tankpanel) {
        super(img, x, y, direction, tankpanel);
    }
    
    public void hitGamer(){
        Rectangle bulletr=this.getRec();
        ArrayList<Tank> gamerList=this.tankpanel.gamerList;

        for (Tank tank : gamerList) {
            if(tank.getRec().intersects(bulletr)){
                for(int i=0;i<7;i++){
                    Boom boom=new Boom("TanK\\image\\boom\\"+(i+1)+".gif", x, y, this.tankpanel);
                    this.tankpanel.boomlist.add(boom);
                }
                this.tankpanel.gamerList.remove(tank);
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
                this.tankpanel.wallList.remove(wall);
                this.tankpanel.bulletsRemoveList.add(this);
                break;
            }
        }
    }

    @Override
    public void paintSelf(Graphics g) {
        g.drawImage(image, x, y, null);
        go();
        hitGamer();
        hitWall();
        moveOutOfBorder();
        hitFeWall();
        hitBase();
    }

    @Override
    public Rectangle getRec() {
    return super.getRec();
    }
}
