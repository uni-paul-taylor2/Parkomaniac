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
    //private Clip jumpSound = SoundManager.getAudio("Sounds/jump.wav");
    //private Clip failSound = SoundManager.getAudio("Sounds/fail.wav");
    //private Clip stepSound = SoundManager.getAudio("Sounds/step.wav");
    
    private int UP, DOWN, LEFT, RIGHT; //keys for movement
    private boolean up=false, down=false, left=false, right=false; //if a key is pressed(true) or released(false)
    
    private GameObject head;
    private GameObject leftLeg;
    private GameObject leftArm;
    private GameObject rightLeg;
    private GameObject rightArm;
    private GameObject neck;
    private GameObject torso;
    private GameObject upperRod; //invisible object for stability of arm oscillation
    private GameObject lowerRod; //same thing for leg oscillation and "main" game object for the player
    
    private double torsoX=50, torsoY=130; //the other x and y positions to be relative to torso
    private double armX=+2, armY=+0, legX=+2, legY=+15, headX=-3, headY=-7, neckX=+3, neckY=-3;
    
    private boolean gameEnds=false;
    private boolean jumping;
    private double jumpSpeed;
    private double origJumpSpeed(){return -0.35*Constants.GRAVITY;}
    
    @Override
    public void addToPanel(GamePanel p){
        p.addItem(lowerRod,true,true);
        p.addItem(upperRod);
        p.addItem(torso);
        p.addItem(head);
        p.addItem(neck);
        p.addItem(leftLeg);
        p.addItem(leftArm);
        p.addItem(rightLeg);
        p.addItem(rightArm);
    }
    @Override
    public void removeFromPanel(GamePanel p){
        p.removeItem(lowerRod);
        p.removeItem(upperRod);
        p.removeItem(torso);
        p.removeItem(head);
        p.removeItem(neck);
        p.removeItem(leftLeg);
        p.removeItem(leftArm);
        p.removeItem(rightLeg);
        p.removeItem(rightArm);
    }
    
    public boolean jump(){
        if(jumping) return false;
        jumpSpeed = origJumpSpeed();
        jumping = true;
        lowerRod.setAcceleration(0,Constants.GRAVITY);
        return true;
    }
    public boolean land(){
        if(!jumping) return false;
        jumpSpeed = 0;
        jumping = false;
        lowerRod.setAcceleration(0,0,true);
        return true;
    }
    public double getX(){return lowerRod.getX();}
    public double getY(){return lowerRod.getY();}
    
    public Player(Color c, int u, int d, int l, int r)
    {
        UP = u;
        DOWN = d;
        LEFT = l;
        RIGHT = r;
        
        //relative to torsoX start
        armX+=torsoX;
        armY+=torsoY;
        legX+=torsoX;
        legY+=torsoY;
        headX+=torsoX;
        headY+=torsoY;
        neckX+=torsoX;
        neckY+=torsoY;
        //relative to torsoX stop
        
        //setting rods start
        Rectangle2D.Double upperShape = new Rectangle2D.Double(armX,armY,3,12);
        Rectangle2D.Double lowerShape = new Rectangle2D.Double(legX,legY,4,15);
        lowerRod = new GameObject(lowerShape,new Color(0,0,0,0),true){
            @Override
            public void onGameTick(int tick, ArrayList<GameObject> collisions){
                boolean justStartedJumping=origJumpSpeed()==speedY, grounded=false;
                if(!justStartedJumping){
                    for(GameObject gameObject: collisions){
                        if(gameObject instanceof Ground){
                            land();
                            grounded=true;
                            break;
                        }
                    }
                }
                if(!grounded) lowerRod.setAcceleration(0, Constants.GRAVITY);
                onGameTickDefault(tick,collisions);
            }
            @Override
            public void onKeyDown(KeyEvent k){
                int key = k.getKeyCode();
                if(key==UP) up=true;
                if(key==DOWN) down=true;
                if(key==LEFT) left=true;
                if(key==RIGHT) right=true;
                //program all the keyboard derived activities here
                if(up){
                    if(!jumping){
                        jump();
                        speedY = jumpSpeed;
                    }
                }
                else if(down){
                    //do nothing useful ;-;
                }
                if(left){
                    speedX = -200;
                }
                else if(right){
                    speedX = 200;
                }
            }
            @Override
            public void onKeyUp(KeyEvent k){
                int key = k.getKeyCode();
                if(key==UP) up=false;
                if(key==DOWN) down=false;
                if(key==LEFT) left=false;
                if(key==RIGHT) right=false;
                if(!left && !right) speedX=0;
            }
        };
        upperRod = new GameObject(upperShape,new Color(0,0,0,0),true);
        jumping = true;
        lowerRod.setAcceleration(0,Constants.GRAVITY); //start off with gravity
        //setting rods stop
        
        RoundRectangle2D.Double torsoShape = new RoundRectangle2D.Double(torsoX,torsoY,5,20,3,3);
        RoundRectangle2D.Double headShape = new RoundRectangle2D.Double(headX,headY,10,10,5,5);
        RoundRectangle2D.Double neckShape = new RoundRectangle2D.Double(neckX,neckY,3,7,1,1);
        RoundRectangle2D.Double leftLegShape = new RoundRectangle2D.Double(legX,legY,4,15,1,1);
        RoundRectangle2D.Double leftArmShape = new RoundRectangle2D.Double(armX,armY,3,12,1,1);
        RoundRectangle2D.Double rightLegShape = new RoundRectangle2D.Double(legX,legY,4,15,1,1);
        RoundRectangle2D.Double rightArmShape = new RoundRectangle2D.Double(armX,armY,3,12,1,1);
        
        torso = new GameObject(torsoShape,c,true){
            @Override
            public void onGameTick(int tick, ArrayList<GameObject> collisions){
                endGameIfCollision(collisions);
                onGameTickDefault(tick,collisions);
            }
        };
        head = new GameObject(headShape,c,true){
            @Override
            public void onGameTick(int tick, ArrayList<GameObject> collisions){
                endGameIfCollision(collisions);
                onGameTickDefault(tick,collisions);
            }
        };
        neck = new GameObject(neckShape,c,true){
            @Override
            public void onGameTick(int tick, ArrayList<GameObject> collisions){
                endGameIfCollision(collisions);
                onGameTickDefault(tick,collisions);
            }
        };
        
        leftLeg = new GameObject(leftLegShape,c,true){
            @Override
            public void onGameTick(int tick, ArrayList<GameObject> collisions){
                endGameIfCollision(collisions);
                onGameTickDefault(tick,collisions);
                double runningSpeed = tick*(16.0/Constants.TICK_RATE);
                rotate(Math.sin(runningSpeed) * Math.PI/4,  lowerRod.getX()+2.5,  lowerRod.getY());
            }
        };
        rightLeg = new GameObject(rightLegShape,c,true){
            @Override
            public void onGameTick(int tick, ArrayList<GameObject> collisions){
                endGameIfCollision(collisions);
                onGameTickDefault(tick,collisions);
                double runningSpeed = tick*(16.0/Constants.TICK_RATE);
                rotate(Math.sin(Math.PI+runningSpeed) * Math.PI/4,  lowerRod.getX()+2.5,  lowerRod.getY());
            }
        };
        
        leftArm = new GameObject(leftArmShape,c,true){
            @Override
            public void onGameTick(int tick, ArrayList<GameObject> collisions){
                endGameIfCollision(collisions);
                onGameTickDefault(tick,collisions);
                double runningSpeed = tick*(16.0/Constants.TICK_RATE);
                rotate(Math.sin(runningSpeed) * Math.PI/4,  upperRod.getX()+2.5,  upperRod.getY());
            }
        };
        rightArm = new GameObject(rightArmShape,c,true){
            @Override
            public void onGameTick(int tick, ArrayList<GameObject> collisions){
                endGameIfCollision(collisions);
                onGameTickDefault(tick,collisions);
                double runningSpeed = tick*(16.0/Constants.TICK_RATE);
                rotate(Math.sin(Math.PI+runningSpeed) * Math.PI/4,  upperRod.getX()+2.5,  upperRod.getY());
            }
        };

        upperRod.attachTo(lowerRod);
        torso.attachTo(lowerRod);
        head.attachTo(lowerRod);
        neck.attachTo(lowerRod);
        leftLeg.attachTo(lowerRod);
        leftArm.attachTo(lowerRod);
        rightLeg.attachTo(lowerRod);
        rightArm.attachTo(lowerRod);
        
        lowerRod.setParent(this);
        upperRod.setParent(this);
        torso.setParent(this);
        head.setParent(this);
        neck.setParent(this);
        leftLeg.setParent(this);
        leftArm.setParent(this);
        rightLeg.setParent(this);
        rightArm.setParent(this);
    }
    public Player(Color c, char u, char d, char l, char r){
        this(
            c,
            KeyEvent.getExtendedKeyCodeForChar(u),
            KeyEvent.getExtendedKeyCodeForChar(d),
            KeyEvent.getExtendedKeyCodeForChar(l),
            KeyEvent.getExtendedKeyCodeForChar(r)
        );
    }
    
    private void endGameIfCollision(ArrayList<GameObject> collisions){
        if(gameEnds) return;
        for(GameObject item: collisions){
            if(item instanceof Ground){
                Ground g = (Ground)item;
                if(g.isDeadly()){
                    gameEnds = true;
                    break;
                }
            }
            if(
                item==leftLeg || item==rightLeg || item==leftArm || item==rightArm
                || item==head || item==upperRod || item==lowerRod || item==torso || item==neck
                || (item.getParent() instanceof Player) || (item instanceof Ground)
            ) continue;
            gameEnds = true;
            break;
        }
        if(!gameEnds) return;
        //SoundManager.playAudio(failSound);
        Game.stop();
    }
}
