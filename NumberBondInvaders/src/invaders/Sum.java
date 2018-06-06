package invaders;

public class Sum 
{
	public static final int ADD = 1;
	public static final int SUBTRACT = 2;
	
	private int num1 = -1;
	private int ans = -1;
	private int operator = -1;
	
	public static Sum factory(int num1, int ans, int operator)
	{
		Sum retval = new Sum();
		retval.num1 = num1;
		retval.ans = ans;
		retval.operator = operator;
		
		return retval;
	}
	
	@Override public String toString()
	{
		String retval = "" + num1;
		if (operator == ADD)
		{
			retval += " + ";
		}
		else if (operator == SUBTRACT)
		{
			retval += " - ";
		}
		retval += "_ = " + ans;
		return retval;
	}
	
	public boolean isCorrect(int num2)
	{
		boolean retval = false;
		if (operator == ADD)
		{
			retval = num1 + num2 == ans;
		}
		else if (operator == SUBTRACT)
		{
			retval = num1 - num2 == ans;
		}
		return retval;
	}
}
