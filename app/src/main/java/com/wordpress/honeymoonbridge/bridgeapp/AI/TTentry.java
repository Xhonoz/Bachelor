package com.wordpress.honeymoonbridge.bridgeapp.AI;

public class TTentry
{
    private int Max;
    private int Min;

    public TTentry(int Max, int Min)
    {
        this.Max = Max;
        this.Min = Min;
    }

	public int getMax() {
		return Max;
	}

	public void setMax(int max) {
		Max = max;
	}

	public int getMin() {
		return Min;
	}

	public void setMin(int min) {
		Min = min;
	}
    
    
}