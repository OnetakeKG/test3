package com.geekbrains.tests

import com.geekbrains.tests.presenter.details.DetailsPresenter
import com.geekbrains.tests.view.details.ViewDetailsContract
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class DetailsPresenterTest {

    private lateinit var presenter: DetailsPresenter

    @Mock
    private lateinit var view: ViewDetailsContract

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        presenter = DetailsPresenter()
    }

    @Test
    fun `onIncrement, onDecrement`() {
        presenter.onAttach(view)

        presenter.setCounter(5)
        presenter.onIncrement()
        verify(view).setCount(6)

        presenter.onDecrement()
        verify(view).setCount(5)

        presenter.onDetach()
    }
}