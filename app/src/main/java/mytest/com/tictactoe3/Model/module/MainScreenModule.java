package mytest.com.tictactoe3.Model.module;

import dagger.Module;
import dagger.Provides;
import mytest.com.tictactoe3.Presenter.CustomScope;
import mytest.com.tictactoe3.View.MainScreenContract;


/**
 * Purpose of MainScreenModule is to provide the View ( in our case MainActivity) to our Presenter
 * when it is injected. The Presenter uses this view as a reference to our MainAvtivity to call the
 * methods in the View (showError(),showComplete()) whenever the network operation are performed
 *
 * It is a means through which the View and the Presenter Communicate with each other
 */

@Module
public class MainScreenModule {
    private final MainScreenContract.View mView;


    public MainScreenModule(MainScreenContract.View mView) {
        this.mView = mView;
    }

    /*
    * In Dagger, two dependent components cannot share the same scope
    * i.e (Our NetComponent  and MainScreenComponent cannot share the scope of @Singlton).
    * So we create a custom  scope  called @CustomeScope to be used by our MainScreenComponent
    * and MainScreenModule
    */
    @Provides
    @CustomScope
    MainScreenContract.View providesMainScreenContractView() {
        return mView;
    }
}