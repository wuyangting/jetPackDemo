package com.example.myapplication.room

//Room使用
//数据库有多张表，一张表只能记录一种Class，Class的具体属性是这个表的列；所有对表的操作都要通过Dao来访问

//注解说明：
//@Enity 作用于Class上，表示创建一张表记录该Class，Class内部属性使用@ColumnInfo声明该列名字和Type
//@Dao 作用于Interface，该interface主要是提供访问表进行增删改查的接口。使用对应增删改查注解标识方法
//@Database 声明数据库(需为abstract)，该注解属性entities需声明该数据库中的所有表，version表示该数据库的版本,数据库中声明返回Dao实例的方法

//使用流程
//通过Room的静态方法databaseBuilder传入context，数据库的class对象，数据库的名称(db结尾)调用build函数就创建了数据库
//借助build的返回值访问Dao，进行增删改查操作
//2.降序升序：在databaseBuilder函数后添加迁移策略addMigrations，构造函数的两个版本对应迁移的版本，migrate函数是匹配对应策略后进行的操作

//大致原理
//疑问：Dao中只是定义了访问数据库的接口，数据库也只是个抽象类返回Dao实例的接口还没有实现？

//在编译期间借助KAPT插件生成@Database标记的实现类，类名只是添加了一个后缀_Impl，而@Dao也会生成对应的_Impl后缀的实现类
//查看Dao的实现类发现其每一个crud都是一个事物，并且在uiThread操作会crash，如果需要多个操作按顺序进行而不是单个事务一样，需要添加@Transition标记方法，内部curd将按顺序执行
// 这两个生成的类可以在该目录下找到build/generated/source/kapt/debug/com/example/myapplication/room

//1、databaseBuilder会创建RoomDatabase的Builder对象保存传入的参数(比如数据库的class,数据库名称，版本迁移策略，数据库打开关闭回调)，初始化一些的配置参数
// build中会反射创建出数据库class+_Impl的实例，然后返回。
//2.获取dao之后进行curd，在crud内部首先看有没有挂起的事务，如果Thread 1提交的事务还没有结束，Thread2提交后会报crash(ThreadLocal保证)
//3.接着打开数据库，也就是在进行crud的时候才会打开数据库。其中会判断是否进行迁移
//4.cud的每个操作验证完23后，就会开启一个事务.beginTransaction()接着设置事务的成功或Fail。最后结束事务



