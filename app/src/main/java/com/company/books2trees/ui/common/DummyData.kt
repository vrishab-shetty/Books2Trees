package com.company.books2trees.ui.common

import com.company.books2trees.ui.models.AwardedBookModel
import com.company.books2trees.ui.models.BookModel
import com.company.books2trees.ui.models.PopularBookModel
import com.company.books2trees.ui.models.SimpleBookModel

val popularItems: List<BookModel> = listOf(
    PopularBookModel(
        id = "58283080",
        position = 1,
        name = "Hook, Line, and Sinker (Bellinger Sisters, #2)",
        cover = "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1627068858i/58283080.jpg",
        rating = 4.12,
        url = "https://www.goodreads.com/book/show/58283080-hook-line-and-sinker"
    ),
    PopularBookModel(
        id = "58438583",
        position = 2,
        name = "One Italian Summer",
        cover = "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1626799802i/58438583.jpg",
        rating = 3.73,
        url = "https://www.goodreads.com/book/show/58438583-one-italian-summer"
    ),
    PopularBookModel(
        id = "58371432",
        position = 3,
        name = "The Book of Cold Cases",
        cover = "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1624553583i/58371432.jpg",
        rating = 3.91,
        url = "https://www.goodreads.com/book/show/58371432-the-book-of-cold-cases"
    )
)

val awardedItems: List<BookModel> = listOf(
    AwardedBookModel(
        id = "58784475",
        name = "Tomorrow, and Tomorrow, and Tomorrow",
        category = "Fiction",
        cover = "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1636978687l/58784475._SY475_.jpg",
        url = "https://www.goodreads.com/choiceawards/best-fiction-books-2022",
    ),
    AwardedBookModel(
        id = "55196813",
        name = "The Maid (Molly the Maid, #1)",
        category = "Mystery & Thriller",
        cover = "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1643228739l/55196813.jpg",
        url = "https://www.goodreads.com/choiceawards/best-mystery-thriller-books-2022",
    ),
    AwardedBookModel(
        id = "60435878",
        name = "Carrie Soto Is Back",
        category = "Historical Fiction",
        cover = "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1649848581l/60435878.jpg",
        url = "https://www.goodreads.com/choiceawards/best-historical-fiction-books-2022",
    )
)

val searchResult: List<BookModel> = listOf(
    SimpleBookModel(
        id = "52861201",
        name = "From Blood and Ash",
        extras = "Jennifer L. Armentrou",
        cover = "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1588843906l/52861201._SY475_.jpg",
        url = "https://www.goodreads.com/book/show/52861201-from-blood-and-ash?from_choice=true"
    ),
    AwardedBookModel(
        id = "58784475",
        name = "Tomorrow, and Tomorrow, and Tomorrow",
        category = "Fiction",
        cover = "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1636978687l/58784475._SY475_.jpg",
        url = "https://www.goodreads.com/choiceawards/best-fiction-books-2022",
    ),
    PopularBookModel(
        id = "58438583",
        position = 2,
        name = "One Italian Summer",
        cover = "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1626799802i/58438583.jpg",
        rating = 3.73,
        url = "https://www.goodreads.com/book/show/58438583-one-italian-summer"
    )
)