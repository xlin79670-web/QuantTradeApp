package com.quanttrade.app.ui.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.quanttrade.app.R

class AccountsFragment : Fragment() {

    private lateinit var accountsViewModel: AccountsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        accountsViewModel =
            ViewModelProvider(this).get(AccountsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_accounts, container, false)
        val textView: TextView = root.findViewById(R.id.text_accounts)
        accountsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }
}