package com.github.snuffix.flickrapp.ui

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.snuffix.flickrapp.R
import com.github.snuffix.flickrapp.databinding.ViewErrorBinding

class ErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val binding: ViewErrorBinding

    init {
        binding = ViewErrorBinding.inflate(LayoutInflater.from(context), this)
        binding.errorImage.setBackgroundResource(R.drawable.network_error_animation)
        (binding.errorImage.background as? AnimationDrawable)?.start()
    }

    fun setRetryListener(listener: () -> Unit) {
        binding.retryButton.setOnClickListener {
            listener()
        }
    }
}