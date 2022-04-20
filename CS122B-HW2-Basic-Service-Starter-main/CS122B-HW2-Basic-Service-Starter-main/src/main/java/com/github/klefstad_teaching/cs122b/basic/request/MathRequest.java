package com.github.klefstad_teaching.cs122b.basic.request;

public class MathRequest {

    private Integer numX, numY, numZ;

    public Integer getNumX() {
        return numX;
    }

    public MathRequest setNumX(Integer numX) {
        this.numX = numX;
        return this;
    }

    public Integer getNumY() {
        return numY;
    }

    public MathRequest setNumY(Integer numY) {
        this.numY = numY;
        return this;
    }

    public Integer getNumZ() {
        return numZ;
    }

    public MathRequest setNumZ(Integer numZ) {
        this.numZ = numZ;
        return this;
    }

}
