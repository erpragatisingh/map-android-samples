package jp.co.yahoo.map.codezine_sample;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class GL20SurfaceView extends GLSurfaceView{

	private GL20MyRenderer mRenderer = null;	//�`�揈���{��
	private Context mContext;	//Context
	private MapView mMapView = null;	//MapView
	
	//�R���X�g���N�^
	public GL20SurfaceView(Context context, MapView mapView) {
		super(context.getApplicationContext());
		mContext = context.getApplicationContext();
		mMapView = mapView;
		//OpenGLES2.0�̎g�p��錾���܂��B
		setEGLContextClientVersion(2);
		//�����_���[�����������A�r���[�ɃZ�b�g���܂��B
		mRenderer = new GL20MyRenderer(mContext,this, mMapView);
		setRenderer(mRenderer);
	}

}
