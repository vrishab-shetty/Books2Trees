package com.company.books2trees.ui.info.adapter

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.company.books2trees.databinding.ItemViewPdfBinding
import com.company.books2trees.repos.PdfRepository.renderAndClose

class ViewPdfAdapter(
    private var renderer: PdfRenderer?,
    private val inflater: LayoutInflater,
    private val width: Int
) : RecyclerView.Adapter<ViewPdfAdapter.ViewPdfHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewPdfHolder(ItemViewPdfBinding.inflate(inflater, parent, false))

    override fun getItemCount() = renderer?.pageCount ?: 0

    override fun onBindViewHolder(holder: ViewPdfHolder, position: Int) {

        renderer?.let {
            holder.bind(
                it.openPage(position)
                    .renderAndClose(width)
            )
        }

    }

    fun setRenderer(renderer: PdfRenderer) {
        this.renderer = renderer
        notifyItemInserted(0)
    }

    inner class ViewPdfHolder(private val binding: ItemViewPdfBinding) : ViewHolder(binding.root) {

        fun bind(bitmap: Bitmap?) {
            binding.pdfPage.setImageBitmap(bitmap)
        }
    }
}