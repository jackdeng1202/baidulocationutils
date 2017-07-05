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
                    Log.i("BaiduLocation","locType="+ mLocType);//161成功
                    Log.i("BaiduLocation","Latitude="+ bdLocation.getLatitude()+"Longitude="+bdLocation.getLongitude());
                    
                    /**
                    61：GPS定位结果，GPS定位成功。
                    62：无法获取有效定位依据，定位失败，请检查运营商网络或者WiFi网络是否正常开启，尝试重新请求定位。
                    63：网络异常，没有成功向服务器发起请求，请确认当前测试手机网络是否通畅，尝试重新请求定位。
                    65：定位缓存的结果。
                    66：离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果。
                    67：离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果。
                    68：网络连接失败时，查找本地离线定位时对应的返回结果。
                    161：网络定位结果，网络定位成功。
                    162：请求串密文解析失败，一般是由于客户端SO文件加载失败造成，请严格参照开发指南或demo开发，放入对应SO文件。
                    167：服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位。
                    502：AK参数错误，请按照说明文档重新申请AK。
                    505：AK不存在或者非法，请按照说明文档重新申请AK。
                    601：AK服务被开发者自己禁用，请按照说明文档重新申请AK。
                    602：key mcode不匹配，您的AK配置过程中安全码设置有问题，请确保：SHA1正确，“;”分号是英文状态；且包名是您当前运行应用的包名，请按照说明文档重新申请AK。
                    501～700：AK验证失败，请按照说明文档重新申请AK
                    */
                    }
                }
            });
                
Step 4. stop locating and unregister
               
        mBaiduLocation.stopLocation();//停止定位
        mBaiduLocation.onLocationDestroy();//注销定位信息监听