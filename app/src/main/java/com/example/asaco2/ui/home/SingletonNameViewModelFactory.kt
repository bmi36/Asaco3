package com.example.asaco2.ui.home

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

class SingletonNameViewModelFactory: ViewModelProvider.NewInstanceFactory() {
    lateinit var viewModel: CalendarViewModel
}