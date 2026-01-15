import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Tankpanel extends JPanel implements KeyListener {
    final static int W=1260;
    final static int H=800;
    public int state=0; 
    private int a=1;
    private int y=475;
    private int wallCount=100;
    private int[] xrr=new int[wallCount];
    private int[] yrr=new int[wallCount];
    public boolean run=true;
    private boolean enter=true;

    Image choose=Toolkit.getDefaultToolkit().getImage("TanK\\image\\player1\\p1tankR.gif");

    GamerOne gamerOne=new GamerOne("TanK\\image\\player1\\p1tankU.gif", 540, 715, 
    this,"TanK\\image\\player1\\p1tankU.gif", 
    "TanK\\image\\player1\\p1tankD.gif",
    "TanK\\image\\player1\\p1tankR.gif", 
    "TanK\\image\\player1\\p1tankL.gif",1);

    GameTwo gameTwo=new GameTwo("TanK\\image\\player1\\p1tankU.gif", 540, 715, 
    this,"TanK\\image\\player1\\p1tankU.gif", 
    "TanK\\image\\player1\\p1tankD.gif",
    "TanK\\image\\player1\\p1tankR.gif", 
    "TanK\\image\\player1\\p1tankL.gif");

    public ArrayList<Bullet> bulletList=new ArrayList<>();
    public ArrayList<EnemyBot> enemyBotsList=new ArrayList<>();
    public ArrayList<Bullet> bulletsRemoveList=new ArrayList<>();
    public ArrayList<Tank> gamerList=new ArrayList<>();
    public ArrayList<Boom> boomlist=new ArrayList<>();
    public ArrayList<Wall> wallList=new ArrayList<>();
    public ArrayList<Grass> grassList=new ArrayList<>();
    public ArrayList<FeWall> fewallList=new ArrayList<>();
    public ArrayList<Base> baseList=new ArrayList<>();

    public int repaintCount=0;
    private int enemyRobotCount=0;

    Image imageBuffer=null;

    public void CreatMap(Graphics g){
        for (Base base : baseList) {
            base.paintSelf(g);
        }

        for (Tank tank : gamerList) {
            tank.paintSelf(g);
        }

        for (Bullet bullet : bulletList) {
            bullet.paintSelf(g);
        }

        for (EnemyBot enemyBot : enemyBotsList) {
            enemyBot.paintSelf(g);
        }

        bulletList.removeAll(bulletsRemoveList);
        bulletsRemoveList.clear();

        for (Boom boom : boomlist) {
            boom.paintSelf(g);
        }

        for (Wall wall : wallList) {
            wall.paintSelf(g);
        }

        for (FeWall feWall : fewallList) {
            feWall.paintSelf(g);
        }

        for (Grass grass : grassList) {
            grass.paintSelf(g);
        }
    }

    @Override
    public void paint(Graphics g){
        if(imageBuffer==null){
            imageBuffer=this.createImage(W, H);
        }
        Graphics gBuffer=imageBuffer.getGraphics();

        gBuffer.setColor(Color.WHITE);
        gBuffer.fillRect(0, 0, W, H);
        
        if(state==0){
            gBuffer.drawImage(Toolkit.getDefaultToolkit().
                getImage("TanK\\image\\interface.png")
                ,0,0,this);
            gBuffer.drawImage(choose, 300, y, this);
        }else if(state==1||state==6){
            if(state==1){
                CreatMap(gBuffer);
            }else{
                CreatMap(gBuffer);
            }//run();
        }else if(state==2){
            if(true){
                run=false;
            }
            CreatMap(gBuffer);
            gBuffer.setColor(new Color(255,250,205));
            gBuffer.setFont(new Font("黑体",Font.BOLD,100));
            gBuffer.drawString("游戏暂停", 450, 450);
        }else if(state==3){
            gBuffer.drawImage(Toolkit.getDefaultToolkit().getImage("TanK\\image\\fail.gif"), 
                0, 0, this);
        }else if(state==4){
            gBuffer.drawImage(Toolkit.getDefaultToolkit().getImage("TanK\\image\\success.gif"), 
                0, 0, this);
        }else{
            System.out.println("状态码无效");
        }

        g.drawImage(imageBuffer, 0, 0, null);
    }

    public void run(){
        while(true){
            if(enemyBotsList.size()==0&&enemyRobotCount==10){
                state=4;
            }

            if(gamerList.size()==0&&(state==1||state==6)){
                state=3;
            }

            if(repaintCount%100==0&&enemyRobotCount<10){
                Random r=new Random();
                int x=r.nextInt(Tankpanel.W-50);

                EnemyBot enemyBot=new EnemyBot("TanK\\image\\enemy\\enemy1D.gif", x, 10, this, 
                    "TanK\\image\\enemy\\enemy1U.gif", "TanK\\image\\enemy\\enemy1D.gif", 
                    "TanK\\image\\enemy\\enemy1R.gif", "TanK\\image\\enemy\\enemy1L.gif");
                
                enemyBot.directionEnum=DirectionEnum.DOWN;
                enemyBotsList.add(enemyBot);
                enemyRobotCount++;
            }

            if(!boomlist.isEmpty())
                boomlist.remove(0);

            if(run){
                repaint();
                repaintCount++;
            }
            
            repaintCount++;
            try{
                Thread.sleep(25);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void addBuild(){
        random();

        for(int i=0;i<21;i++){
            wallList.add(new Wall("TanK\\image\\walls.gif", i*60, 120, this));
        }

        if(state==1){
            wallList.add(new Wall("TanK\\image\\walls.gif",480,720,this));
            wallList.add(new Wall("TanK\\image\\walls.gif",480,660,this));
            wallList.add(new Wall("TanK\\image\\walls.gif",540,660,this));
            wallList.add(new Wall("TanK\\image\\walls.gif",600,660,this));
            wallList.add(new Wall("TanK\\image\\walls.gif",600,720,this));
        }else if(state==6){
            wallList.add(new Wall("TanK\\image\\walls.gif",480,720,this));
            wallList.add(new Wall("TanK\\image\\walls.gif",480,660,this));
            wallList.add(new Wall("TanK\\image\\walls.gif",540,660,this));
            wallList.add(new Wall("TanK\\image\\walls.gif",600,660,this));
            wallList.add(new Wall("TanK\\image\\walls.gif",660,660,this));
            wallList.add(new Wall("TanK\\image\\walls.gif",660,720,this));
        }
        

        for(int i=0;i<wallCount;i++){
            Random a=new Random();
            int num=a.nextInt(4);
            if(num<2){
                Wall wall=new Wall("TanK\\image\\walls.gif", xrr[i],yrr[i], null);
                wallList.add(wall);
            }else if(num<3){
                Grass grass=new Grass("TanK\\image\\cao.gif", xrr[i], yrr[i], null);
                grassList.add(grass);
            }else{
                FeWall f=new FeWall("TanK\\image\\fe.gif", xrr[i], yrr[i], null);
                fewallList.add(f);
            }
        }

        if(state==1)
        baseList.add(new Base("TanK\\image\\base.gif", 540, 715, null));
        else if(state==6)
        baseList.add(new Base("TanK\\image\\base.gif", 565, 715, null));
    }

    public void random(){
        Random r=new Random();
        for(int i=0;i<wallCount;){
            int x=r.nextInt(21);
            int y=r.nextInt(8)+3;
            if(i>0){
                boolean repeat=false;
                for(int j=0;j<i;j++)
                    if(xrr[j]==x*60&&yrr[j]==y*60)
                        repeat=true;

                if(!repeat){
                    xrr[i]=x*60;
                    yrr[i]=y*60;
                    i++;
                }else
                    continue;
            }else{
                xrr[i]=x*60;
                yrr[i]=y*60;
                i++;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key=e.getKeyCode();
        if(key==KeyEvent.VK_1){
            a=1;
            y=475;
        }else if(key==KeyEvent.VK_2){
            a=6;
            y=570;
        }else if(key==KeyEvent.VK_ENTER){
            if(enter){
                state=a;
                if(state==1) gamerList.add(gamerOne);
                else if(state==6){
                    gamerList.add(gameTwo.play1);
                    gamerList.add(gameTwo.play2);
                }
                addBuild();//run();
                Music.startPlay();
                enter=false;
            }
            
        }else if(key==KeyEvent.VK_P){
            if(state!=2){
                a=state;
                state=2;
            }else{
                state=a;
                run=true;
                if(a==0)
                    a=1;
            }
        }else{
            if(state==1) gamerOne.keyPressed(e);
            else if(state==6) gameTwo.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(state==1) gamerOne.keyReleased(e);
        else if(state==6) gameTwo.keyReleased(e);    
    }
}
