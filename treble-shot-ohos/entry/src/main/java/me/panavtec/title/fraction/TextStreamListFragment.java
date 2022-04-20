package me.panavtec.title.fraction;

import com.ohos.trebleshot.database.AccessDatabase;
import com.ohos.trebleshot.database.SQLQuery;
import com.ohos.trebleshot.object.TextStreamObject;
import com.ohos.trebleshot.utils.AppUtils;
import me.panavtec.title.Entity.BaseSelectEntity;
import me.panavtec.title.Entity.TextStream;
import me.panavtec.title.ResourceTable;
import me.panavtec.title.TextEditorAbility;
import me.panavtec.title.hmadapter.TextStreamListProvider;
import me.panavtec.title.hminterface.SliceToMainI;
import me.panavtec.title.hmutils.HMUtils;
import me.panavtec.title.slice.TextEditorAbilitySlice;
import ohos.aafwk.content.Intent;

import java.util.ArrayList;
import java.util.List;

import static me.panavtec.title.hmutils.HMUtils.transferLongToDate;

public class TextStreamListFragment extends BaseListFraction {


    private AccessDatabase mDatabase;
    private TextStreamListProvider mTextStreamListProvider;

    public TextStreamListFragment(SliceToMainI sliceToMainI) {
        super(sliceToMainI);
    }

    @Override
    public List<? extends BaseSelectEntity> getSelectedList() {
        return new ArrayList<>();
    }

    @Override
    public int setXMLId() {
        return ResourceTable.Layout_layout_text_stream;
    }

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);

        mDatabase = AppUtils.getDatabase(this);

        List<TextStream> list = convertTSObjectToTextStream(getTextStreamObjsList());
        mTextStreamListProvider = new TextStreamListProvider(list, getMyContext());
        getListContainer().setItemProvider(mTextStreamListProvider);
        setEmptyView(list.size() == 0, ResourceTable.String_text_listEmptyTextStream, ResourceTable.Graphic_ic_forum_white_24dp);
        viewComponent.findComponentById(ResourceTable.Id_layout_text_stream_fab)
                .setClickedListener(component -> jumpAbility(TextEditorAbility.class));

        getListContainer().setItemClickedListener((listContainer, component, i, l) -> {
            TextStream object = (TextStream) mTextStreamListProvider.getItem(i);
            HMUtils.startAbility(
                    viewComponent.getContext(),
                    new Intent().setParam(TextEditorAbilitySlice.EXTRA_CLIPBOARD_ID, object.id),
                    TextEditorAbility.class.getName(),
                    TextEditorAbilitySlice.ACTION_EDIT_TEXT
            );
        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLongClickChanged(boolean longClickState) {

    }

    @Override
    public void onTextSeared(String s) {

    }

    @Override
    public void onActive(){
        List<TextStream> list = convertTSObjectToTextStream(getTextStreamObjsList());
        setVisible(list.size() == 0);
        mTextStreamListProvider.setList(list);
        mTextStreamListProvider.notifyDataChanged();
    }


    private List<TextStream> getList() {
        ArrayList<TextStream> songHolders = new ArrayList<>();

        TextStream textStream;
        for (int i = 0; i < 20; i++) {
            textStream = new TextStream();
            textStream.setContent("文本" + i);
            songHolders.add(textStream);
        }
        return songHolders;
    }



    private List<TextStream> convertTSObjectToTextStream(List<TextStreamObject> tsObjList) {
        ArrayList<TextStream> tsList = new ArrayList<>();

        TextStream textStream;
        for (TextStreamObject textStreamObj : tsObjList) {
            textStream = new TextStream();
            textStream.setContent(textStreamObj.text);
            textStream.id = textStreamObj.id;
            textStream.setData(transferLongToDate("MM/dd HH:mm", textStreamObj.date));
            tsList.add(textStream);
        }
        return tsList;
    }

    private  List<TextStreamObject> getTextStreamObjsList(){
        return mDatabase.castQuery(new SQLQuery.Select(AccessDatabase.TABLE_CLIPBOARD), TextStreamObject.class);
    }
}
