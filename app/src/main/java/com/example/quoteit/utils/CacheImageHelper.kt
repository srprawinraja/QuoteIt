package com.example.quoteit.utils

import com.example.quoteit.R
import com.example.quoteit.data.TagsItem

class CacheImageHelper {
    val categoryIcons: Map<String, Int> = mapOf(
        "motivational" to R.drawable.motivational_icon,
        "inspirational" to R.drawable.inspirational_icon,
        "life" to R.drawable.life_icon,
        "love" to R.drawable.love_icon,
        "wisdom" to R.drawable.wisdom_icon,
        "friendship" to R.drawable.friendship_icon,
        "change" to R.drawable.change_icon,
        "business" to R.drawable.business_icon,
        "character" to R.drawable.character_icon,
        "competition" to R.drawable.competition_icon,
        "future" to R.drawable.future_icon,
        "happiness" to R.drawable.happiness_icon,
        "history" to R.drawable.history_icon,
        "humorous" to R.drawable.humorous_icon,
        "philosophy" to R.drawable.philosophy_icon,
        "politics" to R.drawable.politics_icon,
        "science" to R.drawable.science_icon,
        "sports" to R.drawable.sports_icon,
        "success" to R.drawable.success_icon,
        "technology" to R.drawable.technology_icon
    )
    fun getCachedImageResource(tag: String):Int{
        return categoryIcons.get(tag) ?: R.drawable.error_icon
    }
    fun containsCachedImage(tag: String): Boolean{
        return categoryIcons.containsKey(tag)
    }
    fun filterOutInvalidTags(tags: ArrayList<TagsItem>): List<TagsItem>{
        val filteredOutTags = tags.filter {  tag-> containsCachedImage(tag.name.lowercase()) }
        filteredOutTags.forEach { tag ->{
                tag.img = getCachedImageResource(tag.name)
            }
        }
        return filteredOutTags
    }
}