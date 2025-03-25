import java.awt.*;
import java.io.File;
import java.awt.geom.*;
import java.util.ArrayList;
import java.awt.event.*;
import javax.sound.sampled.*;
import JavaGameEngine.*;

/**
 * The Player.
 *
 * @author (Paul Taylor)
 * @version (24th/3/2025)
 */
public class Player implements CompositeGameObject
{
    private static Clip jumpSound;
    private static Clip failSound;
    private static Clip stepSound;
    
    private GameObject head;
    private GameObject leftLeg;
    private GameObject leftArm;
    private GameObject rightLeg;
    private GameObject rightArm;
    private GameObject neck;
    private GameObject torso; //will be the "main" object of the player
    
    private double torsoX=50, torsoY=200; //x and y of the "main" object of the player (the torso)
    private double armX=+5, armY=+0, legX=+5, legY=+15, headX=+0, headY=-7, neckX=+3, neckY=-3;
    
    private boolean jumping;
    private double jumpSpeed;
    private double origJumpSpeed(){return -0.35*Constants.GRAVITY;}
    
    @Override
    public void addToPanel(GamePanel p){}
    @Override
    public void removeFromPanel(GamePanel p){}
    
    public boolean jump(){
        if(jumping) return false;
        jumpSpeed = origJumpSpeed();
        jumping = true;
        torso.setAcceleration(0,Constants.GRAVITY);
        return true;
    }
    public boolean land(){
        if(!jumping) return false;
        jumpSpeed = 0;
        jumping = false;
        torso.setAcceleration(0,0,true);
        return true;
    }
    
    public Player()
    {
        //relative to torsoX begin
        armX+=torsoX;
        armY+=torsoY;
        legX+=torsoX;
        legY+=torsoY;
        headX+=torsoX;
        headY+=torsoY;
        neckX+=torsoX;
        neckY+=torsoY;
        //relative to torsoX end
        
        Rectangle2D.Double torsoShape = new Rectangle2D.Double(torsoX,torsoY,5,20);
        Rectangle2D.Double headShape;
        
        torso = new GameObject();
        head = new GameObject();
        neck = new GameObject();
        leftLeg = new GameObject();
        leftArm = new GameObject();
        rightLeg = new GameObject();
        rightArm = new GameObject();

        head.attachTo(torso);
        neck.attachTo(torso);
        leftLeg.attachTo(torso);
        leftArm.attachTo(torso);
        rightLeg.attachTo(torso);
        rightArm.attachTo(torso);
    }
}
