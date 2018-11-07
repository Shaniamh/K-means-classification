package example;

import java.awt.Color;
import java.util.Random;

import javax.swing.JFrame;

import cartesian.coordinate.CCPoint;
import cartesian.coordinate.CCPolygon;
import cartesian.coordinate.CCSystem;
import cartesian.coordinate.CCLine;
import java.awt.BasicStroke;
import java.awt.Paint;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import kmeans.Cluster;
import kmeans.Data;
import kmeans.Point;

public class Main extends JFrame {
    private static final long serialVersionUID = 1L;
    static FileInputStream fstream;
    static DataInputStream in;
    static BufferedReader br;
    static Integer k;
    static ArrayList<Data> listData;
    static Cluster[] listCluster;
    static Point[] listCentroid;
    static Scanner sc;
    
    static Double xMax = 0.0;
    static Double yMax = 0.0;
    static Integer jarak = 1;
    
    Main() throws FileNotFoundException, IOException, InterruptedException {
        super("Viewer");
        setTitle("Viewer");
        
        setVisible(true);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
//        
//        double[] x = new double[]{-50,100,0};
//        double[] y = new double[]{-50,-50,3};
//       CCPolygon ccp = new CCPolygon(x, y);
//        
//        s.add(new CCLine(1.0, 0.0, Color.blue));
//        s.add(new CCLine(-1.0, 10.0, Color.magenta));
//        s.add(new CCLine(1.0, 5.0, Color.red));
//        s.add(new CCLine(-1.0, 5.0, Color.cyan));
//        s.add(new CCLine(-1.0, 15.0, Color.yellow));
//        s.add(new CCLine(1.0, -5.0, Color.green));
//        s.add(new CCLine(1.0, 0.0, 5.0, Color.orange));
//        s.add(new CCLine(0.0, 1.0, 5.0, Color.pink));
//        s.add(ccp);
        
        /*
        Random r = new Random();
        for (int i = 0; i < 300; i++) {
            double a = r.nextDouble();
            double b = r.nextDouble();
            double c = r.nextDouble();
            if (r.nextBoolean()) a = -a;
            if (r.nextBoolean()) b = -b;
            if (r.nextBoolean()) c = -c;
            
            int cint = r.nextInt(13);
            Color color;
            
            switch (cint) {
            case 0: color = Color.black; break;
            case 1: color = Color.blue; break;
            case 2: color = Color.cyan; break;
            case 3: color = Color.darkGray; break;
            case 4: color = Color.gray; break;
            case 5: color = Color.green; break;
            case 6: color = Color.lightGray; break;
            case 7: color = Color.magenta; break;
            case 8: color = Color.orange; break;
            case 9: color = Color.pink; break;
            case 10: color = Color.red; break;
            case 11: color = Color.white; break;
            default: color = Color.yellow;
            }
            
            s.add(new CCLine(a,b,c,color));
        }
        */
         init();
         readTxt();
         setXMaxAndYMax();
         setKValue();
         CCSystem s = new CCSystem(0.0, 0.0, 150.0, 200.0);
         add(s);
         initCluster();
         implementKMeans(s);
    }
    
    private static void initCluster(){
        listCluster = new Cluster[k];
        for(int i=0;i<k;i++){
            listCluster[i] = new Cluster(i);
        }
    }
    
    private static void setXMaxAndYMax(){
        for(int i=0;i<listData.size();i++){
            if(xMax < listData.get(i).getPoint().getX()){
                xMax = listData.get(i).getPoint().getX();
            }
            
            if(yMax < listData.get(i).getPoint().getY()){
                yMax = listData.get(i).getPoint().getY();
            }
        }
    }
    
    private static void init() throws FileNotFoundException{
       fstream = new FileInputStream("ruspini.txt");
       in = new DataInputStream(fstream);  
       br = new BufferedReader(new InputStreamReader(in));
       sc = new Scanner(System.in);
       listData = new ArrayList<>();
    }
    
    private static void readTxt() throws IOException{
        String strLine;
        String dataTemp[];
        int i = 1;
        while((strLine = br.readLine()) != null){
            if(i == 1){
                
            }
            else{
                dataTemp = strLine.split("\t");
                Point point = new Point(Double.parseDouble(dataTemp[0]),Double.parseDouble(dataTemp[1]));
                listData.add(new Data(i,point));
            }
            i++;
        }
    }
    
    private static void setKValue(){
        System.out.println("=================2110161033=================");
        System.out.println("Input nilai K : ");
        k = sc.nextInt();
    }
        
    private static void clearCluster(){
        for(int i=0;i<listCluster.length;i++){
            listCluster[i].getListData().clear();
        }
    }
    
