package com.jjoe64.graphview_os.slice;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.UniqueLegendRenderer;
import com.jjoe64.graphview.helper.GraphViewXML;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;
import com.jjoe64.graphview.utils.LogUtil;
import com.jjoe64.graphview_os.ResourceTable;
import com.jjoe64.graphview_os.base.BaseAbilitySlice;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.ToastDialog;

public class MainAbilitySlice extends BaseAbilitySlice {

    private GraphView graphView;
    private GraphViewXML graphViewXml;
    private Button componentById;
    private int mIndex = 1;

    @Override
    protected int getLayout() {
        return ResourceTable.Layout_ability_main;
    }

    @Override
    protected void initView() {
        graphView = (GraphView) findComponentById(ResourceTable.Id_graphView);
//        graphViewXml = (GraphViewXML) findComponentById(ResourceTable.Id_graphViewXml);
        componentById = (Button) findComponentById(ResourceTable.Id_tvButton);


        graphView.setCursorMode(true);//点击显示数值弹窗

        componentById.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
//                switch (mIndex) {
//                    case 1:
//                        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
//                                new DataPoint(0, 1),
//                                new DataPoint(1, 5),
//                                new DataPoint(2, 3),
//                                new DataPoint(3, 2),
//                                new DataPoint(4, 6),
//                                new DataPoint(5, 4)
//
//                        });
//                        series.setTitle("测试2");
//                        series.setColor(Color.getIntColor("#DC143C"));
//
//                        graphViewXml.setCursorMode(true);
//                        graphViewXml.addSeries(series);
//                        break;
//                    case 2:
//                        BarGraphSeries<DataPoint> mSeries = new BarGraphSeries<DataPoint>(new DataPoint[]{
//                                new DataPoint(0, 1),
//                                new DataPoint(1, 5),
//                                new DataPoint(2, 3),
//                                new DataPoint(3, 2)
//                        });
//                        mSeries.setTitle("测试3");
//                        mSeries.setColor(Color.getIntColor("#00CED1"));
//                        graphViewXml.addSeries(mSeries);
//                        break;
//                    case 3:
//                        BarGraphSeries<DataPoint> mSeriesFo = new BarGraphSeries<DataPoint>(new DataPoint[]{
//                                new DataPoint(0, 1),
//                                new DataPoint(1, 5),
//                                new DataPoint(2, 3),
//                                new DataPoint(3, 2)
//                        });
//                        mSeriesFo.setTitle("测试3");
//                        mSeriesFo.setColor(Color.getIntColor("#00CED1"));
//                        graphView.getLegendRenderer().setVisible(true);
//                        graphView.setTitle("疫情统计");
//                        graphView.addSeries(mSeriesFo);
//                }
//
//                mIndex += 1;
                test15();
            }

        });
    }

    @Override
    protected void initData() {

    }

    private void test1() {
        BarGraphSeries series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 520.5),
                new DataPoint(492.9, 525),
                new DataPoint(521, 523),
                new DataPoint(673.8, 672),
                new DataPoint(784, 896)
        });
        graphView.addSeries(series);
    }

    private void test2() {
        BarGraphSeries barGraphSeries = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graphView.addSeries(barGraphSeries);
        graphView.getViewport().setBackgroundColor(Color.RED.getValue());
    }

    private void test3() {
        BarGraphSeries barGraphSeries = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graphView.addSeries(barGraphSeries);
        graphView.getViewport().setDrawBorder(true);
        graphView.getViewport().setBorderColor(Color.RED.getValue());
    }

    private void test4() {
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, -1),
                new DataPoint(1, 1),
                new DataPoint(2, 2),
                new DataPoint(3, 1),
                new DataPoint(4, 4)
        });
        graphView.setTitle("哈哈");
        graphView.setTitleTextSize(100);
        graphView.addSeries(series2);
        series2.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                ToastDialog td = new ToastDialog(graphView.getContext());
                td.setText("Series1: On Data Point clicked:  +dataPoint");
                td.show();

                LogUtil.error("字体大小--->", graphView.getTitleTextSize() + "");
            }
        });

    }

    private void test5() {
        PointsGraphSeries pointsGraphSeries = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graphView.addSeries(pointsGraphSeries);
        graphView.getGridLabelRenderer().setLabelsSpace(104);
    }

    private void test6() {
        BarGraphSeries series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 520.5),
                new DataPoint(492.9, 525),
                new DataPoint(521, 523),
                new DataPoint(673.8, 672),
                new DataPoint(784, 896)
        });
        graphView.addSeries(series);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED.getValue());
        graphView.setTitle("Sai Graph");
        graphView.setTitleTextSize(-1);

    }

    private void test7() {
        LineGraphSeries seriesNew2 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(3, 20),
                new DataPoint(4, 50)
        });
        PointsGraphSeries seriesNew = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 90),
                new DataPoint(1, 30),
                new DataPoint(2, 60),
                new DataPoint(3, 40),
                new DataPoint(4, 80)
        });
        graphView.getSecondScale().setMinY(20);
        graphView.getSecondScale().setMaxY(100);
        graphView.getSecondScale().addSeries(seriesNew);
        graphView.getSecondScale().addSeries(seriesNew2);
        graphView.getSecondScale().setVerticalAxisTitleColor(Color.BLACK.getValue());
        graphView.getSecondScale().setVerticalAxisTitle("Sai SecondScale");
        graphView.getSecondScale().setVerticalAxisTitleTextSize(-1);
    }

    private void test8() {
        PointsGraphSeries seriesNew = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graphView.addSeries(seriesNew);
        graphView.getGridLabelRenderer().setHorizontalAxisTitle("Sai Horizontal");
        graphView.getGridLabelRenderer().setVerticalAxisTitle("Sai Vertical");
        graphView.getGridLabelRenderer().setTextSize(35.5f);
    }

    private void test9() {
        BarGraphSeries series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graphView.addSeries(series);
        series.setTitle("Bar");
        graphView.getLegendRenderer().setVisible(true);
        graphView.getLegendRenderer().setFixedPosition(583, 90);
        graphView.getLegendRenderer().setMargin(30);
    }

    private void test10() {
        LineGraphSeries lineGraph = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        lineGraph.setDrawAsPath(true);
        PointsGraphSeries pointsGraph = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 2),
                new DataPoint(2, 5),
                new DataPoint(4, 1),
                new DataPoint(6, 4),
                new DataPoint(8, 6)
        });
        graphView.addSeries(lineGraph);
        graphView.addSeries(pointsGraph);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView, new String[]{"old", "new"}, new String[]{"high", "low"});
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }

    private void test11() {
        PointsGraphSeries pointsGraphSeries = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graphView.addSeries(pointsGraphSeries);
        graphView.getGridLabelRenderer().setPadding(4);
    }

    private void test12() {
        PointsGraphSeries pointsGraphSeries = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 2),
                new DataPoint(2, 5),
                new DataPoint(4, 1),
                new DataPoint(6, 4),
                new DataPoint(8, 6)
        });
        graphView.addSeries(pointsGraphSeries);
        graphView.getGridLabelRenderer().setLabelsSpace(104);
    }

    private void test13() {
        BarGraphSeries series = new BarGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graphView.addSeries(series);
    }

    private void test14() {
        LineGraphSeries lineGraph = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0.0, 1.0),
                new DataPoint(2.0, -51.0),
                new DataPoint(2.0, -51.0),
        });
        lineGraph.setTitle("Air");
        lineGraph.setColor(Color.rgb(115,211,230));
        graphView.addSeries(lineGraph);

        LineGraphSeries lineGraph2 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(2.0, -51.0),
                new DataPoint(5.0, -110.0),
                new DataPoint(15.0, -110.0),
                new DataPoint(21.0, -51.0),
                new DataPoint(21.0, -51.0)
        });
        lineGraph2.setTitle("TMX12/50");
        lineGraph2.setColor(Color.getIntColor("#000000"));
        graphView.addSeries(lineGraph2);

        LineGraphSeries lineGraph4 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(34.3, -21.0),
                new DataPoint(37.0, -21.0),
                new DataPoint(37.3, -18.0),
                new DataPoint(41.0, -18.0),
                new DataPoint(41.3, -15.0),
                new DataPoint(46.0, -15.0),
                new DataPoint(46.3, -12.0),
                new DataPoint(54.0, -12.0),
                new DataPoint(54.3, -9.0),
                new DataPoint(65.0, -9.0),
                new DataPoint(65.3, -6.0),
                new DataPoint(84.0, -6.0),
                new DataPoint(84.3, -3.0),
                new DataPoint(124.0, -3.0),
                new DataPoint(124.3, 0.0)
        });
        lineGraph4.setTitle("NTX50");
        lineGraph4.setColor(Color.rgb(115,230,115));
        graphView.addSeries(lineGraph4);

        LineGraphSeries lineGraph3 = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(21.0, -51.0),
                new DataPoint(22.0, -51.0),
                new DataPoint(22.3, -48.0),
                new DataPoint(23.0, -48.0),
                new DataPoint(23.3, -45.0),
                new DataPoint(24.0, -45.0),
                new DataPoint(24.3, -45.0),
                new DataPoint(25.0, -42.0),
                new DataPoint(25.3, -39.0),
                new DataPoint(26.3, -36.0),
                new DataPoint(27.0, -36.0),
                new DataPoint(27.3, -33.0),
                new DataPoint(28.0, -33.0),
                new DataPoint(28.3, -30.0),
                new DataPoint(29.0, -30.0),
                new DataPoint(29.3, -27.0),
                new DataPoint(31.0, -27.0),
                new DataPoint(31.3, -24.0),
                new DataPoint(34.0, -24.0),
                new DataPoint(34.3, -21.0)
        });
        lineGraph3.setTitle("Air");
        lineGraph3.setColor(Color.rgb(115,211,230));
        graphView.addSeries(lineGraph3);

        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(130);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinY(-115);
        graphView.getViewport().setMaxY(0);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getGridLabelRenderer().setHighlightZeroLines(false);

        graphView.setLegendRenderer(new UniqueLegendRenderer(graphView));
        graphView.getLegendRenderer().setVisible(true);

    }

    private void test15(){
        LineGraphSeries lineGraph = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graphView.addSeries(lineGraph);

        PointsGraphSeries pointsGraphSeries = new PointsGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 120),
                new DataPoint(1, -30),
                new DataPoint(2, 60),
                new DataPoint(3, 20),
                new DataPoint(4, 50)
        });
        graphView.getSecondScale().setMinY(20.6f);
        graphView.getSecondScale().setMaxY(100.8f);
        graphView.getSecondScale().addSeries(pointsGraphSeries);
        graphView.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.RED.getValue());

    }
}
