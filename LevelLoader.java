import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import JavaGameEngine.*;
import javax.sound.sampled.*;
import java.io.File;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Class that loads the content for a level.
 *
 * @author (Paul Taylor)
 * @version (25th/4/2025)
 */
public class LevelLoader{
    private HashMap<String,Color> colours;
    private Shape loadShape(Scanner config){
        Shape s=null;
        String shapeType = config.next().trim().toLowerCase();
        if("rectangle".equals(shapeType)){
            double x=config.nextDouble(), y=config.nextDouble(), w=config.nextDouble(), h=config.nextDouble();
            s = new Rectangle2D.Double(x,y,w,h);
        }
        if("roundrectangle".equals(shapeType)){
            double x=config.nextDouble(), y=config.nextDouble(), w=config.nextDouble(), h=config.nextDouble(),
                xr=config.nextDouble(), yr=config.nextDouble();
            s = new RoundRectangle2D.Double(x,y,w,h,xr,yr);
        }
        if("triangle".equals(shapeType)){
            double x1=config.nextDouble(), y1=config.nextDouble(), x2=config.nextDouble(), y2=config.nextDouble(),
                x3=config.nextDouble(), y3=config.nextDouble(); //all 3 points for a rectangle
            Path2D triangle = new Path2D.Double();
            triangle.moveTo(x1,y1);
            triangle.lineTo(x2,y2);
            triangle.lineTo(x3,y3);
            triangle.closePath(); //third line implicitly drawn when path is closed
            s = triangle;
        }
        config.nextLine();
        return s;
    }
    private Color loadColour(Scanner config){
        String c = config.next().trim();
        if(colours.containsKey(c.toLowerCase())) return colours.get(c);
        int alpha = 0;
        if(c.startsWith("#")){
            c="0x"+c.substring(1);
            alpha = 0xff000000;
        }
        int argb = Integer.decode(c) | alpha;
        Color colour = new Color(argb);
        return colour;
    }
    
    public ArrayList<GameObject> load(int level, ParallaxScrollingImage background){
        ArrayList<GameObject> gameObjects = new ArrayList<>();
        try{
            //nextInt, nextLong, nextDouble, next, nextBoolean, nextFloat
            File file = new File("Maps/"+level+".txt");
            Scanner config = new Scanner(file);
            background.deleteAll();
            int images = config.nextInt();
            int objects = config.nextInt();
            config.nextLine();
            for(int i=0;i<images;i++){
                double factor = config.nextDouble();
                String imageFileName = config.nextLine().trim();
                background.insert(new ScrollingImage(imageFileName,factor));
            }
            for(int i=0;i<objects;i++){
                String gameObjectType = config.next().trim().toLowerCase();
                boolean deadly=config.nextBoolean();
                Color colour = loadColour(config);
                Shape shape = loadShape(config);
                if("ground".equals(gameObjectType)) gameObjects.add(new Ground(shape,colour,deadly));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return gameObjects;
    }
    public LevelLoader()
    {
        colours = new HashMap<>();
        colours.put("black", Color.BLACK);
        colours.put("white", Color.WHITE);
        colours.put("red", Color.RED);
        colours.put("green", Color.GREEN);
        colours.put("blue", Color.BLUE);
        colours.put("yellow", Color.YELLOW);
        colours.put("cyan", Color.CYAN);
        colours.put("magenta", Color.MAGENTA);
        colours.put("gray", Color.GRAY);
        colours.put("darkgray", Color.DARK_GRAY);
        colours.put("lightgray", Color.LIGHT_GRAY);
        colours.put("orange", Color.ORANGE);
        colours.put("pink", Color.PINK);
        colours.put("brown", new Color(92,67,39));
    }
}
