package com.contactlist.main


import com.contactlist.ui.list.ItemComponent
import com.contactlist.ui.list.ListActivityModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    fun inject(baseApplication: App)

    fun itemListComponent(listModule: ListActivityModule): ItemComponent

}
