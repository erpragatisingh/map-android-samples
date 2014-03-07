package jp.co.yahoo.map.codezine_sample;

import android.opengl.GLES20;

public class SPShader {

	public static byte ID_MAX = 0;
	public static final byte ID_VaPos= ID_MAX++;
	public static final byte ID_VaTex= ID_MAX++;
	public static final byte ID_VuProjMatrix= ID_MAX++;
	public static final byte ID_VuViewMatrix= ID_MAX++;
	public static final byte ID_VuColor= ID_MAX++;
	public static final byte ID_VuColor2= ID_MAX++;
	protected int[] mShaderParameter = null;

	/**
	 * 頂点シェーダプログラム
	 */
	private final String VSHADER_SOURCE =
			"precision highp float;" +

			"uniform mat4 u_ViewMatrix;\n" +
			"uniform mat4 u_ProjMatrix;\n" +

			"attribute vec4 a_Position;\n" +
			"attribute vec2 a_TexCoord;\n" +

			"uniform vec4 u_Color;\n" +

			"varying vec4 v_Color;\n" +
			"varying vec2 v_TexCoord;\n" +

			"void main() {\n" +
			"  gl_Position = u_ProjMatrix * u_ViewMatrix * a_Position;\n" +
			"  v_Color = u_Color;\n"+
			"  v_TexCoord = a_TexCoord;\n"+
			"}\n";
	
	/**
	 * フラグメントシェーダプログラム
	 */
	private final String FSHADER_SOURCE =
			"precision mediump float;" +
			"varying vec4 v_Color;" +
			"varying vec2 v_TexCoord;" +
			"uniform vec4 u_Color2;" +

			"void main() {\n" +
			"  vec4 pt = u_Color2;" +
			"  float texfH = fract(v_TexCoord.y);"+
			"  gl_FragColor = v_Color;" +

			"  float x = 0.0;"+
			"  float y = 0.0;"+
			"  float z = 0.0;"+

			"  float ptlen = pt.g + pt.b;" +
			"  z = pt.g / ptlen;"+
			"  x = clamp(texfH, y, z);"+
			"  if(x == texfH) return;"+

			"  gl_FragColor.a = 0.0;"+

			"}\n";

	/**
	 * コンストラクタ
	 */
	public SPShader() {
		mShaderParameter = new int[ID_MAX];
	}

	/**
	 * シェーダープログラムを読み込みコンパイルします。
	 * @return
	 */
	public int initShaders() {
		int program = createProgram();
		GLES20.glUseProgram(program);
		return program;
	}
	
	/**
	 * プログラムオブジェクトを作成します。
	 * @return
	 */
	public int createProgram() {
		// シェーダオブジェクトを作成する
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, this.VSHADER_SOURCE);
		int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, this.FSHADER_SOURCE);
		
		// プログラムオブジェクトを作成する
		int program = GLES20.glCreateProgram();
		if (program == 0) {
			throw new RuntimeException("failed to create program");
		}
		
		// シェーダオブジェクトを設定する
		GLES20.glAttachShader(program, vertexShader);
		GLES20.glAttachShader(program, fragmentShader);

		// プログラムオブジェクトをリンクする
		GLES20.glLinkProgram(program);

		// リンク結果をチェックする
		int[] linked = new int[1];
		GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linked, 0);
		if (linked[0] != GLES20.GL_TRUE) {
			String error = GLES20.glGetProgramInfoLog(program);
			throw new RuntimeException("failed to link program: " + error);
		}

		// 値のパイプを設定
		setShaderParamaterId(ID_VaPos, GLES20.glGetAttribLocation(program, "a_Position"));
		setShaderParamaterId(ID_VaTex, GLES20.glGetAttribLocation(program, "a_TexCoord"));
		setShaderParamaterId(ID_VuProjMatrix, GLES20.glGetUniformLocation(program, "u_ProjMatrix"));
		setShaderParamaterId(ID_VuViewMatrix, GLES20.glGetUniformLocation(program, "u_ViewMatrix"));
		setShaderParamaterId(ID_VuColor, GLES20.glGetUniformLocation(program, "u_Color"));
		setShaderParamaterId(ID_VuColor2, GLES20.glGetUniformLocation(program, "u_Color2"));

		return program;
	}
	
	/**
	 * シェーダーを読み込みます。
	 * @param type
	 * @param source
	 * @return
	 */
	public int loadShader(int type, String source) {
		// シェーダオブジェクトを作成する
		int shader = GLES20.glCreateShader(type);
		if (shader == 0) {
			throw new RuntimeException("unable to create shader");
		}
		// シェーダのプログラムを設定する
		GLES20.glShaderSource(shader, source);
		// シェーダをコンパイルする
		GLES20.glCompileShader(shader);
		// コンパイル結果を検査する
		int[] compiled = new int[1];
		GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
		if (compiled[0] != GLES20.GL_TRUE) {
			String error = GLES20.glGetShaderInfoLog(shader);
			throw new RuntimeException("failed to compile shader: " + error);
		}
		return shader;
	}

	/**
	 * パイプ配列を設定する
	 * @param id
	 * @param value
	 */
	public void setShaderParamaterId(byte id, int value) {
		mShaderParameter[id] = value;
	}

	/**
	 * 指定のパイプを返す
	 * @param id
	 * @return
	 */
	public int getShaderParameterId(byte id) {
		return mShaderParameter[id];
	}
}
