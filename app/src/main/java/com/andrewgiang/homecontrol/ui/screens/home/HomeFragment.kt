package com.andrewgiang.homecontrol.ui.screens.home


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.andrewgiang.homecontrol.R
import com.andrewgiang.homecontrol.data.model.Action
import com.andrewgiang.homecontrol.data.model.AppAction
import com.andrewgiang.homecontrol.ui.screens.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


class HomeFragment : BaseFragment(), ActionClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: HomeViewModel
    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getControllerComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        actions.layoutManager = GridLayoutManager(context, 3)

        viewModel.getAppActions().observe(this, handleAppAction())
        viewModel.getData().observe(this, onActionChanged())
    }

    private fun onActionChanged(): Observer<HomeState> {
        return Observer { state ->
            val adapter = HomeActionAdapter(state.actionIds, this)
            actions.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun handleAppAction(): Observer<AppAction> {
        return Observer { action ->
            when (action) {
                is AppAction.FullScreen -> {
                    setFullScreen()
                }


            }
        }
    }

    private fun setFullScreen() {
        activity?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }


    override fun onClick(action: Action) {
        viewModel.onClick(action)
    }
}
