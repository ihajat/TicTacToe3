package mytest.com.tictactoe3.Model.component;

import dagger.Component;
import mytest.com.tictactoe3.MainActivity;
import mytest.com.tictactoe3.Model.module.MainScreenModule;
import mytest.com.tictactoe3.Presenter.CustomScope;


/**
 * Created by iqbalhajat on 28/04/2017.
 */

@CustomScope
@Component(dependencies = NetComponent.class, modules = MainScreenModule.class)
public interface MainScreenComponent {
    void inject(MainActivity activity);
}