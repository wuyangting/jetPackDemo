package com.example.myapplication.viewmodel
//ViewModel的使用
//1.需要先创建ViewModel类，继承自ViewModel重写onclear方法，使得页面销毁的时候能够走到自定义的onClear方法中
//2.在Activity中创建ViewModelProvider实例需要ViewModelOwner作为参数，和LifeCyclerOwner一样都是CoimmpentActivity实现的接口
// ；除此之外还需要一个创建Viwemodel的工厂。
//3该工厂默认实现是获取类名通过反射创建ViewModel实例，也可以自定义工厂函数，会接受一个class的参数只需要返回该实例即可，中间的操作可以自定义

//4.通过get传入对应的Viewmodel的Class对象即可。

//如何实现旋转屏幕数据保持不变？
//答：1.第一次创建  首先会从对应的Activity】中的NoLastConfigure获取activity取出对应的ViewModelStore这个时候由于是第一次所以是null，
// 于是只能new一个ViewModelStore。
//2.Activity重建时会销毁页面将ViewmodelStore保存到lastCongiure中并保存到ActivityClientRecord中传递给AMS端，
// AMS重新调用（这里需要注意如果是配置引起的重建会走RelauchActivity而不是第一次普通的lauchActivity）ReLaunch会通过token取出对应的AcRecord
//在attach的时候将record中上一次保存的lastCoinfigure取出来
// onCreate的时候把store赋值给NoLastConfigure，这个时候页面执行onCreate获取
//ViewModelStore就可以获取到了，而且Store是保存着这个页面的所有Viewmodel所以数据不会发生变化

//详细流程：
//HandlerRelauncherActivity中先调用handlerdestory销毁页面保存重要配置到record中（AMS会保存token{Activity唯一标识}和record的map），
// 在调用lauchActivity重建页面通过token重新取出record，record在取出configure保存到新创建的activity的属性中。

// 1.当调用performDestory的时候创建一个Configure类取出viewmodelStore中如果没有直接取到从上一次的configure中取，创建完configure后保存
// 到record中的lastConfigure属性中。
//2.何时重建：在performLaunchActivity方法中调用attach方法，在这个方法中取出record中的lastConfigure赋值给成员变量mLastConfigure
