package com.example.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    // Represents the game board: 0 = empty, 1 = X, 2 = O
    private val board = Array(9) { 0 }
    private var currentPlayer = 1 // 1 for X, 2 for O
    private var gameActive = true
    private lateinit var buttons: Array<MaterialButton>
    private lateinit var statusText: TextView
    private lateinit var playAgainButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        statusText = findViewById(R.id.statusText)
        playAgainButton = findViewById(R.id.playAgainButton)

        // Initialize buttons array
        buttons = Array(9) { index ->
            findViewById<MaterialButton>(resources.getIdentifier("button_$index", "id", packageName))
        }

        // Set click listeners for all buttons
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                onCellClicked(index)
            }
        }

        playAgainButton.setOnClickListener {
            resetGame()
        }
    }

    private fun onCellClicked(position: Int) {
        if (!gameActive || board[position] != 0) return

        // Update board and button
        board[position] = currentPlayer
        buttons[position].text = if (currentPlayer == 1) "X" else "O"

        // Check for win or draw
        when {
            checkWin() -> endGame("Player ${if (currentPlayer == 1) "X" else "O"} wins!")
            checkDraw() -> endGame("It's a draw!")
            else -> {
                currentPlayer = if (currentPlayer == 1) 2 else 1
                statusText.text = "Player ${if (currentPlayer == 1) "X" else "O"}'s Turn"
            }
        }
    }

    private fun checkWin(): Boolean {
        val winningCombinations = arrayOf(
            // Rows
            arrayOf(0, 1, 2), arrayOf(3, 4, 5), arrayOf(6, 7, 8),
            // Columns
            arrayOf(0, 3, 6), arrayOf(1, 4, 7), arrayOf(2, 5, 8),
            // Diagonals
            arrayOf(0, 4, 8), arrayOf(2, 4, 6)
        )

        return winningCombinations.any { (a, b, c) ->
            board[a] != 0 && board[a] == board[b] && board[b] == board[c]
        }
    }

    private fun checkDraw(): Boolean {
        return board.none { it == 0 }
    }

    private fun endGame(message: String) {
        gameActive = false
        statusText.text = message
        playAgainButton.visibility = View.VISIBLE
    }

    private fun resetGame() {
        // Reset board
        board.fill(0)
        buttons.forEach { button ->
            button.text = ""
        }

        // Reset game state
        currentPlayer = 1
        gameActive = true
        statusText.text = "Player X's Turn"
        playAgainButton.visibility = View.GONE
    }
}