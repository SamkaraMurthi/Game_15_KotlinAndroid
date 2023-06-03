package vistula.kn.l10_kadeknarayana_60092_game15

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var movesTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var puzzleGrid: GridLayout

    private var moves: Int = 0
    private var score: Int = 0

    private var puzzleArray = Array(4) { IntArray(4) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movesTextView = findViewById(R.id.movesTextView)
        scoreTextView = findViewById(R.id.scoreTextView)
        puzzleGrid = findViewById(R.id.puzzleGrid)

        initializePuzzle()
        updateGrid()

        val resetButton: Button = findViewById(R.id.resetButton)
        resetButton.setOnClickListener { resetPuzzle() }
    }

    private fun initializePuzzle() {
        val numbers = (0..15).toMutableList()
        numbers.shuffle()

        for (i in 0 until puzzleGrid.rowCount) {
            for (j in 0 until puzzleGrid.columnCount) {
                val index = i * puzzleGrid.columnCount + j
                val button = Button(this)
                button.text = numbers[index].toString()
                button.setOnClickListener { moveTile(button) }
                puzzleArray[i][j] = numbers[index]
                puzzleGrid.addView(button)
            }
        }
    }

    private fun resetPuzzle() {
        puzzleGrid.removeAllViews()
        initializePuzzle()
        moves = 0
        score = 0
        updateMoves()
        updateScore()
        updateGrid()
    }

    private fun moveTile(button: Button) {
        val buttonIndex = puzzleGrid.indexOfChild(button)
        val buttonRow = buttonIndex / puzzleGrid.columnCount
        val buttonColumn = buttonIndex % puzzleGrid.columnCount

        val emptyButtonIndex = getEmptyButtonIndex()
        val emptyButtonRow = emptyButtonIndex / puzzleGrid.columnCount
        val emptyButtonColumn = emptyButtonIndex % puzzleGrid.columnCount

        if (isValidMove(buttonRow, buttonColumn, emptyButtonRow, emptyButtonColumn)) {
            swapTiles(buttonRow, buttonColumn, emptyButtonRow, emptyButtonColumn)

            if (isInCorrectPosition(buttonRow, buttonColumn, emptyButtonRow, emptyButtonColumn)) {
                score++
            }

            moves++
            updateMoves()
            updateGrid()

            if (isPuzzleSolved()) {
                displayMessage("Victory (You Win)!")
            } else if (isPuzzleAlmostSolved()) {
                displayMessage("Almost Victory (You Almost Win)!")
            }
        }
    }

    private fun isValidMove(row1: Int, col1: Int, row2: Int, col2: Int): Boolean {
        return when {
            col1 > 0 && puzzleArray[row1][col1 - 1] == 0 && row1 == row2 -> true // Move left
            row1 > 0 && puzzleArray[row1 - 1][col1] == 0 && col1 == col2 -> true // Move up
            col1 < puzzleGrid.columnCount - 1 && puzzleArray[row1][col1 + 1] == 0 && row1 == row2 -> true // Move right
            row1 < puzzleGrid.rowCount - 1 && puzzleArray[row1 + 1][col1] == 0 && col1 == col2 -> true // Move down
            else -> false
        }
    }

    private fun swapTiles(row1: Int, col1: Int, row2: Int, col2: Int) {
        val temp = puzzleArray[row1][col1]
        puzzleArray[row1][col1] = puzzleArray[row2][col2]
        puzzleArray[row2][col2] = temp

        val button1 = puzzleGrid.getChildAt(row1 * puzzleGrid.columnCount + col1) as Button
        val button2 = puzzleGrid.getChildAt(row2 * puzzleGrid.columnCount + col2) as Button

        button1.text = puzzleArray[row1][col1].toString()
        button2.text = puzzleArray[row2][col2].toString()
    }

    private fun isInCorrectPosition(row1: Int, col1: Int, row2: Int, col2: Int): Boolean {
        return puzzleArray[row1][col1] == (row1 * puzzleGrid.columnCount + col1 + 1) % 16
                && puzzleArray[row2][col2] == (row2 * puzzleGrid.columnCount + col2 + 1) % 16
    }

    private fun isPuzzleSolved(): Boolean {
        for (i in 0 until puzzleGrid.rowCount) {
            for (j in 0 until puzzleGrid.columnCount) {
                if (puzzleArray[i][j] != (i * puzzleGrid.columnCount + j + 1) % 16) {
                    return false
                }
            }
        }
        return true
    }

    private fun isPuzzleAlmostSolved(): Boolean {
        var count = 1
        for (i in 0 until puzzleGrid.rowCount) {
            for (j in 0 until puzzleGrid.columnCount) {
                if (puzzleArray[i][j] != count % 16) {
                    return false
                }
                count++
            }
        }
        return true
    }

    private fun getEmptyButtonIndex(): Int {
        for (i in 0 until puzzleGrid.rowCount) {
            for (j in 0 until puzzleGrid.columnCount) {
                if (puzzleArray[i][j] == 0) {
                    return i * puzzleGrid.columnCount + j
                }
            }
        }
        return -1
    }

    private fun updateGrid() {
        for (i in 0 until puzzleGrid.rowCount) {
            for (j in 0 until puzzleGrid.columnCount) {
                val button = puzzleGrid.getChildAt(i * puzzleGrid.columnCount + j) as Button
                button.text = puzzleArray[i][j].toString()
            }
        }
    }

    private fun updateMoves() {
        movesTextView.text = "Moves: $moves"
    }

    private fun updateScore() {
        scoreTextView.text = "Score: $score"
    }

    private fun displayMessage(message: String) {
    }
}