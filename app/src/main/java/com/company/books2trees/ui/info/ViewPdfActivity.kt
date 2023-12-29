package com.company.books2trees.ui.info

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.company.books2trees.databinding.ActivityPdfViewBinding
import com.company.books2trees.ui.info.adapter.ViewPdfAdapter
import com.company.books2trees.ui.common.PdfViewState
import com.company.books2trees.utils.UIHelper
import com.company.books2trees.utils.UIHelper.setUpToolbar


class ViewPdfActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfViewBinding
    private val args: InfoFragmentArgs by navArgs()
    private val vm: ViewPdfViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        binding = ActivityPdfViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val model = vm.getModel(args.id)
        val adapter = ViewPdfAdapter(null, layoutInflater, UIHelper.getScreenWidth(this))

        model?.let {
            this.setUpToolbar(it.name)
            binding.viewPdf.apply {
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
                this.adapter = adapter
            }
        }

        vm.renderer.observe(this) { viewState ->
            when (viewState) {
                is PdfViewState.Loading -> {
                }

                is PdfViewState.Content ->
                    adapter.setRenderer(viewState.renderer)

                is PdfViewState.Error -> {
                }
            }

        }
    }

}