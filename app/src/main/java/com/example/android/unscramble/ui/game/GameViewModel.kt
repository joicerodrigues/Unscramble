package com.example.android.unscramble.ui.game

import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel


class GameViewModel : ViewModel() {

    //private val viewModel = GameViewModel() // modelo de visualização usando o construtor padrão
    //movendo variáveis

    private var _score = MutableLiveData(0) // variável mutável recebendo 0
    val score: LiveData<Int> // variável que vai para o fragment recebendo liveData que recebe o valor via get
        get() = _score // recebe valor

    private var _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<Spannable> = Transformations.map(_currentScrambledWord) {
        if (it == null) {
            SpannableString("")
        } else {
            val scrambledWord = it.toString()
            val spannable: Spannable = SpannableString(scrambledWord)
            spannable.setSpan(
                TtsSpan.VerbatimBuilder(scrambledWord).build(),
                0,
                scrambledWord.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannable
        }
    }


    private var wordsList: MutableList<String> = mutableListOf() //armazenar lista de palavras
    private lateinit var currentWord: String // armazena palavra que o jogador está tentando decifrar


    init {
      //  Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
      //  _score.postValue(5)

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
            _currentScrambledWord.value =
                String(tempWord) // atualiza o valor da variável com a palavra recém embaralhada
            _currentWordCount.value = (_currentWordCount.value)?.inc() // aumenta contagem de palavras com seguraça de tipo nulo; adicionando value para acessar um objeto liveData
            wordsList.add(currentWord) // adiciona nova palavra em wordlist
        }

    }


    /*
    * Retorna verdadeiro se a contagem de palavras atual for menor que MAX_NO_OF_WORDS.
    * Atualiza a próxima palavra.
    */

     fun increaseScore() {
        _score.value = (_score.value)?.plus(SCORE_INCREASE) //Aumenta a variável score usando SCORE_INCREASE; adicionando value para acessar um objeto liveData
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            //valida a palavra do jogador e aumente a pontuação se o palpite estiver correto
            //_score += SCORE_INCREASE; // adiciona +20 na váriavel
            increaseScore()
            return true
        }
        return false
    }


    fun nextWord(): Boolean {
        _currentWordCount.value?.let {
            return if (it < MAX_NO_OF_WORDS) {// adicionando value para acessar um objeto liveData
                getNextWord()
                true
            } else false
        }
        return false
    }

    /*
    * Reinicializa os dados do jogo para reiniciar o jogo.
    */
    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0 // adicionando value para acessar um objeto liveData
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


