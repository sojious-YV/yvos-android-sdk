package co.youverify.yvos_sdk.di

import co.youverify.yvos_sdk.modules.workflowBuilder.WorkflowBuilderModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {


    @Component.Factory
    interface Factory{
        fun create():AppComponent
    }

    fun inject (vformModule: WorkflowBuilderModule)
}