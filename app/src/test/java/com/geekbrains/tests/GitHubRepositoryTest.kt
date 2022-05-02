package com.geekbrains.tests

import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.repository.GitHubApi
import com.geekbrains.tests.repository.GitHubRepository
import com.geekbrains.tests.repository.GitHubRepository.GitHubRepositoryCallback
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GitHubRepositoryTest {

    private lateinit var repository: GitHubRepository

    @Mock
    private lateinit var gitHubApi: GitHubApi

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = GitHubRepository(gitHubApi)
    }

    @Test
    fun searchGithub_Test() {
        val searchQuery = "some query"
        val call = mock(Call::class.java) as Call<SearchResponse?>

        `when`(gitHubApi.searchGithub(searchQuery)).thenReturn(call)
        repository.searchGithub(searchQuery, mock(GitHubRepositoryCallback::class.java))
        verify(gitHubApi, times(1)).searchGithub(searchQuery)
    }

    @Test
    fun searchGithub_TestCallback() {
        val searchQuery = "some query"
        val response = mock(Response::class.java) as Response<SearchResponse?>
        val gitHubRepositoryCallBack = mock(GitHubRepositoryCallback::class.java)

        val call = mock(Call::class.java) as Call<SearchResponse?>
        `when`(call.enqueue(any())).then {
            val callback = it.getArgument<Callback<SearchResponse?>>(0)
            callback.onResponse(call, response)
            callback.onFailure(call, Throwable())
        }

        `when`(gitHubApi.searchGithub(searchQuery)).thenReturn(call)
        repository.searchGithub(searchQuery, gitHubRepositoryCallBack)

        verify(gitHubRepositoryCallBack, times(1)).handleGitHubResponse(response)
        verify(gitHubRepositoryCallBack, times(1)).handleGitHubError()
    }

    @Test
    fun searchGithub_TestCallback_WithMock() {
        val searchQuery = "some query"
        val call = mock(Call::class.java) as Call<SearchResponse?>
        val callBack = mock(Callback::class.java) as Callback<SearchResponse?>
        val gitHubRepositoryCallBack = mock(GitHubRepositoryCallback::class.java)
        val response = mock(Response::class.java) as Response<SearchResponse?>

        `when`(gitHubApi.searchGithub(searchQuery)).thenReturn(call)
        `when`(call.enqueue(any())).then {
            callBack.onResponse(any(), any())
        }
        `when`(callBack.onResponse(any(), any())).then {
            gitHubRepositoryCallBack.handleGitHubResponse(response)
        }

        repository.searchGithub(searchQuery, gitHubRepositoryCallBack)

        verify(gitHubRepositoryCallBack, times(1)).handleGitHubResponse(response)
    }
}
