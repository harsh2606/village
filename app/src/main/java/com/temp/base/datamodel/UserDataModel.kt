package com.temp.base.datamodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.temp.datasource.UserRepository
import com.temp.network.APIClient
import com.temp.network.APIinterface

class UserDataModel {
    fun getArea(context: Context): MutableLiveData<AllArea> {
        val apInterface: APIinterface =
            APIClient.newRequestRetrofit(context).create(APIinterface::class.java)
        val userRepository = UserRepository(apInterface)
        return userRepository.getArea("")
    }
}