package pacman;

import java.awt.image.BufferedImage;

public class Texture {
	public static BufferedImage player;
	public static BufferedImage ghost;

	
	public Texture()
	{
		player = Pacman.spritesheet.getSprite(0, 0);
		ghost = Pacman.spritesheet.getSprite(0, 16);
	}
}
