package com.example.dhaka;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends Activity {
    private MyGLSurfaceView glView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        glView = new MyGLSurfaceView(this);
        setContentView(glView);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (glView != null) {
            glView.onPause();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (glView != null) {
            glView.onResume();
        }
    }
}
