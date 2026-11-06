package com.example.dhaka;

import android.opengl.GLES20;

public class Shader {
    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public static final String DEFAULT_VERTEX_SHADER =
        "uniform mat4 uMVPMatrix;" +
        "uniform vec4 vColor;" +
        "attribute vec4 vPosition;" +
        "void main() {" +
        "  gl_Position = uMVPMatrix * vPosition;" +
        "}";

    public static final String DEFAULT_FRAGMENT_SHADER =
        "precision mediump float;" +
        "varying vec4 vColor;" +
        "void main() {" +
        "  gl_FragColor = vColor;" +
        "}";
}
