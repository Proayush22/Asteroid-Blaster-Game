
import java.util.*;
import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Game extends JComponent{
    private int shipX;
    private int shipY;
    private double time;
    private int lives;
    private int asteroidsHit;
    private int shotsFired;
    private boolean gameOver;


    private ArrayList <Asteroid> asteroids = new ArrayList<>();
    private ArrayList <Projectile> projectiles = new ArrayList<>();
    private ArrayList <Rectangle> projectileRectangles = new ArrayList<>();
    private ArrayList <Rectangle> enemyRectangles = new ArrayList<>();
    private Rectangle playerRectangle;
    private JFrame frame;
    public Game(JFrame frame){
        this.frame = frame;
        time= 15000;
        shipX = 175;
        shipY = 360;
        lives = 3;
        playerRectangle = new Rectangle(shipX, shipY, 46, 58);
        setFocusable(true);
        addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent event){
                handleKeyPress(event);
            }
        });


        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (!gameOver) {
                    updateScreen();
                    frame.repaint();
                }
            }
        });
        timer.start();



    }

    public void updateEnemyRectangles(){
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid asteroid = asteroids.get(i);
            Rectangle enemyRectangle = new Rectangle(asteroid.getAsteroidX(), (asteroid.getAsteroidY() + 110), 30, 30);
            enemyRectangles.set(i, enemyRectangle);
        }
    }

    private void handleKeyPress(KeyEvent event){
        if(event.getExtendedKeyCode() == KeyEvent.VK_UP || event.getExtendedKeyCode() == KeyEvent.VK_W){
            if((shipY + 10) > 370)
                shipY -= 10;
            updateScreen();
        }
        if(event.getExtendedKeyCode() == KeyEvent.VK_DOWN || event.getExtendedKeyCode() == KeyEvent.VK_S){
            if((shipY + 58 + 10) < 600)
                shipY += 10;
            updateScreen();
        }
        if(event.getExtendedKeyCode() == KeyEvent.VK_LEFT || event.getExtendedKeyCode() == KeyEvent.VK_A){
            if((shipX + 10) > 10)
                shipX -= 10;
            updateScreen();
        }
        if(event.getExtendedKeyCode() == KeyEvent.VK_RIGHT || event.getExtendedKeyCode() == KeyEvent.VK_D){
            if((shipX + 46 + 10) < 390)
                shipX += 10;
            updateScreen();
        }
        if(event.getExtendedKeyCode() == KeyEvent.VK_SPACE){
            shoot();
            updateScreen();
        }
	    playerRectangle.setLocation(shipX, shipY);
        repaint();
    }
    private void shoot(){
        Projectile proj = new Projectile((shipX + (46 / 2) - 8), shipY);
        Rectangle projRect = new Rectangle((shipX + (46 / 2) - 8), shipY , 15, 10);
        projectiles.add(proj);
        projectileRectangles.add(projRect);
        shotsFired++;
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("blasterSound.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        }
        catch(UnsupportedAudioFileException | IOException | LineUnavailableException ex){
            System.out.println("Error with playing sound.");
        }


    }
    private void checkForAsteroidCollisions(){
	    /*if(asteroids.size() > 0){
            for (int i = 0; i < enemyRectangles.size(); i++) {
	            Rectangle enemyRectangle = enemyRectangles.get(i);
	            if (playerRectangle.intersects(enemyRectangle)) {
	                removeAsteroid(i);
	                lives--;
	            }
	        }
        } */
        for(int i = 0; i < enemyRectangles.size(); i++){
            if((enemyRectangles.get(i)).intersects(playerRectangle)){
                removeAsteroid(i);
                lives--;
                asteroidsHit++;
            }

        }
    }
    private void generateNewAsteroid(){
        if(time % 100 == 0) {
            Asteroid aster = new Asteroid(this);
            asteroids.add(aster);
            enemyRectangles.add(new Rectangle(aster.getAsteroidX(), aster.getAsteroidY(), 25, 25));
        }
    }
    private void removeAsteroid(int index){
        asteroids.remove(index);
        enemyRectangles.remove(index);
    }
    private void updateAsteroidLocation(){
        if(asteroids.size() > 0){
            for(Asteroid aster: asteroids){
                aster.updateAsteroid();
            }
        }
    }
    private void checkProjectileCollisions(){
        updateProjectiles();
        for(int i = 0; i < asteroids.size(); i++){
            for(int j = 0; j < projectiles.size(); j++){
                if(enemyRectangles.get(i).intersects(projectileRectangles.get(j))){
                    projectiles.remove(j);
                    projectileRectangles.remove(j);
                    asteroids.remove(i);
                    enemyRectangles.remove(i);
                    asteroidsHit++;
                }
                repaint();
            }
        }
    }
    private void updateProjectiles(){
	    for (Projectile projectile : projectiles) {
            projectile.updateProjectilePosition();
            projectileRectangles.set(projectiles.indexOf(projectile), new Rectangle(projectile.getX(), projectile.getY(), 10, 10));
        }
        for(int i = 0; i < projectiles.size(); i++){
            if(projectiles.get(i).getY() < 0){
                projectiles.remove(i);
                projectileRectangles.remove(i);
            }
        }
    }
    private void updateScreen(){
        checkForAsteroidCollisions();
        updateAsteroidLocation();
        updateEnemyRectangles();
        generateNewAsteroid();
        checkProjectileCollisions();
        updateProjectiles();
        repaint();
    }
    private void drawShip(Graphics graphics){

        ImageIcon blaster = new ImageIcon("Blaster.png");
        graphics.drawImage(blaster.getImage(), shipX, shipY, 46, 58, this);
        if(lives == 3){
            int alpha = 20;
            Color myColour = new Color(0, 255, 0, alpha);
            graphics.setColor(myColour);
            graphics.fillOval(shipX, shipY, 50, 50);
        }
        if(lives == 2){
            int alpha = 20;
            Color myColour = new Color(255, 165, 0, alpha);
            graphics.setColor(myColour);
            graphics.fillOval(shipX, shipY, 50, 50);
        }
        if(lives == 1){
            int alpha = 20;
            Color myColour = new Color(255, 0, 0, alpha);
            graphics.setColor(myColour);
            graphics.fillOval(shipX, shipY, 50, 50);
        }

        //graphics.setColor(Color.BLUE);
        //graphics.fillRect(shipX, shipY, 50, 65);

    }
    private void drawAsteroids(Graphics graphics){

        graphics.setColor(Color.RED);
        for(Asteroid aster: asteroids){
            //graphics.fillOval(aster.getAsteroidX(), aster.getAsteroidY(), 25, 25);

            ImageIcon comet = new ImageIcon("asteroidI.png");
            graphics.drawImage(comet.getImage(), aster.getAsteroidX(), aster.getAsteroidY(), 30, 140, this);

            updateEnemyRectangles();
        }
        repaint();
    }
    private void drawProjectiles(Graphics graphics){

        graphics.setColor(Color.MAGENTA);
        for(Projectile proj: projectiles){
            ImageIcon shot = new ImageIcon("shot.png");
            graphics.drawImage(shot.getImage(), proj.getX(), proj.getY(), 15, 40, this);

            //graphics.fillRect(proj.getX(), proj.getY(), 10, 10);
        }
        repaint();
    }
    private void setEndScreenText(Graphics graphics, String str)  {
        graphics.setColor(Color.RED);
        Font stringFont = new Font("TimesRoman", Font.PLAIN, 14);
        graphics.setFont(stringFont);
        graphics.drawString(str, 40, 370);







        graphics.setColor(Color.CYAN);
        String stats1 = asteroidsHit + " Asteroids Hit";
        String stats2 = shotsFired + " Shots Fired";
        graphics.drawString("Stats:", 40, 410);
        graphics.drawString(stats1, 40, 427);
        graphics.drawString(stats2, 40, 444);

    }
    private void setGameOver(Graphics graphics){

        if (asteroids.size() > 0) {
            asteroids.subList(0, asteroids.size()).clear();
        }
        if (projectiles.size() > 0) {
            projectiles.subList(0, projectiles.size()).clear();
        }

        graphics.setColor(Color.BLACK);
        graphics.fillRect(27, 190, 330, 300);
        graphics.setColor(Color.WHITE);
        graphics.drawRect(27, 190, 330, 300);
        graphics.setColor(Color.RED);
        Font stringFont = new Font("Impact", Font.PLAIN, 70);
        graphics.setFont(stringFont);
        graphics.drawString("GAME OVER", 38, 300);


        if(asteroidsHit >= 10 && time > 0) {
            setEndScreenText(graphics, "10 ASTEROIDS DESTROYED, YOU WIN!");
        }
        else if(lives == 0) {
            setEndScreenText(graphics, "ALL LIVES LOST, YOU LOSE!");
        }
        else if(time <= 0) {
            setEndScreenText(graphics, "OUT OF TIME, YOU LOSE! ");
        }

    }
    protected void paintComponent(Graphics graphics){ // from JComponent

        Image icon = new ImageIcon("giffy.gif").getImage();
        graphics.drawImage(icon, 0, 0, frame.getWidth(), frame.getHeight(), this);

	  	//graphics.setColor(Color.BLACK);
		//graphics.draw(0, 0, frame.getWidth(), frame.getHeight());

		graphics.setColor(Color.WHITE);
	    graphics.drawString("Asteroids Hit: " + asteroidsHit, 10, 20);

		graphics.setColor(Color.RED);
	    graphics.drawString("Time: " + (time/1000), frame.getWidth() - 80, 20);

		graphics.setColor(Color.RED);
	    graphics.drawString("Lives: "+ lives, 165, 20);

	    if (!gameOver){
			drawShip(graphics);
		    drawAsteroids(graphics);
			drawProjectiles(graphics);
				if (time > 0) {
				    time--;
				}
                if(time == 0 || lives <= 0 || asteroidsHit >= 10){
					gameOver = true;
                    setGameOver(graphics);
				}
		}
        else{
			setGameOver(graphics);
		}
		drawAsteroids(graphics);

        repaint();

    }
}



