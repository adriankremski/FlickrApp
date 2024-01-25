package com.github.snuffix.flickrapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.snuffix.flickrapp.databinding.ActivityMainBinding
import com.github.snuffix.flickrapp.repository.FlickrItem
import com.github.snuffix.flickrapp.ui.FlickItemsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    private val adapter = FlickItemsAdapter {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)

        setContentView(binding.root)

        with(binding) {
            recyclerView.adapter = adapter
            errorView.setRetryListener { viewModel.retry() }
        }

        with(viewModel) {
            items.collectWithLifecycle(::renderItems)
            isLoading.collectWithLifecycle(::renderLoading)
            isShowingFullScreenError.collectWithLifecycle(::renderFullScreenError)
            events.collectWithLifecycle(::handleEvents)
        }
    }

    private fun renderLoading(isLoading: Boolean) {
        with(binding) {
            progressBar.isVisible = isLoading

            if (isLoading) {
                recyclerView.isVisible = false
                errorView.isVisible = false
            }
        }
    }

    private fun renderItems(items: List<FlickrItem>) {
        binding.recyclerView.isVisible = true
        adapter.submitList(items)
    }

    private fun renderFullScreenError(isShowing: Boolean) {
        with(binding) {
            errorView.isVisible = isShowing

            if (isShowing) {
                progressBar.isVisible = false
                recyclerView.isVisible = false
            }
        }
    }

    private fun handleEvents(event: Event) {
        when (event) {
            is Event.ErrorFetchingItems -> {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun <T : Any?> Flow<T>.collectWithLifecycle(block: T.() -> Unit) {
        lifecycleScope.launch {
            flowWithLifecycle(lifecycle).collect { it.block() }
        }
    }
}