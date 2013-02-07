gamesol
=======

A generic game solving library in Java.

Solvers are included as example for the following games:
- Soduko [wiki](http://en.wikipedia.org/wiki/Soduko)
- Mazes [wiki](http://en.wikipedia.org/wiki/Maze)
- Tetravex [wiki](http://en.wikipedia.org/wiki/Tetravex)
- N-Puzzle [wiki](http://en.wikipedia.org/wiki/15_puzzle)
- Peg Solitaire [wiki](http://en.wikipedia.org/wiki/Peg_solitaire)

How do you use gamesol to solve your game?

There are two classes to implement: Move and State.

<b>State</b> is the state of your game at some point in time. Your implementation must determine the list of possible moves from that state as well as be able to apply those moves to the current state to generate a new state. If the state of your game can be repeatable (you could reach the exact same state in the future) you must return a hash so states are not repeated.

```java
public interface State<M>
{
  public boolean isSolution();
  public Iterator<M> getMoves();
  public State<M> getCopy();
  public void addMove( M move );
  public void setParent( State<M> parent );
  public State<M> getParent();
  public void setDepth( int depth );
  public int getDepth();
  public Object getHash();
}
```

<b>Move</b> is a possible move that can be made in your game and has no predefined interface, a Move can be anything chosen by the State implementation.

<b>Links</b>:
- [Documentation](http://clickermonkey.github.com/gamesol/)
- [Builds](https://github.com/ClickerMonkey/gamesol/blob/master/build)
- [Examples](Examples//org/magnos/solver)

Sudoko Example [Full Code](Examples/org/magnos/solver/soduko/Soduko.java)

```java
// solve a 3-grid (9x9) puzzle
board = new short[][] {
    {0, 0, 0,/**/ 0, 5, 3,/**/ 6, 0, 0},
	{0, 3, 0,/**/ 1, 0, 0,/**/ 0, 0, 0},
	{1, 0, 5,/**/ 0, 7, 0,/**/ 0, 9, 0},
	/* ****************************** */
	{0, 0, 0,/**/ 2, 0, 0,/**/ 7, 5, 0},
	{0, 8, 0,/**/ 0, 0, 0,/**/ 0, 3, 0},
	{0, 5, 4,/**/ 0, 0, 1,/**/ 0, 0, 0},
	/* ****************************** */
	{0, 6, 0,/**/ 0, 2, 0,/**/ 5, 0, 8},
	{0, 0, 0,/**/ 0, 0, 6,/**/ 0, 2, 0},
	{0, 0, 3,/**/ 4, 1, 0,/**/ 0, 0, 0}
};
SodukoBoard soduko = new SodukoBoard(3, board);

Solver<SodukoMove> solver = new Solver<SodukoMove>();
solver.setInitialState(soduko);
solver.setRevisitStates(true);    // True since there can never be duplicate states
solver.setSaveParent(true);       // If you want a move-by-move history saved for each solution
solver.solve();

// All possible solutions for the given board
List<SodukoBoard> solutions = solver.getSolutions();

// If you provide an empty board, all possible Soduko boards will be created... I can't promise it will ever finish.
```
