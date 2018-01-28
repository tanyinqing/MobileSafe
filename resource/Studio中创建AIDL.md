- Android Studio中创建AIDL Service
- http://blog.csdn.net/shenzhonglaoxu/article/details/42737195
- 客户端调用
 首先要拷贝AIDL文件，这里要保证文件的内容一模一样，包括包的名称，
 比如本例子中服务器端AIDL文件所在包的名称是com.sysu.aidlclient.aidlcilent，
 如何做到这一点，先新建一个项目，然后在：项目文件夹/app/src/main目录下建立一个aidl
 文件夹，与java文件夹同级，在Android Studio中就可以看到这个目录，
 在这个目录上右键New>Package，建立一个com.sysu.aidlclient.aidlclient的包，
 再将aidl文件拷进去。这样才能保证生成的java接口文件完全一样，否则会提示找不到接口。