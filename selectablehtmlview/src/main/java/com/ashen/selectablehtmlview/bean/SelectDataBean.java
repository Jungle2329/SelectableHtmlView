package com.ashen.selectablehtmlview.bean;

import java.io.Serializable;

/**
 * Created by Jungle on 2019/6/21 0021.
 *
 * @author JungleZhang
 * @version 1.0.0
 * @Description 选中的数据
 */
public class SelectDataBean implements Serializable {

    private int startPoint;
    private int endPoint;
    private String linkExplain;

    public int getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(int startPoint) {
        this.startPoint = startPoint;
    }

    public int getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(int endPoint) {
        this.endPoint = endPoint;
    }

    public String getLinkExplain() {
        return linkExplain;
    }

    public void setLinkExplain(String linkExplain) {
        this.linkExplain = linkExplain;
    }

    @Override
    public String toString() {
        return "SelectDataBean{" +
                "startPoint=" + startPoint +
                ", endPoint=" + endPoint +
                ", linkExplain='" + linkExplain + '\'' +
                '}';
    }
}
