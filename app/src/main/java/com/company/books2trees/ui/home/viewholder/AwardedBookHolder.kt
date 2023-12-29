package com.company.books2trees.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.company.books2trees.databinding.ItemBookBinding
import com.company.books2trees.ui.common.BookHolder
import com.company.books2trees.ui.home.callbacks.OnBookClicked
import com.company.books2trees.ui.home.callbacks.OnBookLongPressed
import com.company.books2trees.ui.models.AwardedBookModel

class AwardedBookHolder(
    binding: ItemBookBinding,
    onItemClick: OnBookClicked,
    onItemLongClick: OnBookLongPressed
) :  BookHolder(
    binding,
    onItemClick,
    onItemLongClick
)