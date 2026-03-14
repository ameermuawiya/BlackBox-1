package top.niunaijun.blackboxa.view.xp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import top.niunaijun.blackboxa.data.XpRepository

@Suppress("UNCHECKED_CAST")
class XpFactory(private val repo: XpRepository): ViewModelProvider.NewInstanceFactory() {

    /*
     * Updated ViewModel upper bound
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return XpViewModel(repo) as T
    }
}
