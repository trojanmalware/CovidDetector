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
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentVerdictBinding


class VerdictFragment : Fragment() {
    data class Explanation(
            val text: String, val title: String)

    // The first answer is the correct one.  We randomize the answers before showing the text.
    // All questions must have four answers.  We'd want these to contain references to string
    // resources so we could internationalize. (Or better yet, don't define the questions in code...)
    private val explanations: MutableList<Explanation> = mutableListOf(
            Explanation(text = "Jika sudah lewat 10 hari sejak onset pertama gejala muncul dan ditambah minimal 3 hari setelahnya tidak ada gejala demam atau pernafasan dan tidak dilakukan tes usap tenggorokan atau hidung RT PCR ulang.\n" +
                    "ATAU Sudah dilakukan tes usap tenggorokan atau hidung RT PCR ulang 1x dengan hasil NEGATIF, serta ditambah minimal 3 hari setelahnya tidak ada gejala demam atau pernafasan, maka kamu dapat dimasuk ke dalam kriteria SELESAI ISOLASI.\n" +
                    "Disarankan untuk melakukan test PCR untuk memastikan kondisi kamu.",
                    title = "Kamu termasuk kategori KASUS POSITIF DENGAN GEJALA."),
            Explanation(text =
                    "Jika tidak bergejala dan sudah lewat 10 hari sejak pengambilan tes usap tenggorokan atau hidung RT PCR, dan tidak dilakukan tes ulang, maka kamu dapat termasuk ke dalam kriteria SELESAI ISOLASI.\n",
                    title = "Kamu termasuk kasus POSITIF TANPA GEJALA."),
            Explanation(title = "Kamu termasuk kasus SUSPEK atau PROBABLE.",
                    text = "Jika gejala kamu berat dan sesak nafas hebat maka kamu termasuk kasus PROBABLE, jika tidak maka kemungkinan masuk kriteria SUSPEK.\n" +
                    "Jika kamu masuk kriteria Suspek dan hasil tes usap tenggorok dan hidung dengan RT PCR kamu 2 kali negatif selama 2 hari berurutan dengan selang waktu > 24 jam, kamu dapat tergolong kriteria Resiko Rendah\n"),
            Explanation(title = "Kamu termasuk kategori KONTAK ERAT.",
                    text = "Jika kamu sudah selesai melakukan karantina mandiri selama 14 hari dan tidak ada gejala maka kamu bisa dimasukan ke dalam kriteria Resiko Rendah\n" + "Disarankan untuk melakukan test PCR untuk memastikan kondisi kamu."),
            Explanation(title = "Kamu termasuk kriteria Pelaku Perjalanan",
                    text = "Disarankan untuk melakukan test PCR untuk memastikan kondisi kamu. "),
            Explanation(title = "Kamu termasuk kategori RESIKO RENDAH.",
                    text = "Tetap pastikan konsultasikan ke dokter atau tenaga medis profesional.\n" +
                    "Meski berisiko rendah, namun jangan lengah.")

    )

    lateinit var currentExplan : Explanation

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentVerdictBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_verdict, container, false)

        binding.verdict = this

        binding.nextMatchButton.setOnClickListener { view: View ->
            view.findNavController().navigate(VerdictFragmentDirections.actionGameWonFragmentToGameFragment())
        }
        val args = VerdictFragmentArgs.fromBundle(requireArguments())
        if (args.numCorrect % 7 == 0 && args.numCorrect % 5 == 0)
        {
            currentExplan = explanations[0]
        }
        else if(args.numCorrect%7 ==0)
        {
            currentExplan = explanations[1]
        }
        else if(args.numCorrect%5==0)
        {
            if(args.numCorrect%2!=0&&args.numCorrect%3!=0)
            {
                currentExplan = explanations[5]
            }
            else
            {
                currentExplan = explanations[2]
            }
        }
        else if(args.numCorrect%3==0)
        {
            currentExplan = explanations[3]
        }
        else if(args.numCorrect%2==0)
        {
            currentExplan = explanations[4]
        }
        else
        {
            currentExplan = explanations[5]
        }



        return binding.root
    }
}
