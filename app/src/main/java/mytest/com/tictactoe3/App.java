package mytest.com.tictactoe3;

import android.app.Application;

import mytest.com.tictactoe3.Model.component.DaggerNetComponent;
import mytest.com.tictactoe3.Model.component.NetComponent;
import mytest.com.tictactoe3.Model.module.AppModule;
import mytest.com.tictactoe3.Model.module.NetModule;


/**
 * Created by iqbalhajat on 28/04/2017.
 */

public class App extends Application {
    private NetComponent mNetComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule("https://simpleosandxs.herokuapp.com"))
                .build();
    }
    public NetComponent getNetComponent() {
        return mNetComponent;
    }
}