package com.shon.marketorder2;

public interface ConditionInterface {
	public void setTitle(String str);
	public String getText();
	// 是否满足了必选的条件
	public boolean checkMandatory();
	
}
