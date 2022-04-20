package me.panavtec.title.hmadapter;

import me.panavtec.title.Entity.TextStream;
import me.panavtec.title.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.app.Context;

import java.util.List;

public class TextStreamListProvider extends BaseListProvider<TextStream, TextStreamListProvider.MusicHolder> {


    public TextStreamListProvider(List<TextStream> list, Context context) {
        super(list, context);
    }

    @Override
    int getItemLayout() {
        return ResourceTable.Layout_list_text_stream;
    }

    @Override
    void convertItem(int position, List<TextStream> data, MusicHolder viewHolder) {
        TextStream textStream = data.get(position);
        viewHolder.text1.setText(textStream.getContent());
        viewHolder.text2.setText(textStream.getData());
    }

    @Override
    MusicHolder getViewHolder(Component component) {
        return new MusicHolder(component);
    }

    public static class MusicHolder extends BaseListProvider.BaseViewHolder {

        final Text text1;
        final Text text2;
        final Text text3;

        public MusicHolder(Component component) {
            super(component);
            text1 = (Text) component.findComponentById(ResourceTable.Id_text);
            text2 = (Text) component.findComponentById(ResourceTable.Id_text2);
            text3 = (Text) component.findComponentById(ResourceTable.Id_text3);
        }
    }
}
