package com.contactlist.ui.list

import dagger.Subcomponent

@ItemScope
@Subcomponent(modules = arrayOf(ListActivityModule::class))
interface ItemComponent {
    fun inject(itemListFragment: ItemListFragment)
}