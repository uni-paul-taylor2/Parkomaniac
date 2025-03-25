import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import JavaGameEngine.*;

/**
 * The game's entry point.
 *
 * @author (Paul Taylor)
 * @version (7th/2/2025)
 */
public class Game
{
    private static GamePanel panel;
    private static JLabel pointsLabel;
    private static boolean stopped = true;
    private static boolean pausedOnce = false;
    private static ScrollingImage background = null;
    //more code can come here :D
    private static void start(){
        if(!stopped) return;
        //more code can come here
        panel.start();
        //more code can come here
        stopped = false;
    }
    public static void stop(){
        if(stopped) return;
        panel.stop();
        stopped = true;
        //more code can come here
    }
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.setSize(Constants.DEFAULT_PANEL_WIDTH, Constants.DEFAULT_PANEL_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new GamePanel(){
            @Override
            public void onKeyDown(KeyEvent e){
                Game.start();
                if(getTick()==1 && pausedOnce) resume();
            }
            @Override
            public void perTickCallback(){
                //code can come here
                if(getTick()!=1) return;
                if(!pausedOnce) pause();
                pausedOnce = true;
                //code can come here
            }
        };
        //code can come here
        start();
    }
}
