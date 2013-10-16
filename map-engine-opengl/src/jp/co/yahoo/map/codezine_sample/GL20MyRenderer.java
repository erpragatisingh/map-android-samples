package jp.co.yahoo.map.codezine_sample;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import jp.co.yahoo.map.codezine_sample.GL20SurfaceView;
import jp.co.yahoo.map.codezine_sample.MapView;
import jp.co.yahoo.map.codezine_sample.Shader;


import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

public class GL20MyRenderer implements GLSurfaceView.Renderer{

	private MapView mMapView;	//MapView
	private Context mContext;	//Context
	private GL20SurfaceView mParentView;	//�e��SurfaceView
	private Shader mShader;		//��`����Shader
	private float[] mProjMatrix = new float[16];	//���e�ϊ��p�s��
	private int muProjMatrix;	//���e�ϊ��p�s��̃n���h��
	private float[] mViewMatrix = new float[16];	//���_�ϊ��p�s��
	private int muViewMatrix;	//���_�ϊ��p�s��̃n���h��
	private float[] mCoordList;	//���_�f�[�^
	private int mVertexId;	//���_�f�[�^��VRAM�̈ʒu
	private int mNumVertices;	//���_��
	private float mEyeX = 140000.0f, mEyeY = 50000.0f, mEyeZ = 10000000.0f; //���_�̍��W
	private int mAPosition;	//�ʒu�n���h��

	private final int FSIZE = Float.SIZE / Byte.SIZE;	//float�̃o�C�g�T�C�Y

	//�R���X�g���N�^
	public GL20MyRenderer(Context context, GL20SurfaceView parent , MapView mapView){
		mContext = context;
		mParentView = parent;
		mMapView = mapView;
		mShader = new Shader();
		mCoordList = mMapView.getCoordinateList();
	}

	@Override
	public void onDrawFrame(GL10 arg0) {
		//�w�i�F��ݒ肵�܂��B
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		//������ݒ肵�܂�
		GLES20.glLineWidth(2.0f);

		//���_�o�b�t�@��ݒ肵�܂�
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVertexId);
		GLES20.glVertexAttribPointer(mAPosition, 3, GLES20.GL_FLOAT, false, FSIZE*3, 0);

		//�`��
		GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, mNumVertices);	
	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height) {
		int size = (width <= height) ? width : height;

		//�r���[�|�[�g�̐ݒ肵�܂��B
		GLES20.glViewport((width - size) / 2, (height - size) / 2, size, size);

		//�r���[�{�����[����ݒ肵�܂��B
		Matrix.orthoM(mProjMatrix, 0, -1000000.0f, 1000000.0f, -1000000.0f, 1000000.0f, 0.0f, 10000000.0f);
		GLES20.glUniformMatrix4fv(muProjMatrix, 1, false, mProjMatrix, 0);
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
		//�ʒu�n���h���ia_Position�j�̎擾�ƗL�����B
		int program = mShader.initShaders();
		mAPosition = GLES20.glGetAttribLocation(program, "a_Position");
		if (mAPosition == -1) {
			throw new RuntimeException("a_Position�̊i�[�ꏊ�̎擾�Ɏ��s");
		}
		GLES20.glEnableVertexAttribArray(mAPosition);
		mNumVertices = initVertexBuffers(); 

		//�r���[�{�����[���Ǝ��_�̊i�[�ꏊ���擾���A��o�^���܂��B
		muViewMatrix = GLES20.glGetUniformLocation(program, "u_ViewMatrix");
		muProjMatrix = GLES20.glGetUniformLocation(program, "u_ProjMatrix");
		if (muViewMatrix == -1 || muProjMatrix == -1) {
			throw new RuntimeException("a_Position�̊i�[�ꏊ�̎擾�Ɏ��s");
		}

		//���_�i�J�����j�̈ʒu��ݒ肵�܂��B
		Matrix.setLookAtM(mViewMatrix, 0, mEyeX, mEyeY, mEyeZ, mEyeX, mEyeY, 0.0f, 0.0f, 1.0f, 0.0f);
	  	GLES20.glUniformMatrix4fv(muViewMatrix, 1, false, mViewMatrix, 0);
	}

	//�`��p�̒��_�f�[�^���쐬���܂��B
	@SuppressLint("NewApi")
	public int initVertexBuffers() {
		//FloatBuffer�I�u�W�F�N�g���쐬���܂��B
		FloatBuffer vertices = makeFloatBuffer(mCoordList);
		int vertex_num = mCoordList.length / 3;			//���_��	

		//VRAM�Ƀf�[�^��ݒ肵�܂��B
		int[] vertexId = new int[1];
		GLES20.glGenBuffers(1, vertexId, 0);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexId[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, FSIZE * vertices.limit(), vertices, GLES20.GL_STATIC_DRAW);
		mVertexId = vertexId[0];

		return vertex_num;
	}

	//�t���[�g�z�񂩂�t���[�g�o�b�t�@���쐬���܂��B  
	public FloatBuffer makeFloatBuffer(float[] array) {
		if (array == null) throw new IllegalArgumentException();
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * array.length);
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		floatBuffer.put(array);
		floatBuffer.position(0);
		return floatBuffer;
	}
}
