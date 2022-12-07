package com.kdh.eatwd.presenter.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

fun EditText.textChangesToFlow() : Flow<CharSequence?> {
    return callbackFlow {
        val listener = object : TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                trySend(text)
            }
        }
        addTextChangedListener(listener)
        awaitClose{removeTextChangedListener(listener)}
    }.onStart {
        emit(text)
    }
}

//
//fun <T : ViewBinding> BottomSheetDialogFragment.viewBinding(viewBindingFactory: (View) -> T) =
//    FragmentViewBindingDelegate(this, viewBindingFactory)