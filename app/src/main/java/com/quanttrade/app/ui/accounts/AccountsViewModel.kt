package com.quanttrade.app.ui.accounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AccountsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "账户管理"
    }
    val text: LiveData<String> = _text
    
    // 账户列表
    private val _accounts = MutableLiveData<List<Account>>().apply {
        value = emptyList()
    }
    val accounts: LiveData<List<Account>> = _accounts
    
    // 添加账户
    fun addAccount(account: Account) {
        val currentList = _accounts.value?.toMutableList() ?: mutableListOf()
        currentList.add(account)
        _accounts.value = currentList
    }
    
    // 删除账户
    fun removeAccount(accountId: String) {
        val currentList = _accounts.value?.toMutableList() ?: mutableListOf()
        currentList.removeAll { it.id == accountId }
        _accounts.value = currentList
    }
    
    // 更新账户状态
    fun updateAccountStatus(accountId: String, isConnected: Boolean) {
        val currentList = _accounts.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.id == accountId }
        if (index != -1) {
            currentList[index] = currentList[index].copy(isConnected = isConnected)
            _accounts.value = currentList
        }
    }
}

data class Account(
    val id: String,
    val name: String,
    val type: AccountType,
    val isConnected: Boolean = false,
    val balance: Double = 0.0,
    val equity: Double = 0.0
)

enum class AccountType {
    MT4,
    MT5,
    BINANCE
}