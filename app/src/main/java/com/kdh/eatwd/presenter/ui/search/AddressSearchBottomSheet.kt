package com.kdh.eatwd.presenter.ui.search

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kdh.eatwd.databinding.DialogBottomAddressSearchBinding
import com.kdh.eatwd.presenter.util.textChangesToFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class AddressSearchBottomSheet : BottomSheetDialogFragment() {


    lateinit var binding: DialogBottomAddressSearchBinding
    private val viewModel: AddressSearchViewModel by viewModels()
    private var textCoroutineJob: Job = Job()
    private val textCoroutineContext: CoroutineContext
        get() = Dispatchers.IO + textCoroutineJob

    //    private lateinit var addressSearchAdapter: AddressSearchAdapter
    private lateinit var addressSearchPagingAdapter: AddressSearchPagingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("dodo55 ", " getScreenHeight onCreateView")
        binding = DialogBottomAddressSearchBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.dialog_bottom_address_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAddressSearchEvent()
        initAdapter()
        initAddressInfoObserving()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d("dodo55 ", " getScreenHeight onCreateDialog")
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { bottomDialog ->
            val bottomSheetDialog = bottomDialog as BottomSheetDialog
            setupRatio(bottomSheetDialog)
        }

        return dialog
    }


    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        Log.d("dodo55 ", " getScreenHeight setupRatio")
        val bs = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from<View>(bs)
        val layoutParams = bs.layoutParams

        layoutParams.height = getScreenHeight() * 85 / 100
        bs.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

    }

    private fun getScreenHeight(): Int {
        Log.d("dodo55 ", " getScreenHeight getScreenHeight")
        val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = wm.currentWindowMetrics
            val insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.bottom - insets.top
        } else {
            // TODO ?????????
            return 0
        }
    }


    private fun initAddressSearchEvent() {
        lifecycleScope.launch(context = textCoroutineContext) {
            val editFlow = binding.etAddressSearch.textChangesToFlow()
            editFlow
                .debounce(1000)
                .filter { it?.length!! > 1 }
                .onEach {
                    // Api??????
                    viewModel.searchAddress(it.toString())

                }
                .launchIn(this)
        }
    }

    private fun initAddressInfoObserving() {
        lifecycleScope.launch {
            viewModel.addressInfo.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collectLatest { juso ->
//                addressSearchAdapter.submitList(juso)
                juso?.let {
                    addressSearchPagingAdapter.submitData(it)
                }
            }
        }
    }

    private fun initAdapter() {
        addressSearchPagingAdapter = AddressSearchPagingAdapter()
        binding.rvAddressSearchInfo.adapter = addressSearchPagingAdapter
        binding.rvAddressSearchInfo.layoutManager = LinearLayoutManager(context)
    }

    override fun onDestroy() {
        textCoroutineContext.cancel()
        super.onDestroy()

    }

}

