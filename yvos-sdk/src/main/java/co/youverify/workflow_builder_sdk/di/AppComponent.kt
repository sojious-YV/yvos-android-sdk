package co.youverify.workflow_builder_sdk.di

import co.youverify.workflow_builder_sdk.modules.vform.VFormModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {


    @Component.Factory
    interface Factory{
        fun create():AppComponent
    }

    fun inject (vformModule: VFormModule)
}