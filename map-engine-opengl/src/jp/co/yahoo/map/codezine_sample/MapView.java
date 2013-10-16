package jp.co.yahoo.map.codezine_sample;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import android.content.Context;
import android.widget.FrameLayout;

public class MapView extends FrameLayout{
	
	private GL20SurfaceView mSurfaceView = null;	//�`��p�r���[
	private Context mContext = null;	//Context
	private float[] mCoordList;		//Float�`���̍��W���i�[����z��
	
	//�R���X�g���N�^
	public MapView(Context context,InputStream is) throws IOException {
		super(context);

		//�n�}�f�[�^��ǂݍ���
		mCoordList = loatData(is);

		//Context��ݒ�
		mContext = context;

		//�`��̂��߂̃r���[��������
		mSurfaceView = new GL20SurfaceView(mContext, this);

		//SurfaceView��ǉ�����
		this.addView(mSurfaceView);
	}

	//�n�}�f�[�^��ݒ肵�܂��B
	private float[] loatData(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String str;
		ArrayList<String> list = new ArrayList<String>(); //�s���Ƃ̃��X�g
		//�t�@�C������P�s���Ƃɓǂݍ���
		while ((str = br.readLine()) != null) {
			list.add(str);
		}
		br.close();
		if(!(list.size()>0)) return null;

		//�ǂݍ��񂾃t�@�C����Float�z��֕ϊ�����
		float[] res = new float[list.size() * 3];  //x,y,z�̏��Ɋi�[���ꂽfloat�z��
		for(int i=0; i<list.size(); i++){
			str = list.get(i);
			String[] str_coord = str.split("\t");
			res[i * 3 + 0] = Float.valueOf(str_coord[0]);	//X���W
			res[i * 3 + 1] = Float.valueOf(str_coord[1]);	//Y���W
			res[i * 3 + 2] = 0.0f;							//Z���W
		}
		list.clear();

		return res;
	}

	//�n�}�f�[�^��Ԃ��܂��B
	public float[] getCoordinateList() {
		return mCoordList;
	}

	//�n�}�f�[�^�̏��������܂��B
	public void release() {
		mCoordList = null;
	}

}
