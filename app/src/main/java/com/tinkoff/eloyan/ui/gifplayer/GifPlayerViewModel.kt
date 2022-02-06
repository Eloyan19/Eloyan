package com.tinkoff.eloyan.ui.gifplayer


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinkoff.eloyan.data.GifItem
import com.tinkoff.eloyan.data.GifList
import com.tinkoff.eloyan.service.GifAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class GifPlayerViewModel @Inject constructor(private val gifAdapter: GifAdapter) :
    ViewModel() {

    private val _gifList =
        MutableLiveData<GifList>(GifList(ArrayList<GifItem>(), 0))

    val gifList: LiveData<GifList>
        get() = _gifList

    private val _error = MutableLiveData<Exception>(null)

    val error: LiveData<Exception>
        get() = _error

    init {
        getNextGif()
    }

    fun getPreviousGif() {
        if (_gifList.value!!.currentPosition != 0) {
            _gifList.value!!.currentPosition--
            _gifList.value = _gifList.value
        }
    }

    fun getNextGif(isRandomChecked : Boolean = true) {
        viewModelScope.launch {
            if (_gifList.value!!.gifList.size - 1 > _gifList.value!!.currentPosition) {
                _gifList.value!!.currentPosition++
                _gifList.value = _gifList.value
            } else {
                try {
                    if (_gifList.value!!.gifList.isNotEmpty()) {
                        _gifList.value!!.currentPosition++
                    }
                    getGif(isRandomChecked)
                    _gifList.value = _gifList.value
                    _error.value = null
                } catch (e: HttpException) {
                    Log.d("tag", "getNextGif ${e.code()} ${e.message}")
                    _error.value = e
                } catch (ex: Exception) {
                    _error.value = ex
                    Log.d("tag", "getNextGif ${ex.cause} ${ex.message}")
                }
            }
        }
    }


    fun downloadNextGif(isRandomChecked : Boolean = true) {
        viewModelScope.launch {
            try {
                getGif(isRandomChecked)
                _gifList.value = _gifList.value
                _error.value = null
            } catch (e: HttpException) {
                Log.d("tag", "downloadNextGif ${e.code()} ${e.message}")
                _error.value = e
            } catch (ex: Exception) {
                _error.value = ex
                Log.d("tag", "downloadNextGif ${ex.cause} ${ex.message}")
            }
        }
    }

    suspend fun getGif(isRandomChecked: Boolean = true)
    {
        if (isRandomChecked) {
            val newGif = gifAdapter.getRandomGif()
            _gifList.value!!.gifList.add(newGif)
        } else
        {
            val newGif = gifAdapter.getLatestGifs()
            newGif.result.forEach {
                _gifList.value!!.gifList.add(it)
            }
        }
    }

    fun selectCurrentPosition(position: Int) {
        _gifList.value!!.currentPosition = position
        _gifList.value = _gifList.value
    }
}