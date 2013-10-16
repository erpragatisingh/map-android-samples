package jp.co.yahoo.map.codezine_sample;

import android.opengl.GLES20;

public class Shader {
	//���_�V�F�[�_�v���O����
	private final String VSHADER_SOURCE =
			"precision highp float;" +
			"attribute vec4 a_Position;\n" +
			"uniform mat4 u_ViewMatrix;\n" +
			"uniform mat4 u_ProjMatrix;\n" +
			"varying vec4 v_Color;\n" +
			"void main() {\n" +
			"  gl_Position = u_ProjMatrix * u_ViewMatrix * a_Position;\n" +
			"  gl_PointSize = 16.0;" +
			"}\n";

	//�t���O�����g�V�F�[�_�v���O����
	private final String FSHADER_SOURCE =
			"precision highp float;" +
			"void main() {\n" +
			"  gl_FragColor = vec4(1.0,1.0,1.0,1.0);\n" +
			"}\n";

	//�V�F�[�_�[�v���O������ǂݍ��݃R���p�C�����܂��B
	public int initShaders() {
		int program = createProgram();
		GLES20.glUseProgram(program);
		return program;
	}

	//�v���O�����I�u�W�F�N�g���쐬���܂��B
	public int createProgram() {
		// �V�F�[�_�I�u�W�F�N�g���쐬����
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, this.VSHADER_SOURCE);
		int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, this.FSHADER_SOURCE);

		// �v���O�����I�u�W�F�N�g���쐬����
		int program = GLES20.glCreateProgram();
		if (program == 0) {
			throw new RuntimeException("failed to create program");
		}

		// �V�F�[�_�I�u�W�F�N�g��ݒ肷��
		GLES20.glAttachShader(program, vertexShader);
		GLES20.glAttachShader(program, fragmentShader);

		// �v���O�����I�u�W�F�N�g�������N����
		GLES20.glLinkProgram(program);

		// �����N���ʂ��`�F�b�N����
		int[] linked = new int[1];
		GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linked, 0);
		if (linked[0] != GLES20.GL_TRUE) {
			String error = GLES20.glGetProgramInfoLog(program);
			throw new RuntimeException("failed to link program: " + error);
		}
		return program;
	}

	//�V�F�[�_�[��ǂݍ��݂܂��B
	public int loadShader(int type, String source) {
		// �V�F�[�_�I�u�W�F�N�g���쐬����
		int shader = GLES20.glCreateShader(type);
		if (shader == 0) {
			throw new RuntimeException("unable to create shader");
		}
		// �V�F�[�_�̃v���O������ݒ肷��
		GLES20.glShaderSource(shader, source);
		// �V�F�[�_���R���p�C������
		GLES20.glCompileShader(shader);
		// �R���p�C�����ʂ���������
		int[] compiled = new int[1];
		GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
		if (compiled[0] != GLES20.GL_TRUE) {
			String error = GLES20.glGetShaderInfoLog(shader);
			throw new RuntimeException("failed to compile shader: " + error);
		}
		return shader;
	}

}
