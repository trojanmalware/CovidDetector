/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentQuestionsBinding

class QuestionsFragment : Fragment() {
    data class Question(
            val text: String,
            val answers: List<String>)

    // The first answer is the correct one.  We randomize the answers before showing the text.
    // All questions must have four answers.  We'd want these to contain references to string
    // resources so we could internationalize. (Or better yet, don't define the questions in code...)
    private val questions: MutableList<Question> = mutableListOf(
            Question(text = "Apakah kamu pernah melakukan tes usap tenggorok atau hidung dengan RT-PCR dengan hasil positif?",
                    answers = listOf("Ya", "Tidak")),
            Question(text = "Apakah kamu memiliki gejala infeksi saluran pernapasan atas seperti demam lebih dari 38*C, batuk, pilek, hidung tersumbat, nyeri menelan, hilang indera penciuman atau sesak nafas?",
                    answers = listOf("Ya","Tidak")),
            Question(text = "Apakah kamu memiliki kontak erat dengan kasus positif/probable COVID-19 dalam 14 hari terakhir ? Yang termasuk kontak erat adalah kontak dengan orang yang memberikan perawatan/merawat, kontak tatap muka/berdekatan dalam radius 1 meter atau bersentuhan fisik langsung.",
                    answers = listOf("Ya", "Tidak")),
            Question(text = "Apakah kamu tinggal di wilayah tranmisi lokal atau baru saja pulang bepergian ke negara yang melaporkan transmisi lokal dalam 14 hari terakhir?\n",
                    answers = listOf("Ya","Tidak"))
    )



    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = 4
    private var points =1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentQuestionsBinding>(
                inflater, R.layout.fragment_questions, container, false)

        randomizeQuestions()

        binding.quest = this

        binding.submitButton.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        { view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                }
                if (answers[answerIndex] == currentQuestion.answers[0]) {
                    if(questionIndex==0)
                    {
                        points*= 7
                    }
                    if(questionIndex==1)
                    {
                        points*= 5
                    }
                    if(questionIndex==2)
                    {
                        points*= 3
                    }
                    if(questionIndex==3)
                    {
                        points*= 10
                    }
                    questionIndex++
                    if (questionIndex < numQuestions) {
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                        binding.invalidateAll()
                    } else {
                        view.findNavController()
                                .navigate(QuestionsFragmentDirections
                                        .actionGameFragmentToGameWonFragment(questionIndex, points))
                    }
                }else {

                    questionIndex++
                    if (questionIndex < numQuestions) {
                        setQuestion()
                        binding.invalidateAll()
                    } else {
                        view.findNavController()
                                .navigate(QuestionsFragmentDirections
                                        .actionGameFragmentToGameWonFragment(questionIndex, points))
                    }
                }

            }
        }
        return binding.root
    }

    private fun randomizeQuestions() {
        questionIndex = 0
        setQuestion()
    }

    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        answers = currentQuestion.answers.toMutableList()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_android_trivia_question, questionIndex + 1, numQuestions)
    }
}
