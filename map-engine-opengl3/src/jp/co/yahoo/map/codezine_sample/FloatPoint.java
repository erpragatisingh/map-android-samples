package jp.co.yahoo.map.codezine_sample;

public class FloatPoint implements Cloneable {
	public float x;
	public float y;
	public FloatPoint() {
	}
	
	@Override
	public FloatPoint clone() {
		FloatPoint c = new FloatPoint();
		c.x = this.x;
		c.y = this.y;
		return c;
	}
	public FloatPoint(float x,float y) {
		this.x=x;
		this.y=y;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public void translate(float dx,float dy) {
		x+=dx;
		y+=dy;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FloatPoint other = (FloatPoint) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "FloatPoint [x=" + x + ", y=" + y + "]";
	}
}
