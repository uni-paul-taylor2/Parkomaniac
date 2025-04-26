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
    private static Clip track = SoundManager.getAudio("Sounds/Backgrounds/mainBG.wav");
    private static Player player1, player2;
    //more code can come here :D
    private static void start(){
        if(!stopped) return;
        if(level>3){
            pointsLabel.setText("Game WON :D");
            return;
        }
        if(level > 0) track = SoundManager.getAudio("Sounds/Backgrounds/Level"+level+"BG.wav");
        SoundManager.playAudio(track,true);
        //more code can come here
        player1 = new Player(Color.GREEN, 'w', 's', 'a', 'd');
        player2 = new Player(Color.GRAY, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
        //more code can come here
        panel.start();
        ArrayList<GameObject> gameObjects = loader.load(level,background); //loaded game objects for the level
        panel.addItem(background);
        panel.addItem(player1);
        panel.addItem(player2);
        for(GameObject item: gameObjects) panel.addItem(item);
        pointsLabel.setText("Good Luck on level "+(level+1)+" :D");
        stopped = false;
    }
    public static void stop(){
        if(stopped) return;
        track.stop();
        panel.stop();
        stopped = true;
        pausedOnce = false;
        pointsLabel.setText("Game Over");
        //more code can come here
    }
    public static void stop(boolean keepTrack){
        if(stopped) return;
        if(!keepTrack) track.stop();
        panel.stop();
        stopped = true;
        pausedOnce = false;
        pointsLabel.setText("Game Over");
        //more code can come here
    }
    public static void nextLevel(){
        level = level+1;
        stop(level>3);
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
                else pointsLabel.setText("Good Luck on level "+(level+1)+" :D");
                double furthestX = Math.max(player1.getX(), player2.getX());
                double cameraX = panel.getCameraX();
                double end = Constants.DEFAULT_PANEL_WIDTH;
                double margin = end/3;
                if(furthestX-margin>=cameraX && cameraX+end-margin>=furthestX) return; //no camera changes required
                if(furthestX-margin<cameraX) panel.setCamera(furthestX-margin, panel.getCameraY());
                else if(cameraX+end-margin<furthestX) panel.setCamera(furthestX-(end-margin), panel.getCameraY());
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
