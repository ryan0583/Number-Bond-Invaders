package invaders;

import java.awt.Color;
import java.awt.Graphics2D;

public class Star 
{
	private int posX = -1;
	private int posY = -1;
	
	public static Star factory(int posX, int posY)
	{
		Star retval = new Star();
		retval.posX = posX;
		retval.posY = posY;
		return retval;
	}
	
	public void draw(Graphics2D g2d)
	{
		g2d.setColor(Color.WHITE);
		g2d.drawRect(posX, posY, 2, 2);
	}
	
	public void move()
	{
		posY += 2;
		if (posY > GameFrame.HEIGHT)
		{
			posY = 0;
		}
	}
}
