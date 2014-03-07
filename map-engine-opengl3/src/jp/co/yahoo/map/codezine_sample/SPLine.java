package jp.co.yahoo.map.codezine_sample;

public class SPLine {

	public float[] vertex = null;

	public SPLine(float[] coord,int w,int h) {
		this.vertex = ffconvert(coord,w,h);
	}

	/**
	 * 破線
	 * **/
	public static float[] ffconvert(float[] line, int w, int h) {
		
		int pLen = line.length / 3;  //直線数
		
		float buf[] = new float[(pLen)*5];
		ffmakeLine(line, buf, w, h, line.length);

		return buf;
	}

	private static void ffmakeLine(float[] line, float[] buf, int w, int h, int lLen) {
		
		float nextTexHeight = 0.0f;
		try {
			float x1,y1,z1,x2,y2,z2;

			buf[0] = line[0];
			buf[1] = line[1];
			buf[2] = line[2];
			buf[3] = 0.5f;
			buf[4] = nextTexHeight;

			for(int i=3,j=5;i<lLen;i+=3,j+=5) {
				x1 = line[i-3];
				y1 = line[i-2];
				x2 = line[i];
				y2 = line[i+1];
				z2 = line[i+2];

				double dx = x2 - x1;
				double dy = y2 - y1;
				float texh = (float) (Math.abs(Math.sqrt((dx*dx) + (dy*dy)))/h);
				nextTexHeight = nextTexHeight+texh;
				buf[j] = x2;
				buf[j+1] = y2;
				buf[j+2] = z2;
				buf[j+3] = 0.5f;
				buf[j+4] = nextTexHeight;
			}
		} catch (Exception e) {
		}
	}
	
}
