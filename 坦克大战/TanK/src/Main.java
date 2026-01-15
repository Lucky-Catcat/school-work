import javax.swing.JFrame;

public class Main extends JFrame{

    public void launch(){
        Tankpanel panel=new Tankpanel();
        add(panel);
        
        setTitle("坦克大战");
        setSize(Tankpanel.W, Tankpanel.H);
        setLocationRelativeTo(null);
        //setResizable(false);
        setVisible(true);
        addKeyListener(panel);
        panel.run();
    }
    public static void main(String[] args) {
        Main frame=new Main();
        frame.launch();
    }
}