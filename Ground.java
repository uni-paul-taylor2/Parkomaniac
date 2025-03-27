import java.awt.*;
import java.awt.geom.*;
import JavaGameEngine.*;

/**
 * A type of GameObject that is seen as ground to negate gravity.
 *
 * @author (Paul Taylor)
 * @version (25th/3/2025)
 */
public class Ground extends GameObject{
    private boolean deadly=false;
    public void setDeadly(boolean potent){deadly=potent;}
    public boolean isDeadly(){return deadly;}
    
    public Ground(boolean potent){
        super(new Rectangle2D.Double(0,219,599,179),new Color(92,67,39),true);
        setDeadly(potent);
    }
}