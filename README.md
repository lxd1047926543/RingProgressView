# RingProgressView
这是一个环形进度条控件并且易于自定义参数，环形进度条控件中每个参数如下图所示。关于为何自己画阴影，是因为安卓原生阴影需关闭硬件加速，但是关闭硬件加速后绘制有明显卡顿，我没有细找原因，如有其他原因或有更好展现阴影的方式，欢迎讨论。<br />
A view that displays progress in the form of arc and is easy to customize. The parameters of the RingProgressView are displayed as the pic below. The reason that why don't make Android draw the shadow itself is that disabling the hardward acceleration is essential in that way. But that will lower the performance of the drawing procedure. I didn't go straight to find the real reason which leads this happen, if you happen to know the reason or how to draw the shadow better you are welcomed to discuss. <br />
# The Visual Parameters of RingProgressView
![image](https://github.com/lxd1047926543/RingProgressView/blob/master/RingProgressViewParameters.png)<br />
<br />
# Usage
你可以选择从xml布局文件或者代码处定义控件参数，可定义的参数除如图所示，还可以定义环形进度条的样式（包括渐变和纯色两种）、是否以动画形式展现以及动画时间。控件中内置两种预设主题（包括红色主题和蓝色主题）。此外，控件在测量的过程中会遵从先xml布局文件再到代码定义的顺序，详情请见onMeasure方法。关于画笔的内容请见画笔的初始化方法。<br />
<br />
You can define the parameters from either the xml layout file or code. The parameters that can be defined in addition to the parameters showed in the pic are the style of the arc progress, including the styles of gradient and solid, whether to display by animation and the duration. Two preset theme are provided in the view which are the red theme and the blue theme. Besides, the measure procedure will consider the xml layout file first and then the code, you can take a look at the details through the onMeasure method. The details about the paints lays in the method of initializing the paints.
