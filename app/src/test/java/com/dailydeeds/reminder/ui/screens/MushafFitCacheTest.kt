package com.dailydeeds.reminder.ui.screens

import com.dailydeeds.reminder.data.FavoriteKey
import com.dailydeeds.reminder.data.QuranPages
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** The fit cache: a page is fitted again only when its size or its own favourites change. */
class MushafFitCacheTest {

    private val page2 = QuranPages.page(2) // al-Baqara 1-5
    private fun key(favorites: Set<FavoriteKey>, page: QuranPages.Page = page2, h: Int = 2000) =
        mushafFitKey(page, favorites, 1080, h, density = 2.75f, fontScale = 1f)

    @Test
    fun aFavouriteOnAnotherPageDoesNotChangeTheKey() {
        assertEquals(key(emptySet()), key(setOf(FavoriteKey.Ayah(1, 1), FavoriteKey.Mafatih("x"))))
    }

    @Test
    fun aFavouriteOnThePageChangesTheKey() {
        val starred = key(setOf(FavoriteKey.Ayah(2, 3)))
        assertNotEquals(key(emptySet()), starred)
        assertEquals(setOf(FavoriteKey.Ayah(2, 3)), starred.favorites)
    }

    @Test
    fun sizeAndFontScaleArePartOfTheKey() {
        assertNotEquals(key(emptySet()), key(emptySet(), h = 1800))
        assertNotEquals(
            mushafFitKey(page2, emptySet(), 1080, 2000, 2.75f, 1f),
            mushafFitKey(page2, emptySet(), 1080, 2000, 2.75f, 1.3f)
        )
    }

    @Test
    fun theCacheKeepsTheMostRecentlyUsedPages() {
        val cache = MushafFitCache(capacity = 2)
        val a = key(emptySet(), QuranPages.page(3))
        val b = key(emptySet(), QuranPages.page(4))
        val c = key(emptySet(), QuranPages.page(5))
        cache.put(a, 20f)
        cache.put(b, 21f)
        assertEquals(20f, cache.get(a)) // a is now the most recent
        cache.put(c, 22f)
        assertNull(cache.get(b))
        assertEquals(20f, cache.get(a))
        assertEquals(22f, cache.get(c))
    }
}
