package com.Route.project.modules.map.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

import android.net.wifi.aware.DiscoverySession;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.Route.project.modules.map.databean.GoogleTileSource;
import com.Route.project.modules.map.databean.MapData;
import com.Route.project.modules.map.databean.PermissionUtils;
import com.Route.project.modules.todolist.UI.TodoList;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.*;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.*;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.SimpleLocationOverlay;
import com.xuexiang.templateproject.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class BaseMap extends AppCompatActivity  implements PermissionUtils.PermissionCallbacks {
    private LocationManager mLocationManager;
    private String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    List<MapData> mapDataList = new ArrayList<>();
    List<GeoPoint> midList = new ArrayList<>();
    MapData mapData = new MapData();
    private static final int REQUEST_PERMISSION_CODE = 12;
    Polyline line = new Polyline();
    GeoPoint startPoint = new GeoPoint(27.914492710098862,112.90664106476612);
    GeoPoint midPoint = new GeoPoint(27.905831143677858,112.91826347971147);
    //North Gate
    GeoPoint endPoint = new GeoPoint(27.907426747500864,112.9113408598435);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_basemap);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());
        InitialMap();

        InitialMapPressEvent();
        StartLocationManage();
        //startLocate();
        //ShowItemsClicked();
        InitialThePolyline();
        DrawMultiRoute();
        Intent intent = getIntent();
        double destinationLat = intent.getDoubleExtra("destinationLat",0.0);
        double destinationLon = intent.getDoubleExtra("destinationLon",0.0);
        GeoPoint location=new GeoPoint(mapData.latitude,mapData.longitude);
        if (location.getLatitude()==0.0&&location.getLongitude()==0.0){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    /**
                     *??????????????????
                     */
                    Toast.makeText(getBaseContext(),"?????????!",Toast.LENGTH_SHORT).show();

                }
            }, 1000);//3????????????Runnable??????run??????
        }

        if(destinationLat!=0.0&&destinationLon!=0.0){
            if (location.getLatitude()==0.0&&location.getLongitude()==0.0){
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         *??????????????????
                         */
                        GeoPoint location=new GeoPoint(mapData.latitude,mapData.longitude);
                        if (location.getLatitude()!=0.0&&location.getLongitude()!=0.0){
                            GeoPoint destination=new GeoPoint(destinationLat,destinationLon);
                            DrawTheRecommendRoute(location,destination);
                        }

                    }
                }, 1000);//3????????????Runnable??????run??????
            }

        }
        /*
        @SuppressLint("HandlerLeak")
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                map.invalidate();
            }
        };

         */
