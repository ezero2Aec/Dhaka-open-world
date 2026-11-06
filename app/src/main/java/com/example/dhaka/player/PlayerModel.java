package com.example.dhaka.player;

import com.example.dhaka.Mesh;

public class PlayerModel {

    private final Mesh headMesh;

    private final Mesh torsoMesh;

    private final Mesh[] armMeshes = new Mesh[2];

    private final Mesh[] handMeshes = new Mesh[2];

    private final Mesh[] legMeshes = new Mesh[2];

    private final Mesh[] shoeMeshes = new Mesh[2];


    public PlayerModel() {

        headMesh = createBoxMesh(0.5f,0.5f,0.5f);

        torsoMesh = createBoxMesh(0.35f,0.6f,0.2f);

        armMeshes[0] = createBoxMesh(0.12f,0.45f,0.12f);

        armMeshes[1] = createBoxMesh(0.12f,0.45f,0.12f);

        handMeshes[0] = createBoxMesh(0.05f,0.08f,0.02f);

        handMeshes[1] = createBoxMesh(0.05f,0.08f,0.02f);

        legMeshes[0] = createBoxMesh(0.14f,0.55f,0.14f);

        legMeshes[1] = createBoxMesh(0.14f,0.55f,0.14f);

        shoeMeshes[0] = createBoxMesh(0.1f,0.05f,0.2f);

        shoeMeshes[1] = createBoxMesh(0.1f,0.05f,0.2f);

    }


    public void initMeshes(String vs, String fs) {

        headMesh.init(vs,fs);

        torsoMesh.init(vs,fs);

        for (Mesh m : armMeshes) m.init(vs,fs);

        for (Mesh m : handMeshes) m.init(vs,fs);

        for (Mesh m : legMeshes) m.init(vs,fs);

        for (Mesh m : shoeMeshes) m.init(vs,fs);

    }


    public void draw(float[] vp, float[] normal, float[] skin, float[] shirt, float[] pants, float[] shoe) {

        torsoMesh.draw(vp,normal,shirt);

        headMesh.draw(vp,normal,skin);

        armMeshes[0].draw(vp,normal,shirt);

        armMeshes[1].draw(vp,normal,shirt);

        handMeshes[0].draw(vp,normal,skin);

        handMeshes[1].draw(vp,normal,skin);

        legMeshes[0].draw(vp,normal,pants);

        legMeshes[1].draw(vp,normal,pants);

        shoeMeshes[0].draw(vp,normal,shoe);

        shoeMeshes[1].draw(vp,normal,shoe);

    }


    private Mesh createBoxMesh(float w,float h,float d) {

        float[] verts = {

            -w/2,0,d/2, w/2,0,d/2, w/2,h,d/2, -w/2,h,d/2,

            -w/2,0,-d/2, w/2,0,-d/2, w/2,h,-d/2, -w/2,h,-d/2,

            -w/2,0,-d/2, -w/2,0,d/2, -w/2,h,d/2, -w/2,h,-d/2,

            w/2,0,d/2, w/2,0,-d/2, w/2,h,-d/2, w/2,h,d/2,

            -w/2,h,d/2, -w/2,h,-d/2, w/2,h,-d/2, w/2,h,d/2,

            -w/2,0,-d/2, w/2,0,-d/2, w/2,0,d/2, -w/2,0,d/2

        };

        short[] idx = {0,1,2,0,2,3,4,5,6,4,6,7,8,9,10,8,10,11,12,13,14,12,14,15,16,17,18,16,18,19,20,21,22,20,22,23};

        return new Mesh(verts,null,idx,null);

    }


}
