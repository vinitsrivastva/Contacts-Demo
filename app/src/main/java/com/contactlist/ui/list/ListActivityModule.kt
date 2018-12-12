package com.contactlist.ui.list


import dagger.Module
import dagger.Provides


@Module
class ListActivityModule(private var view: ListActivityMVP.View) {


    @ItemScope
    @Provides
    fun provideMainActivityPresenter() : ListActivityMVP.Presenter {

        return ListActivityPresenter()
    }

}