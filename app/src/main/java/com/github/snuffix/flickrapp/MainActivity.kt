package com.github.snuffix.flickrapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.window.layout.WindowMetricsCalculator
import com.github.snuffix.flickrapp.databinding.ActivityMainBinding
import com.github.snuffix.flickrapp.repository.FlickrItem
import com.github.snuffix.flickrapp.ui.FlickItemsAdapter
import com.github.snuffix.flickrapp.ui.ItemSpacingDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    private val adapter = FlickItemsAdapter {
        viewModel.openImage(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)

        setContentView(binding.root)

        binding.root.addView(object : View(this) {
            override fun onConfigurationChanged(newConfig: Configuration?) {
                super.onConfigurationChanged(newConfig)
                computeWindowSizeClasses()
            }
        })


        with(binding) {
            recyclerView.adapter = adapter
            computeWindowSizeClasses()
            recyclerView.addItemDecoration(ItemSpacingDecoration(this@MainActivity))
            errorView.setRetryListener { viewModel.retry() }
        }

        with(viewModel) {
            items.collectWithLifecycle(::renderItems)
            isLoading.collectWithLifecycle(::renderLoading)
            isShowingFullScreenError.collectWithLifecycle(::renderFullScreenError)
            events.collectWithLifecycle(::handleEvents)
        }
    }

    private fun computeWindowSizeClasses() {
        val metrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
        val width = metrics.bounds.width()
        val height = metrics.bounds.height()
        val density = resources.displayMetrics.density
        val windowSizeClass = WindowSizeClass.compute(width / density, height / density)
        val widthWindowSizeClass = windowSizeClass.windowWidthSizeClass
        if (widthWindowSizeClass in listOf(
                WindowWidthSizeClass.EXPANDED,
                WindowWidthSizeClass.MEDIUM
            )
        ) {
            binding.recyclerView.setLayoutManager(GridLayoutManager(this, 3))
        } else {
            binding.recyclerView.setLayoutManager(LinearLayoutManager(this))
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

            is Event.OpenImage -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.item.link))
                startActivity(intent)
            }
        }
    }

    private fun <T : Any?> Flow<T>.collectWithLifecycle(block: T.() -> Unit) {
        lifecycleScope.launch {
            flowWithLifecycle(lifecycle).collect { withContext(Dispatchers.Main) { it.block() } }
        }
    }
}