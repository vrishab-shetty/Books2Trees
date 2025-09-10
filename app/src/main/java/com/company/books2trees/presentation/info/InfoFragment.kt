package com.company.books2trees.presentation.info

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.company.books2trees.R
import com.company.books2trees.databinding.FragmentInfoBinding
import com.company.books2trees.domain.model.PdfModel
import com.company.books2trees.presentation.common.base.ViewBindingFragment
import com.company.books2trees.presentation.info.adapter.PdfListAdapter
import com.company.books2trees.presentation.utils.UIHelper.navigateTo

class InfoFragment : ViewBindingFragment<FragmentInfoBinding>(FragmentInfoBinding::inflate) {

    private lateinit var adapter: PdfListAdapter
    private val vm: InfoViewModel by viewModels()
    private val openDoc =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            it?.let {
                vm.onUriReceived(it)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PdfListAdapter(
            layoutInflater,
            onItemClick = {
                openPdf(it)
            },
            onOptionsCLick = {
                deletePdf(it)
            }
        )
        useBinding { binding ->
            binding.pdfList.apply {
                adapter = this@InfoFragment.adapter
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        val fab = binding.addPdf
                        if (dy > 0) {
                            fab.shrink()
                        } else if (dy < -5) {
                            fab.extend()
                        }
                        super.onScrolled(recyclerView, dx, dy)
                    }
                })
            }

            binding.addPdf.setOnClickListener {
                openDoc.launch(arrayOf("application/pdf"))
            }

        }

        vm.pdfList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun deletePdf(model: PdfModel) {
        vm.onDeletePdf(model)
    }

    private fun openPdf(model: PdfModel) {
        activity?.navigateTo(R.id.viewPdfActivity, Bundle().apply { putString("id", model.id) })
    }

}