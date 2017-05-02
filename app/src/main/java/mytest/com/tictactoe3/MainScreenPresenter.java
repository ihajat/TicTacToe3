package mytest.com.tictactoe3;

import android.widget.Button;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mytest.com.tictactoe3.Model.AIPlayer;
import mytest.com.tictactoe3.View.MainScreenContract;
import retrofit2.Retrofit;
import retrofit2.http.GET;


/**
 *
 * MainScreenPresenter which will implement the methods to make network calls(Your apps logic)
 */

public class MainScreenPresenter implements MainScreenContract.Presenter {

    public  String[] buttons = new String[9];
    public boolean game_over = false;
    Retrofit retrofit;
    MainScreenContract.View mView;
    public boolean stop =false;

    /*
    * The retrofit dependency will be provided by NetComponent
    * and the MainScreenContract.View dependency will be provided by the
    * MainScreenComponent whenever we inject it in our
    * Views(in our case we will inject it in MainActivity)
     */
    @Inject
    public MainScreenPresenter(Retrofit retrofit, MainScreenContract.View mView) {
        this.retrofit = retrofit;
        this.mView = mView;
        for(int i=0;i<9;i++)buttons[i]="";
    }

    /*
    * implementation of the method getMove() to make NetworkRequest using the
    * retrofit object and RequestInterface.
    * It uses rxJava's repeatUntil, to continue emitting until, the AI Web service returns a valid move
    * ( this is because the AI web service is just a random generator, hence, will return
    * both valid and invalid moves )
    */
    @Override
    public void AIPlayerMove() {
        mView.addAIPlayerMove(retrofit.create(RequestInterface.class).getMove()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
                .repeatUntil(new BooleanSupplier() {
                    @Override
                    public boolean getAsBoolean() throws Exception {
                        return stop;
                    }
                })
                .subscribe(new Consumer<AIPlayer>() {
                    @Override
                    public void accept(AIPlayer aiPlayer) throws Exception {
                        mView.handleResponse(aiPlayer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable error) throws Exception {
                        mView.handleError(error);
                    }
                }));
    }

    public void setPlayerMove(Button btn) {
        mView.printPlayerMove(btn);
        setBtnText(Integer.parseInt(btn.getTag().toString()) , "X");
        if(!hasGameEnded("X")) {
            stop = false;
            AIPlayerMove();
        }
    }

    private void setBtnText(int i, String str) {
        buttons[i]=str;
    }


    public void setAIPlayerMove(Button btn) {
        mView.printAIPlayerMove(btn);
        setBtnText(Integer.parseInt(btn.getTag().toString()) , "O");
        hasGameEnded("O");
        stop = true;
    }

    public  void RequestNewGame(String title){
        mView.printRequestNewGame(title);
    }
    public  void InitGame(){
        game_over = false;
        stop =false;
        for(int i=0;i<9;i++)buttons[i]="";
        mView.printInitGame();
    }
    public  boolean hasGameEnded(String theSeed){
        if(hasWon(theSeed))
        {
            mView.printHasWon(theSeed);
            game_over = true;
            return true;
        }
        else if(isDraw())
        {
            mView.printIsDraw();
            game_over = true;
            return true;
        }
        return false;
    }
    /*
    * used by func hasWon to check a single row ( horizontal, vertical, diagonal ) if there is a winner
     */
    public boolean check(int a, int b, int c, String v){
        if(buttons[a].equals(buttons[b]) &&  buttons[a].equals(buttons[c]) && buttons[a].equals(v)) return true;
        return false;
    }
    public Button getButton(List<Button> btns,  String s){
        return btns.get(getIndexFromAIReturnValue(s));
    }

    /*
    * converts the values returned from the AI web service (eg MID:RIGHT, BOTTOM:LEFT, TOP:MID, etc)
    * to the indices we are using 0-8
     */
    private int getIndexFromAIReturnValue(String s) {
        String[] ss = s.split(":");
        System.out.println(ss[1]);
        int index=0;
        if(ss[0].equals("MID")) index += 3;
        else if(ss[0].equals("BOTTOM")) index += 6;
        if(ss[1].equals("MID")) index += 1;
        else if(ss[1].equals("RIGHT")) index += 2;
        return index;
    }

    /*
    * since the grid is using an index of 0-8, and not 2D, we use a simple check for all
    * permutations to check if there is a winner
     */
    public  boolean hasWon(String theSeed){
        if(check(0,1,2,theSeed)
                || check(3,4,5,theSeed)
                || check(6,7,8,theSeed)
                || check(0,3,6,theSeed)
                || check(1,4,7,theSeed)
                || check(2,5,8,theSeed)
                || check(0,4,8,theSeed)
                || check(2,4,6,theSeed))return true;
        return false;
    }
    /*
    * if grid is full, and no winner, then it is a draw
     */
    public  boolean isDraw(){
        for(int i = 0; i< 9; i++){
            if(buttons[i].equals("")) return false;
        }
        return true;
    }

    /*
    * this is our retrofit web service interface
     */
    private interface RequestInterface {
        @GET("/game/1234/player/9876/nextturn")
        Observable<AIPlayer> getMove();
    }
}