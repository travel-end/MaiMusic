package wind.maimusic.model

import wind.widget.tablayout.CustomTab

class MaiTab(private var title: String, private var selectedIcon: Int = 0, private var unSelectedIcon: Int = 0) :
    CustomTab {

    override fun getTabTitle(): String {
        return title
    }

    override fun getTabSelectedIcon(): Int {
        return selectedIcon
    }

    override fun getTabUnselectedIcon(): Int {
        return unSelectedIcon
    }

}