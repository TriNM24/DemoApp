package android.com.demo.ui.mainMenu.buyList

import android.com.demo.data.api.Resource
import android.com.demo.data.api.Status
import android.com.demo.data.api.response.BuyObjectResponse
import android.com.demo.data.repository.DataRepository
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
class BuyListViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: DataRepository
) : ViewModel() {
    val mDataList: MutableLiveData<Resource<List<BuyObjectResponse>>> = MutableLiveData()

    init {
        Timber.d("Create BuyListViewModel")
    }

    fun getData(){
        viewModelScope.launch {
            mDataList.value = Resource.loading(null)
            val data = repository.getBuyList()
            if(data.status == Status.SUCCESS){
                mDataList.value = data
            }else{
                mDataList.value = Resource.error(data.message?:"",null)
            }
        }
    }
}