package invaders;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Explosion 
{
	private static final int FRAME_LENGTH = 10;
	
	private int posX = -1;
	private int posY = -1;
	private ArrayList<Image> imgs = new ArrayList<Image>();
	private int imgIndex = 0;
	private int changeImageCounter = 0;
	private int height = -1;
	private int width = -1;
	
	public static Explosion factory(int posX, int posY, int height, int width)
	{
		Explosion retval = new Explosion();
		retval.posX = posX;
		retval.posY = posY;
		retval.height = height;
		retval.width = width;
		
		try
		{
			URL url = Explosion.class.getResource("/exp1.png");
			BufferedImage bi = ImageIO.read(url);
			retval.imgs.add(bi);
			URL url2 = Explosion.class.getResource("/exp2.png");
			BufferedImage bi2 = ImageIO.read(url2);
			retval.imgs.add(bi2);
			URL url3 = Explosion.class.getResource("/exp3.png");
			BufferedImage bi3 = ImageIO.read(url3);
			retval.imgs.add(bi3);
		}
		catch (Throwable t)
		{
			System.out.println("Failed to load image for ship.");
		}
		
		return retval;
	}
	
	public void draw(Graphics2D g2d)
	{
		if (done())
		{
			return;
		}
		Image img = imgs.get(imgIndex);
		g2d.drawImage(img, posX, posY, height, width, null);
		
		changeImageCounter++;
		if (changeImageCounter > FRAME_LENGTH)
		{
			imgIndex++;
			changeImageCounter = 0;
		}
	}
	
	public boolean done()
	{
		return imgIndex >= imgs.size();
	}
}
