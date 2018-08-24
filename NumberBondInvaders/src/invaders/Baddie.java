package invaders;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Baddie 
{
	public static final int WIDTH = 40;
	public static final int HEIGHT = 40;
	
	private int posX = -1;
	private int posY = -1;
	private int speedX = -1;
	private int speedY = -1;
	private int num = -1;

	public static Baddie factory(int posX, int posY, int speedX, int speedY, int num)
	{
		Baddie retval = new Baddie();
		retval.posX = posX;
		retval.posY = posY;
		retval.speedX = speedX;
		retval.speedY = speedY;
		retval.num = num;
		
		return retval;
	}
	
	public void draw(Graphics2D g2d)
	{
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Arial", Font.PLAIN, 30)); 
		
		g2d.drawString("" + num, posX, posY);
	}
	
	public void move(int bottomBoundary, int topBoundary)
	{
		posX = posX + speedX;
		posY = posY + speedY;
		
		if (posX < 0
		  || posX > GameFrame.WIDTH)
		{
			speedX = -speedX;
		}
		
		if (posY < topBoundary
		  || posY > bottomBoundary)
		{
			speedY = -speedY;
		}
	}
	
	public boolean hit(Bullet bul, Sum sum)
	{
		if (!sum.isCorrect(num))
		{
			return false;
		}
		
		int bulPosY = bul.getPosY();
		int bulPosX = bul.getPosX();
		int bulLength = bul.getLength();

		boolean retval = false;
		
		boolean tipIntersects = bulPosY >= posY && bulPosY <= posY + HEIGHT;
		boolean backIntersects = bulPosY + bulLength >= posY && bulPosY + bulLength <= posY + HEIGHT;
		boolean intersectsY = tipIntersects || backIntersects;
		boolean intersectsX = bulPosX >= posX && bulPosX <= posX + WIDTH;
		if (intersectsY
		  && intersectsX)
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
	public int getNum()
	{
		return num;
	}
}
