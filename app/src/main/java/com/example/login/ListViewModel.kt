package com.example.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.d10.Model.Photos

class ListViewModel: ViewModel() {
    private val listItems = MutableLiveData<List<Photos>>()

    fun setlistItems(list:List<Photos>){
        listItems.value=list
    }
    fun getlistItems():LiveData<List<Photos>>{
        return  listItems
    }
}