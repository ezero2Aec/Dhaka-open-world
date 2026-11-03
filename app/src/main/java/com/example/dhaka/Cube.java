package com.example.dhaka;

import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Cube {
    private final float[] vertices = {
        // Front face
        -1, -1,  1,
         1, -1,  1,
         1,  1,  1,
        -1,  1,  1,
        // Back face
        -1, -1, -1,
        -1,  1, -1,
         1,  1, -1,
         1, -1, -1,
        // Left face
        -1, -1, -1,
        -1, -1,  1,
        -1,  1,  1,
        -1,  1, -1,
        // Right face
         1, -1, -1,
         1,  1, -1,
         1,  1,  1,
         1, -1,  1,
        // Top face
        -1,  1, -1,
        -1,  1,  1,
         1,  1,  1,
         1,  1, -1,
        // Bottom face
        -1, -1, -1,
         1, -1, -1,
         1, -1,  1,
        -1, -1,  1,
    };

    private final short[] indices = {
        0, 1, 2, 0, 2, 3,       // front
        4, 5, 6, 4, 6, 7,       // back
        8, 9,10, 8,10,11,       // left
       12,13,14,12,14,15,       // right
       16,17,18,16,18,19,       // top
       20,21,22,20,22,23        // bottom
    };

    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;
    private int program;
    private int positionHandle;
    private int mvpMatrixHandle;
    private int colorHandle;

    public void init() {
        // Allocate buffers
        vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexBuffer.put(vertices).position(0);

        indexBuffer = ByteBuffer.allocateDirect(indices.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();
        indexBuffer.put(indices).position(0);

        String vertexShaderCode =
                "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = uMVPMatrix * vPosition;" +
                "}";
        String fragmentShaderCode =
                "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}";

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(program);
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false,
                3 * 4, vertexBuffer);

        mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        colorHandle = GLES20.glGetUniformLocation(program, "vColor");
        GLES20.glUniform4f(colorHandle, 0.6f, 0.8f, 0.2f, 1.0f);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length,
                GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
