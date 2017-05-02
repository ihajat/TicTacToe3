package mytest.com.tictactoe3.View;

import android.widget.Button;

import java.util.List;

import io.reactivex.disposables.Disposable;
import mytest.com.tictactoe3.Model.AIPlayer;

/**
 * Created by iqbalhajat on 28/04/2017.
 */

public interface MainScreenContract {
    /*
    * View interface holds all the methods which we will implement in our MainScreen View
     */
    interface View {

        public  void printPlayerMove(Button btn);
        public  void printAIPlayerMove(Button btn);
        public  void addAIPlayerMove(Disposable d);
        public  void printInitGame();
        public  void printHasWon(String theSeed);
        public  void printIsDraw();
        public  void printCell(Button btn,String content);
        public  void printRequestNewGame(String title);
        public  void handleError(Throwable error);
        public  void handleResponse(AIPlayer aiPlayer);
        public List<Button> getButtons();
    }

    /*
    * Presenter interface has all the methods that we will implement in our MainScreenPresenter
     */
    interface Presenter {
        void AIPlayerMove();
    }
}
