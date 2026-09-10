package com.prawin.quoteit.viewModels

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prawin.quoteit.api.NetworkResponse
import com.prawin.quoteit.data.firebase.FireStoreRepository
import com.prawin.quoteit.data.model.Tag
import com.prawin.quoteit.db.tag.TagEntity
import com.prawin.quoteit.db.tag.TagRepository
import com.prawin.quoteit.utils.ContextHelper
import com.prawin.quoteit.utils.NetworkHelper
import com.prawin.quoteit.utils.SharedPreferenceHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val TAG = "TagsViewModel"

class TagsViewModel(
    val tagRepository: TagRepository,
    val contextHelper: ContextHelper,
    val sharedPreferenceHelper: SharedPreferenceHelper,
): ViewModel() {
    private val _uiState = MutableStateFlow<NetworkResponse<List<TagEntity>>>(NetworkResponse.Loading)
    private val networkHelper: NetworkHelper = NetworkHelper(contextHelper){
        getListOfTags()
    }

    private val changedTagsItem = mutableListOf<TagEntity>()

    init {
        getListOfTags()
    }
    val tagFlow = tagRepository.getAllTagsFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    fun getListOfTags(){
        viewModelScope.launch {
            val tag = sharedPreferenceHelper.getValue("tag")
            if(tag!=null) {
                _uiState.value = NetworkResponse.Success(tagRepository.getAllTags())
            } else {
                tagRepository.deleteAll()
                try {
                    if (networkHelper.isNetworkAvailable()) {
                        networkHelper.stopMonitoring()
                        val tags: List<Tag> = FireStoreRepository.getTags()
                        if(tags.isNotEmpty()){
                            tagRepository.insertAll(tags)
                            _uiState.value = NetworkResponse.Success(tagRepository.getAllTags())
                            sharedPreferenceHelper.save("tag", "")
                        } else {
                            NetworkResponse.Error("no tags available")
                        }
                    }
                    else {
                        NetworkResponse.Error("no internet")
                        networkHelper.startMonitoring()
                    }
                } catch (_: Exception){
                    // Log.d(TAG, e.toString())
                    NetworkResponse.Error("error occurred while retrieving tags")
                }
            }

        }
    }


    fun updateChanges(tagEntity: TagEntity) {
        if(!isTagSelected(tagEntity.id)){
            changedTagsItem.add(tagEntity)
        }
        tagEntity.isMarked = !tagEntity.isMarked
    }


    fun isTagSelected(id: Int): Boolean{
        for(tagEntity in changedTagsItem){
            if(tagEntity.id == id){
                return true
            }
        }
        return false
    }
    fun discardChanges(){
        changedTagsItem.clear()
    }
    fun commitChanges(){
        viewModelScope.launch {
            tagRepository.updateMarked(changedTagsItem)
            discardChanges()
            Toast.makeText(contextHelper.getContext(), "saved successfully", Toast.LENGTH_SHORT).show()
        }
    }


}