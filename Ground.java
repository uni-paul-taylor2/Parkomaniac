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
}