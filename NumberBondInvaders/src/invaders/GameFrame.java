package invaders;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import javazoom.jl.player.Player;

public class GameFrame extends JPanel 
					   implements MouseListener
{
	public static int HEIGHT = 750;
	public static int WIDTH = 1375;
	
	private static final long serialVersionUID = 1;
	private static final int STARS = 100;
	private static final int MAX_BULLETS = 10;
	private static final int START_RECT_X = (int)((WIDTH / 2.73) + 55);
	private static final int START_RECT_Y = HEIGHT / 2 - 70;
	private static final int START_RECT_WIDTH = 200;
	private static final int START_RECT_HEIGHT = 100;
	private static final int MAX_SUM_ANS = 20;
	private static final int MAX_BADDIE_SPEED = 1;
	private static final int TOP_BAR_HEIGHT = 150;
	
	private static JFrame frame = null;
	private Ship ship = null;
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private ArrayList<Baddie> baddies = new ArrayList<Baddie>();
	private ArrayList<Explosion> exps = new ArrayList<Explosion>();
	private ArrayList<Star> stars = new ArrayList<Star>();
	private boolean gameStarted = false;
	private boolean gameWon = false;
	private boolean gameOver = false;
	private boolean flashText = true;
	private int flashCount = 0;
	private int doneTimer = 0;
	private int score = 0;
	private Sum sum = null;
	private ArrayList<Integer> answers = new ArrayList<Integer>();
	
	public static void initGameFrame() throws InterruptedException
	{
		frame = new JFrame("Invaders");
		frame.setUndecorated(true);
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = e.getDefaultScreenDevice();
		frame.setResizable(false);
		gd.setFullScreenWindow(frame);
		
		GameFrame gf = new GameFrame();
		frame.getContentPane().add(gf);
		//frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		gf.setBackground(Color.BLACK);
		
		gf.addMouseListener(gf);
		
		AbstractAction escapeAction = new AbstractAction() 
		{
		    private static final long serialVersionUID = 1L;

		    @Override
		    public void actionPerformed(ActionEvent ae) 
		    {
		        System.exit(0);
		    }
		};
		JRootPane root = gf.getRootPane();
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESCAPE_KEY");
		root.getActionMap().put("ESCAPE_KEY", escapeAction);
		
		gf.startGame();
	}
	
	private void startGame() throws InterruptedException
	{
		reset();
		
		ship = Ship.factory();
		
		for (int i=0; i<MAX_SUM_ANS; i++)
		{
			int num = i;
			addBaddie(num);
		}
		
		newSum();
		
		for (int i=0; i<STARS; i++)
		{
			int posX = (int)(WIDTH * Math.random());
			int posY = (int)(HEIGHT * Math.random());
			Star s =  Star.factory(posX, posY);
			stars.add(s);
		}
		
		while (true)
		{
			repaint();
			isGameWon();
			isGameOver();
			if (exps.isEmpty()
			  && (gameWon
			  || gameOver))
			{
				doneTimer++;
				if (doneTimer > 100)
				{
					break;
				}
			}
			
			Thread.sleep(15);
		}	
		
		gameStarted = false;
		startGame();
	}
	
	private void reset()
	{
		gameWon = false;
		gameOver = false;
		flashText = true;
		flashCount = 0;
		doneTimer = 0;
		bullets.clear();
		baddies.clear();
		stars.clear();
		exps.clear();
		score = 0;
		sum = null;
		answers.clear();
	}
	
	private void addBaddie(int num)
	{
		int posX = (int)(WIDTH * Math.random());
		int posY = (int)(HEIGHT * Math.random());
		int maxPosY = HEIGHT - Ship.BASE_POS - ship.getHeight();
		if (posY < TOP_BAR_HEIGHT)
		{
			posY = TOP_BAR_HEIGHT;
		}
		else if (posY > maxPosY)
		{
			posY = maxPosY;
		}
		
		double backwardsD = Math.random();
		boolean backwards = backwardsD > 0.5;
		int backMultiplier = 1;
		if (backwards)
		{
			backMultiplier = -1;
		}
		int speedX = (int)(backMultiplier * MAX_BADDIE_SPEED * Math.random()) + (backMultiplier * 1);
		double upD = Math.random();
		boolean up = upD > 0.5;
		int upMultiplier = 1;
		if (up)
		{
			upMultiplier = -1;
		}
		int speedY = (int)(upMultiplier * MAX_BADDIE_SPEED * Math.random()) + (upMultiplier * 1);
		
		Baddie bad = Baddie.factory(posX, posY, speedX, speedY, num);
		baddies.add(bad);
	}
	
	@Override public void paint(Graphics g)
	{
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		drawStars(g2d);
		
		if (!gameStarted)
		{
			showTitleScreen(g2d);
			return;
		}
		synchronized (exps) 
		{
			if (exps.isEmpty()
			  && gameWon)
			{
				showFinishMessage("YOU WIN!!!", g2d);
				return;
			}
			if (exps.isEmpty()
			  && gameOver)
			{
				showFinishMessage("YOU LOSE!!!", g2d);
				return;
			}
		}
		
		drawScore(g2d);
		
		drawSum(g2d);
		
		drawExplosions(g2d);
		
		drawShip(g2d);
		
		drawBullets(g2d);
		
		drawBaddies(g2d);
	}

	private void drawScore(Graphics2D g2d)
	{
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Arial", Font.PLAIN, 18)); 
		g2d.drawString("SCORE: " + score, frame.getWidth() - 200, 30);
	}
	
	private void drawSum(Graphics2D g2d)
	{
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Arial", Font.PLAIN, 50)); 
		g2d.drawString("" + sum, frame.getWidth()/2 - 200, 40);
	}
	
	private void drawStars(Graphics2D g2d)
	{
		for (int i=0; i<stars.size(); i++)
		{
			Star s = stars.get(i);
			s.draw(g2d);
			s.move();
		}
	}
	private void drawShip(Graphics2D g2d)
	{
		if (ship != null)
		{
			ship.draw(g2d, frame);
		}
	}
	private void drawBullets(Graphics2D g2d)
	{
		for (int i=bullets.size()-1; i>=0; i--)
		{
			Bullet b = bullets.get(i);
			b.draw(g2d);
			b.move();
			
			if (b.getPosY() <= 0)
			{
				bullets.remove(b);
			}
			
			removeBaddies(b);
		}
	}
	
	private void removeBaddies(Bullet b)
	{
		boolean hitBaddie = false;
		for (int i=baddies.size()-1; i>=0; i--)
		{
			Baddie bad = baddies.get(i);
			if (bad.hit(b, sum))
			{
				score += 100;
				playSound("explosion_large_distant.mp3");
				Explosion exp = Explosion.factory(bad.getPosX() - Baddie.WIDTH/2, bad.getPosY() - Baddie.HEIGHT/2, Baddie.HEIGHT, Baddie.WIDTH);
				exps.add(exp);
				
				baddies.remove(bad);
				bullets.remove(b);
				
				addBaddie(bad.getNum());
				hitBaddie = true;
			}
		}
		
		if (hitBaddie)
		{
			newSum();
		}
	}
	
	private void drawBaddies(Graphics2D g2d) 
	{
		for (int i=0; i<baddies.size(); i++)
		{
			Baddie bad = baddies.get(i);
			bad.draw(g2d);
			bad.move(HEIGHT - ship.getHeight() - Ship.BASE_POS, 0 + TOP_BAR_HEIGHT);
		}
	}

	private void playSound(final String fileName) 
	{
		Runnable r = new Runnable() 
		{
			public void run() 
		    {
				try
				{
					URL url = GameFrame.class.getResource("/" + fileName);
					InputStream is = url.openStream();
			 		Player playMP3 = new Player(is);
			 		playMP3.play();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
		    }
		};
		new Thread(r).start();
	}
	
	private void drawExplosions(Graphics2D g2d)
	{
		for (int i=exps.size()-1; i>=0; i--)
		{
			Explosion exp = exps.get(i);
			exp.draw(g2d);
			if (exp.done())
			{
				exps.remove(exp);
			}
		}
	}
	
	private void showTitleScreen(Graphics g2d)
	{
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Arial", Font.ITALIC, 72)); 
		g2d.drawString("NUMBER BOND INVADERS", frame.getWidth() / 6, frame.getHeight() / 4);
		
		g2d.setColor(Color.RED);
		g2d.fillRect(START_RECT_X, START_RECT_Y, START_RECT_WIDTH, START_RECT_HEIGHT);
		
		
		g2d.setColor(Color.WHITE);
		if (isMouseOnStart())
		{
			g2d.setColor(Color.YELLOW);
		}
		
		g2d.setFont(new Font("Arial", Font.PLAIN, 36)); 
		g2d.drawString("START", (int)(frame.getWidth() / 2.3), frame.getHeight() / 2);
	}
	
	private void showFinishMessage(String message, Graphics2D g2d)
	{
		g2d.setColor(Color.WHITE);
		if (flashText)
		{
			g2d.setFont(new Font("Arial", Font.ITALIC, 72)); 
			g2d.drawString(message, frame.getWidth() / 3 + 20, frame.getHeight() / 2);
			flashCount++;
			if (flashCount > 10)
			{
				flashText = false;
				flashCount = 0;
			}
		}
		else
		{
			flashCount++;
			if (flashCount > 10)
			{
				flashText = true;
				flashCount = 0;
			}
		}
		
		g2d.setFont(new Font("Arial", Font.PLAIN, 72)); 
		g2d.drawString("SCORE: " + score, frame.getWidth() / 3 + 30, frame.getHeight() / 2 + 200);
		
		return;
	}
	
	private void newSum()
	{
		double operatorRand = Math.random();
		int operator = Sum.ADD;
		if (operatorRand > 0.5)
		{
			operator = Sum.SUBTRACT;
		}
		
		int ans = -1;
		int num1 = -1;
		if (operator == Sum.ADD)
		{
			ans = (int)(Math.random() * MAX_SUM_ANS) + 1;
			num1 = (int)(Math.random() * ans) + 1;
		}
		else if (operator == Sum.SUBTRACT)
		{
			num1 = (int)(Math.random() * MAX_SUM_ANS) + 1;
			ans = (int)(Math.random() * num1) + 1;
		}
		
		sum = Sum.factory(num1, ans, operator);
	}
	
	private void isGameWon()
	{
		if (gameWon)
		{
			return;
		}
		if (baddies.size() <= 0)
		{
			gameWon = true;
		}
	}
	
	private void isGameOver()
	{
		if (gameOver)
		{
			return;
		}
		synchronized (exps) 
		{
			for (int i=0; i<baddies.size(); i++)
			{
				Baddie bad =  baddies.get(i);
				gameOver = ship.hit(bad);
				if (gameOver)
				{
					break;
				}
			}
			
			if (gameOver)
			{	
				playSound("large_explosion.mp3");
				Explosion exp = Explosion.factory(ship.getPosX(), ship.getPosY(), ship.getHeight(), ship.getWidth());
				exps.add(exp);
				ship = null;
			}
		}
	}
	
	private boolean isMouseOnStart()
	{
		boolean retval = false;
		PointerInfo pi = MouseInfo.getPointerInfo();
		Point mouseLocation = pi.getLocation();
		int mouseX = (int)mouseLocation.getX();
		int mouseY = (int)mouseLocation.getY();
		
		if (mouseX >= START_RECT_X
		  && mouseX <= START_RECT_X + START_RECT_WIDTH
		  && mouseY-22 >= START_RECT_Y
		  && mouseY-22 <= START_RECT_Y + START_RECT_HEIGHT)
		{
			retval = true;
		}
		
		return retval;
	}


	@Override
	public void mouseClicked(MouseEvent e) 
	{
	}


	@Override
	public void mouseEntered(MouseEvent e) 
	{
	}


	@Override
	public void mouseExited(MouseEvent e) 
	{
	}


	@Override
	public void mousePressed(MouseEvent e) 
	{
	}


	@Override
	public void mouseReleased(MouseEvent e) 
	{
		if (!gameStarted)
		{
			if (isMouseOnStart())
			{
				gameStarted = true;
			}
		}
		else
		{
			fireBullet();
		}
	}
	
	private void fireBullet()
	{
		if (bullets.size() >= MAX_BULLETS)
		{
			return;
		}
		
		playSound("science_fiction_laser_005.mp3");
		
		Bullet b = Bullet.factory(frame.getHeight()-100);
		bullets.add(b);
	}
}
