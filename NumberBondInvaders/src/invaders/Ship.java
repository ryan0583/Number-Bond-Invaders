package invaders;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Ship 
{
	private int posX = -1;
	private int posY = -1;
	private int basePos = 50;
	private Image img = null;
	private int width = -1;
	private int height = -1;
	
	public static Ship factory()
	{
		Ship retval = new Ship();
		try
		{
			URL url = Ship.class.getResource("/ship.png");
			BufferedImage bi = ImageIO.read(url);
			retval.img = bi;
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
		PointerInfo pi = MouseInfo.getPointerInfo();
		Point mouseLocation = pi.getLocation();
		int mouseX = (int)mouseLocation.getX();
		
		int frameX = frame.getX();
		
		posX = mouseX - frameX - width/2;
		if (posX < 0)
		{
			posX = 0;
		}
		else if (posX + width >= frame.getWidth())
		{
			posX = frame.getWidth() - width;
		}
		posY = frame.getHeight() - basePos - height;
		
		g2d.drawImage(img, posX, posY, null);
	}
	
	public boolean hit(Baddie bad)
	{
		boolean retval = false;
		if (posY == -1)
		{
			return retval;
		}
		
		if (bad.getPosY() + bad.getHeight() >= posY
		  && (bad.getPosX() <= posX + width
		  || bad.getPosX() + bad.getWidth() >= posX))
		{
			retval = true;
		}
		
		return retval;
	}
	
	public boolean hit(Bullet b)
	{
		boolean retval = false;
		int bPosX = b.getPosX();
		int bTopPosY = b.getPosY();
		int bBottomPosY = bTopPosY + b.getLength();
		if (bPosX >= posX
		  && bPosX <= posX + width
		  && bBottomPosY >= posY
		  && bTopPosY <= posY + height)
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
