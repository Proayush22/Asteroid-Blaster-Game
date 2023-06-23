
import javax.swing.JComponent;

public class Asteroid {
    private int asteroidX;
    private int asteroidY;
    private boolean isDestroyed;
    private JComponent component;
    public Asteroid(JComponent component){
        this.component = component;
        isDestroyed = false;
        asteroidX = (int)(Math.random() * 350);
        asteroidY = -150;
    }
    public int getAsteroidX(){
        return asteroidX;
    }
    public int getAsteroidY(){
        return asteroidY;
    }
    public boolean isDestroyed(){
        return isDestroyed;
    }
    public void setDestroyed(boolean isDestroyed){
        isDestroyed = true;
    }
    public void updateAsteroid(){
        if(!isDestroyed){
            asteroidY += 2;
        }

        if(asteroidY > component.getHeight()){
            asteroidY = -150;
            asteroidX = (int)(Math.random()*400);
        }
    }
}
