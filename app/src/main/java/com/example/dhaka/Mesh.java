package com.example.dhaka;

import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Mesh {
    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;
    private FloatBuffer normalBuffer;
    private FloatBuffer colorBuffer;
    private int program;
    private int positionHandle;
    private int normalHandle;
    private int colorHandle;
    private int mvpMatrixHandle;
    private int normalMatrixHandle;

    private final int vertexCount;
    private final short indexCount;

    public Mesh(float[] vertices, float[] normals, short[] indices, float[] vertexColor) {
        this.vertexCount = vertices.length / 3;
        this.indexCount = (short) indices.length;

        vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexBuffer.put(vertices).position(0);

        if (normals != null && normals.length > 0) {
            normalBuffer = ByteBuffer.allocateDirect(normals.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            normalBuffer.put(normals).position(0);
        }

        indexBuffer = ByteBuffer.allocateDirect(indices.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();
        indexBuffer.put(indices).position(0);
    }

    public void init(String vertexShaderCode, String fragmentShaderCode) {
        int vertexShader = Shader.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);

        positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        normalHandle = GLES20.glGetAttribLocation(program, "vNormal");
        colorHandle = GLES20.glGetUniformLocation(program, "vColor");
        mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        normalMatrixHandle = GLES20.glGetUniformLocation(program, "uNormalMatrix");
    }

    public void draw(float[] mvpMatrix, float[] normalMatrix, float[] color) {
        GLES20.glUseProgram(program);

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, vertexBuffer);

        if (normalBuffer != null) {
            GLES20.glEnableVertexAttribArray(normalHandle);
            GLES20.glVertexAttribPointer(normalHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, normalBuffer);
            if (normalMatrix != null) {
                GLES20.glUniformMatrix4fv(normalMatrixHandle, 1, false, normalMatrix, 0);
            }
        }

        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);
        if (color != null) {
            GLES20.glUniform4fv(colorHandle, 1, color, 0);
        }
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexCount, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        GLES20.glDisableVertexAttribArray(positionHandle);
        if (normalBuffer != null) {
            GLES20.glDisableVertexAttribArray(normalHandle);
        }
    }
}