    private static void setView(CCSystem s){
        s.clear();
        for(int i=0;i<k;i++){
            System.out.println(listCentroid[i].getX() + " , " + listCentroid[i].getY());
            s.add(new CCPoint(listCentroid[i].getX(), listCentroid[i].getY(), Color.RED, new BasicStroke(1f)));
        }
        for(int i=0;i<listData.size();i++){
            Data data = listData.get(i);
            Paint paint = Color.BLACK;
            switch(data.getCluster() % 6){
                case 0 : paint = Color.BLUE;
                        break;
                case 1 : paint = Color.DARK_GRAY;
                        break;
                case 2 : paint = Color.GRAY;
                          break;
                case 3 : paint = Color.GREEN;
                        break;
                case 4 : paint = Color.magenta;
                        break;
                case 5 : paint = Color.ORANGE;
            }
            s.add(new CCPoint(data.getPoint().getX(), data.getPoint().getY(), paint, new BasicStroke(1f)));
        }
    }
    
    private static void setNewCentroid(){
        Point[] listNewCentroid = new Point[k];
        boolean flag = true;
        for(int i=0;i<k;i++){
            listNewCentroid[i] = countNewCentroidPosition(listCluster[i]);
        }
        
        for(int i=0; i<k ; i++){
            if(listCentroid[i].getX() != listNewCentroid[i].getX() 
                && listCentroid[i].getY() != listNewCentroid[i].getY()){
                flag = false;
            }
            listCentroid[i] = listNewCentroid[i];
        }
        if(flag){
            jarak = 0;
        }
    }
    
    private static Point countNewCentroidPosition(Cluster cluster){
       Double rata2x = 0.0;
       Double rata2y = 0.0;
       Point point;
       for(int i=0 ; i< cluster.getListData().size() ; i++){
           rata2x += cluster.getData(i).getPoint().getX();
           rata2y += cluster.getData(i).getPoint().getY();
       }
       if(cluster.getListData().size() == 0){
            point = new Point(-50.0, -50.0);
       }
       else{
           rata2x = rata2x/cluster.getListData().size();
           rata2y = rata2y/cluster.getListData().size();
           point = new Point(rata2x, rata2y);
       }
       return point;
    }
    

        
    private static void setClustertoListCluster(Data data){
        for(int i=0 ; i<listCluster.length ; i++){
            if(data.getCluster() == listCluster[i].getCluster()){
                listCluster[i].addData(data);
            }
        }
    }
    
    private static void countJarak(Data dataTes, Double[] allJarak){
        for(int i=0 ; i<k ; i++){
            allJarak[i] = Math.sqrt(Math.pow((listCentroid[i].getX() - dataTes.getPoint().getX()), 2) 
                                    + Math.pow((listCentroid[i].getY() - dataTes.getPoint().getY()), 2));
        }
    }
    
    private static Integer getClusterMinimum(Double[] allJarak){
        Double min = Double.MAX_VALUE;
        Integer cluster = 0;
        for(int i=0 ; i < allJarak.length; i++){
            System.out.println(allJarak[i]);
            if(min > allJarak[i]){
                min = allJarak[i];
                cluster = i;
            }
        }
        System.out.println(cluster);
        System.out.println("============");
        return cluster;
    }
    
    private static void setCluster(){
        for(int i=0 ; i<listCluster.length ; i++){
            listCluster[i].setCluster(i);
        }
        for(int i=0 ; i<listData.size() ; i++){
            Double allJarak[] = new Double[k];
            countJarak(listData.get(i), allJarak);
            Integer clusterJarakTerdekat = getClusterMinimum(allJarak);
            listData.get(i).setCluster(clusterJarakTerdekat);
            setClustertoListCluster(listData.get(i));
        }
    }
    
    private static void createRandomCentroid(){
        listCentroid = new Point[k];
        for(int i=1; i<=k ; i++){
            listCentroid[i-1] = new Point(i*20.0,i*30.0);
        }
        Random rand = new Random();
        for(int i=0 ; i<listCentroid.length ; i++){
            Integer xTemp = rand.nextInt(xMax.intValue());
            Integer yTemp = rand.nextInt(yMax.intValue());
            if(xTemp < 0){
                xTemp *= -1;
            }
            if(yTemp < 0){
                yTemp *= -1;
            }
            Double x = xTemp.doubleValue();
            Double y = yTemp.doubleValue();
            listCentroid[i].setX(x);
            listCentroid[i].setY(y);
        }
        
    }
   private static void implementKMeans(CCSystem s) throws InterruptedException{
        createRandomCentroid();
        while(jarak == 1){
            setCluster();
            setView(s);
            Thread.sleep(2000);
            setNewCentroid();
            clearCluster();
        }
    }
    
    public static void main(String[] args) throws IOException, FileNotFoundException, InterruptedException {
        new Main();
    }
}
