/**
 * GraphView
 * Copyright 2016 Jonas Gehring
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jjoe64.graphview.helper;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.*;
import com.jjoe64.graphview.utils.LogUtil;
import ohos.agp.components.AttrSet;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;

import static com.jjoe64.graphview.utils.AttrSetString.*;


/**
 * helper class to use GraphView directly
 * in a XML layout file.
 * <p>
 * You can set the data via attribute <b>app:seriesData</b>
 * in the format: "X=Y;X=Y;..." e.g. "0=5.0;1=5;2=4;3=9"
 * <p>
 * Other styling options:
 * <li>app:seriesType="line|bar|points"</li>
 * <li>app:seriesColor="#ff0000"</li>
 * <li>app:seriesTitle="foobar" - if this is set, the legend will be drawn</li>
 * <li>ohos:title="foobar"</li>
 * <p>
 * Example:
 * <pre>
 * {@code
 *  <com.jjoe64.graphview.helper.GraphViewXML
 *      ohos:layout_width="match_parent"
 *      ohos:layout_height="100dip"
 *      app:seriesData="0=5;2=5;3=0;4=2"
 *      app:seriesType="line"
 *      app:seriesColor="#ee0000" />
 * }
 * </pre>
 *
 * @author jjoe64
 */
public class GraphViewXML extends GraphView {
    private String dataStr = "";
    private Color color = new Color(Color.getIntColor("#1E90FF"));
    private String type = "line";
    private String seriesTitle = "";
    private String title = "";

    /**
     * creates the graphview object with data and
     * other options from xml attributes.
     *
     * @param context
     * @param attrs
     */
    public GraphViewXML(Context context, AttrSet attrs) {
        super(context, attrs);



        dataStr = attrs.getAttr(GRAPH_VIEW_SERIES_DATA).isPresent() ? attrs.getAttr(GRAPH_VIEW_SERIES_DATA).get().getStringValue() : dataStr;
        type = attrs.getAttr(GRAPH_VIEW_SERIES_TYPE).isPresent() ? attrs.getAttr(GRAPH_VIEW_SERIES_TYPE).get().getStringValue() : type;
        color = attrs.getAttr(GRAPH_VIEW_SERIES_COLOR).isPresent() ? attrs.getAttr(GRAPH_VIEW_SERIES_COLOR).get().getColorValue() : color;
        seriesTitle = attrs.getAttr(GRAPH_VIEW_SERIES_TITLE).isPresent() ? attrs.getAttr(GRAPH_VIEW_SERIES_TITLE).get().getStringValue() : seriesTitle;
        title = attrs.getAttr(GRAPH_VIEW_TITLE).isPresent() ? attrs.getAttr(GRAPH_VIEW_TITLE).get().getStringValue() : title;


        // decode data
        DataPoint[] data = new DataPoint[0];
        if (dataStr == null || dataStr.isEmpty()) {
            throwSleepTime("Attribute seriesData is required in the format: 0=5.0;1=5;2=4;3=9");
        } else {
            String[] d = dataStr.split(";");
            try {
                data = new DataPoint[d.length];
                int i = 0;
                for (String dd : d) {
                    String[] xy = dd.split("=");
                    data[i] = new DataPoint(Double.parseDouble(xy[0]), Double.parseDouble(xy[1]));
                    i++;

                    LogUtil.info(TAG, Double.parseDouble(xy[0]) + "~~~" + Double.parseDouble(xy[1]));
                }
            } catch (Exception e) {
                throwSleepTime("Attribute seriesData is broken. Use this format: 0=5.0;1=5;2=4;3=9");
            }
        }

        // create series
        BaseSeries<DataPoint> series = null;
        if (type == null || type.isEmpty()) {
            type = "line";
        }
        if (type.equals("line")) {
            series = new LineGraphSeries<DataPoint>(data);
        } else if (type.equals("bar")) {
            series = new BarGraphSeries<DataPoint>(data);
        } else if (type.equals("points")) {
            series = new PointsGraphSeries<DataPoint>(data);
        } else {
            throwSleepTime("unknown graph type: " + type + ". Possible is line|bar|points");
        }
        if (color.getValue() != 0) {
            series.setColor(color.getValue());
        }
        addSeries(series);

        if (seriesTitle != null && !seriesTitle.isEmpty()) {
            series.setTitle(seriesTitle);
            getLegendRenderer().setVisible(true);
        }

        if (title != null && !title.isEmpty()) {
            setTitle(title);
        }
    }

    private void throwSleepTime(String str){
        new EventHandler(EventRunner.create()).postTask(new Runnable() {
            @Override
            public void run() {
                throw new IllegalArgumentException(str);

            }
        },500);
    }
}
