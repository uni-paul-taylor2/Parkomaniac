import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import JavaGameEngine.*;
import javax.sound.sampled.*;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

//scrollPane.getViewport().setViewPosition(new Point(0, 100));

/**
 * The game's entry point.
 *
 * @author (Paul Taylor)
 * @version (7th/2/2025)
 */
public class Game
{
    private static int level=0;
    private static GamePanel panel;
    private static JLabel pointsLabel;
    private static boolean stopped = true;
    private static boolean pausedOnce = false;
    private static ParallaxScrollingImage background = new ParallaxScrollingImage();
    private static LevelLoader loader = new LevelLoader();
    private static GameObject root = new GameObject(); //invisible main object for other game objects
    //private static Clip track = SoundManager.getAudio("Sounds/background.wav");
    private static Player player1, player2;
    private static double prevShiftX = 0, prevShiftY = 0;
    private static boolean shiftedBefore = false;
    //more code can come here :D
    private static void start(){
        if(!stopped) return;
        //more code can come here
        player1 = new Player(Color.GREEN, 'w', 's', 'a', 'd');
        player2 = new Player(Color.GRAY, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
        //more code can come here
        panel.start();
        ArrayList<GameObject> gameObjects = loader.load(level,background); //loaded game objects for the level
        panel.addItem(root);
        panel.addItem(background);
        panel.addItem(player1);
        panel.addItem(player2);
        for(GameObject item: gameObjects){
            if(item.getAttatchedItem()==null) item.attatchTo(root);
            else item.getAttatchedItem().attatchTo(root);
            panel.addItem(item);
        }
        stopped = false;
    }
    public static void stop(){
        if(stopped) return;
        panel.stop();
        stopped = true;
        pointsLabel.setText("Game Over");
        //more code can come here
    }
    public static void nextLevel(){
        stop();
        level = level+1;
        if(level > 3) return; //all levels finished
        start();
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
                if(getTick()==1){
                    if(!pausedOnce) pause();
                    pausedOnce = true;
                    pointsLabel.setText("Press any key to play :D");
                    return;
                }
                double centerX = (frame.getX()+frame.getWidth()) / 2;
                double centerY = (frame.getY()+frame.getHeight()) / 2;
                double cameraX = (player1.getX()+player2.getX()) / 2;
                double cameraY = (player1.getY()+player2.getY()) / 2;
                double shiftX = centerX - cameraX;
                double shiftY = centerY - cameraY;
                double trueShiftX = prevShiftX - shiftX;
                double trueShiftY = prevShiftY - shiftY;
                if(shiftedBefore){
                    background.scroll(-trueShiftX, 0);
                    root.moveTo(root.getX()-trueShiftX, root.getY());
                    //y shifting is kinda annoying
                    //background.scroll(-trueShiftX, -trueShiftY);
                    //root.moveTo(root.getX()-trueShiftX, root.getY()-trueShiftY);
                }
                else shiftedBefore=true; //no shifting at first to hold current alignment
                prevShiftX = shiftX;
                prevShiftY = shiftY;
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
