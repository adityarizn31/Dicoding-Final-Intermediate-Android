package com.dicoding.sub1_appsstory

import com.dicoding.sub1_appsstory.Data.GetStoryResp
import com.dicoding.sub1_appsstory.Data.ListStoryItem

object DataDummyy {
    fun generateDummyStories() : GetStoryResp {
        val ListStory = ArrayList<ListStoryItem>()
        for (i in 1..20 ) {
            val Story = ListStoryItem(
                id = "id_$i",
                name = "name $i",
                description = "description $i",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1684502365446_gIOeOwMH.jpg",
                createdAt = "19-05-2023",
                lat = i.toDouble() * 10,
                lon = i.toDouble() * 10,
            )
            ListStory.add(Story)
        }
        return GetStoryResp(
            error = false,
            message = "Stories Success",
            listStory = ListStory
        )
    }
}