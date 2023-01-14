package android.com.demo.data.repository

import android.com.demo.data.api.ApiService
import android.com.demo.data.api.Resource
import android.com.demo.data.api.response.BuyObjectResponse
import android.com.demo.data.api.response.CallObjectResponse
import android.com.demo.data.room.dao.SellDao
import android.com.demo.data.room.entity.SellEntity
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val apiService: ApiService,
    private val sellDao: SellDao
) {
    suspend fun getCallList(): Resource<List<CallObjectResponse>> {
        return apiService.getCallList()
    }
    suspend fun getBuyList(): Resource<List<BuyObjectResponse>> {
        return apiService.getBuyList()
    }

    suspend fun getSellList(): List<SellEntity> {
        return sellDao.getAll()
    }
}