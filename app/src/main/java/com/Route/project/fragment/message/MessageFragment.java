package com.Route.project.fragment.message;

import com.Route.project.core.BaseFragment;
import com.xuexiang.templateproject.R;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;

@Page(anim = CoreAnim.none)
public class MessageFragment extends BaseFragment {

    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initViews() {

    }
}
