/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ohos.trebleshot.popupwindow;

import ohos.agp.components.*;
import ohos.app.Context;

import java.util.HashMap;
import java.util.List;

/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
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

public abstract class BaseProvider<T> extends BaseItemProvider {

    private Context context;
    private List<T> data;
    private int mLayoutId;

    /**
     * @param context   上下文
     * @param data      数据源
     * @param mLayoutId 条目的资源文件id
     * @return 适配器对象
     */
    public BaseProvider(Context context, List<T> data, int mLayoutId) {
        this.context = context;
        this.mLayoutId = mLayoutId;
        this.data = data;
    }

    public void setData(List<T> data) {
        this.data = data;
        notifyDataChanged();
    }

    public List<T> getData() {
        return data;
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return data != null ? data.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component convertComponent, ComponentContainer parent) {
        ViewHolder viewHolder;
        if (convertComponent != null) {
            viewHolder = (ViewHolder) convertComponent.getTag();
        } else {
            convertComponent = LayoutScatter.getInstance(context).parse(mLayoutId, null, false);
            viewHolder = new ViewHolder(convertComponent);
            convertComponent.setTag(viewHolder);
        }
        bind(viewHolder, getItem(position), position);
        return convertComponent;
    }

    /**
     * 每个条目需要执行的逻辑方法
     *
     * @param holder   条目绑定的holder对象
     * @param itemData 条目对应的数据
     * @param position 条目的索引
     */
    protected abstract void bind(ViewHolder holder, T itemData, int position);

    /**
     * 绑定条目控件的自定义holder类
     */
    protected static class ViewHolder {

        /**
         * 绑定的条目控件
         */
        public Component itemView;
        private final HashMap<Integer, Component> mViews = new HashMap<>();

        ViewHolder(Component component) {
            this.itemView = component;
        }

        /**
         * 链式调用，用于给Text控件快速设置文本
         *
         * @param viewId Text控件的资源id
         * @param text   需要设置的文本
         * @return holder对象自身
         */
        public ViewHolder setText(int viewId, String text) {
            ((Text) getView(viewId)).setText(text);
            return this;
        }

        /**
         * 根据id或者条目中的某个控件
         *
         * @param viewId 需要获取的控件的资源id
         * @param <E>    需要获取控件的类型
         * @return 需要获取的控件
         */
        public <E extends Component> E getView(int viewId) {
            E view = (E) mViews.get(viewId);
            if (view == null) {
                view = (E) itemView.findComponentById(viewId);
                mViews.put(viewId, view);
            }
            return view;
        }

    }

}
