package me.panavtec.title.adapterBase;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.agp.components.Component;
import ohos.app.Context;

import java.util.HashMap;

import static me.panavtec.title.hmutils.Toast.LENGTH_LONG;

/**
 * created by: veli
 * date: 7/31/18 11:54 AM
 */
public class CustFraction extends Fraction implements FractionIF, SnackbarSupport
{
	private boolean mIsMenuShown;
	public CustFraction() {
    }
    //	private boolean mIsMenuShown;
	private Component mSnackbarContainer;
	private int mSnackbarLength = LENGTH_LONG;

//	public static Object instantiate(AbilitySlice addDevicesToTransferActivity, String name, Object assigneeFragmentArgs) {
//		return null;
//	}

	public Snackbar createSnackbar(int resId, Object... objects)
	{
		Component drawOverView = mSnackbarContainer == null ? getComponent() : mSnackbarContainer;

//		return drawOverView!= null
//				? Snackbar.make(drawOverView, mSnackbarContainer.getContext().getString(resId, objects), mSnackbarLength)
//				: null;

		return  new Snackbar(this);
	}

	@Override
	public void setArguments(HashMap arguments) {

	}

	public HashMap getArguments() {
			return null;
	}

	public boolean isMenuShown()
	{
		return mIsMenuShown;
	}

//	@Override
	public void setMenuVisibility(boolean menuVisible)
	{
//		super.setMenuVisibility(menuVisible);
		mIsMenuShown = menuVisible;
	}

	public void setSnackbarLength(int length)
	{
		mSnackbarLength = length;
	}

	public void setSnackbarContainer(Component component)
	{
		mSnackbarContainer = component;
	}

    /**
     * created by: veli
     * date: 26.03.2018 11:12
     */
}
