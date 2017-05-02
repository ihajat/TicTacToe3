package mytest.com.tictactoe3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import mytest.com.tictactoe3.Model.AIPlayer;
import mytest.com.tictactoe3.Model.component.DaggerMainScreenComponent;
import mytest.com.tictactoe3.Model.module.MainScreenModule;
import mytest.com.tictactoe3.View.MainScreenContract;

public class MainActivity extends Activity implements MainScreenContract.View {

    @BindViews({ R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9 })
    public List<Button> btns;
    boolean players_turn = true;
    public CompositeDisposable mCompositeDisposable;

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9})
    public void onClick(Button btn) {
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
        if(players_turn) {
            presenter.setPlayerMove(btn);

        }
    }
    /*
    * We create a variable for our MainScreenPresenter and use @Inject annotation to inject it.
    * We will use this variable to call the getMove() method which is implemented in MainScreenPresenter
     */
    @Inject
    MainScreenPresenter presenter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mCompositeDisposable = new CompositeDisposable();

        DaggerMainScreenComponent.builder()
                .netComponent(((App) getApplicationContext()).getNetComponent())
                .mainScreenModule(new MainScreenModule(this))
                .build().inject(this);

    }
    @Override
    public void printCell(Button btn,String content) {
        btn.setText(content);
        btn.setEnabled(false);
    }
    /*
    * updates the player move
     */
    @Override
    public void printPlayerMove(Button btn) {
        printCell(btn,"X");
        players_turn = false;
    }
    /*
    * updates the AI move
     */
    @Override
    public void printAIPlayerMove(Button btn) {
        printCell(btn,"O");
        players_turn = true;
    }
    /*
    * handles the successful response from the AI web service
     */
    public void handleResponse(AIPlayer aiPlayer) {
        Log.i("myapp",aiPlayer.getLocation());
        Button btn = presenter.getButton(btns, aiPlayer.getLocation());
        if(btn.getText().equals("")) {
            presenter.setAIPlayerMove(btn);
        }
    }

    @Override
    public List<Button> getButtons() {
        return btns;
    }
    /*
    * handles the error response from the AI web service
     */
    public void handleError(Throwable error) {

        Toast.makeText(this, getString(R.string.error)+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    /*
    * adds the successful response from the AI web service observable into a CompositeDisposable
    * variable
    */
    @Override
    public void addAIPlayerMove(Disposable d) {
        mCompositeDisposable.add(d);
    }

    @Override
    public void printInitGame() {
        players_turn = true;
        for(int i = 0; i< btns.size(); i++){
            btns.get(i).setText("");
            btns.get(i).setEnabled(true);
        }
    }
    public void callDialog(String title){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle(title);

        alertDialog.setMessage(R.string.do_you_want_start_new_game);

        alertDialog.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.InitGame();
                        Toast.makeText(MainActivity.this.getApplicationContext(), R.string.new_game_started, Toast.LENGTH_SHORT).show();
                    }
                });
        alertDialog.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        MainActivity.this.finish();
                    }
                });

        alertDialog.show();
    }
    @Override
    public void printHasWon(String theSeed) {
        callDialog(getString(R.string.game_complete_winner)+theSeed+getString(R.string.wins));
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
    @Override
    public void printIsDraw() {
        callDialog(getString(R.string.game_complete_draw));
    }

    @Override
    public void printRequestNewGame(String title) {
        callDialog(title);
    }

}
