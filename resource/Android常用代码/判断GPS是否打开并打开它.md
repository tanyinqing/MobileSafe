
- Android判断GPS是否开启和强制帮用户打开GPS
- 引子：在我们的应用为用户提供定位服务时，通常想为用户提供精确点的定位服务，这是需要用户配合的。我们必须先检测用户手机的GPS当前是否打开，若没打开则弹出对话框提示。用户若不配合我们也没办法，只能采用基站定位方式。如果我们的应用必须用户打开GPS才可使用，这时流氓一点的做法，就是强制帮用户打开GPS。
-  定位服务GPS：全球卫星定位系统，使用24个人造卫星所形成的网络来三角定位接受器的位置，并提供经纬度坐标。虽然GPS提供绝佳的位置的精确度，但定位的位置需要在可看见人造卫星或轨道所经过的地方。
-  定位服务AGPS：辅助全球卫星定位系统（英语：Assisted Global Positioning System，简称：AGPS）是一种GPS的运行方式。它可以利用手机基地站的资讯，配合传统GPS卫星，让定位的速度更快。用中文来说应该是网络辅助GPS定位系统。通俗的说AGPS是在以往通过卫星接受定位信号的同时结合移动运营的GSM或者CDMA网络机站的定位信息，就是一方面由具有AGPS的手机获取来自卫星的定位信息，而同时也要靠该手机透过中国移动的GPRS网络下载辅助的定位信息，两者相结合来完成定位。与传统GPS(GlobalPositioningSystem全球定位系统)首次定位要2、3分钟相比AGPS的首次定位时间最快仅需几秒钟，同时AGPS也彻底解决了普通GPS设备在室内无法获取定位信息的缺陷。
- 一、检测用户手机的GPS当前是否打开,，代码如下：

```
/**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager
                                 = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }
```
- 强制帮用户打开GPS，代码如下：

```
/**
     * 强制帮用户打开GPS
     * @param context
     */
    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (CanceledException e) {
            e.printStackTrace();
        }
    }
```
- 在AndroidManifest.xml文件里需要添加的权限：

```
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
```