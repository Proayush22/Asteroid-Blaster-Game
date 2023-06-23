
public class Projectile {
    private int xPos;
    private int yPos;
    private static int SPEED;
    public Projectile(int xPos, int yPos){
        SPEED = 5;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public int getX(){
        return xPos;
    }
    public int getY(){
        return yPos;
    }

    public void updateProjectilePosition(){
        yPos -= SPEED;
    }
}
