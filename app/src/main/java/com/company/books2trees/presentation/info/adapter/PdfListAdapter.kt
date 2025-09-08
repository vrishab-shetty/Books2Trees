package com.company.books2trees.presentation.info.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.company.books2trees.databinding.ItemImportPdfBinding
import com.company.books2trees.domain.model.PdfModel

class PdfListAdapter(
    private val inflater: LayoutInflater,
    private val onItemClick: (PdfModel) -> Unit,
    private val onOptionsCLick: (PdfModel) -> Unit
) :
    ListAdapter<PdfModel, PdfListAdapter.PdfViewHolder>(PdfModel.PdfModelDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PdfViewHolder(
            ItemImportPdfBinding.inflate(inflater, parent, false),
            onItemClick,
            onOptionsCLick
        )

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PdfViewHolder(
        private val binding: ItemImportPdfBinding,
        private val onItemCLick: (PdfModel) -> Unit,
        private val onOptionsCLick: (PdfModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {

            binding.root.setOnClickListener {
                binding.model?.let { model -> onItemCLick(model) }
            }
            binding.options.setOnClickListener {
                binding.model?.let { model -> onOptionsCLick(model) }
            }

        }

        fun bind(model: PdfModel) {
            binding.model = model
            binding.executePendingBindings()
        }
    }
}