package android.com.demo.ui.mainMenu.sellList

import android.com.demo.R
import android.com.demo.data.api.Resource
import android.com.demo.data.repository.DataRepository
import android.com.demo.data.room.entity.SellEntity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SellListViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: DataRepository
) : ViewModel() {

    val mDataList: MutableLiveData<Resource<List<SellEntity>>> = MutableLiveData()

    init {
        Timber.d("Create CallListViewModel")
    }

    fun getData() {
        viewModelScope.launch {
            mDataList.value = Resource.loading(null)
            val data = repository.getSellList()
            if (data.isNotEmpty()) {
                mDataList.value = Resource.success(data)
            } else {
                mDataList.value =
                    Resource.error(context.getString(R.string.message_do_not_have_data), null)
            }
        }
    }
}