package test;

import thewebsemantic.Nodeid;
import thewebsemantic.neo;

public class FunkyWinkerBean {
	
	transient Nodeid id;
	@neo public int i;
	@neo public int j;
	@neo public long x;
	@neo Boolean bad;
	@neo Boolean good;
	@neo public String a;
	@neo public String b;

	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public int getJ() {
		return j;
	}
	public void setJ(int j) {
		this.j = j;
	}
	public long getX() {
		return x;
	}
	public void setX(long x) {
		this.x = x;
	}

}