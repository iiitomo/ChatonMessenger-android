/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package im.vector.riotredesign.features.home.room.detail.timeline.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.TransitionManager
import butterknife.BindView
import butterknife.ButterKnife
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import im.vector.riotredesign.R

/**
 * Quick Reaction Fragment (agree / like reactions)
 */
class QuickReactionFragment : BaseMvRxFragment() {

    private val viewModel: QuickReactionViewModel by fragmentViewModel(QuickReactionViewModel::class)

    @BindView(R.id.root_layout)
    lateinit var rootLayout: ConstraintLayout


    @BindView(R.id.quick_react_1)
    lateinit var quickReact1: View
    @BindView(R.id.quick_react_2)
    lateinit var quickReact2: View
    @BindView(R.id.quick_react_3)
    lateinit var quickReact3: View
    @BindView(R.id.quick_react_4)
    lateinit var quickReact4: View

    @BindView(R.id.quick_react_1_text)
    lateinit var quickReact1Text: TextView

    @BindView(R.id.quick_react_2_text)
    lateinit var quickReact2Text: TextView

    @BindView(R.id.quick_react_3_text)
    lateinit var quickReact3Text: TextView

    @BindView(R.id.quick_react_4_text)
    lateinit var quickReact4Text: TextView

    var interactionListener: InteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.adapter_item_action_quick_reaction, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quickReact1Text.text = viewModel.agreePositive
        quickReact2Text.text = viewModel.agreeNegative
        quickReact3Text.text = viewModel.likePositive
        quickReact4Text.text = viewModel.likeNegative

        //configure click listeners
        quickReact1.setOnClickListener {
            viewModel.toggleAgree(true)
        }
        quickReact2.setOnClickListener {
            viewModel.toggleAgree(false)
        }
        quickReact3.setOnClickListener {
            viewModel.toggleLike(true)
        }
        quickReact4.setOnClickListener {
            viewModel.toggleLike(false)
        }

    }

    override fun invalidate() = withState(viewModel) {

        TransitionManager.beginDelayedTransition(rootLayout)
        when (it.agreeTrigleState) {
            TriggleState.NONE -> {
                quickReact1.alpha = 1f
                quickReact2.alpha = 1f
            }
            TriggleState.FIRST -> {
                quickReact1.alpha = 1f
                quickReact2.alpha = 0.2f

            }
            TriggleState.SECOND -> {
                quickReact1.alpha = 0.2f
                quickReact2.alpha = 1f
            }
        }
        when (it.likeTriggleState) {
            TriggleState.NONE -> {
                quickReact3.alpha = 1f
                quickReact4.alpha = 1f
            }
            TriggleState.FIRST -> {
                quickReact3.alpha = 1f
                quickReact4.alpha = 0.2f

            }
            TriggleState.SECOND -> {
                quickReact3.alpha = 0.2f
                quickReact4.alpha = 1f
            }
        }

        if (it.selectionResult != null) {
            interactionListener?.didQuickReactWith(it.selectionResult)
        }
    }

    interface InteractionListener {
        fun didQuickReactWith(reactions: List<String>)
    }

    companion object {
        fun newInstance(): QuickReactionFragment {
            return QuickReactionFragment()
        }
    }
}