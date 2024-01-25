package com.github.snuffix.flickrapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.github.snuffix.flickrapp.repository.FlickrItem
import com.github.snuffix.flickrapp.repository.FlickrRepository
import com.github.snuffix.flickrapp.repository.RepositoryError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: FlickrRepository
) : ViewModel() {
    private val refresh = MutableSharedFlow<Unit>()

    private val _showLoading = MutableStateFlow(false)
    private val _showFullScreenError = MutableStateFlow(false)

    private val _events = MutableSharedFlow<Event>()

    private val _items = MutableStateFlow<List<FlickrItem>>(emptyList())

    val items: Flow<List<FlickrItem>>
        get() = _items

    val isLoading: Flow<Boolean>
        get() = _showLoading

    val isShowingFullScreenError: Flow<Boolean>
        get() = _showFullScreenError

    val events: Flow<Event>
        get() = _events

    init {
        refresh
            .onStart { emit(Unit) }
            .onEach {
                _showLoading.emit(true)
                _showFullScreenError.emit(false)
            }
            .flatMapLatest {
                repository.getFlickrItems()
            }
            .onEach { result ->
                _showLoading.emit(false)

                result
                    .onSuccess {
                        _items.emit(it)
                    }.onFailure { error ->
                        when (error) {
                            is RepositoryError.GetFlickrItemsError -> {
                                _items.emit(error.cachedItems)

                                if (error.cachedItems.isEmpty()) {
                                    _showFullScreenError.emit(true)
                                } else {
                                    _events.emit(Event.ErrorFetchingItems)
                                }
                            }
                        }
                    }

            }
            .launchIn(viewModelScope)
    }

    fun retry() {
        viewModelScope.launch {
            refresh.emit(Unit)
        }
    }
}

sealed class Event {
    data object ErrorFetchingItems : Event()
}