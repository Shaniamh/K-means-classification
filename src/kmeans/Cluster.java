/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kmeans;

import java.util.ArrayList;

/**
 *
 * @author ASUS
 */
public class Cluster {
    private Integer cluster;
    private ArrayList<Data> listData;
    private Point point;
    
    public Cluster(Integer cluster){
        this.cluster = cluster;
        this.listData = new ArrayList<>();
        this.point = new Point(0.0,0.0);
    }

    public Integer getCluster() {
        return cluster;
    }

    public void setCluster(Integer cluster) {
        this.cluster = cluster;
    }

    public ArrayList<Data> getListData() {
        return listData;
    }

    public void setListData(ArrayList<Data> listData) {
        this.listData = listData;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
    
    public void addData(Data data){
        this.listData.add(data);
    }
    
    public void removeData(Data data){
        this.listData.remove(data);
    }
    
    public void removeData(int index){
        this.listData.remove(index);
    }
    
    public Data getData(int index){
        return this.listData.get(index);
    }
}
