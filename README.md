	1	The architecture that I have used is MVP, so that it is easier to test.
	2	The MainActivityView defines the interface that provides the functionality for this app. 
  We can use this to guide the TDD. So, first, we create a test called, printInitGame, which fails. 
  We then write the code for it until it passes and then refactor the code, as required.
	3	I have used interfaces ( e.g. for the activity , we present instead, to the presenter, an activityView), 
  since, this is more fully testable, easily extensible, complies with best object-oriented practices ( SOLID ). 
  Explanation: If we pass an instance of the activity through to the presenter, then the presenter then
	4	knows about the activity, then if the activity is complicated, and if we write a test for the presenter, 
  then we have to write a mock version of the activity, which is difficult to do, so, we give the presenter 
  an interface, then the presenter doesn’t know who implements the interface, and doesn’t care , and the test 
  doesn’t care either, but the test can control and examine the view, to check if certain things have passed/failed.
	5	This architecture facilitates TDD. Why because in TDD we follow 3 steps, 1) Write a failing test,
	2	write code to pass that test, 3) refactor. Since, we use interfaces, which is our contract, the interfaces 
  don’t change. The contract , of how to use the code doesn’t change. Hence, it won’t break the tests, when we refactor the code. 
  The internals of the code is hidden from the tests.
	5	We inject dependencies into the Presenter. Dependency Injection is a form of Dependency Inversion ( SOLID ).
	6	Espresso shouldn’t be used, when conditions need checking, since, it doesn’t support it, 
  and there are other libraries that do, e.g. Robotium. However, due to time constraints, for this example, 
  I’ve taken a reference to the activity to get state information.
	7	The difference between Espresso and Robotium, is that Espresso is synchronised with the UI thread. 
  However, if there is a web service API call, which is on a separate thread, then you will need to synchronise it yourself. 
  There isn’t a really elegant solution to this ( not that I am aware of ). 
  One solution is to create your own Executor & wrap the Espresso’s idling resource around it. 
  This executor service is then passed to OKHttp’s dispatcher to make Espresso aware of Retrofit’s workings. 
  So in simple words your executor service extends from framework’s executor service and implements 
  the idling resource of espresso. Since, due to time constraints , I just added a 2 sec sleep (sorry, my bad!).
	8	The AI Player web service has a bug ( I believe ) . It doesn’t ever return value “BOTTOM:RIGHT”
	9	Since, the AI Player is nothing more than a random generator of possible values, therefore, 
  it has to be called several times, to ensure it doesn’t return a value that is already taken. 
  There are a few ways this can be done. Solution 1) If retrofit was used, then onResponse, 
  we would have to check the return value, and then repeat the call, if the position is already used. 
  2) Use rxJava. We can use rxJava, to continually emit, until we have a value that is valid. 
  This is the more elegant solution and the solution I used. I may have had to use back-pressure, but, it seems ok without it.
	10	We can use mockito to write some unit tests; due to time constraints, 
  I have left them out; though, the structure is there for a good portion of the app to be unit tested.
	11	We can also do unit test for network behaviour. We could use Mockery or mockwebserver or another library.
	12	I used butterknife for convenience, which is then converted into an index, again due to time constraints. 
  I would have preferred to use 2D array to represent the positions.
	13	There may be some functions that I have kept, but are unused ( currently ). Due to time constraints, 
  otherwise, I may refactor the interface, MainActivityView.
	14	Again, due to time constraints, otherwise I would have revisited the UI aspect.
	15	Again, due to time constraints, otherwise I would have added Dagger 2.
	16	Instrumentation Test, checkOneWholeGame, passed.
	17	It uses the following libraries: Dagger 2, retrofit 2, okhttp 3, gson, butterknife, rxJava 2, espresso
	18	I've added one instrumentation test; which does one complete game.