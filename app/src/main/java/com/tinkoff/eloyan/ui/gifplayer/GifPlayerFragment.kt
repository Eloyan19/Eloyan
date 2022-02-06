package com.tinkoff.eloyan.ui.gifplayer

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.tinkoff.eloyan.R
import com.tinkoff.eloyan.databinding.GifPlayerFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GifPlayerFragment : Fragment(R.layout.gif_player_fragment) {

    private val viewModel by viewModels<GifPlayerViewModel>()
    private var _binding: GifPlayerFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = GifPlayerFragmentBinding.bind(view)

        val gifAdapter = GifPlayerAdapter()

        if (savedInstanceState != null) {
            binding.viewPagerGallery.currentItem = 1
        }

        binding.apply {
            viewPagerGallery.adapter = gifAdapter
            viewPagerGallery.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    buttonBack?.isEnabled = position != 0

                    viewModel.selectCurrentPosition(position)
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    val lastScreen = gifAdapter.itemCount - 1
                    if (lastScreen == position) {
                        viewModel.downloadNextGif(randomCheckbox.isChecked)
                    }
                }
            })

            buttonForward?.setOnClickListener {
                viewModel.getNextGif(randomCheckbox.isChecked)
            }

            buttonBack?.setOnClickListener {
                viewModel.getPreviousGif()
            }

            buttonRetry.setOnClickListener {
                viewModel.getNextGif(randomCheckbox.isChecked)
            }

            latestCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                randomCheckbox.isChecked = !isChecked
            }

            randomCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                latestCheckbox.isChecked = !isChecked
            }
        }

        viewModel.gifList.observe(viewLifecycleOwner) {
            Log.d("CachedGifList", it.toString())
            if (it.gifList.isEmpty()) {
                binding.viewPagerGallery.visibility = ViewGroup.GONE
                binding.controlButtons?.visibility = ViewGroup.GONE
                binding.progressBar.visibility = ViewGroup.VISIBLE
            } else {
                binding.viewPagerGallery.visibility = ViewGroup.VISIBLE
                binding.controlButtons?.visibility = ViewGroup.VISIBLE
                binding.progressBar.visibility = ViewGroup.GONE
            }
            binding.viewPagerGallery.currentItem = it.currentPosition
            gifAdapter.updateDataset(it.gifList)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            if (it!=null) {
                binding.viewPagerGallery.visibility = ViewGroup.GONE
                binding.controlButtons?.visibility = ViewGroup.GONE
                binding.progressBar.visibility = ViewGroup.GONE
                binding.linearLayoutNetworkError.visibility = ViewGroup.VISIBLE
            } else {
                binding.viewPagerGallery.visibility = ViewGroup.VISIBLE
                binding.controlButtons?.visibility = ViewGroup.VISIBLE
                binding.progressBar.visibility = ViewGroup.GONE
                binding.linearLayoutNetworkError.visibility = ViewGroup.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}