package name.caiyao.microreader.ui.iView;

import java.util.ArrayList;

import name.caiyao.microreader.bean.guokr.GuokrHotItem;

/**
 * Created by 蔡小木 on 2016/4/22 0022.
 */
public interface IGuokrFragment extends IBaseFragment {
    void updateList(ArrayList<GuokrHotItem> guokrHotItems);
}
