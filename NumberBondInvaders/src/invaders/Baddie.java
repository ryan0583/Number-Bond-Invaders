package invaders;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Baddie 
{
	private static final int FRAME_LENGTH = 10;
	
	private int posX = -1;
	private int posY = -1;
	private boolean moveRight = true;
	private boolean image1 = true;
	private int changeImageCounter = 0;
	private Image img1 = null;
	private Image img2 = null;
	private int width = -1;
	private int height = -1;

	public static Baddie factory(int posX, int posY, boolean moveRight)
	{
		Baddie retval = new Baddie();
		retval.posX = posX;
		retval.posY = posY;
		retval.moveRight = moveRight;
		
		try
		{
			URL url = Baddie.class.getResource("/baddie1.png");
			BufferedImage bi = ImageIO.read(url);
			retval.img1 = bi;
			URL url2 = Baddie.class.getResource("/baddie2.png");
			BufferedImage bi2 = ImageIO.read(url2);
			retval.img2 = bi2;
			retval.width = bi.getWidth();
			retval.height = bi.getHeight();
			
		}
		catch (Throwable t)
		{
			System.out.println("Failed to load image for ship.");
		}
		
		return retval;
	}
	
	public void draw(Graphics2D g2d, JFrame frame)
	{
		if (image1)
		{
			g2d.drawImage(img1, posX - width/2, posY, null);
		}
		else
		{
			g2d.drawImage(img2, posX - width/2, posY, null);
		}
		changeImageCounter++;
		if (changeImageCounter > FRAME_LENGTH)
		{
			image1 = !image1;
			changeImageCounter = 0;
		}
	}
	
	public void move(boolean changeDir)
	{
		if (changeDir)
		{
			moveRight = !moveRight;
		}
		
		if (moveRight)
		{
			posX++;
		}
		else
		{
			posX--;
		}
		if (changeDir)
		{
			this.posY += 30;
		}
	}
	
	public boolean hit(Bullet bul)
	{
		int bulPosY = bul.getPosY();
		int bulPosX = bul.getPosX();
		int bulLength = bul.getLength();

		boolean retval = false;
		
		boolean tipIntersects = bulPosY >= posY && bulPosY <= posY + height;
		boolean backIntersects = bulPosY + bulLength >= posY && bulPosY + bulLength <= posY + height;
		boolean intersectsY = tipIntersects || backIntersects;
		boolean intersectsX = bulPosX >= posX && bulPosX <= posX + width;
		if (intersectsY
		  && intersectsX)
		{
			retval = true;
		}
		
		return retval;
	}
	
	public boolean changeDir(JFrame frame)
	{
		boolean retval = false;
		if (posX + width/2 >= frame.getWidth()
		  || posX <= 0)
		{
			retval = true;
		}
		
		return retval;
	}
	
	public int getPosX()
	{
		return posX;
	}
	public int getPosY()
	{
		return posY;
	}
	public int getHeight()
	{
		return height;
	}
	public int getWidth()
	{
		return width;
	}
}
