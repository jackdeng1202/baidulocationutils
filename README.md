# baiduservice
提供百度定位功能

######简单的调用接口可以获得定位信息.免去下载jar包和自己封装的麻烦.

Step 1. Add the JitPack repository to your build file,Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
  
Step 2. Add the dependency,add configuration information
  
	dependencies {
	    ...
        compile 'com.github.jackdengchuangliang:baidulocationutils:1.0'
	}
	
    defaultConfig {
        ...
        ndk {
            // 设置支持的SO库架构
            abiFilters 'armeabi'
        }
    }
    
	android {
	    ...
        sourceSets {
            main {
                jniLibs.srcDirs = ['libs']
            }
        }
	}
	
Step 3. Instantiate the target object

        CustomBaiduLocation mBaiduLocation = new CustomBaiduLocation(context);//初始化定位工具类

Step 4. Start locating and listening

        mBaiduLocation.start();//开始定位
        mBaiduLocation.setLocationListner(new CustomBaiduLocation.OnLocationReceivedListner() {
                @Override
                public void onLocationReceived(BDLocation bdLocation) {
                    //接收到定位信息
                    if (bdLocation == null || mMapView == null) {
                        Logger.i("test==bdLocation is null ");
                        return;
                    }
    
                    mBaiduLocation.stopLocation();
                   
                    mCity = bdLocation.getCity();

                    MyLocationData locData = new MyLocationData.Builder()
                            .accuracy(bdLocation.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                            .direction(100).latitude(bdLocation.getLatitude())
                            .longitude(bdLocation.getLongitude()).build();
                    mBaiduMap.setMyLocationData(locData);
                    mCurLocation = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                    BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.map_location);
                    OverlayOptions option = new MarkerOptions().position(mCurLocation).icon(bitmap);
                    mBaiduMap.addOverlay(option);
                    
                    }
                }
            });
                
Step 4. stop locating and unregister
               
        mBaiduLocation.stopLocation();//停止定位
        mBaiduLocation.onLocationDestroy();//注销定位信息监听