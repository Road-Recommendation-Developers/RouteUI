package com.Route.project.modules.map.databean;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class MapRunnable implements Runnable {
    private GeoPoint start;
    private GeoPoint destination;
    private RoadManager roadManager;
    private MapView mapView;
    private Drawable marker;
    private Handler handler;
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    private Object lock=new Object();
    @Override
    public void run() {

        //设置起始点和中心
        //GeoPoint startPoint = new GeoPoint(27.914492710098862,112.90664106476612);
        IMapController mapController = mapView.getController();
        //设置线路的起始点位置
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();

        //27.905831143677858,112.91826347971147
        //waypoints.add(new GeoPoint(27.905831143677858,112.91826347971147)); //end point

        synchronized (lock){
            mapController.setCenter(start);
            waypoints.add(start);
            waypoints.add(destination);
        }
        //获取相关的线路对象
        Road road = roadManager.getRoad(waypoints);
        //创建线路图层
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road, 0xFF000000,3.0f);
        //添加线路图层
        mapView.getOverlays().add(roadOverlay);
        //设置线路的上的标志

        for (int i=0; i<road.mNodes.size(); i++){
            RoadNode node = road.mNodes.get(i);
            Marker nodeMarker =  new  Marker(mapView);
            nodeMarker.setPosition(node.mLocation);
            nodeMarker.setTitle("Step "+i);
            nodeMarker.setIcon(marker);
            if(i==0){
                //nodeMarker.setTextIcon("Start");
                nodeMarker.setSnippet("Start");
            }
            if(i==road.mNodes.size()-1){
                nodeMarker.setTextIcon("Destination");
            }
            mapView.getOverlays().add(nodeMarker);
        }
        Message msg = new Message();
        msg.what = 1;
        handler.sendMessage(msg);
        //mapView.invalidate();
        /*
        if(count==1){
            handler.obtainMessage(1).sendToTarget();
        }else if(count==2){
            handler.obtainMessage(2).sendToTarget();
        }

         */
    }

    public void setParam(GeoPoint start,GeoPoint destination,RoadManager roadManager,MapView mapView,Drawable marker,Handler handler){
        this.start=start;
        this.destination=destination;
        this.roadManager=roadManager;
        this.mapView=mapView;
        this.marker=marker;
        this.handler=handler;
   }
}
