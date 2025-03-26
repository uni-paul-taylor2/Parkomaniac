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
    private static Clip jumpSound = SoundManager.getAudio("songs/jump.wav");
    private static Clip failSound = SoundManager.getAudio("songs/fail.wav");
    private static Clip stepSound = SoundManager.getAudio("songs/step.wav");
    
    private char UP, DOWN, LEFT, RIGHT; //keys for movement
    
    private GameObject head;
    private GameObject leftLeg;
    private GameObject leftArm;
    private GameObject rightLeg;
    private GameObject rightArm;
    private GameObject neck;
    private GameObject torso; //will be the "main" object of the player
    private GameObject upperRod; //for arm
    private GameObject lowerRod; //for leg
    
    private double torsoX=50, torsoY=200; //x and y of the "main" object of the player (the torso)
    private double armX=+5, armY=+0, legX=+5, legY=+15, headX=+0, headY=-7, neckX=+3, neckY=-3;
    
    private boolean gameEnds=false;
    private boolean jumping;
    private double jumpSpeed;
    private double origJumpSpeed(){return -0.35*Constants.GRAVITY;}
    
    @Override
    public void addToPanel(GamePanel p){
        p.addItem(torso,true,true);
        p.addItem(head,true,true);
        p.addItem(neck,true,true);
        p.addItem(leftLeg,true,true);
        p.addItem(leftArm,true,true);
        p.addItem(rightLeg,true,true);
        p.addItem(rightArm,true,true);
    }
    @Override
    public void removeFromPanel(GamePanel p){
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
    
    public Player(Color c, char up, char down, char left, char right)
    {
        UP = up;
        DOWN = down;
        LEFT = left;
        RIGHT = right;
        
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
        Rectangle2D.Double upperShape = new Rectangle2D.Double(armX,armY,2,7);
        Rectangle2D.Double lowerShape = new Rectangle2D.Double(legX,legY,3,10);
        upperRod = new GameObject(upperShape,new Color(0,0,0,0),true);
        lowerRod = new GameObject(lowerShape,new Color(0,0,0,0),true){
            @Override
            public void onGameTick(int tick, ArrayList<GameObject> collisions){
                boolean justStartedJumping = origJumpSpeed()==speedY;
                if(!justStartedJumping){
                    for(GameObject gameObject: collisions){
                        if(gameObject instanceof Ground){
                            land();
                            break;
                        }
                    }
                }
                onGameTickDefault(tick,collisions);
            }
        };
        //setting rods stop
        
        RoundRectangle2D.Double torsoShape = new RoundRectangle2D.Double(torsoX,torsoY,5,20,3,3);
        RoundRectangle2D.Double headShape = new RoundRectangle2D.Double(headX,headY,10,10,5,5);
        RoundRectangle2D.Double neckShape = new RoundRectangle2D.Double(neckX,neckY,3,7,1,1);
        RoundRectangle2D.Double leftLegShape = new RoundRectangle2D.Double(legX,legY,3,10,1,1);
        RoundRectangle2D.Double leftArmShape = new RoundRectangle2D.Double(armX,armY,2,7,1,1);
        RoundRectangle2D.Double rightLegShape = new RoundRectangle2D.Double(legX,legY,3,10,1,1);
        RoundRectangle2D.Double rightArmShape = new RoundRectangle2D.Double(armX,armY,2,7,1,1);
        
        torso = new GameObject(torsoShape,c,true){
            @Override
            public void onKeyPress(KeyEvent k){
                //handle movement of the player on the whole here
                boolean jumpKeyPressed = true; //obviously to change
                if(jumpKeyPressed){
                    if(!jumping){
                        jump();
                        speedY = jumpSpeed;
                    }
                }
            }
        };
        head = new GameObject(headShape,c,true){
            //todo
        };
        neck = new GameObject(neckShape,c,true){
            //todo
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

        head.attatchTo(torso);
        neck.attatchTo(torso);
        leftLeg.attatchTo(torso);
        leftArm.attatchTo(torso);
        rightLeg.attatchTo(torso);
        rightArm.attatchTo(torso);
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
            ) continue;
            gameEnds = true;
            break;
        }
        if(!gameEnds) return;
        SoundManager.playAudio(failSound);
        Game.stop();
    }
}
