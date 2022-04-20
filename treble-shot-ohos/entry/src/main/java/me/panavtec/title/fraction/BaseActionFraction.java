package me.panavtec.title.fraction;

import me.panavtec.title.Entity.BaseSelectEntity;
import me.panavtec.title.hminterface.MainToSliceI;
import me.panavtec.title.hminterface.SliceToMainI;

import java.util.ArrayList;
import java.util.List;

public  class BaseActionFraction extends BaseListFraction implements MainToSliceI {


    public BaseActionFraction(SliceToMainI sliceToMainI) {
        super(sliceToMainI);
    }

    @Override
    public List<? extends BaseSelectEntity> getSelectedList() {
        return new ArrayList<>();
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
    public int setXMLId() {
        return 0;
    }

    @Override
    public void clearSelected() {

    }

    @Override
    public void deleteItems() {

    }

    @Override
    public void doAction(int action) {

    }
}
