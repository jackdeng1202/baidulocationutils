package com.wtm.baiduservicelib;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by jack on 2016/11/23 0023.
 */

public class CustomBaiduLocation implements BDLocationListener {

    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = this;
    private LatLng mLatLng;
    private int mLocType;
    private String mCoorType;

    /**
     * 描述:获取坐标的坐标系,默认为bd09ll
     * gcj02,bd09,bd09ll
     */
    private static String DEFULT_COORTYPE="bd09ll";
    private Context mContext;

    public CustomBaiduLocation(Context context) {
        mContext =context;
        mCoorType=DEFULT_COORTYPE;
        mLocationClient = new LocationClient(context);     //声明LocationClient类
        initLocation();
    }

    /**
     * 获取经纬度的类
     * @param context 上下文
     * @param coorType 坐标系类型,可选参数:gcj02,bd09,bd09ll
     */
    public CustomBaiduLocation(Context context, String coorType) {
        mCoorType=coorType;
        mLocationClient = new LocationClient(context);     //声明LocationClient类
        initLocation();
    }

    public void start(){
        mLocationClient.registerLocationListener( myListener );    //注册监听函数
        mLocationClient.start();
    }

    public int getLocType(){
        return mLocType;
    }

    public void stopLocation(){
        mLocationClient.unRegisterLocationListener(myListener);
        mLocationClient.stop();
    }

    public void onLocationDestroy(){
        if (mLocationClient!=null){
            mLocationClient.stop();
            mLocationClient.unRegisterLocationListener(this);
            mLocationClient=null;
        }
    }

    private  void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(mCoorType);//可选，默认gcj02，设置返回的定位结果坐标系(gcj02,gps,bd09,bd09ll)
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        mLocType = bdLocation.getLocType();
        mLatLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        if(locationListner!=null){
            locationListner.onLocationReceived(bdLocation);
        }else{
            mLocationClient.unRegisterLocationListener(this);
            mLocationClient.stop();
        }
        if (62==mLocType)
            Toast.makeText(mContext,"定位失败，请检查后重试",Toast.LENGTH_SHORT).show();
        if (63==mLocType||mLocType==68)
            Toast.makeText(mContext,"网络链接异常",Toast.LENGTH_SHORT).show();
        if (162<=mLocType&&mLocType<=700)
            Toast.makeText(mContext,"定位异常",Toast.LENGTH_SHORT).show();

        Log.i("CustomBaiduLocation","locType="+ mLocType);//161成功
        Log.i("CustomBaiduLocation","Latitude="+ bdLocation.getLatitude()+"Longitude="+bdLocation.getLongitude());
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }

    public void setLocationListner(OnLocationReceivedListner locationListner) {
        this.locationListner = locationListner;
    }

    private OnLocationReceivedListner locationListner;

    public interface OnLocationReceivedListner{
        void onLocationReceived(BDLocation bdLocation);
    }


}
