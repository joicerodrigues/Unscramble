package com.example.android.unscramble.ui.game

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding

class GameViewModel : ViewModel() {

    //private val viewModel = GameViewModel() // modelo de visualização usando o construtor padrão
    //movendo variáveis
    private var _score = 0
    val score: Int
        get() = _score

    private var _currentWordCount = 0
    val currentWordCount: Int
        get() = _currentWordCount

    private lateinit var _currentScrambledWord: String //somente os dados armazenados no objeto mudarão

    //propriedade de apoio
    val currentScrambledWord: String
        get() = _currentScrambledWord

    private var wordsList: MutableList<String> = mutableListOf() //armazenar lista de palavras
    private lateinit var currentWord: String // armazena palavra que o jogador está tentando decifrar

    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
    }


    /*
    * Atualiza currentWord e currentScrambledWord com a próxima palavra.
    */
    private fun getNextWord() {
        currentWord = allWordsList.random() //acessando palavra aleatória
        val tempWord = currentWord.toCharArray() // convertendo string para matriz
        tempWord.shuffle() // atribuindo matriz para variável tempWord e embaralha os caracteres

        while (String(tempWord).equals(currentWord, false)) {
            //embaralhar a palavra para continuar a repetição até que a palavra embaralhada não seja igual à palavra original.
            tempWord.shuffle()
        }

        if (wordsList.contains(currentWord)) { //verificar se a palavra foi usada ou não
            getNextWord()
        } else {
            _currentScrambledWord =
                String(tempWord) // atualiza o valor da variável com a palavra recém embaralhada
            ++_currentWordCount // aumenta contagem de de palavras
            wordsList.add(currentWord) // adiciona nova palavra em wordlist
        }

    }


    /*
    * Retorna verdadeiro se a contagem de palavras atual for menor que MAX_NO_OF_WORDS.
    * Atualiza a próxima palavra.
    */

     fun increaseScore() {
        _score += SCORE_INCREASE //Aumenta a variável score usando SCORE_INCREASE
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            //valida a palavra do jogador e aumente a pontuação se o palpite estiver correto
            _score += SCORE_INCREASE; // adiciona +1 na váriavel numero

            return true
        }
        return false
    }

    fun nextWord(): Boolean {
        return if (_currentWordCount < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

    /*
    * Reinicializa os dados do jogo para reiniciar o jogo.
    */
    fun reinitializeData() {
        _score = 0
        _currentWordCount = 0
        wordsList.clear()
        getNextWord()
    }
//
        //lateinit var binding: GameFragmentBinding
//        var playerWord
//        if (isUserWordCorrect(playerWord) == true) {
//            _score +20
//        }
//        else {
//            _score
//        }
    }


