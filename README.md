# jetPackDemo
JetPack库练习



# Room 使用Flow订阅缺陷：
1.数据处理只能由外部触发，内部无法发射。且Flow内部不允许使用其他协程emit数据，源码中注释因为是不安全的；
解决方案：
（1）但是还是可以通过内部阻塞队列实现，外部唤醒或者使用标识符flow内部阻塞等机制，但是这和冷流推出的意义不符合。
（2）使用Channel转换的Flow本质上还是flow但是其内部使用channel进行发送

原因参考：
这里用冷流是否可行？显然并不合适，因为首先直观上冷流就无法在构造器以外发射数据。
但实际上答案并不绝对，通过在flow构造器内部使用channel，同样可以实现动态发射，如channelFlow。但是channelFlow本身不支持在构造器以外发射值，通过Channel.receiveAsFlow操作符可以将Channel转换成channelFlow。这样产生的Flow“外冷内热”，使用效果和直接收集Channel几乎没有区别。



2.SharedFlow和StateFlow：
public fun <T> MutableSharedFlow(

    // 每个新的订阅者订阅时收到的回放的数目，默认0
    replay: Int = 0,

    // 除了replay数目之外，缓存的容量，默认0
    extraBufferCapacity: Int = 0,

    // 缓存区溢出时的策略，默认为挂起。只有当至少有一个订阅者时，onBufferOverflow才会生效。当无订阅者时，只有最近replay数目的值会保存，并且onBufferOverflow无效。 
    onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND
)

  //MutableStateFlow等价于使用如下构造参数的SharedFlow

MutableSharedFlow(
    replay = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)
SharedFlow被Pass的原因主要有两个：事件丢弃和一对多

SharedFlow支持被多个订阅者订阅，导致同一个事件会被多次消费，并不符合预期。
如果认为1还可以通过开发规范控制，SharedFlow的在无订阅者时会丢弃数据的特性则让其彻底无缘被选用承载必须被执行的事件
SharedFlow类似BroadcastChannel，支持多个订阅者，一次发送多处消费。
SharedFlow配置灵活，如默认配置 capacity = 0， replay = 0，意味着新订阅者不会收到类似LiveData的回放。无订阅者时会直接丢弃，正符合上述时效性事件的特点。
  
作者：字节大力智能
链接：https://juejin.cn/post/7031726493906829319
来源：稀土掘金
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
  
  
  StateFlow（特殊的ShareFlow 不收回放，无订阅者丢弃，一对多），SharedFlow（可设置flow的回访次数，无订阅者缓存，可设置缓冲数量。背压策略无订阅者时失效，也可用于解决数据倒灌），ChannelFlow（一对多订阅，实时性，不会导致数据倒灌）

