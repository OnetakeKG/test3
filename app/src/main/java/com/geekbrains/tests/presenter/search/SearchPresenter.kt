import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.repository.GitHubRepository
import com.geekbrains.tests.repository.GitHubRepository.GitHubRepositoryCallback
import com.geekbrains.tests.view.ViewContract
import com.geekbrains.tests.view.search.ViewSearchContract
import retrofit2.Response
import java.lang.RuntimeException

/**
 * В архитектуре MVP все запросы на получение данных адресуются в Репозиторий.
@@ -15,36 +17,46 @@ import retrofit2.Response
 */

internal class SearchPresenter internal constructor(
    private val repository: GitHubRepository
) : PresenterSearchContract, GitHubRepositoryCallback {

    private var viewContract: ViewSearchContract? = null

    override fun searchGitHub(searchQuery: String) {
        viewContract?.displayLoading(true)
        repository.searchGithub(searchQuery, this)
    }

    override fun onAttach(viewContract: ViewContract) {
        this.viewContract = viewContract as? ViewSearchContract
            ?: throw RuntimeException("viewContract must implement ViewSearchContract")
    }

    override fun onDetach() {
        viewContract = null
    }

    override fun handleGitHubResponse(response: Response<SearchResponse?>?) {
        viewContract?.displayLoading(false)
        if (response != null && response.isSuccessful) {
            val searchResponse = response.body()
            val searchResults = searchResponse?.searchResults
            val totalCount = searchResponse?.totalCount
            if (searchResults != null && totalCount != null) {
                viewContract?.displaySearchResults(
                    searchResults,
                    totalCount
                )
            } else {
                viewContract?.displayError("Search results or total count are null")
            }
        } else {
            viewContract?.displayError("Response is null or unsuccessful")
        }
    }

    override fun handleGitHubError() {
        viewContract?.displayLoading(false)
        viewContract?.displayError()
    }
}