
package pacman;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import javax.swing.*;
import java.util.*;

public class Pacman extends Canvas implements Runnable,KeyListener {
    
    private boolean isRun = false;
    public static final int WIDTH = 640, HEIGHT = 480; 
    public static final String TITLE = "PAC-MAN";
    
    
    private Thread thread;
    public static Player player;
    public static Level level;
    public static SpriteSheet spritesheet;
    
    public Pacman()
    {
        Dimension dimension = new Dimension(Pacman.WIDTH, Pacman.HEIGHT);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);
        
        addKeyListener(this);
        player = new Player(Pacman.WIDTH/2, Pacman.HEIGHT/2);
        level = new Level("/bmap/map.png");
        spritesheet = new SpriteSheet("/sprites/spritesheet.png");
        
        new Texture();
        
    }
    
    public synchronized void start()
    {
        if (isRun) return;
        isRun = true;
        thread = new Thread(this);
        thread.start();
    }
    
    public synchronized void stop()
    {
        if (isRun) return;
        isRun = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    void tick()
    {
        player.tick();
        level.tick();
    }

    void render()
    {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) 
        {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, Pacman.WIDTH, Pacman.HEIGHT);
        player.render(g);
        level.render(g);
        g.dispose();
        bs.show();
        
    }
    
    @Override
    public void run() {
        requestFocus();
        int fps = 0;
        double timer = System.currentTimeMillis();
        long lastTime = System.nanoTime();
        double targetTick = 60.0;
        double delta = 0;
        double ns = 1000000000/targetTick;
        while (isRun)
        {
            long now = System.nanoTime();
            delta += (now - lastTime)/ns;
            lastTime = now;
            
            while (delta >= 1) {                
                tick();
                render();
                fps++;
                delta--;
            }
            if (System.currentTimeMillis() - timer >= 1000) {
                System.err.println(fps);
                fps = 0;
                timer+=1000;
            }
        }
        stop();
    }
    
    
    
    public static void main(String[] args)
    {
        Pacman game = new Pacman();
        JFrame frame = new JFrame(Pacman.TITLE);
        frame.add(game);
        frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        game.start();
        
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) player.right = true;
        if(e.getKeyCode() == KeyEvent.VK_LEFT) player.left = true;
        if(e.getKeyCode() == KeyEvent.VK_UP) player.up = true;
        if(e.getKeyCode() == KeyEvent.VK_DOWN) player.down = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) player.right = false;
        if(e.getKeyCode() == KeyEvent.VK_LEFT) player.left = false;
        if(e.getKeyCode() == KeyEvent.VK_UP) player.up = false;
        if(e.getKeyCode() == KeyEvent.VK_DOWN) player.down = false;
    }
    
}
