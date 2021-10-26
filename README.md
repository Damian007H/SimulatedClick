# SimulatedClick
模拟点击项目
> 懒惰使人进步、一点都没错，我就是那个懒惰的人。

@[toc]

#### 项目实现功能
可以模拟用户操作    点击    滑动    返回功能。


#### 使用方法

 1. 安装apk，首次安装后引导至 开启悬浮窗权限 和 无障碍权限。
![在这里插入图片描述](https://img-blog.csdnimg.cn/7a81c15f1f0547bcaa6492ed9e9f5e59.gif)

-------------------

 2. 操作功能介绍
第一步骤后界面会弹出一个 可移动的悬浮窗
![在这里插入图片描述](https://img-blog.csdnimg.cn/f6f5a3edb97341faa8cc23dd85001f6f.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA6JCM5aiDMDA3,size_13,color_FFFFFF,t_70,g_se,x_16)
 
	(1）添加任务，点击加号，显示弹窗
![在这里插入图片描述](https://img-blog.csdnimg.cn/c597e6168f374bee83fa8ee4a63685eb.png?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA6JCM5aiDMDA3,size_11,color_FFFFFF,t_70,g_se,x_16)
选择具体的任务，选择项： 点击 、滑动、返回；
延迟时间：输入执行该任务之前的延迟时间，时间不得小于50（单位ms）；
循环次数：当前添加的任务循环次数，不能小于1次；
坐标：需要模拟的点击坐标位置。（滑动时会出来两个坐标，一个是起始坐标，一个是结束坐标，模拟返回不需要填入任何坐标）

     根据自己业务场景填入相关数据点击确认，会将您所选择的任务添加到任务列表如下图

    ![在这里插入图片描述](https://img-blog.csdnimg.cn/acf1386595c74839a1c2b7933b164218.png)
	(2) 执行任务，点击播放按钮

	执行任务过程中正在执行和已执行的任务字体颜色变蓝，播放按钮编程暂停按钮
![在这里插入图片描述](https://img-blog.csdnimg.cn/7a60fb1e563e48f0b299c785c9cc7fc0.png)

	（3）取消任务 如果当前任务正在执行，可点击暂停按钮进行取消当前的任务

	（4）删除任务 如果当前任务未执行或已执行完毕，可通过长摁 加号按钮将所有任务清空，恢复至初始状态。




#### 功能演示

我们以京东为例，需要模拟京东秒杀两件商品添加购物车逻辑（ps：这里我只是举例子，但实际可操作性远比我这个强）

 1. 手动模拟所有流程，分析流程
![请添加图片描述](https://img-blog.csdnimg.cn/5c61617a1ecb4948bcafd1fe0983d137.gif)
由于这里图片大小的限制，录的gif帧率有点低大家凑活看吧，根据上面的流程大致可以将所有事件拆分为以下阶段：
1：点击；2：滑动；3：点击；4：点击；5：点击；6：返回；7：点击；8：点击；9：返回；10：返回。


 2. 找到关键点位置坐标
	> 找坐标可以利用我们刚才安装的apk，打开后手指触摸屏幕，会显示当前手指的坐标；
	
	![请添加图片描述](https://img-blog.csdnimg.cn/8eb950e4a98247ecbe8acaf11b63d218.gif)

 
	根据上面演示中的需求找到合适的坐标点，并记录下来，将记录的坐标添加到任务队列中



	1：点击（181,1845）；
	2：滑动（向上滑动 500,1571； 500，641）；
	3：点击（抢购按钮 914,1484）；；
	4：点击（加购按钮 543,2075）；
	5：点击（弹窗确认 543,2075）；
	6：返回
	7：点击（抢购按钮 920,1895）；
	8：点击（加购按钮 543,2075）；
	82：点击（弹窗确认 543,2075）；  这里录屏的时候商品变化了，导致两件商品都需要规格选择确认，所以这里加一行
	9：返回；
	10：返回。

	> 注意所有的延迟时间都放长一些，因为涉及到跨页面等需要稍微等待一下


 3. 开始表演

	
	如遇无法播放请点开此处观看 [视频地址](https://www.bilibili.com/video/BV1pL411g7uF?t=0.0)
		[video(video-mXMQVaRh-1635234488710)(type-bilibili)(url-https://player.bilibili.com/player.html?aid=463759737)(image-https://img-blog.csdnimg.cn/img_convert/345d62fd3c9ac86a3a8d93febde48ec2.png)(title-)]

		

	

	



#### 技术分析

熟悉Android的同学到这里应该都看懂了，其实就是用的AccessibilityService 中的几个相关的方法，实现也是非常简单，由于只是个Demo写的比较简陋，不喜勿喷，创作不易，感谢支持。


我已将所有代码开源至Github，如有需要请移步查看。

apk下载地址：[https://download.csdn.net/download/qq_36652498/34649909](https://download.csdn.net/download/qq_36652498/34649909)

Github地址：[https://github.com/Len-jidong/SimulatedClick](https://github.com/Len-jidong/SimulatedClick)

原文连接：[https://blog.csdn.net/qq_36652498/article/details/120957234](https://blog.csdn.net/qq_36652498/article/details/120957234)