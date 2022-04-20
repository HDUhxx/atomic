package me.panavtec.title.hmadapter;

import me.panavtec.title.Entity.PreloadedGroup;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.hmutils.FileUtils;
import me.panavtec.title.hmutils.TextUtils;
import ohos.agp.components.*;
import ohos.app.Context;

import java.util.List;

public class FileDetailProvider extends BaseListProvider<PreloadedGroup, FileDetailProvider.ViewHolder> {

    public FileDetailProvider(List<PreloadedGroup> list, Context context) {
        super(list, context);
    }

    @Override
    int getItemLayout() {
        return ResourceTable.Layout_list_transfer_file;
    }

    @Override
    void convertItem(int position, List<PreloadedGroup> data, ViewHolder viewHolder) {
        setUi(viewHolder, position, data);
    }


    @Override
    ViewHolder getViewHolder(Component component) {
        return new ViewHolder(component);
    }


    private void setUi(ViewHolder holder, int position, List<PreloadedGroup> list) {
//        try {
        PreloadedGroup object = list.get(position);
//            int percentage = (int) (object.totalPercent * 100);
        holder.tvItemName.setText(nullCheck(object.assignees));
        holder.selector.setVisibility(object.isSelected() ? Component.VISIBLE : Component.HIDE);
        if (!TextUtils.isEmpty(object.getSize()))
            holder.text2.setText(nullCheck(FileUtils.sizeExpression(Long.parseLong(object.getSize()), false)));
//            holder.text2.setText(FileUtils.sizeExpression(object.totalBytes, false));
//            holder.text3.setText(object.totalPercent + "");
//            String s = fraction.getResourceManager().getElement(ResourceTable.String_text_transferStatusFiles).getString(object.totalCountCompleted, object.totalCount);
//            holder.text4.setText(s);
//            holder.progressBar.setMaxValue(100);
//            holder.progressBar.setProgressValue(percentage <= 0 ? 1 : percentage);
//            boolean b = object.ismIsSelected();
//        try {
//            Resource resource = fraction.getResourceManager().getResource(ResourceTable.Graphic_background_text_white);

//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (NotExistException e) {
//            e.printStackTrace();
//        }
//        catch (WrongTypeException e) {
//            e.printStackTrace();
//        }
    }


    static class ViewHolder extends BaseListProvider.BaseViewHolder {
        final RoundProgressBar progressBar;
        final Image image;
        final Image selector;

        final Text tvItemName;
        final Text text2;
        final Text text3;
        final Text text4;
        final DirectionalLayout statusLayoutWeb;

        ViewHolder(Component component) {
            super(component);
            progressBar = (RoundProgressBar) component.findComponentById(ResourceTable.Id_progressBar);
            image = (Image) component.findComponentById(ResourceTable.Id_image);
            selector = (Image) component.findComponentById(ResourceTable.Id_selector);
            tvItemName = (Text) component.findComponentById(ResourceTable.Id_text);

            text2 = (Text) component.findComponentById(ResourceTable.Id_text2);
            text3 = (Text) component.findComponentById(ResourceTable.Id_text3);
            text4 = (Text) component.findComponentById(ResourceTable.Id_text4);
            statusLayoutWeb = (DirectionalLayout) component.findComponentById(ResourceTable.Id_statusLayoutWeb);
        }
    }
}

