import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import JavaGameEngine.*;
import javax.sound.sampled.*;

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
    private static Clip track = SoundManager.getAudio("Sounds/background.wav");
    private static Player player1, player2;
    private static Ground placeholder; //I don't think this would actually get used
    //more code can come here :D
    private static void start(){
        if(!stopped) return;
        //more code can come here
        player1 = new Player(Color.GREEN, 'w','s','a','d');
        placeholder = new Ground(false);
        panel.start();
        panel.addItem(player1);
        panel.addItem(placeholder,true,true);
        //more code can come here
        stopped = false;
    }
    public static void stop(){
        if(stopped) return;
        panel.stop();
        stopped = true;
        pointsLabel.setText("Game Over");
        //more code can come here
    }
    private static void loadLevel(int level){}
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
                pointsLabel.setText("Press any key to play :D");
                //code can come here
            }
        };
        //code can come here
        JPanel pointsPanel = new JPanel();
        pointsLabel = new JLabel(">:D");
        pointsPanel.add(pointsLabel);
        frame.setLayout(new BorderLayout());
        frame.add(pointsPanel, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
        start();
    }
}
