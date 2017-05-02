package mytest.com.tictactoe3.Model.component;

import javax.inject.Singleton;

import dagger.Component;
import mytest.com.tictactoe3.Model.module.AppModule;
import mytest.com.tictactoe3.Model.module.NetModule;
import retrofit2.Retrofit;

/**
 * we will be using this Component as a dependency for our MainScreenComponent
 * so we need retrofit as a dependency. Hence we expose its return type so
 * that when MainScreen injects in any of the activities or fragments the
 * MainScreenComponent can find and Inject Retrofit from this Module
 */

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {
    // downstream components need these exposed with the return type
    // method name does not really matter
    Retrofit retrofit();
}