/*
        Runnable runnable=new Runnable() {
            private int count=0;
            private Object lock=new Object();
            @Override
            public void run() {
                MapView map = findViewById(R.id.map);
                RoadManager roadManager = new OSRMRoadManager(getBaseContext());
                //????????????????????????
                //GeoPoint startPoint = new GeoPoint(27.914492710098862,112.90664106476612);
                IMapController mapController = map.getController();
                //??????????????????????????????
                ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();

                //27.905831143677858,112.91826347971147
                //waypoints.add(new GeoPoint(27.905831143677858,112.91826347971147)); //end point

                synchronized (lock){
                    count++;
                    if(count==1){
                        mapController.setCenter(startPoint);
                        waypoints.add(startPoint);
                        waypoints.add(midPoint);
                    }else if(count==2){
                        mapController.setCenter(midPoint);
                        waypoints.add(midPoint);
                        waypoints.add(endPoint);
                    }
                }
                //???????????????????????????
                Road road = roadManager.getRoad(waypoints);
                //??????????????????
                Polyline roadOverlay = RoadManager.buildRoadOverlay(road, 0xFF000000,3.0f);
                //??????????????????
                map.getOverlays().add(roadOverlay);
                //???????????????????????????
                //final ArrayList<ExtendedOverlayItem> roadItems =    new ArrayList<ExtendedOverlayItem>();
                Drawable marker = getResources().getDrawable(R.drawable.ic_map_location);
                for (int i=0; i<road.mNodes.size(); i++){
                    RoadNode node = road.mNodes.get(i);
                    Marker nodeMarker =  new  Marker(map);
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
                    map.getOverlays().add(nodeMarker);
                }
                if(count==1){
                    handler.obtainMessage(1).sendToTarget();
                }else if(count==2){
                    handler.obtainMessage(2).sendToTarget();
                }
            }

        };


        Thread thread1=new Thread(runnable,"First");
        thread1.setPriority(10);
        //thread1.start();

        Thread thread2=new Thread(runnable,"Second");
        thread2.setPriority(1);
        //thread2.start();
*/
        // MapView map = findViewById(R.id.map);
        /*
        RoadManager roadManager = new OSRMRoadManager(getBaseContext());
        Drawable marker = getResources().getDrawable(R.drawable.ic_map_location);
        MapRunnable mapRunnable1=new MapRunnable();
        mapRunnable1.setParam(startPoint,midPoint,roadManager,map,marker);
        Thread thread3=new Thread(mapRunnable1,"Third");
        thread3.setPriority(10);
        thread3.start();
        //map.invalidate();

        MapRunnable mapRunnable2=new MapRunnable();
        mapRunnable2.setParam(midPoint,endPoint,roadManager,map,marker);
        Thread thread4=new Thread(mapRunnable2,"Fourth");
        thread4.setPriority(1);
        thread4.start();

         */

    }

    private void InitialMap(){
        MapView map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        //map.setTileSource(GoogleTileSource.MAPNIK);
        //??????????????????
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(true);
        //????????????min&max????????????????????????
        map.setMaxZoomLevel((double) 24);
        map.setMinZoomLevel((double) 10);
        IMapController mapController = map.getController();
        mapController.setZoom(18.5);

        //support for map rotation
        RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(map);
        //true???????????????false???????????????
        mRotationGestureOverlay.setEnabled(true);
        map.getOverlays().add(mRotationGestureOverlay);

        //Mini map
        MinimapOverlay mMinimapOverlay = new MinimapOverlay(getBaseContext(), map.getTileRequestCompleteHandler());
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //mMinimapOverlay.setVerticalWrapEnabled(true);
        mMinimapOverlay.setZoomDifference(4);
        mMinimapOverlay.setWidth(dm.widthPixels / 5);
        mMinimapOverlay.setHeight(dm.heightPixels / 5);

        map.getOverlays().add(mMinimapOverlay);

        //map scale
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setMaxLength((float) 0.8);
        mScaleBarOverlay.setAlignBottom(true);
        //mScaleBarOverlay.setAlignRight(true);
        //mScaleBarOverlay.setEnableAdjustLength(true);
        //??????????????????????????????
        //mScaleBarOverlay.setScaleBarOffset(0, 100);
        map.getOverlays().add(mScaleBarOverlay);


        //27.912553,112.911016
        //GeoPoint startPoint_init = new GeoPoint(27.902552,112.92361);
        //mapController.setCenter(startPoint_init);
        //GeoPoint ensPoint_init = new GeoPoint(27.902552,112.92361);

        //map.getController().animateTo(ensPoint_init);
        CompassOverlay mCompassOverlay = new CompassOverlay(getBaseContext(), new InternalCompassOrientationProvider(getBaseContext()), map);
        map.getOverlays().add(mCompassOverlay);
        mCompassOverlay.enableCompass();

    }

    private void InitialMapPressEvent(){
        MapView map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        final MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                Toast.makeText(getBaseContext(),"??????????????????????????????????????????!",Toast.LENGTH_SHORT).show();
                MapView map = findViewById(R.id.map);
                map.invalidate();
                return false;
            }

            @Override
            public boolean longPressHelper(final GeoPoint p) {

                //Toast.makeText(getBaseContext(),"????????????!",Toast.LENGTH_SHORT).show();
                if(mapDataList.size()>1){
                    //line.addPoint(p);
                    GeoPoint location=new GeoPoint(mapData.latitude,mapData.longitude);
                    midList.add(location);
                    midList.add(p);
                    if(midList.size()==2){
                        DrawTheRecommendRoute(location,p);
                    }else {
                        DrawTheRecommendRoute(midList.get(midList.size()-3),midList.get(midList.size()-1));
                    }

                }
                //DrawMultiRoute();
                System.out.println("The location is:"+p);
                /*
                Toast.makeText(getBaseContext(),"????????????!",Toast.LENGTH_SHORT).show();
                final MapView map = findViewById(R.id.map);

                @SuppressLint("HandlerLeak")
                Handler mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 1) {
                            map.invalidate();
                        }
                    }
                };
                //final MapView map = findViewById(R.id.map);
                RoadManager roadManager = new OSRMRoadManager(getBaseContext());
                Drawable marker = getResources().getDrawable(R.drawable.ic_map_location);
                GeoPoint location=new GeoPoint(mapData.latitude,mapData.longitude);
                MapRunnable mapRunnable=new MapRunnable();

                mapRunnable.setParam(location,p,roadManager,map,marker,mHandler);
                if(mapDataList.size()>1){
                    Thread thread=new Thread(mapRunnable);
                    thread.start();

                }

                 */
                return true;
            }
        };
        map.getOverlays().add(new MapEventsOverlay(mReceive));
    }

    private void StartLocationManage(){
        MapView map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        doClick(map);
    }

    private void ShowItemsClicked(){
        MapView map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        final ArrayList<OverlayItem> items = new ArrayList<>();
        items.add(new OverlayItem("Hannover", "SampleDescription", new GeoPoint(52.370816, 9.735936)));
        items.add(new OverlayItem("Berlin", "SampleDescription", new GeoPoint(52.380816, 9.745936)));
        items.add(new OverlayItem("Washington", "SampleDescription", new GeoPoint(52.360816, 9.725936)));

        /* OnTapListener for the Markers, shows a simple Toast. */

        ItemizedIconOverlay mMyLocationOverlay = new ItemizedIconOverlay<>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        Toast.makeText(
                                getBaseContext(),
                                "Item '" + item.getTitle() + "' (index=" + index
                                        + ") got single tapped up", Toast.LENGTH_LONG).show();
                        return true; // We 'handled' this event.??????true??????????????????????????????????????????
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        Toast.makeText(
                                getBaseContext(),
                                "Item '" + item.getTitle() + "' (index=" + index
                                        + ") got long pressed", Toast.LENGTH_LONG).show();
                        return true;//false??????????????????????????????????????????
                    }
                }, getApplicationContext());
        map.getOverlays().add(mMyLocationOverlay);

    }

    private void DrawTheRecommendRoute(GeoPoint start,GeoPoint destination){
        MapView map = findViewById(R.id.map);
        RoadManager roadManager = new OSRMRoadManager(getBaseContext());
        Drawable marker = getResources().getDrawable(R.drawable.ic_map_location);
        //GeoPoint location=new GeoPoint(mapData.latitude,mapData.longitude);
        //????????????????????????
        //GeoPoint startPoint = new GeoPoint(27.914492710098862,112.90664106476612);
        IMapController mapController = map.getController();
        //??????????????????????????????
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();

        //27.905831143677858,112.91826347971147
        //waypoints.add(new GeoPoint(27.905831143677858,112.91826347971147)); //end point
        Object lock=new Object();
        synchronized (lock){
            mapController.setCenter(start);
            waypoints.add(start);
            waypoints.add(destination);
        }
        //???????????????????????????
        Road road = roadManager.getRoad(waypoints);
        //??????????????????
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road, 0xFF000000,3.0f);
        //??????????????????
        map.getOverlays().add(roadOverlay);
        //???????????????????????????

        for (int i=0; i<road.mNodes.size(); i++){
            RoadNode node = road.mNodes.get(i);
            Marker nodeMarker =  new  Marker(map);
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
            map.getOverlays().add(nodeMarker);
        }
        //Message msg = new Message();
        //msg.what = 1;
        //handler.sendMessage(msg);
    }

    private void DrawMultiRoute(){
        MapView map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.getOverlays().add(line);
        IMapController mapController = map.getController();
        mapController.setZoom(18.5);
        //Simple??????
        SimpleLocationOverlay simpleLocation = new SimpleLocationOverlay(((BitmapDrawable)getBaseContext().getResources().getDrawable(R.drawable.person)).getBitmap());
        simpleLocation.setEnabled(true);
        //GeoPoint start = new GeoPoint(mapDataList.get(0).latitude, mapDataList.get(0).longitude);
        if(mapData.latitude!=0.0&&mapData.longitude!=0.0){
            GeoPoint start=new GeoPoint(mapData.latitude,mapData.longitude);
            mapController.setCenter(start);
            //GeoPoint start=new GeoPoint(mapData.latitude,mapData.longitude);
            simpleLocation.setLocation(start);
        }

        map.getOverlays().add(simpleLocation);
    }
    private void InitialThePolyline(){
        //PathOverlay ??????Overlay
        line.setWidth(1);
    }

    public void doClick(View view) {
        if (!PermissionUtils.hasPermissions(this, permissions)) {
            PermissionUtils.requestPermissions(this, REQUEST_PERMISSION_CODE, permissions);
        } else {
            startLocate();
        }
    }



    private void startLocate() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean providerEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (providerEnabled) { //GPS?????????
            /**
             * ????????????
             * ??????1???????????????GPS_PROVIDER???NETWORK_PROVIDER??????????????????GPS,?????????GPRS??????WIFI??????
             * ??????2???????????????????????????.???????????????
             * ??????3??????????????????????????????????????????????????????????????????????????????????????????
             * ??????4?????????
             * ???????????????2???3???????????????3??????0???????????????3???????????????3???0?????????????????????????????????????????????0??????????????????
             */
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            // GeoPoint startPoint_init = new GeoPoint(27.902552,112.92361);
            //GeoPoint startPoint_init = new GeoPoint(locationListener.onLocationChanged(););


        } else {
            Toast.makeText(this, "?????????GPS", Toast.LENGTH_SHORT).show();
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //???????????????????????????
            //?????????????????????????????????????????????
            Log.e("xyh", "???????????????" + location.getProvider());
            Log.e("xyh", "?????????" + location.getLatitude());
            Log.e("xyh", "?????????" + location.getLongitude());
            Log.e("xyh", "?????????" + location.getAltitude());
            Log.e("xyh", "?????????" + location.getTime());


            mapData.setProvider(location.getProvider());
            mapData.setLatitude(location.getLatitude());
            mapData.setLongitude(location.getLongitude());
            mapData.setAltitude(location.getAltitude());
            mapData.setTime(location.getTime());
            mapDataList.add(mapData);
            if(mapDataList.size()==1){
                GeoPoint start=new GeoPoint(mapData.latitude,mapData.longitude);
                line.addPoint(start);
                DrawMultiRoute();
            }
            /*
            if(mapDataList.size()%10==1){
                //?????????????????????????????????,10???????????????
                MapView map = findViewById(R.id.map);
                IMapController mapController = map.getController();
                GeoPoint startPoint_init = new GeoPoint(location.getLatitude(),location.getLongitude());
                mapController.setCenter(startPoint_init);
            }

             */
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //GPS?????????????????????
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.e("onStatusChanged", "??????GPS?????????????????????");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.e("onStatusChanged", "??????GPS???????????????????????????");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.e("onStatusChanged", "??????GPS???????????????????????????");
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            //GPS???????????????
            Log.e("xyh", "onProviderEnabled: ");
        }

        @Override
        public void onProviderDisabled(String provider) {
            //GPS???????????????
            Log.e("xyh", "onProviderDisabled: ");
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsAllGranted(int requestCode, List<String> perms, boolean isAllGranted) {
        if (isAllGranted) {
            startLocate();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (PermissionUtils.somePermissionPermanentlyDenied(this, perms)) {
            PermissionUtils.showDialogGoToAppSettting(this);
        } else {
            PermissionUtils.showPermissionReason(requestCode, this, permissions, "??????????????????");
        }
    }


}
