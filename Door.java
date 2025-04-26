import JavaGameEngine.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Write a description of class Door here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Door extends GameObject{
    private LinkedHashSet<Player> players=new LinkedHashSet<>();
    private boolean triggered=false;
    @Override
    public void onGameTick(int tick, ArrayList<GameObject> collisions){
        for(GameObject item: collisions){
            if(item.getParent()!=null && item.getParent() instanceof Player) players.add((Player)item.getParent());
        }
        if(players.size()==2) trigger(); //because the level is complete since all players reached the door
        onGameTickDefault(tick,collisions);
    }
    public void trigger(){
        if(triggered) return;
        triggered=true;
        Game.nextLevel();
    }
    public Door(Shape s, Color c, boolean potent)
    {
        super(s,c,true);
    }
}
