package me.panavtec.title.hmadapter.about;

import com.bumptech.glide.Glide;
import me.panavtec.title.ResourceTable;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.app.Context;
import ohos.utils.net.Uri;

import java.util.List;

/** @noinspection unchecked*/
public class AboutContributorListProvider extends BaseItemProvider {
    private List<ContributorObject> mList;
    private Context mContext;

    public void setList(List list){
        mList = list;
    }

    public AboutContributorListProvider(Context context) {
        this.mList = null;
        this.setContext(context);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mList != null && position >= 0 && position < mList.size()) {
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component convertComponent, ComponentContainer componentContainer) {
        Component cpt;
        ViewHolder viewHolder;
        if (convertComponent == null) {
            cpt = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_list_contributors, null, false);
            viewHolder = new ViewHolder(cpt, mList.get(position));
            cpt.setTag(viewHolder);
        } else {
            cpt = convertComponent;
            viewHolder = (ViewHolder) cpt.getTag();
        }

        Glide.with(getContext())
                .load(mList.get(position).urlAvatar)
                .override(90)
                .circleCrop()
                .into(viewHolder.image);

//        viewHolder.image.setId(mList.get(position).urlAvatar);
        viewHolder.name.setText(mList.get(position).name);
        return cpt;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }
    
    
    static class ViewHolder {
        final Image image;
        final Text  name;
        ViewHolder(Component component, ContributorObject contributorObject) {
            image = (Image) component.findComponentById(ResourceTable.Id_contributor_image_control);
            name  = (Text) component.findComponentById(ResourceTable.Id_contributor_name_text);

            component.findComponentById(ResourceTable.Id_contributor_visitView).setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    component.getContext().startAbility(new Intent().setUri(Uri.parse(contributorObject.url)),0);
                }
            });
        }
    }
    
    public static class ContributorObject
    {
        final String name;
        final String url;
        final String urlAvatar;

        public ContributorObject(String name, String url, String urlAvatar)
        {
            this.name = name;
            this.url = url;
            this.urlAvatar = urlAvatar;
        }
    }
}

