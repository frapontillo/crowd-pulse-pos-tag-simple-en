/*
 * Copyright 2015 Francesco Pontillo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.frapontillo.pulse.crowd.postagsimple.en;

import com.github.frapontillo.pulse.crowd.data.entity.Message;
import com.github.frapontillo.pulse.crowd.data.entity.Token;
import com.github.frapontillo.pulse.crowd.postagsimple.ISimplePOSTaggerOperator;
import com.github.frapontillo.pulse.spi.ISingleablePlugin;
import com.github.frapontillo.pulse.spi.VoidConfig;
import rx.Observable;

import java.util.List;

/**
 * @author Francesco Pontillo
 */
public class EnglishSimplePOSTagger extends ISingleablePlugin<Message, VoidConfig> {
    public final static String PLUGIN_NAME = "simplepostagger-en";

    @Override public String getName() {
        return PLUGIN_NAME;
    }

    @Override public VoidConfig getNewParameter() {
        return new VoidConfig();
    }

    @Override public Observable.Operator<Message, Message> getOperator(VoidConfig parameters) {
        EnglishSimplePOSTagger actualTagger = this;
        return new ISimplePOSTaggerOperator(this) {
            @Override public List<Token> posTagMessageTokens(Message message) {
                return actualTagger.singleItemProcess(message).getTokens();
            }
        };
    }

    @Override public Message singleItemProcess(Message message) {
        if (message.getTokens() == null) {
            return null;
        }

        // associate each POS with the corresponding Token
        for (Token token : message.getTokens()) {
            String simplePos = null;
            if (token.getPos().startsWith("NN")) {
                simplePos = "n";
            } else if (token.getPos().startsWith("VB")) {
                simplePos = "v";
            } else if (token.getPos().startsWith("JJ")) {
                simplePos = "a";
            } else if (token.getPos().startsWith("RB")) {
                simplePos = "r";
            }
            token.setSimplePos(simplePos);
        }

        return message;
    }
}
