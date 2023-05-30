package com.dicoding.sub1_appsstory.Ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.sub1_appsstory.Adapter.AdapterStory
import com.dicoding.sub1_appsstory.Data.ListStoryItem
import com.dicoding.sub1_appsstory.DataDummyy
import com.dicoding.sub1_appsstory.Repository.StoryAppRepository
import com.dicoding.sub1_appsstory.MainDispatcherRule
import com.dicoding.sub1_appsstory.Map.MapsActivity
import com.dicoding.sub1_appsstory.ViewModel.MainViewModel
import com.dicoding.sub1_appsstory.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get: Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var StoryAppRepositoryy: StoryAppRepository
    private lateinit var mainViewModel: MainViewModel
    private var dataDummy = DataDummyy.generateDummyStories()

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(StoryAppRepositoryy)
    }

    @Test
    fun `when Get Stories Should Not Null and Return Success` () = runTest {
        val data: PagingData<ListStoryItem> = StoryPagingSource.snapshot(dataDummy.listStory)
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data

        val mockContext = mock(MapsActivity::class.java)
        val token = mainViewModel.getPreference(mockContext).value.toString()
        `when`(StoryAppRepositoryy.getStory("Bearer $token")).thenReturn(expectedStory)

        val listStoryViewModel = MainViewModel(StoryAppRepositoryy)
        val actualStory: PagingData<ListStoryItem> =
            listStoryViewModel.story("Bearer $token").getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = AdapterStory.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        differ.submitData(actualStory)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dataDummy.listStory, differ.snapshot())
        Assert.assertEquals(dataDummy.listStory.size, differ.snapshot().size)
        Assert.assertEquals(dataDummy.listStory[0], differ.snapshot()[0])

    }

    @Test
    fun `when getStories is Null and Return Error` () = runTest {
        val data: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data

        val mockContext = mock(MapsActivity::class.java)
        val token = mainViewModel.getPreference(mockContext).value.toString()
        `when`(StoryAppRepositoryy.getStory("Bearer $token")).thenReturn(expectedStory)

        val listStoryViewModel = MainViewModel(StoryAppRepositoryy)
        val actualStory: PagingData<ListStoryItem> =
            listStoryViewModel.story("Bearer $token").getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = AdapterStory.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)
        Assert.assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}


