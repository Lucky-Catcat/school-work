import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music {
    private static Clip start; 
    private static Clip attack;
    private static Clip boom;
    private static Clip wall;

    static{
        File bgMusicStartFile = new File("TanK\\music\\bgm.wav");
        File bgMusicAttackFile = new File("TanK\\music\\attack.wav");
        File bgMusicExplodeFile = new File("TanK\\music\\boom.wav");
        File bgMusicWallFile = new File("TanK\\music\\wall.wav");

        try {
            AudioInputStream audioInputStreamStart=AudioSystem.getAudioInputStream(bgMusicStartFile);
            start=AudioSystem.getClip();
            start.open(audioInputStreamStart);
            AudioInputStream audioInputStreamAttack = AudioSystem.getAudioInputStream(bgMusicAttackFile);
            attack = AudioSystem.getClip();
            attack.open(audioInputStreamAttack);
            AudioInputStream audioInputStreamStartExplode = AudioSystem.getAudioInputStream(bgMusicExplodeFile);
            boom = AudioSystem.getClip();
            boom.open(audioInputStreamStartExplode);
            AudioInputStream audioInputStreamStartWall = AudioSystem.getAudioInputStream(bgMusicWallFile);
            wall = AudioSystem.getClip();
            wall.open(audioInputStreamStartWall); 
        } catch (Exception e) {
            // TODO: handle exception
        }
    } 

    public static void startPlay(){
        start.start();
        start.setFramePosition(0);
    }

    public static void attackPlay(){
        attack.start();
        attack.setFramePosition(0);
    }

    public static void boomPlay(){
        boom.start();
        boom.setFramePosition(0);
    }

    public static void wallPlay(){
        wall.start();
        wall.setFramePosition(0);
    }

}
