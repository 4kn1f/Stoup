package com.submission.stoup

import com.submission.stoup.data.remote.response.ListStoryItem

object DummyDataTesting {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val storyItem = ListStoryItem(
                id = "unique_id_$i",
                photoUrl = "https://example.com/photo_$i.jpg",
                createdAt = "2025-01-16T10:00:00Z",
                name = "Author $i",
                description = "Story $i where our story begins",
                lat = -6.0 - i,
                lon = 100.0 + i
            )
            items.add(storyItem)
        }
        return items
    }
}