# Night

使用databinding 进行黑夜白天模式大多数情况只需再布局中增加相应的字段
 
* 再activity启动时注册      Night.getInstance().addListener(this); （当页面不需要实时变化时可不实现）

* 切换模式  Night.getInstance().setNight(!flag); 

* 模式有改变之后回调 onNightChange()

* 对布局做递归改变 Night.getInstance().change(mBinding.clRootView); 

* xml 中的设置 


        设置字体颜色 night:textcolor='@{"h"}'
        设置imageview图片 night:setimageview='@{"url"}'
        ...
        ...
 
