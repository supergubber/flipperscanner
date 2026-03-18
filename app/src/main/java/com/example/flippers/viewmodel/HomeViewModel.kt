package com.example.flippers.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flippers.data.local.ProScanDatabase
import com.example.flippers.data.local.ScannedDocument
import com.example.flippers.data.repository.DocumentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DocumentRepository

    val recentFiles: StateFlow<List<ScannedDocument>>

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        val dao = ProScanDatabase.getDatabase(application).scannedDocumentDao()
        repository = DocumentRepository(application, dao)
        recentFiles = repository.getRecentDocuments()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun deleteDocument(document: ScannedDocument) {
        viewModelScope.launch {
            repository.deleteDocument(document)
        }
    }

    fun getShareIntent(document: ScannedDocument) = repository.getShareIntent(document)
}
