package com.dhlu;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.widget.TextView;

import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public void GetCodeType(){
        //check code type.
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {

                MediaCodecList mc = new MediaCodecList(MediaCodecList.ALL_CODECS);
                MediaCodecInfo[] codecInfo = mc.getCodecInfos();
                int num = codecInfo.length;
                for (int i = 0; i < num; i++) {
                    String[] types = codecInfo[i].getSupportedTypes();
                    for (String type : types) {
                        Log.e("check:", "deocdename:"+codecInfo[i].getName() + " decodetype:" + type);
                    }
                }
                /*
                int numCodecs = MediaCodecList.getCodecCount();
                for (int i = 0; i < numCodecs; i++) {
                    MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
                    String[] types = codecInfo.getSupportedTypes();
                    for (String type : types) {
                        Log.e("MediaCodecList, name:", codecInfo.getName() + ",type:" + type);
                    }
                }*/
            }
        }catch (Exception e){
            String str = e.toString();
        }
    };
    private void showSupportedColorFormat(MediaCodecInfo.CodecCapabilities caps) {
        System.out.print("supported color format: ");
        for (int c : caps.colorFormats) {
            System.out.print(c + "\t");
        }
        System.out.println();
    }
    public void GetMediacodeColor(){

        int numCodecs = MediaCodecList.getCodecCount();
        for (int i = 0; i < numCodecs; i++) {
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            if (codecInfo.isEncoder()) {
                continue;
            }
            for (String type : codecInfo.getSupportedTypes()) {
                if (type.equalsIgnoreCase(MediaFormat.MIMETYPE_VIDEO_AVC)) {
                    MediaCodecInfo.CodecCapabilities cap = codecInfo.getCapabilitiesForType(MediaFormat.MIMETYPE_VIDEO_AVC);
                    MediaCodecInfo.VideoCapabilities vcap = cap.getVideoCapabilities();
                    Size supportedSize = new Size(vcap.getSupportedWidths().getUpper(), vcap.getSupportedHeights().getUpper());
                    Log.d("check:", "AVC decoder=\"" + codecInfo.getName() + "\""
                            + " supported-size=" + supportedSize
                            + " color-formats=" + Arrays.toString(cap.colorFormats)
                    );

                }
            }
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        //GetCodeType();
        GetMediacodeColor();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


}
