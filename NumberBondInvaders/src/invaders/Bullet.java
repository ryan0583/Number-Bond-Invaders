package invaders;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

public class Bullet 
{
	private int posX = -1;
	private int posY = -1;
	private int length = 30;
	private boolean bad = false;
	
	public static Bullet factory(int posY)
	{
		Bullet retval = new Bullet();
		PointerInfo pi = MouseInfo.getPointerInfo();
		Point mouseLocation = pi.getLocation();
		int mouseX = (int)mouseLocation.getX();
		retval.posX = mouseX;
		retval.posY = posY;
		return retval;
	}
	
	public static Bullet factoryBad(int posX, int posY)
	{
		Bullet retval = new Bullet();
		retval.posX = posX;
		retval.posY = posY;
		retval.bad = true;
		return retval;
	}
	
	public void draw(Graphics2D g2d)
	{
		if (bad)
		{
			g2d.setColor(Color.WHITE);
			int y = posY;
			while (y - posY <= length)
			{
				/*g2d.drawLine(posX + 6, y, posX - 6, y + 6);
				y += 6;
				g2d.drawLine(posX - 6, y, posX + 6, y + 6);
				y += 6;*/
				
				g2d.drawLine(posX, y, posX - 3, y + 3);
				y += 3;
				g2d.drawLine(posX - 3, y, posX + 3, y + 6);
				y += 6;
				g2d.drawLine(posX + 3, y, posX, y + 3);
				y += 3;
			}
		}
		else
		{
			g2d.setColor(Color.WHITE);
			g2d.drawLine(posX, posY, posX, posY + length);
		}
	}
	
	
	public void move()
	{
		if (bad)
		{
			posY += 3;
		}
		else
		{
			posY -= 3;
		}
	}
	
	public int getPosY()
	{
		return posY;
	}
	public int getPosX()
	{
		return posX;
	}
	public int getLength()
	{
		return length;
	}
}
