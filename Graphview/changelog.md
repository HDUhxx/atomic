# GraphView
v1.1.0


不支持功能
1.滑动功能 ,缩放功能，
 
不支持功能具体API如下

1.  BaseSeries -->  public void appendData(E dataPoint,boolean scrollToEnd,int maxDataPoints,boolean silent);
2.  BaseSeries -->  public void appendData(E dataPoint,boolean scrollToEnd,int maxDataPoints);
3.  LineGraphSeries -->  public void appendData(E dataPoint,boolean scrollToEnd,int maxDataPoints,boolean silent);
4.  Viewport --> public boolean isScrollable();
5.  Viewport --> public boolean setScrollable(boolean mIsScrollable);
6.  Viewport --> public boolean isScalable();
7.  Viewport --> public boolean setScalable(boolean mIsScalable);
8.  Viewport --> public void scrollToEnd();
9.  Viewport --> public OnXAxisBoundsChangedListener getOnXAxisBoundsChangedListener ();
10. Viewport --> public void setOnXAxisBoundListener(OnXAxisBoundsChangedListener l);
11. Viewport --> public void setScrollableY(boolean scrollableY);  
12. Viewport --> public void setScalableY(boolean scalableY); 
13. Viewport --> public double getMaxXAxisSize();
14. Viewport --> public double getMaxYAxisSize();
15. Viewport --> public double setMaxXAxisSize(double mMaxXAxisViewportSize);
16. Viewport --> public double setMaxYAxisSize(double mMaxYAxisViewportSize);
17. Viewport --> public double setMinimalViewport(double minX,double maxX,double minY,double maxY);
18. StaticLabelsFormatter -- >StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView_01,new String[] {"old", "new"},new 
    String[] {"high", "low"});    
    （When the static label formatter passes 2 different vertical labels, it displays the first label twice instead of displaying the two collection 
    labels。）
19. GraphView --> public class GraphView 内takeSnapshotAndShare()    
20. LineGraphSeries--> public class LineGraphSeries 内 public void setDrawAsPath() 
21. LineGraphSeries -->public class LineGraphSeries 内 public void setAnimated(boolean animated)
22. BarGraphSeries --> public class BarGraphSeries 内  public void setAnimated(boolean animated)
23. Viewport --> public class Viewport 内OverScroller / onScroll 
24. LineGraphSeries --> public class LineGraphSeries 内 AccelerateInterpolator
25. 如果2个图形系列具有相同的系列标题，具有不同的系列颜色，则图例中仅显示第一个系列信息
26. When Y axis labels are huge values or strings, some part of vertical label is crossing vertical axis
27. When background color is set for graph using setBackgroundColor() method, little bit of color is crossing horizontal axis
28. setBorderColor and setBorderPaint methods of viewport class are not supported